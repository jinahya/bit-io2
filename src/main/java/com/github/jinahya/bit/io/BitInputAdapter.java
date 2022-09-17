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
 * An implementation of {@link BitInput} adapts an instance of {@link ByteInput}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitOutputAdapter
 */
public class BitInputAdapter
        implements BitInput {

    /**
     * Creates a new instance which reads bytes from specified byte input.
     *
     * @param input the byte input from which bytes are read.
     * @return a new instance.
     * @apiNote Closing the result does not close the {@code input}.
     */
    @SuppressWarnings({"unchecked"})
    public static BitInput from(final ByteInput input) {
        Objects.requireNonNull(input, "input is null");
//        final BitInputAdapter instance = new BitInputAdapter(BitIoUtils.emptySupplier());
        final BitInputAdapter instance
                = new BitInputAdapter((Supplier<? extends ByteInput>) BitIoConstants.EMPTY_SUPPLIER());
        instance.input(input);
        return instance;
    }

    /**
     * Creates a new instance with specified input supplier.
     *
     * @param supplier the input supplier.
     */
    public BitInputAdapter(final Supplier<? extends ByteInput> supplier) {
        super();
        this.supplier = Objects.requireNonNull(supplier, "supplier is null");
    }

    /**
     * Closes this input and releases any system resources associated with it.
     *
     * @throws IOException if an I/O error occurs.
     * @see ByteInput#close()
     */
    @Override
    public void close() throws IOException {
        BitInput.super.close();
        if (close) {
            final ByteInput input = input(false);
            if (input != null) {
                input.close();
            }
        }
    }

    @Override
    public int readInt(final boolean unsigned, int size) throws IOException {
        BitIoConstraints.requireValidSizeForInt(unsigned, size);
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
            throw new IllegalArgumentException("bytes(" + bytes + ") is not positive");
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
     * Reads an unsigned {@code int} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Byte#SIZE}, both inclusive.
     * @return an unsigned {@code int} value.
     * @throws IOException if an I/O error occurs.
     */
    private int unsigned8(final int size) throws IOException {
        assert size > 0 && size <= Byte.SIZE;
        if (available == 0) {
            octet = input(true).read();
            assert octet >= 0 && octet < 256;
            count++;
            available = Byte.SIZE;
        }
        if (available == Byte.SIZE && size == Byte.SIZE) { // return, directly, a full 8-bit octet
            available = 0;
            return octet;
        }
        final int required = size - available;
        if (required > 0) {
            return (unsigned8(available) << required) | unsigned8(required);
        }
        available -= size;
        return (octet >> available) & BitIoConstants.mask(size);
    }

    private ByteInput input(final boolean get) {
        if (get) {
            if (input(false) == null) {
                input(supplier.get());
                close = true;
            }
            return input(false);
        }
        return input;
    }

    private void input(final ByteInput input) {
        if (input(false) != null) {
            throw new IllegalStateException("input already has been set");
        }
        this.input = Objects.requireNonNull(input, "input is null");
    }

    private final Supplier<? extends ByteInput> supplier;

    private ByteInput input = null;

    /**
     * a flag indicates that the {@code input} is supplied from the {@code supplier}; not directly set.
     */
    private boolean close = false;

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
