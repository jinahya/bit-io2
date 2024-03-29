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

/**
 * An interface for reading values of arbitrary number of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BitOutput
 */
public interface BitInput {

    /**
     * Reads a {@code boolean} value.
     *
     * @return the value read.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation reads a {@code 1}-bit unsigned {@code int} value, and returns {@code true}
     * for {@code 0b1} and {@code false} for {@code 0b0}.
     */
    default boolean readBoolean() throws IOException {
        return readInt(true, 1) == 0x01;
    }

    /**
     * Reads a {@code byte} value of specified number of bits.
     *
     * @param unsigned a flag for indicating an unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Byte#SIZE} -
     *                 ({@code unsigned ? 1 : 0})), both inclusive.
     * @return a {@code byte} value of {@code size} bits.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #readInt(boolean, int)} method with given arguments, and
     * returns the result cast as a {@code byte} value.
     */
    default byte readByte(final boolean unsigned, final int size) throws IOException {
        BitIoConstraints.requireValidSizeForByte(unsigned, size);
        return (byte) readInt(unsigned, size);
    }

    /**
     * Reads a {@code short} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Short#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @return a {@code short} value of {@code size} bits.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #readInt(boolean, int)} method with given arguments, and
     * returns the result cast as a {@code short} value.
     */
    default short readShort(final boolean unsigned, final int size) throws IOException {
        BitIoConstraints.requireValidSizeForShort(unsigned, size);
        return (short) readInt(unsigned, size);
    }

    /**
     * Reads an {@code int} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Integer#SIZE} -
     *                 ({@code unsigned ? 1: 0})), both inclusive.
     * @return an {@code int} value of {@code size} bits.
     * @throws IOException if an I/O error occurs.
     */
    int readInt(boolean unsigned, int size) throws IOException;

    /**
     * Reads a {@code long} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Long#SIZE} -
     *                 ({@code unsigned ? 1: 0})), both inclusive.
     * @return a {@code long} value of {@code size} bits.
     * @throws IOException if an I/O error occurs.
     */
    default long readLong(final boolean unsigned, int size) throws IOException {
        BitIoConstraints.requireValidSizeForLong(unsigned, size);
        long value = 0L;
        if (!unsigned) {
            value -= readInt(true, 1);
            if (--size > 0) {
                value <<= size;
                value |= readLong(true, size);
            }
            return value;
        }
        if (size >= Integer.SIZE) {
            value = readInt(false, Integer.SIZE) & 0xFFFFFFFFL;
            size -= Integer.SIZE;
        }
        if (size > 0) {
            value <<= size;
            value |= readInt(true, size);
        }
        return value;
    }

    /**
     * Reads a {@code char} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Character#SIZE}, both inclusive.
     * @return a {@code char} value of {@code size} bits.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #readInt(boolean, int)} method with {@code true} and
     * {@code size}, and returns the result cast as a {@code char}.
     */
    default char readChar(final int size) throws IOException {
        BitIoConstraints.requireValidSizeForChar(size);
        return (char) readInt(true, size);
    }

    /**
     * Reads a {@code float} value.
     *
     * @param exponentSize    the number of lower exponent bits to read; between
     *                        {@value FloatConstants#SIZE_MIN_EXPONENT} and {@value FloatConstants#SIZE_EXPONENT}, both
     *                        inclusive.
     * @param significandSize the number of left-most significand bits to read; between
     *                        {@value FloatConstants#SIZE_MIN_SIGNIFICAND} and {@value FloatConstants#SIZE_SIGNIFICAND},
     *                        both inclusive.
     * @return the {@code float} value read.
     * @throws IOException if a I/O error occurs.
     * @see BitOutput#writeFloat(int, int, float)
     */
    default float readFloat(final int exponentSize, final int significandSize) throws IOException {
        FloatConstraints.requireValidExponentSize(exponentSize);
        FloatConstraints.requireValidSignificandSize(significandSize);
        if (exponentSize == FloatConstants.SIZE_EXPONENT && significandSize == FloatConstants.SIZE_SIGNIFICAND) {
            return Float.intBitsToFloat(readInt(false, Integer.SIZE));
        }
        return Float.intBitsToFloat(
                (readInt(true, 1) << FloatConstants.SHIFT_SIGN_BIT)
                | (readInt(true, exponentSize) << FloatConstants.SIZE_SIGNIFICAND)
                | (readInt(true, significandSize) << (FloatConstants.SIZE_SIGNIFICAND - significandSize))
        );
    }

    /**
     * Reads a {@code double} value.
     *
     * @param exponentSize    the number of lower exponent bits to read; between
     *                        {@value DoubleConstants#SIZE_MIN_EXPONENT} and {@value DoubleConstants#SIZE_EXPONENT},
     *                        both inclusive.
     * @param significandSize the number of left-most significand bits to read; between
     *                        {@value DoubleConstants#SIZE_MIN_SIGNIFICAND} and
     *                        {@value DoubleConstants#SIZE_SIGNIFICAND}, both inclusive.
     * @return the {@code double} value read.
     * @throws IOException if a I/O error occurs.
     * @see BitOutput#writeDouble(int, int, double)
     */
    default double readDouble(final int exponentSize, final int significandSize) throws IOException {
        DoubleConstraints.requireValidExponentSize(exponentSize);
        DoubleConstraints.requireValidSignificandSize(significandSize);
        if (exponentSize == DoubleConstants.SIZE_EXPONENT && significandSize == DoubleConstants.SIZE_SIGNIFICAND) {
            return Double.longBitsToDouble(readLong(false, Long.SIZE));
        }
        return Double.longBitsToDouble(
                (readLong(true, 1) << DoubleConstants.SHIFT_SIGN_BIT)
                | (readLong(true, exponentSize) << DoubleConstants.SIZE_SIGNIFICAND)
                | (readLong(true, significandSize) << (DoubleConstants.SIZE_SIGNIFICAND - significandSize))
        );
    }

    /**
     * Reads a value using specified reader.
     *
     * @param reader the reader.
     * @param <T>    value type parameter
     * @return a value read.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link BitReader#read(BitInput)} method on {@code reader} with
     * {@code this}, and returns the result.
     */
    default <T> T readObject(final BitReader<? extends T> reader) throws IOException {
        Objects.requireNonNull(reader, "reader is null");
        return reader.read(this);
    }

    /**
     * Reads (and discards) specified number of bits.
     *
     * @param bits the number of bits to read; must be positive.
     * @throws IllegalArgumentException if {@code bits} is not positive.
     * @throws IOException              if an I/O error occurs.
     * @see BitOutput#skip(int)
     */
    default void skip(int bits) throws IOException {
        if (bits <= 0) {
            throw new IllegalArgumentException("not positive; bits: " + bits);
        }
        for (; bits >= Integer.SIZE; bits -= Integer.SIZE) {
            readInt(false, Integer.SIZE);
        }
        if (bits > 0) {
            readInt(true, bits);
        }
    }

    /**
     * Aligns to specified number of bytes by reading (and discarding) required number of bits.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of bits discarded; non-negative, always.
     * @throws IllegalArgumentException if {@code bytes} is not positive.
     * @throws IOException              if an I/O error occurs.
     */
    long align(int bytes) throws IOException;

    /**
     * Resets the total number of bytes read so far to {@code 0}.
     *
     * @throws IllegalStateException if this input is not {@link #align(int) aligned}.
     */
    void reset();
}
