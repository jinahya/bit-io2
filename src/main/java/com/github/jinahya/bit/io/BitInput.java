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

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

/**
 * An interface for reading values of an arbitrary number of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BitOutput
 */
public interface BitInput
        extends Closeable {

    /**
     * Closes this input and releases any system resources associated with it.
     *
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation does nothing.
     */
    @Override
    default void close() throws IOException {
        // does nothing.
    }

    /**
     * Reads a {@code 1}-bit {@code boolean} value. This method reads a {@code 1}-bit unsigned {@code int} value and
     * returns {@code true} for {@code 0b1} and {@code false} for {@code 0b0}.
     *
     * @return the value read.
     * @throws IOException if an I/O error occurs.
     */
    default boolean readBoolean() throws IOException {
        return readUnsignedInt(1) == 0x01;
    }

    /**
     * Reads a {@code byte} value of specified number of bits.
     *
     * @param unsigned a flag for indicating an unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Byte#SIZE} - ({@code
     *                 unsigned ? 1 : 0})), both inclusive.
     * @return a {@code byte} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     */
    default byte readByte(final boolean unsigned, final int size) throws IOException {
        return (byte) readInt(unsigned, BitIoConstraints.requireValidSizeForByte(unsigned, size));
    }

    /**
     * Reads a signed {@code byte} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Byte#SIZE}, both inclusive.
     * @return a signed {@code byte} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see #readByte(boolean, int)
     */
    default byte readByte(final int size) throws IOException {
        return readByte(false, size);
    }

    /**
     * Reads a {@value java.lang.Byte#SIZE}-bit signed {@code byte} value. The {@code readByte8()} method of {@code
     * BitInput} interface invokes {@link #readByte(int)} method with {@value java.lang.Byte#SIZE} and returns the
     * result.
     *
     * @return a signed {@value java.lang.Byte#SIZE}-bit {@code byte} value.
     * @throws IOException if an I/O error occurs.
     * @see #readByte(int)
     */
    default byte readByte8() throws IOException {
        return readByte(Byte.SIZE);
    }

    /**
     * Reads an unsigned {@code byte} value of specified number of bits. The {@code readUnsignedByte(int)} method of
     * {@code BitInput} interface invokes {@link #readByte(boolean, int)} method with {@code true} and given {@code
     * size} argument and returns the result.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Byte#SIZE}
     *             (exclusive).
     * @return an unsigned {@code byte} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     */
    default byte readUnsignedByte(final int size) throws IOException {
        return readByte(true, size);
    }

    /**
     * Reads a {@code short} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Short#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @return a {@code short} value of specified {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see #readInt(boolean, int)
     */
    default short readShort(final boolean unsigned, final int size) throws IOException {
        return (short) readInt(unsigned, BitIoConstraints.requireValidSizeForShort(unsigned, size));
    }

    /**
     * Reads a signed {@code short} value of specified number of bits. The {@code readShort(int)} method of {@code
     * BitInput} interface invokes {@link #readShort(boolean, int)} method with {@code false} and given {@code size}
     * argument and returns the result.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Short#SIZE}, both inclusive.
     * @return a signed {@code short} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see #readShort(boolean, int)
     */
    default short readShort(final int size) throws IOException {
        return readShort(false, size);
    }

    /**
     * Reads a {@value java.lang.Short#SIZE}-bit signed {@code short} value. The {@code readShort16()} method of {@code
     * BitInput} interface invokes {@link #readShort(int)} method with {@value java.lang.Short#SIZE} and returns the
     * result.
     *
     * @return a {@value java.lang.Short#SIZE}-bit signed {@code short} value read.
     * @throws IOException if an I/O error occurs.
     * @see #readShort(int)
     */
    default short readShort16() throws IOException {
        return readShort(Short.SIZE);
    }

    /**
     * Reads an unsigned {@code short} value of specified number of bits. The {@code readUnsignedShort()} method of
     * {@code BitInput} interface invokes {@link #readShort(boolean, int)} with {@code true} and given {@code size}
     * argument and returns the result.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Short#SIZE}
     *             (exclusive).
     * @return an unsigned {@code short} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see #readShort(boolean, int)
     */
    default short readUnsignedShort(final int size) throws IOException {
        return readShort(true, size);
    }

    /**
     * Reads an {@code int} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Integer#SIZE} - ({@code
     *                 unsigned ? 1: 0})), both inclusive.
     * @return an {@code int} value of specified {@code size}.
     * @throws IOException if an I/O error occurs.
     */
    int readInt(boolean unsigned, int size) throws IOException;

    /**
     * Reads a signed {@code int} value of specified number of bits. The {@code readInt(int)} method of {@code BitInput}
     * interface invokes {@link #readInt(boolean, int)} with {@code false} and given {@code size} argument and returns
     * the result.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Integer#SIZE}, both inclusive.
     * @return a signed {@code int} value.
     * @throws IOException if an I/O error occurs.
     * @see #readInt(boolean, int)
     */
    default int readInt(final int size) throws IOException {
        return readInt(false, size);
    }

    /**
     * Reads a {@value java.lang.Integer#SIZE}-bit signed {@code int} value. The {@code readInt32()} method of {@code
     * BitInput} interface invokes {@link #readInt(int)} method with {@value java.lang.Integer#SIZE} and returns the
     * result.
     *
     * @return a {@value java.lang.Integer#SIZE}-bit signed {@code int} value read.
     * @throws IOException if an I/O error occurs.
     * @see #readInt(int)
     */
    default int readInt32() throws IOException {
        return readInt(Integer.SIZE);
    }

    /**
     * Reads an unsigned {@code int} value of specified number of bits. The {@code readUnsignedInt(int)} method of
     * {@code BitInput} interface invokes {@link #readInt(boolean, int)} method with {@code true} and {@code size}
     * argument and returns the result.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Integer#SIZE}
     *             (exclusive).
     * @return an unsigned {@code int} value.
     * @throws IOException if an error occurs.
     * @see #readInt(boolean, int)
     */
    default int readUnsignedInt(final int size) throws IOException {
        return readInt(true, size);
    }

    /**
     * Reads a {@code long} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Long#SIZE} - ({@code
     *                 unsigned ? 1: 0})), both inclusive.
     * @return a {@code long} value of specified number of bits.
     * @throws IOException if an I/O error occurs.
     * @see #readInt(boolean, int)
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
     * Reads a signed {@code long} value of specified number of bits. The {@code readLong(int)} method of {@code
     * BitInput} interface invokes {@link #readLong(boolean, int)} method with {@code false} and given {@code size}
     * argument and returns the result.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Long#SIZE}, both inclusive.
     * @return a signed {@code long} value.
     * @throws IOException if an I/O error occurs.
     * @see #readLong(boolean, int)
     */
    default long readLong(final int size) throws IOException {
        return readLong(false, size);
    }

    /**
     * Reads a {@value java.lang.Long#SIZE}-bit signed {@code long} value. The {@code readLong64()} method of {@code
     * BitInput} interface invokes {@link #readLong(int)} with {@value java.lang.Long#SIZE} and returns the result.
     *
     * @return a {@value java.lang.Long#SIZE}-bit signed {@code long} value.
     * @throws IOException if an I/O error occurs.
     * @see #readLong(int)
     */
    default long readLong64() throws IOException {
        return readLong(Long.SIZE);
    }

    /**
     * Reads an unsigned {@code long} value of specified number of bits. The {@code readUnsignedLong(int)} method of
     * {@code BitInput} interface invokes {@link #readLong(boolean, int)} method with {@code true} and given {@code
     * size} arguments and returns the result.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Long#SIZE}
     *             (exclusive).
     * @return an unsigned {@code long} value.
     * @throws IOException if an error occurs.
     * @see #readLong(boolean, int)
     */
    default long readUnsignedLong(final int size) throws IOException {
        return readLong(true, size);
    }

    /**
     * Reads a {@code char} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Character#SIZE}, both inclusive.
     * @return a {@code char} value read.
     * @throws IOException if an I/O error occurs.
     * @see #readInt(boolean, int)
     */
    default char readChar(final int size) throws IOException {
        return (char) readUnsignedInt(BitIoConstraints.requireValidSizeForChar(size));
    }

    /**
     * Reads a {@value java.lang.Character#SIZE}-bit {@code char} value. The {@code readChar16()} method of {@code
     * BitInput} interface invokes {@link #readChar(int)} method with {@value java.lang.Character#SIZE} and returns the
     * result.
     *
     * @return a {@code char} value.
     * @throws IOException if an I/O error occurs.
     * @see #readChar(int)
     */
    default char readChar16() throws IOException {
        return readChar(Character.SIZE);
    }

    /**
     * Reads a {@value java.lang.Float#SIZE}-bit {@code float} value. The {@code readFloat32()} method of {@code
     * BitInput} interface reads a {@value java.lang.Integer#SIZE}-bit signed {@code int} value and returns the values
     * as converted to a {@code float} using {@link Float#intBitsToFloat(int)} method.
     *
     * @return a {@value java.lang.Float#SIZE}-bit {@code float} value
     * @throws IOException if an I/O error occurs.
     * @see #readInt32()
     * @see Float#intBitsToFloat(int)
     */
    default float readFloat32() throws IOException {
        return Float.intBitsToFloat(readInt32());
    }

    /**
     * Reads a {@value java.lang.Double#SIZE}-bit {@code double} value. The {@code readDouble64()} method of {@code
     * BitInput} interface reads a {@value java.lang.Long#SIZE}-bit signed {@code long} value and returns the value as
     * converted to a {@code double} using {@link Double#longBitsToDouble(long)} method.
     *
     * @return a {@value java.lang.Double#SIZE}-bit {@code double} value
     * @throws IOException if an I/O error occurs.
     * @see #readLong64()
     * @see Double#longBitsToDouble(long)
     */
    default double readDouble64() throws IOException {
        return Double.longBitsToDouble(readLong64());
    }

    /**
     * Reads a value using specified reader. The {@code readValue(ValueReader)} method of {@code BitInput} interface
     * invokes {@link BitReader#read(BitInput)} method on specified {@code reader} with {@code this} and returns the
     * result.
     *
     * @param reader the reader.
     * @param <T>    value type parameter
     * @return a value read.
     * @throws IOException if an I/O error occurs.
     */
    default <T> T readValue(final BitReader<? extends T> reader) throws IOException {
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
            readInt32();
        }
        if (bits > 0) {
            readUnsignedInt(bits);
        }
    }

    /**
     * Aligns to specified number of bytes by discarding required number of bits.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of bits discarded while aligning.
     * @throws IllegalArgumentException if {@code bytes} is not positive.
     * @throws IOException              if an I/O error occurs.
     */
    long align(int bytes) throws IOException;

    /**
     * Aligns to a single byte by discarding required number of bits. The {@code align()} method of {@code BitInput}
     * interface invokes {@link #align(int)} method with {@value java.lang.Byte#BYTES}.
     *
     * @return the number of bits discarded while aligning.
     * @throws IOException if an I/O error occurs.
     * @see #align(int)
     */
    default long align() throws IOException {
        return align(Byte.BYTES);
    }
}
