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
import java.util.function.Supplier;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static java.util.Objects.requireNonNull;

/**
 * An implementation of {@link BitOutput} adapts an instance of {@link ByteOutput}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitInputAdapter
 */
public class BitOutputAdapter implements BitOutput {

    // TODO: 2020-05-01 required? preferred?
    static final Supplier<ByteOutput> NULL_OUTPUT_SUPPLIER = () -> null;

    /**
     * Creates a new instance which writes bytes directly to specified byte output.
     *
     * @param output the byte output to which bytes are written.
     * @return a new instance.
     * @see BitInputAdapter#from(ByteInput)
     */
    public static BitOutputAdapter from(final ByteOutput output) {
        final BitOutputAdapter instance = new BitOutputAdapter(NULL_OUTPUT_SUPPLIER);
        instance.output = requireNonNull(output, "output is null");
        return instance;
    }

    /**
     * Creates a new instance with specified output supplier.
     *
     * @param outputSupplier the output supplier.
     */
    public BitOutputAdapter(final Supplier<? extends ByteOutput> outputSupplier) {
        super();
        this.outputSupplier = requireNonNull(outputSupplier, "outputSupplier is null");
    }

    @Override
    public void writeInt(final boolean unsigned, int size, int value) throws IOException {
        requireValidSizeInt(unsigned, size);
        if (!unsigned) {
            writeInt(true, 1, value < 0 ? 1 : 0);
            size--;
            if (size > 0) {
                writeInt(true, size, value);
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
            throw new IllegalArgumentException("bytes(" + bytes + ") <= 0");
        }
        long bits = 0L; // number of bits padded
        if (available < Byte.SIZE) {
            bits += available; // must be prior to the below
            writeUnsignedInt(available, 0x00);
        }
        assert available == Byte.SIZE;
        if (bytes == 1) {
            return bits;
        }
        for (bytes = bytes - (int) (count % bytes); bytes > 0; bytes--) {
            writeUnsignedInt(Byte.SIZE, 0x00);
            bits += Byte.SIZE;
        }
        assert bytes == 0;
        return bits;
    }

    private void unsigned8(final int size, int value) throws IOException {
        final int required = size - available;
        if (required > 0) {
            unsigned8(available, value >> required);
            unsigned8(required, value);
            return;
        }
        octet <<= size;
        octet |= (value & ((1 << size) - 1));
        available -= size;
        if (available == 0) {
            assert octet >= 0 && octet < 256;
            output().write(octet);
            octet = 0x00;
            available = Byte.SIZE;
            count++;
        }
    }

    private ByteOutput output() {
        if (output == null) {
            output = outputSupplier.get();
        }
        return output;
    }

    private final Supplier<? extends ByteOutput> outputSupplier;

    private transient ByteOutput output;

    /**
     * The current octet.
     */
    private int octet;

    /**
     * The number of available bits in {@link #octet}.
     */
    private int available = Byte.SIZE;

    /**
     * The number of bytes written so far.
     */
    private long count;
}
