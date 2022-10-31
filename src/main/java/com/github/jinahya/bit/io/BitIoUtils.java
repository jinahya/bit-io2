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

    private static final int SIZE_COUNT = 31;

    static void writeCount(final BitOutput output, final int count) throws IOException {
        assert output != null;
        assert count >= 0;
        output.writeInt(true, SIZE_COUNT, count);
    }

    static int readCount(final BitInput input) throws IOException {
        assert input != null;
        return input.readInt(true, SIZE_COUNT);
    }

    private static final int SIZE_SIZE_COUNT_COMPRESSED = 4;

    static void writeCountCompressed(final BitOutput output, final int count) throws IOException {
        assert output != null;
        assert count >= 0;
        if (count == 0) {
            output.writeBoolean(true); // zero
            return;
        }
        output.writeBoolean(false); // not zero
        if (count < 65536) { // 21 bits in maximum; 1 + 4 + 16
            output.writeBoolean(true); // compressed
            final int size = Integer.SIZE - Integer.numberOfLeadingZeros(count);
            assert size > 0 && size <= Short.SIZE; // [1..16]
            output.writeInt(true, SIZE_SIZE_COUNT_COMPRESSED, size - 1); // 4 bits for [0..15]
            output.writeInt(true, size, count); // 16 bits in maximum
            return;
        }
        output.writeBoolean(false); // uncompressed
        writeCount(output, count);
    }

    static int readCountCompressed(final BitInput input) throws IOException {
        if (input.readBoolean()) { // zero
            return 0;
        }
        if (input.readBoolean()) { // compressed
            final int size = input.readInt(true, SIZE_SIZE_COUNT_COMPRESSED) + 1; // [0..15] + 1 -> [1..16]
            assert size > 0 && size <= 16;
            final int count = input.readInt(true, size);
            assert count > 0;
            assert count < 65536;
            return count;
        }
        return readCount(input);
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
        throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
