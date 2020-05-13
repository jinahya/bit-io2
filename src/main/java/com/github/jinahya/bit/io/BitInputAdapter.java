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
 * An implementation of {@link BitInput} adapts an instance of {@link ByteInput}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitOutputAdapter
 */
public class BitInputAdapter extends BitBase implements BitInput {

    /**
     * Creates a new instance with specified input supplier.
     *
     * @param inputSupplier the input supplier.
     */
    public BitInputAdapter(final Supplier<? extends ByteInput> inputSupplier) {
        super();
        this.inputSupplier = requireNonNull(inputSupplier, "inputSupplier is null");
    }

    @Override
    public int readInt(final boolean unsigned, int size) throws IOException {
        requireValidSizeInt(unsigned, size);
        int value = 0;
        if (!unsigned) {
            value -= readInt(true, 1);
            if (--size > 0) {
                value <<= size;
                value |= readInt(true, size);
            }
            return value;
        }
        for (; size >= Byte.SIZE; size -= Byte.SIZE) {
            value <<= Byte.SIZE;
            value |= unsigned8(Byte.SIZE);
        }
        if (size > 0) {
            value <<= size;
            value |= unsigned8(size);
        }
        return value;
    }

    @Override
    public long align(int bytes) throws IOException {
        if (bytes <= 0) {
            throw new IllegalArgumentException("bytes(" + bytes + ") <= 0");
        }
        long bits = 0L; // number of discarded bits
        if (available > 0) {
            bits += available; // must be prior to the below
            readInt(true, available);
        }
        if (bytes == 1) {
            return bits;
        }
        for (bytes = bytes - (int) (count % bytes); bytes > 0L; bytes--) {
            readInt(true, Byte.SIZE);
            bits += Byte.SIZE;
        }
        return bits;
    }

    /**
     * Reads an unsigned {@code int} value of specified bit size.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Byte#SIZE}, both inclusive.
     * @return an unsigned {@code int} value.
     * @throws IOException if an I/O error occurs.
     */
    private int unsigned8(final int size) throws IOException {
        assert size > 0;
        assert size <= Byte.SIZE;
        if (available == 0) {
            octet = input().read();
            assert octet >= 0 && octet < 256;
            count++;
            available = Byte.SIZE;
            update(octet);
        }
        final int required = size - available;
        if (required > 0) {
            return (unsigned8(available) << required) | unsigned8(required);
        }
        available -= size;
        return (octet >> available) & mask(size);
    }

    /**
     * Returns an instance of {@link ByteInput}.
     *
     * @return an instance of {@link ByteInput}.
     */
    private ByteInput input() {
        if (input == null) {
            input = inputSupplier.get();
        }
        return input;
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
     * A supplier for {@link #input}.
     */
    private final Supplier<? extends ByteInput> inputSupplier;

    /**
     * A value supplied from {@link #inputSupplier}.
     *
     * @see #input()
     */
    private ByteInput input;

    /**
     * The current octet.
     */
    private int octet;

    /**
     * The number of available bits in {@link #octet}.
     */
    private int available = 0;

    /**
     * The number of bytes read from {@link #input} so far.
     */
    private long count;
}
