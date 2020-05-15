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

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForChar;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForLong;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForShort;
import static java.lang.Double.longBitsToDouble;
import static java.lang.Float.intBitsToFloat;
import static java.util.Objects.requireNonNull;

/**
 * An interface for reading values of an arbitrary number of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BitOutput
 */
public interface BitInput extends Closeable, OctetConsumerAttachable {

    /**
     * Closes this input and releases any system resources associated with it. The {@code close} method of {@code
     * BitInput} interface does nothing.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    default void close() throws IOException {
        // does nothing.
    }

    /**
     * Reads a {@code 1}-bit {@code boolean} value. This method reads a {@code 1}-bit unsigned {@code int} and returns
     * {@code true} for {@code 0b1} and {@code false} for {@code 0b0}.
     *
     * @return {@code true} for {@code 0b1}, {@code false} for {@code 0b0}
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeBoolean(boolean)
     */
    default boolean readBoolean() throws IOException {
        return readInt(true, 1) == 0x01;
    }

    /**
     * Reads a {@code byte} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Byte#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @return a {@code byte} value of specified {@code size}.
     * @throws IOException if an I/O error occurs.
     */
    default byte readByte(final boolean unsigned, final int size) throws IOException {
        return (byte) readInt(unsigned, requireValidSizeForByte(unsigned, size));
    }

    /**
     * Reads a signed {@code byte} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Byte#SIZE}, both inclusive.
     * @return a signed {@code byte} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     */
    default byte readByte(final int size) throws IOException {
        return readByte(false, size);
    }

    /**
     * Reads a signed {@value java.lang.Byte#SIZE}-bit {@code byte} value.
     *
     * @return a signed {@value java.lang.Byte#SIZE}-bit {@code byte} value.
     * @throws IOException if an I/O error occurs.
     */
    default byte readByte8() throws IOException {
        return readByte(Byte.SIZE);
    }

    /**
     * Reads an unsigned {@code byte} value of specified number of bits.
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
     */
    default short readShort(final boolean unsigned, final int size) throws IOException {
        return (short) readInt(unsigned, requireValidSizeForShort(unsigned, size));
    }

    /**
     * Reads a signed {@code short} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Short#SIZE}, both inclusive.
     * @return a signed {@code short} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     */
    default short readShort(final int size) throws IOException {
        return readShort(false, size);
    }

    /**
     * Reads a signed {@value java.lang.Short#SIZE}-bit {@code short} value.
     *
     * @return a signed {@value java.lang.Short#SIZE}-bit {@code short} value.
     * @throws IOException if an I/O error occurs.
     * @see #readShort(int)
     */
    default short readShort16() throws IOException {
        return readShort(Short.SIZE);
    }

    /**
     * Reads a signed {@value java.lang.Short#SIZE}-bit {@code short} value in little endian byte order.
     *
     * @return a signed {@value java.lang.Short#SIZE}-bit {@code short} value in little endian byte order.
     * @throws IOException if an I/O error occurs.
     */
    default short readShort16Le() throws IOException {
        return (short) ((readByte8() & 0xFF) | (readByte8() << Byte.SIZE));
    }

    /**
     * Reads an unsigned {@code short} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Short#SIZE}
     *             (exclusive).
     * @return an unsigned {@code short} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     */
    default short readUnsignedShort(final int size) throws IOException {
        return readShort(true, size);
    }

    /**
     * Reads an {@code int} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Integer#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @return an {@code int} value of specified {@code size}.
     * @throws IOException if an I/O error occurs.
     */
    int readInt(boolean unsigned, int size) throws IOException;

    /**
     * Reads a signed {@code int} value of specified bit size.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Integer#SIZE}, both inclusive.
     * @return a signed {@code int} value.
     * @throws IOException if an I/O error occurs.
     */
    default int readInt(final int size) throws IOException {
        return readInt(false, size);
    }

    /**
     * Reads a signed {@value java.lang.Integer#SIZE}-bit {@code int} value.
     *
     * @return a signed {@value java.lang.Integer#SIZE}-bit {@code int} value.
     * @throws IOException if an I/O error occurs.
     */
    default int readInt32() throws IOException {
        return readInt(Integer.SIZE);
    }

    /**
     * Reads a signed {@value java.lang.Integer#SIZE}-bit {@code int} value in little endian byte order.
     *
     * @return a signed {@value java.lang.Integer#SIZE}-bit {@code int} value.
     * @throws IOException if an I/O error occurs.
     */
    default int readInt32Le() throws IOException {
        return readShort16Le() & 0xFFFF | readShort16Le() << Short.SIZE;
    }

    /**
     * Reads an unsigned {@code int} value of specified bit size.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Integer#SIZE}
     *             (exclusive).
     * @return an unsigned {@code int} value.
     * @throws IOException if an error occurs.
     */
    default int readUnsignedInt(final int size) throws IOException {
        return readInt(true, size);
    }

    /**
     * Reads a {@code long} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Long#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @return a {@code long} value of specified bit size.
     * @throws IOException if an I/O error occurs.
     */
    default long readLong(final boolean unsigned, int size) throws IOException {
        requireValidSizeForLong(unsigned, size);
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
     * Reads a signed {@code long} value of specified bit size.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Long#SIZE}, both inclusive.
     * @return a signed {@code long} value.
     * @throws IOException if an I/O error occurs.
     */
    default long readLong(final int size) throws IOException {
        return readLong(false, size);
    }

    /**
     * Reads a signed {@value java.lang.Long#SIZE}-bit {@code long} value.
     *
     * @return a signed {@value java.lang.Long#SIZE}-bit {@code long} value.
     * @throws IOException if an I/O error occurs.
     */
    default long readLong64() throws IOException {
        return readLong(Long.SIZE);
    }

    /**
     * Reads a signed {@value java.lang.Long#SIZE}-bit {@code long} value in little endian byte order.
     *
     * @return a signed {@value java.lang.Long#SIZE}-bit {@code long} value.
     * @throws IOException if an I/O error occurs.
     */
    default long readLong64Le() throws IOException {
        return readInt32Le() & 0xFFFFFFFFL | ((long) readInt32Le()) << Integer.SIZE;
    }

    /**
     * Reads an unsigned {@code long} value of specified bit size.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Long#SIZE}
     *             (exclusive).
     * @return an unsigned {@code long} value.
     * @throws IOException if an error occurs.
     */
    default long readUnsignedLong(final int size) throws IOException {
        return readLong(true, size);
    }

    /**
     * Reads a {@code char} value of specified bit size.
     *
     * @param size the number of bits to read.
     * @return a {@code char} value.
     * @throws IOException if an I/O error occurs.
     * @see #readChar16()
     */
    default char readChar(final int size) throws IOException {
        return (char) readInt(true, requireValidSizeForChar(size));
    }

    /**
     * Reads a {@value java.lang.Character#SIZE}-bit {@code char} value.
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
     * BitInput} interface reads a {@value java.lang.Integer#SIZE}-bit {@link Float#intBitsToFloat(int) int bits} and
     * returns it as a {@code float} value.
     *
     * @return a {@value java.lang.Float#SIZE}-bit {@code float} value
     * @throws IOException if an I/O error occurs.
     * @see Float#intBitsToFloat(int)
     */
    default float readFloat32() throws IOException {
        return intBitsToFloat(readInt(false, Integer.SIZE));
    }

    /**
     * Reads a {@value java.lang.Double#SIZE}-bit {@code double} value. The {@code readDouble64()} method of {@code
     * BitInput} interface reads a {@value java.lang.Long#SIZE}-bit {@link Double#longBitsToDouble(long) long bits} and
     * returns it as a {@code double} value.
     *
     * @return a {@value java.lang.Double#SIZE}-bit {@code double} value
     * @throws IOException if an I/O error occurs.
     * @see Double#longBitsToDouble(long)
     */
    default double readDouble64() throws IOException {
        return longBitsToDouble(readLong(false, Long.SIZE));
    }

    /**
     * Reads a value using specified adapter. The {@code readValue(ValueAdapter)} method of {@code BitInput} interface
     * invokes {@link ValueAdapter#read(BitInput)} method with {@code this}.
     *
     * @param adapter the adapter.
     * @param <T>     value type parameter
     * @return a value read.
     * @throws IOException if an I/O error occurs.
     */
    default <T> T readValue(final ValueAdapter<? extends T> adapter) throws IOException {
        return requireNonNull(adapter, "adapter is null").read(this);
    }

    /**
     * Skips specified number of bits by discarding bits.
     *
     * @param bits the number of bit to skip; must be positive.
     * @throws IllegalArgumentException if {@code bits} is not positive.
     * @throws IOException              if an I/O error occurs.
     */
    default void skip(int bits) throws IOException {
        if (bits <= 0) {
            throw new IllegalArgumentException("bits(" + bits + ") <= 0");
        }
        for (; bits >= Integer.SIZE; bits -= Integer.SIZE) {
            readInt(false, Integer.SIZE);
        }
        if (bits > 0) {
            readInt(true, bits);
        }
    }

    /**
     * Aligns to specified number of bytes by discarding bits.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of bits discarded while aligning.
     * @throws IllegalArgumentException if {@code bytes} is not positive.
     * @throws IOException              if an I/O error occurs.
     */
    long align(int bytes) throws IOException;

    /**
     * Aligns to a single byte by discarding bits.
     *
     * @return the number of bits discarded while aligning.
     * @throws IOException if an I/O error occurs.
     * @see #align(int)
     */
    default long align() throws IOException {
        return align(Byte.BYTES);
    }
}
