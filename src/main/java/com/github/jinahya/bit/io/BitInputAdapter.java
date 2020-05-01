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
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeUnsigned8;
import static java.util.Objects.requireNonNull;

/**
 * An implementation of {@link BitInput} adapting an instance of {@link ByteInput}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitOutputAdapter
 */
public class BitInputAdapter implements BitInput {

    // TODO: 2020-05-01 required? preferred?
    static final Supplier<ByteInput> NULL_INPUT_SUPPLIER = () -> null;

    /**
     * Creates a new instance which reads bytes directly from specified byte input.
     *
     * @param input the byte input from which bytes are read.
     * @return a new instance.
     * @see BitOutputAdapter#from(ByteOutput)
     */
    public static BitInputAdapter from(final ByteInput input) {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        final BitInputAdapter instance = new BitInputAdapter(NULL_INPUT_SUPPLIER);
        instance.input = input;
        return instance;
    }

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
            size--;
            if (size > 0) {
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
            readUnsignedInt(available);
        }
        assert available == 0;
        if (bytes == 1) {
            return bits;
        }
        for (bytes = bytes - (int) (count % bytes); bytes > 0L; bytes--) {
            readUnsignedInt(Byte.SIZE);
            bits += Byte.SIZE;
        }
        assert bytes == 0;
        return bits;
    }

    private int unsigned8(final int size) throws IOException {
        requireValidSizeUnsigned8(size); // TODO: 2020-04-24 remove!!!
        if (available == 0) {
            octet = input().read();
            assert octet >= 0 && octet < 256;
            available = Byte.SIZE;
            count++;
        }
        final int required = size - available;
        if (required > 0) {
            return (unsigned8(available) << required) | unsigned8(required);
        }
        return (octet >> (available -= size)) & ((1 << size) - 1);
    }

    private ByteInput input() {
        if (input == null) {
            input = inputSupplier.get();
        }
        return input;
    }

    private final Supplier<? extends ByteInput> inputSupplier;

    private transient ByteInput input;

    /**
     * The current octet.
     */
    private int octet;

    /**
     * The number of available bits in {@link #octet}.
     */
    private int available = 0;

    /**
     * The number of bytes read so far.
     */
    private long count;
}
