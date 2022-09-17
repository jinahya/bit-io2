package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * An implementation of {@link BitOutput} adapts an instance of {@link ByteOutput}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitInputAdapter
 */
public class BitOutputAdapter
        implements BitOutput {

    /**
     * Creates a new instance which writes bytes to specified byte output.
     *
     * @param output the byte output to which bytes are written.
     * @return a new instance.
     * @apiNote Closing the result does not close the {@code output}.
     */
    public static BitOutput from(final ByteOutput output) {
        Objects.requireNonNull(output, "output is null");
        final BitOutputAdapter instance = new BitOutputAdapter(() -> null);
        instance.output(output);
        return instance;
    }

    /**
     * Creates a new instance with specified output supplier.
     *
     * @param supplier the output supplier.
     */
    public BitOutputAdapter(final Supplier<? extends ByteOutput> supplier) {
        super();
        this.supplier = Objects.requireNonNull(supplier, "supplier is null");
    }

    /**
     * Flushes this output by writing any buffered output to the underlying output.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void flush() throws IOException {
        BitOutput.super.flush();
        final ByteOutput output = output(false);
        if (output != null) {
            output.flush();
        }
    }

    /**
     * Closes this output and releases any system resources associated with it.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        BitOutput.super.close();
        if (close) {
            final ByteOutput output = output(false);
            if (output != null) {
                output.close();
            }
        }
    }

    @Override
    public void writeInt(final boolean unsigned, int size, int value) throws IOException {
        BitIoConstraints.requireValidSizeForInt(unsigned, size);
        if (!unsigned) {
            writeUnsignedInt(1, value < 0 ? 1 : 0);
            if (--size > 0) {
                writeUnsignedInt(size, value);
            }
            return;
        }
        final int quotient = size >> 3;
        final int remainder = size & 7;
        if (remainder > 0) {
            unsigned8(remainder, value >> (quotient << 3));
        }
        for (int i = Byte.SIZE * (quotient - 1); i >= 0; i -= Byte.SIZE) {
            unsigned8(Byte.SIZE, value >> i);
        }
    }

    @Override
    public long align(int bytes) throws IOException {
        if (bytes <= 0) {
            throw new IllegalArgumentException("bytes(" + bytes + ") is not positive");
        }
        long bits = 0L; // number of bits padded
        if (available < Byte.SIZE) {
            bits += available; // must be prior to the below
            writeInt(true, available, 0x00);
        }
        if (bytes == 1) {
            return bits;
        }
        for (bytes = bytes - (int) (count % bytes); bytes > 0; bytes--) {
            writeInt(true, Byte.SIZE, 0x00);
            bits += Byte.SIZE;
        }
        return bits;
    }

    /**
     * Writes specified unsigned value of specified number of bits.
     *
     * @param size  the number of bits to write; between {@code 1} and {@value java.lang.Byte#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     */
    private void unsigned8(final int size, final int value) throws IOException {
        assert size > 0 && size <= Byte.SIZE;
        if (size == Byte.SIZE && available == Byte.SIZE) { // write, a full 8-bit octet, directly to the output
            output(true).write(value);
            count++;
            assert octet == 0x00 : "'octet' should be remained as 0x00";
            assert available == Byte.SIZE : "'available' should be remained as Byte.SIZE";
            return;
        }
        final int required = size - available;
        if (required > 0) {
            unsigned8(available, value >> required);
            unsigned8(required, value);
            return;
        }
        octet <<= size;
        octet |= value & BitIoConstants.mask(size);
        available -= size;
        if (available == 0) {
            assert octet >= 0 && octet < 256;
            output(true).write(octet);
            count++;
            octet = 0x00;
            available = Byte.SIZE;
        }
    }

    private ByteOutput output(final boolean get) {
        if (get) {
            if (output(false) == null) {
                output(supplier.get());
                close = true;
            }
            return output(false);
        }
        return output;
    }

    private void output(final ByteOutput output) {
        if (output(false) != null) {
            throw new IllegalStateException("output already has been set");
        }
        this.output = Objects.requireNonNull(output, "output is null");
    }

    private final Supplier<? extends ByteOutput> supplier;

    private ByteOutput output = null;

    /**
     * a flag indicates that the {@code output} is supplied from the {@code supplier}; not directly set.
     */
    private boolean close = false;

    /**
     * The current octet.
     */
    private int octet;

    /**
     * The number of available bits in {@link #octet}.
     */
    private int available = Byte.SIZE;

    /**
     * The number of bytes written to {@link #output} so far.
     */
    private long count;
}
