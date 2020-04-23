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

import static com.github.jinahya.bit.io.BitIoConstants.SIZE_EXPONENT_BYTE;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeUnsigned8;
import static java.util.Objects.requireNonNull;

public class BitInputAdapter implements BitInput {

    // -----------------------------------------------------------------------------------------------------------------
    public static BitInput of(final ByteInput byteInput) {
        return new BitInputAdapter(() -> byteInput);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public BitInputAdapter(final Supplier<? extends ByteInput> inputSupplier) {
        super();
        this.inputSupplier = requireNonNull(inputSupplier, "inputSupplier is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
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
        for (int i = size >> SIZE_EXPONENT_BYTE; i > 0; i--) {
            value <<= Byte.SIZE;
            value |= unsigned8(Byte.SIZE);
        }
        final int remainder = size & (Byte.SIZE - 1);
        if (remainder > 0) {
            value <<= remainder;
            value |= unsigned8(remainder);
        }
        return value;
    }

    @Override
    public long align(int bytes) throws IOException {
        if (bytes <= 0) {
            throw new IllegalArgumentException("bytes(" + bytes + ") <= 0");
        }
        long bits = 0;
        if (available > 0) {
            bits += available;
            readInt(true, available);
        }
        assert available == 0;
        for (; (count % bytes) > 0L; bits += Byte.SIZE) {
            readInt(true, Byte.SIZE);
        }
        assert count % bytes == 0L;
        return bits;
    }

    // -----------------------------------------------------------------------------------------------------------------
    int unsigned8(final int size) throws IOException {
        requireValidSizeUnsigned8(size);
        if (available == 0) {
            octet = input().read();
            assert octet >= 0 && octet < 256;
            count++;
            available = Byte.SIZE;
        }
        final int required = size - available;
        if (required > 0) {
            return (unsigned8(available) << required) | unsigned8(required);
        }
        return (octet >> (available -= size)) & ((1 << size) - 1);
    }

    ByteInput input() {
        if (input == null) {
            input = inputSupplier.get();
        }
        if (input == null) {
            throw new RuntimeException("null input supplied");
        }
        return input;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final Supplier<? extends ByteInput> inputSupplier;

    private transient ByteInput input;

    /**
     * The current octet.
     */
    private int octet;

    /**
     * The number of available bits in {@link #octet} for reading.
     */
    private int available = 0;

    /**
     * The number of bytes read so far.
     */
    private long count;
}
