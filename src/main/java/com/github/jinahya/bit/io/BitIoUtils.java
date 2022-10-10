package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 - 2022 Jinahya, Inc.
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
import java.util.Arrays;

/**
 * Utilities for bit-io.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class BitIoUtils {

    // https://stackoverflow.com/a/680040/330457
    private static int log2(final long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("value(" + value + ") is not positive");
        }
        final int result = Long.SIZE - Long.numberOfLeadingZeros(value);
        assert result > 0;
        return result;
    }

    /**
     * Returns the number of required bits for specified value.
     *
     * @param value the value whose size is calculated.
     * @return the number of required bits for {@code value}; always positive.
     */
    public static int size(final long value) {
        if (value < 0) {
            return size(~value) + 1;
        }
        if (value == 0L) {
            return 1;
        }
        return log2(value);
    }

    /**
     * Reads an unsigned value of specified number of bits for a count.
     *
     * @param input a bit-input from a value is read.
     * @param size  the number of bits.
     * @return an unsigned value of {@code size} bits.
     * @throws IOException if an I/O error occurs.
     */
    static int readCount(final BitInput input, final int size) throws IOException {
        assert input != null;
        return input.readInt(true, size);
    }

    /**
     * Writes specified unsigned value of specified number of bits for a count.
     *
     * @param output a bit-output to which the value is written.
     * @param size   the number of bits.
     * @param value  an unsigned value of {@code size} bits.
     * @return the {@value} right-shifted by {@code size}.
     * @throws IOException if an I/O error occurs.
     */
    static int writeCount(final BitOutput output, final int size, final int value) throws IOException {
        assert output != null;
        assert value >= 0;
        output.writeInt(true, size, value);
        return value & (-1 >>> (Integer.SIZE - size));
    }

    private static final int[] BIT_MASKS = new int[30]; // (size -1) 를 피하기 위해 [0] 은 버린다.

    static {
        Arrays.fill(BIT_MASKS, 0);
    }

    static int bitMaskSingle(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("non-positive size: " + size);
        }
        if (size > Integer.SIZE) {
            throw new IllegalArgumentException("size(" + size + ") > " + Integer.SIZE);
        }
        if (size == 1) {
            return 0x00000001;
        }
        if (size == Integer.SIZE) {
            return 0xFFFFFFFF;
        }
        final int index = size - 2; // size = 2 -> index = 0;
        final int bitMask = BIT_MASKS[index];
        if (bitMask > 0) {
            return bitMask;
        }
        BIT_MASKS[index] = -1 >>> (Integer.SIZE - size);
        assert BIT_MASKS[index] > 0;
        return bitMaskSingle(size);
    }

    static long bitMaskDouble(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("non-positive size: " + size);
        }
        if (size > Long.SIZE) {
            throw new IllegalArgumentException("size(" + size + ") > " + Long.SIZE);
        }
        if (size == Long.SIZE) {
            return 0xFFFFFFFFFFFFFFFFL;
        }
        if (size <= Integer.SIZE) {
            return bitMaskSingle(size) & 0xFFFFFFFFL;
        }
        return (bitMaskDouble(size - Integer.SIZE) << Integer.SIZE) | bitMaskDouble(Integer.SIZE);
    }

    private BitIoUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
