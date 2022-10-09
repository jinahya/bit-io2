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
 * An interface for reading values of an arbitrary number of bits.
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
     * for {@code 0b1} and {@code false} for {@code 0b0}
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
     * @return a {@code byte} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #readInt(boolean, int)} method with given arguments, and
     * returns the result as a {@code byte} value.
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
     * @return a {@code short} value of specified {@code size}.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #readInt(boolean, int)} method with given arguments, and
     * returns the result as a {@code short} value.
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
     * @return an {@code int} value of specified {@code size}.
     * @throws IOException if an I/O error occurs.
     */
    int readInt(boolean unsigned, int size) throws IOException;

    /**
     * Reads a {@code long} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Long#SIZE} -
     *                 ({@code unsigned ? 1: 0})), both inclusive.
     * @return a {@code long} value of specified number of bits.
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
     * @return a {@code char} value read.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #readInt(boolean, int)} method with given {@code size}, and
     * returns the result as a {@code char}.
     */
    default char readChar(final int size) throws IOException {
        BitIoConstraints.requireValidSizeForChar(size);
        return (char) readInt(true, size);
    }

    default float readFloat(final int exponentSize, final int significandSize) throws IOException {
        FloatConstraints.requireValidExponentSize(exponentSize);
        FloatConstraints.requireValidSignificandSize(significandSize);
        int bits = readInt(true, 1) << FloatConstants.SHIFT_SIGN_BIT;
        bits |= FloatReader.readExponentBits(this, exponentSize);
        bits |= FloatReader.readSignificandBits(this, significandSize);
        return Float.intBitsToFloat(bits);
    }

    /**
     * Reads a zero value of {@code float}.
     *
     * @return a zero value of {@code float} read.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeFloatOfZero(int)
     */
    default float readFloatOfZero() throws IOException {
        return Float.intBitsToFloat(FloatReader.readZeroBits(this));
    }

    /**
     * Reads an infinity value of {@code float}.
     *
     * @return a zero value of {@code float} read.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeFloatOfInfinity(int)
     */
    default float readFloatOfInfinity() throws IOException {
        return FloatReader.readInfinity(this);
    }

    /**
     * .
     *
     * @param size .
     * @return .
     * @throws IOException if an I/O error occurs.
     */
    default float readFloatOfNaN(final int size) throws IOException {
        FloatConstraints.requireValidSignificandSize(size);
        return Float.intBitsToFloat(FloatReader.readSignificandBits(this, size) | FloatConstants.MASK_EXPONENT);
    }

    default double readDouble(final int exponentSize, final int significandSize) throws IOException {
        DoubleConstraints.requireValidExponentSize(exponentSize);
        DoubleConstraints.requireValidSignificandSize(significandSize);
        long bits = readLong(true, 1) << DoubleConstants.SHIFT_SIGN_BIT;
        bits |= DoubleReader.readExponent(this, exponentSize);
        bits |= DoubleReader.readSignificand(this, significandSize);
        return Double.longBitsToDouble(bits);
    }

    /**
     * Reads a zero value of {@code double}.
     *
     * @return a zero value of {@code double} read.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeDoubleOfZero(long)
     */
    default double readDoubleOfZero() throws IOException {
        return Double.longBitsToDouble(DoubleReader.readZeroBits(this));
    }

    /**
     * Reads an infinity value of {@code double}.
     *
     * @return a zero value of {@code double} read.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeDoubleOfInfinity(long)
     */
    default double readDoubleOfInfinity() throws IOException {
        return DoubleReader.readInfinity(this);
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
     * Skips by reading (and discarding) specified number of bits.
     *
     * @param bits the number of bits to skip; must be positive.
     * @throws IllegalArgumentException if {@code bits} is not positive.
     * @throws IOException              if an I/O error occurs.
     */
    default void skip(int bits) throws IOException {
        if (bits <= 0) {
            throw new IllegalArgumentException("bits(" + bits + ") is not positive");
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
}
