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
import java.security.MessageDigest;
import java.util.function.Supplier;
import java.util.zip.Checksum;

import static com.github.jinahya.bit.io.BitIoConstants.mask;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static java.util.Objects.requireNonNull;

/**
 * An implementation of {@link BitOutput} adapts an instance of {@link ByteOutput}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitInputAdapter
 */
public class BitOutputAdapter extends BitBase implements BitOutput {

    /**
     * Creates a new instance with specified output supplier.
     *
     * @param outputSupplier the output supplier.
     * @see BitInputAdapter#BitInputAdapter(Supplier)
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
            if (--size > 0) {
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
     * Writes specified unsigned value of specified bit size.
     *
     * @param size  the number of bits to write; between {@code 1} and {@value java.lang.Byte#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     */
    private void unsigned8(final int size, final int value) throws IOException {
        assert size > 0;
        assert size <= Byte.SIZE;
        final int required = size - available;
        if (required > 0) {
            unsigned8(available, value >> required);
            unsigned8(required, value);
            return;
        }
        octet <<= size;
        octet |= value & mask(size);
        available -= size;
        if (available == 0) {
            update(octet);
            assert octet >= 0 && octet < 256;
            output().write(octet);
            count++;
            octet = 0x00;
            available = Byte.SIZE;
        }
    }

    /**
     * Returns an instance of {@link ByteOutput}.
     *
     * @return an instance of {@link ByteOutput}.
     */
    private ByteOutput output() {
        if (output == null) {
            output = outputSupplier.get();
        }
        return output;
    }

    @Override
    public boolean attach(final Checksum checksum) {
        return attach(Checksum.class, checksum);
    }

    @Override
    public boolean detach(final Checksum checksum) {
        return detach(Checksum.class, checksum);
    }

    @Override
    public boolean attach(final MessageDigest messageDigest) {
        return attach(MessageDigest.class, messageDigest);
    }

    @Override
    public boolean detach(final MessageDigest messageDigest) {
        return detach(MessageDigest.class, messageDigest);
    }

    /**
     * The supplier for {@link #output}.
     */

    private final Supplier<? extends ByteOutput> outputSupplier;

    /**
     * A value supplied from {@link #outputSupplier}.
     *
     * @see #output()
     */
    private ByteOutput output;

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
