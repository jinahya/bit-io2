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
     * Reads a {@code 1}-bit <em>unsigned</em> {@code int} value and returns {@code true} for {@code 0b1} and {@code
     * false} for {@code 0b0}
     *
     * @return the value read.
     * @throws IOException if an I/O error occurs.
     * @see #readUnsignedInt(int)
     */
    default boolean readBoolean() throws IOException {
        return readUnsignedInt(1) == 0b01;
    }

    /**
     * Reads a {@code byte} value of specified number of bits.
     *
     * @param unsigned a flag for indicating an unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Byte#SIZE} - ({@code
     *                 unsigned ? 1 : 0})), both inclusive.
     * @return a {@code byte} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #readInt(boolean, int)} method with given arguments and
     * returns the result as a {@code byte} value.
     */
    default byte readByte(final boolean unsigned, final int size) throws IOException {
        return (byte) readInt(unsigned, BitIoConstraints.requireValidSizeForByte(unsigned, size));
    }

    /**
     * Reads a <em>signed</em> {@code byte} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Byte#SIZE}, both inclusive.
     * @return a signed {@code byte} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #readByte(boolean, int)} method with {@code false} and {@code
     * size} and returns the result.
     * @see #readByte(boolean, int)
     */
    default byte readByte(final int size) throws IOException {
        return readByte(false, size);
    }

    /**
     * Reads an {@value java.lang.Byte#SIZE}-bit {@code byte} value.
     *
     * @return a signed {@value java.lang.Byte#SIZE}-bit {@code byte} value.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #readByte(int)} method with {@link java.lang.Byte#SIZE} and
     * returns the result.
     * @see #readByte(int)
     */
    default byte readByte8() throws IOException {
        return readByte(Byte.SIZE);
    }

    /**
     * Reads an <em>unsigned</em> {@code byte} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Byte#SIZE}
     *             (exclusive).
     * @return an unsigned {@code byte} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #readByte(boolean, int)} method with {@code true} and given
     * {@code size} and returns the result.
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
     * @implNote The default implementation invokes {@link #readInt(boolean, int)} method with given arguments and
     * returns the result as a {@code short} value.
     * @see #readInt(boolean, int)
     */
    default short readShort(final boolean unsigned, final int size) throws IOException {
        return (short) readInt(unsigned, BitIoConstraints.requireValidSizeForShort(unsigned, size));
    }

    /**
     * Reads a <em>signed</em> {@code short} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Short#SIZE}, both inclusive.
     * @return a signed {@code short} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #readShort(boolean, int)} method with {@code false} and
     * {@code size} and returns the result.
     * @see #readShort(boolean, int)
     */
    default short readShort(final int size) throws IOException {
        return readShort(false, size);
    }

    /**
     * Reads a {@value java.lang.Short#SIZE}-bit {@code short} value.
     *
     * @return a {@value java.lang.Short#SIZE}-bit {@code short} value read.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #readShort(int)} method with {@link java.lang.Short#SIZE} and
     * returns the result.
     * @see #readShort(int)
     */
    default short readShort16() throws IOException {
        return readShort(Short.SIZE);
    }

    /**
     * Reads an <em>unsigned</em> {@code short} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Short#SIZE}
     *             (exclusive).
     * @return an unsigned {@code short} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #readShort(boolean, int)} method with {@code true} and given
     * {@code size} and returns the result.
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
     * Reads a signed {@code int} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Integer#SIZE}, both inclusive.
     * @return a signed {@code int} value.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #readInt(boolean, int)} method with {@code false} and given
     * {@code size} and returns the result.
     * @see #readInt(boolean, int)
     */
    default int readInt(final int size) throws IOException {
        return readInt(false, size);
    }

    /**
     * Reads a {@value java.lang.Integer#SIZE}-bit {@code int} value.
     *
     * @return a {@value java.lang.Integer#SIZE}-bit {@code int} value read.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #readInt(int)} method with {@link java.lang.Integer#SIZE} and
     * returns the result.
     * @see #readInt(int)
     */
    default int readInt32() throws IOException {
        return readInt(Integer.SIZE);
    }

    /**
     * Reads an <em>unsigned</em> {@code int} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Integer#SIZE}
     *             (exclusive).
     * @return an unsigned {@code int} value.
     * @throws IOException if an error occurs.
     * @implNote The default implementation invokes {@link #readInt(boolean, int)} method with {@code true} and {@code
     * size} and returns the result.
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
     * Reads a <em>signed</em> {@code long} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Long#SIZE}, both inclusive.
     * @return a signed {@code long} value.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #readLong(boolean, int)} method with {@code false} and given
     * {@code size} and returns the result.
     * @see #readLong(boolean, int)
     */
    default long readLong(final int size) throws IOException {
        return readLong(false, size);
    }

    /**
     * Reads a {@value java.lang.Long#SIZE}-bit {@code long} value.
     *
     * @return a {@value java.lang.Long#SIZE}-bit {@code long} value.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #readLong(int)} method with {@link java.lang.Long#SIZE} and
     * returns the result.
     * @see #readLong(int)
     */
    default long readLong64() throws IOException {
        return readLong(Long.SIZE);
    }

    /**
     * Reads an <em>unsigned</em> {@code long} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Long#SIZE}
     *             (exclusive).
     * @return an unsigned {@code long} value.
     * @throws IOException if an error occurs.
     * @implNote The default implementation invokes {@link #readLong(boolean, int)} method with {@code true} and given
     * {@code size} and returns the result.
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
     * @implNote The default implementation invokes {@link #readUnsignedInt(int)} method with given {@code size} and
     * returns the result as a {@code char}.
     * @see #readUnsignedInt(int)
     */
    default char readChar(final int size) throws IOException {
        return (char) readUnsignedInt(BitIoConstraints.requireValidSizeForChar(size));
    }

    /**
     * Reads a {@value java.lang.Character#SIZE}-bit {@code char} value.
     *
     * @return a {@code char} value.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #readChar(int)} method with {@link java.lang.Character#SIZE}
     * and returns the result.
     * @see #readChar(int)
     */
    default char readChar16() throws IOException {
        return readChar(Character.SIZE);
    }

    /**
     * Reads a {@value java.lang.Float#SIZE}-bit {@code float} value.
     *
     * @return a {@value java.lang.Float#SIZE}-bit {@code float} value
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation reads a {@value java.lang.Integer#SIZE}-bit signed {@code int} value and
     * returns the value as converted to a {@code float} value using {@link Float#intBitsToFloat(int)} method.
     * @see #readInt32()
     * @see Float#intBitsToFloat(int)
     */
    default float readFloat32() throws IOException {
        return Float.intBitsToFloat(readInt32());
    }

    /**
     * Reads a {@value java.lang.Double#SIZE}-bit {@code double} value.
     *
     * @return a {@value java.lang.Double#SIZE}-bit {@code double} value
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation reads a {@value java.lang.Long#SIZE}-bit signed {@code long} value and
     * returns the value as converted to a {@code double} value using {@link Double#longBitsToDouble(long)} method.
     * @see #readLong64()
     * @see Double#longBitsToDouble(long)
     */
    default double readDouble64() throws IOException {
        return Double.longBitsToDouble(readLong64());
    }

    /**
     * Reads a value using specified reader.
     *
     * @param reader the reader.
     * @param <T>    value type parameter
     * @return a value read.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link BitReader#read(BitInput)} method on specified {@code reader}
     * with {@code this} and returns the result.
     */
    default <T> T readObject(final BitReader<? extends T> reader) throws IOException {
        Objects.requireNonNull(reader, "reader is null");
        return reader.read(this);
    }

    default byte[] readBytes(final byte[] array, final int offset, final int length) throws IOException {
        return array;
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
     * Aligns to specified number of <em>bytes</em> by discarding required number of bits.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of bits discarded while aligning; not negative, always.
     * @throws IllegalArgumentException if {@code bytes} is not positive.
     * @throws IOException              if an I/O error occurs.
     */
    long align(int bytes) throws IOException;

    /**
     * Aligns to a single <em>byte</em> by discarding required number of bits.
     *
     * @return the number of bits discarded while aligning; between {@code 0} (inclusive) and {@value
     * java.lang.Byte#SIZE} (exclusive).
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #align(int)} method with {@link java.lang.Byte#BYTES}.
     * @see #align(int)
     */
    default long align() throws IOException {
        final long d = align(Byte.BYTES);
        assert d >= 0L;
        assert d < Byte.SIZE;
        return d;
    }
}
