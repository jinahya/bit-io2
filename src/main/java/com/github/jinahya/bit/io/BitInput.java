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

//    /**
//     * Reads a signed {@code byte} value of specified number of bits.
//     *
//     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Byte#SIZE}, both inclusive.
//     * @return a signed {@code byte} value of specified {@code size}.
//     * @throws IOException if an I/O error occurs.
//     * @implSpec The default implementation invokes {@link #readByte(boolean, int)} method with {@code false} and
//     * {@code size}, and returns the result.
//     */
//    default byte readByte(final int size) throws IOException {
//        return readByte(false, size);
//    }
//
//    /**
//     * Reads a signed {@value java.lang.Byte#SIZE}-bit {@code byte} value.
//     *
//     * @return a signed {@value java.lang.Byte#SIZE}-bit {@code byte} value.
//     * @throws IOException if an I/O error occurs.
//     * @implSpec The default implementation invokes {@link #readByte(int)} method with {@value java.lang.Byte#SIZE}, and
//     * returns the result.
//     */
//    default byte readByte() throws IOException {
//        return readByte(Byte.SIZE);
//    }
//
//    /**
//     * Reads an unsigned {@code byte} value of specified number of bits.
//     *
//     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Byte#SIZE}
//     *             (exclusive).
//     * @return an unsigned {@code byte} value of specified bit {@code size}.
//     * @throws IOException if an I/O error occurs.
//     * @implSpec The default implementation invokes {@link #readByte(boolean, int)} method with {@code true} and given
//     * {@code size}, and returns the result.
//     */
//    default byte readUnsignedByte(final int size) throws IOException {
//        return readByte(true, size);
//    }

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

//    /**
//     * Reads a signed {@code short} value of specified number of bits.
//     *
//     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Short#SIZE}, both inclusive.
//     * @return a signed {@code short} value of specified bit {@code size}.
//     * @throws IOException if an I/O error occurs.
//     * @implSpec The default implementation invokes {@link #readShort(boolean, int)} method with {@code false} and
//     * {@code size}, and returns the result.
//     * @see #readShort(boolean, int)
//     */
//    default short readShort(final int size) throws IOException {
//        return readShort(false, size);
//    }
//
//    /**
//     * Reads a signed {@value java.lang.Short#SIZE}-bit {@code short} value.
//     *
//     * @return a {@value java.lang.Short#SIZE}-bit {@code short} value read.
//     * @throws IOException if an I/O error occurs.
//     * @implSpec The default implementation invokes {@link #readShort(int)} method with {@value java.lang.Short#SIZE},
//     * and returns the result.
//     */
//    default short readShort() throws IOException {
//        return readShort(Short.SIZE);
//    }
//
//    /**
//     * Reads an unsigned {@code short} value of specified number of bits.
//     *
//     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Short#SIZE}
//     *             (exclusive).
//     * @return an unsigned {@code short} value of specified bit {@code size}.
//     * @throws IOException if an I/O error occurs.
//     * @implSpec The default implementation invokes {@link #readShort(boolean, int)} method with {@code true} and
//     * {@code size}, and returns the result.
//     */
//    default short readUnsignedShort(final int size) throws IOException {
//        return readShort(true, size);
//    }

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

//    /**
//     * Reads a signed {@code int} value of specified number of bits.
//     *
//     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Integer#SIZE}, both inclusive.
//     * @return a signed {@code int} value.
//     * @throws IOException if an I/O error occurs.
//     * @implSpec The default implementation invokes {@link #readInt(boolean, int)} method with {@code false} and given
//     * {@code size}, and returns the result.
//     */
//    default int readInt(final int size) throws IOException {
//        return readInt(false, size);
//    }
//
//    /**
//     * Reads a signed {@value java.lang.Integer#SIZE}-bit {@code int} value.
//     *
//     * @return a {@value java.lang.Integer#SIZE}-bit {@code int} value read.
//     * @throws IOException if an I/O error occurs.
//     * @implSpec The default implementation invokes {@link #readInt(int)} method with {@value java.lang.Integer#SIZE},
//     * and returns the result.
//     */
//    default int readInt() throws IOException {
//        return readInt(Integer.SIZE);
//    }
//
//    /**
//     * Reads an unsigned {@code int} value of specified number of bits.
//     *
//     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Integer#SIZE}
//     *             (exclusive).
//     * @return an unsigned {@code int} value.
//     * @throws IOException if an error occurs.
//     * @implSpec The default implementation invokes {@link #readInt(boolean, int)} method with {@code true} and
//     * {@code size}, and returns the result.
//     */
//    default int readUnsignedInt(final int size) throws IOException {
//        return readInt(true, size);
//    }

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

//    /**
//     * Reads a signed {@code long} value of specified number of bits.
//     *
//     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Long#SIZE}, both inclusive.
//     * @return a signed {@code long} value.
//     * @throws IOException if an I/O error occurs.
//     * @implSpec The default implementation invokes {@link #readLong(boolean, int)} method with {@code false} and given
//     * {@code size}, and returns the result.
//     */
//    default long readLong(final int size) throws IOException {
//        return readLong(false, size);
//    }
//
//    /**
//     * Reads a signed {@value java.lang.Long#SIZE}-bit {@code long} value.
//     *
//     * @return a {@value java.lang.Long#SIZE}-bit {@code long} value.
//     * @throws IOException if an I/O error occurs.
//     * @implSpec The default implementation invokes {@link #readLong(int)} method with {@value java.lang.Long#SIZE}, and
//     * returns the result.
//     */
//    default long readLong() throws IOException {
//        return readLong(Long.SIZE);
//    }
//
//    /**
//     * Reads an unsigned {@code long} value of specified number of bits.
//     *
//     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Long#SIZE}
//     *             (exclusive).
//     * @return an unsigned {@code long} value.
//     * @throws IOException if an error occurs.
//     * @implSpec The default implementation invokes {@link #readLong(boolean, int)} method with {@code true} and given
//     * {@code size}, and returns the result.
//     */
//    default long readUnsignedLong(final int size) throws IOException {
//        return readLong(true, size);
//    }

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

//    /**
//     * Reads a {@value java.lang.Character#SIZE}-bit {@code char} value.
//     *
//     * @return a {@code char} value.
//     * @throws IOException if an I/O error occurs.
//     * @implSpec The default implementation invokes {@link #readChar(int)} method with
//     * {@value java.lang.Character#SIZE}, and returns the result.
//     */
//    default char readChar() throws IOException {
//        return readChar(Character.SIZE);
//    }

    /**
     * Reads a {@value java.lang.Float#SIZE}-bit {@code float} value.
     *
     * @return a {@value java.lang.Float#SIZE}-bit {@code float} value
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation reads a {@value java.lang.Integer#SIZE}-bit signed {@code int} value, and
     * returns the value as a {@code float} value converted using {@link Float#intBitsToFloat(int)} method.
     */
    default float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt(false, Integer.SIZE));
    }

    /**
     * Reads a {@value java.lang.Double#SIZE}-bit {@code double} value.
     *
     * @return a {@value java.lang.Double#SIZE}-bit {@code double} value
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation reads a {@value java.lang.Long#SIZE}-bit signed {@code long} value, and
     * returns the value as a {@code double} value converted using {@link Double#longBitsToDouble(long)} method.
     */
    default double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong(true, Long.SIZE));
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

//    /**
//     * Aligns to a single byte by reading (and discarding) required number of bits.
//     *
//     * @return the number of bits discarded; between {@code 0} (inclusive) and {@value java.lang.Byte#SIZE} (exclusive).
//     * @throws IOException if an I/O error occurs.
//     * @implSpec The default implementation invokes {@link #align(int)} method with {@value java.lang.Byte#BYTES}, and
//     * returns the result.
//     * @see #align(int)
//     */
//    default long align() throws IOException {
//        final long discarded = align(Byte.BYTES);
//        assert discarded >= 0L;
//        assert discarded < Byte.SIZE;
//        return discarded;
//    }
}
