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
import java.util.Objects;

/**
 * Utilities for reading/writing bits.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public final class BitIoUtils {

    /**
     * Reads a {@value Short#SIZE}-bit unsigned count value from specified input.
     *
     * @param input the input from which the count value is read.
     * @return a count value read.
     * @throws IOException if an I/O error occurs.
     */
    public static int readCountShort(final BitInput input) throws IOException {
        Objects.requireNonNull(input, BitIoConstants.MESSAGE_INPUT_IS_NULL);
        return input.readInt(true, Short.SIZE);
    }

    /**
     * Writes specified count value as a {@value Short#SIZE}-bit unsigned value to specified output.
     *
     * @param output the output to which the {@code count} is written.
     * @param count  the value to write.
     * @throws IOException if an I/O error occurs.
     */
    public static void writeCountShort(final BitOutput output, final int count) throws IOException {
        Objects.requireNonNull(output, BitIoConstants.MESSAGE_OUTPUT_IS_NULL);
        output.writeInt(true, Short.SIZE, count);
    }

    private static final int SIZE_COUNT = 31;

    /**
     * Reads a {@code 31}-bit unsigned count value from specified input.
     *
     * @param input the input from which the count value is read.
     * @return a count value read.
     * @throws IOException if an I/O error occurs.
     */
    public static int readCount(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        return input.readInt(true, SIZE_COUNT);
    }

    /**
     * Writes specified count value as a {@code 31}-bit unsigned value to specified output.
     *
     * @param output the output to which the {@code count} is written.
     * @param count  the value to write.
     * @throws IOException if an I/O error occurs.
     */
    public static void writeCount(final BitOutput output, final int count) throws IOException {
        Objects.requireNonNull(output, "output is null");
        if (count < 0) {
            throw new IllegalArgumentException("negative count: " + count);
        }
        output.writeInt(true, SIZE_COUNT, count);
    }

    private static final int SIZE_SIZE_COUNT_COMPRESSED = 4;

    /**
     * Writes specified count value to specified output, in a compressed manner.
     *
     * @param output the output to which the {@code count} value is written.
     * @param count  the value to write.
     * @throws IOException if an I/O error occurs.
     */
    public static void writeCountCompressed(final BitOutput output, final int count) throws IOException {
        Objects.requireNonNull(output, "output is null");
        if (count < 0) {
            throw new IllegalArgumentException("negative count: " + count);
        }
        if (count == 0) {
            output.writeBoolean(true); // zero
            return;
        }
        output.writeBoolean(false); // not zero
        if (count <= 65536) {
            output.writeBoolean(true); // compressed
            int size = Integer.SIZE - Integer.numberOfLeadingZeros(count - 1);
            if (size == 0) {
                size++;
            }
            assert size <= (Short.SIZE + 1); // [1..16]
            output.writeInt(true, SIZE_SIZE_COUNT_COMPRESSED, size - 1); // 4 bits for [0..15]
            output.writeInt(true, size, count - 1); // 16 bits in maximum
            return;
        }
        output.writeBoolean(false); // uncompressed
        writeCount(output, count);
    }

    /**
     * Reads a count value from specified input, in a compressed-manner.
     *
     * @param input the input from which the count value is read.
     * @return the count value read.
     * @throws IOException if an I/O error occurs.
     */
    public static int readCountCompressed(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        if (input.readBoolean()) { // zero
            return 0;
        }
        if (input.readBoolean()) { // compressed
            final int size = input.readInt(true, SIZE_SIZE_COUNT_COMPRESSED) + 1; // [0..15] + 1 -> [1..16]
            assert size > 0 && size <= 16;
            final int count = input.readInt(true, size) + 1;
            assert count > 0;
            assert count <= 65536;
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

    /**
     * Writes specified value, as VLC-encoded, to specified output.
     *
     * @param output the output to which encoded octets are written.
     * @param value  the value to write; must be non-negative.
     * @throws IOException if an I/O error occurs.
     * @see <a href="https://en.wikipedia.org/wiki/Variable-length_quantity">Variable-length quantity</a> (Wikipedia)
     * @see #readVlq(BitInput)
     * @see #writeVlqLong(BitOutput, long)
     */
    public static void writeVlq(final BitOutput output, int value) throws IOException {
        if (value < 0) {
            throw new IllegalArgumentException("value(" + value + ") negative");
        }
        writeVlqLong(output, value);
    }

    /**
     * Reads a VLC-encoded value from specified input.
     *
     * @param input the input from which value is read.
     * @return the value read from the {@code input}; not negative, always.
     * @throws IOException if an I/O error occurs.
     * @see <a href="https://en.wikipedia.org/wiki/Variable-length_quantity">Variable-length quantity</a> (Wikipedia)
     * @see #writeVlq(BitOutput, int)
     * @see #readVlqLong(BitInput)
     */
    public static int readVlq(final BitInput input) throws IOException {
        return Math.toIntExact(readVlqLong(input));
    }

    /**
     * Writes specified value, as VLC-encoded, to specified output.
     *
     * @param output the output to which encoded octets are written.
     * @param value  the value to write; must be non-negative.
     * @throws IOException if an I/O error occurs.
     * @see <a href="https://en.wikipedia.org/wiki/Variable-length_quantity">Variable-length quantity</a> (Wikipedia)
     * @see #readVlqLong(BitInput)
     * @see #writeVlq(BitOutput, int)
     */
    public static void writeVlqLong(final BitOutput output, long value) throws IOException {
        if (value < 0L) {
            throw new IllegalArgumentException("value(" + value + ") negative");
        }
        if (value == 0L) {
            output.writeLong(true, Byte.SIZE, 0L);
            return;
        }
        final int ones = Long.SIZE - Long.numberOfLeadingZeros(value);
        final int quotient = ones / 7;
        final int remainder = ones % 7;
        final byte[] bytes = new byte[quotient + (remainder > 0 ? 1 : 0)];
        int index = 0;
        if (quotient > 0) {
            bytes[index++] = (byte) (value & 0x7FL); // last octet
            value >>= 7;
        }
        for (int i = 1; i < quotient; i++) {
            bytes[index++] = (byte) (0x80L | (value & 0x7FL)); // intermediate octets
            value >>= 7;
        }
        if (remainder > 0) {
            bytes[index++] = (byte) ((quotient > 0 ? 0x80L : 0x00L) | (value & 0x7FL)); // first octet
        }
        assert index == bytes.length;
        for (int i = index - 1; i >= 0; i--) {
            output.writeInt(true, Byte.SIZE, bytes[i]);
        }
    }

    /**
     * Reads a VLC-encoded value from specified input.
     *
     * @param input the input from which value is read.
     * @return the value read from the {@code input}; not negative, always.
     * @throws IOException if an I/O error occurs.
     * @see <a href="https://en.wikipedia.org/wiki/Variable-length_quantity">Variable-length quantity</a> (Wikipedia)
     * @see #writeVlqLong(BitOutput, long)
     * @see #readVlq(BitInput)
     */
    public static long readVlqLong(final BitInput input) throws IOException {
        long value = 0L;
        while (true) {
            final int last = input.readInt(true, 1);
            value <<= 7;
            value |= input.readLong(true, 7);
            if (last == 0) {
                break;
            }
        }
        return value;
    }

    private BitIoUtils() {
        throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
