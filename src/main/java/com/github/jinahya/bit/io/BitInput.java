package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
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

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;

/**
 * An interface for reading values of an arbitrary number of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BitOutput
 */
public interface BitInput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a {@code 1}-bit {@code boolean} value. This method reads a {@code 1}-bit unsigned {@code int} and returns
     * {@code true} for {@code 0b1} and {@code false} for {@code 0b0}.
     *
     * @return {@code true} for {@code 0b1}, {@code false} for {@code 0b0}
     * @throws IOException if an I/O error occurs.
     */
    default boolean readBoolean() throws IOException {
        return readInt(true, 1) == 0x01;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a {@code byte} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Byte#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @return a {@code byte} value of specified {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeByte(boolean, int, byte)
     */
    default byte readByte(final boolean unsigned, final int size) throws IOException {
        return (byte) readInt(unsigned, requireValidSizeByte(unsigned, size));
    }

    /**
     * Reads a signed {@code byte} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and ({@value java.lang.Byte#SIZE}, both inclusive.
     * @return a signed {@code byte} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeByte(boolean, int, byte)
     */
    default byte readByte(final int size) throws IOException {
        return readByte(false, size);
    }

    /**
     * Reads a signed {@value java.lang.Byte#SIZE}-bit {@code byte} value.
     *
     * @return an {@value java.lang.Byte#SIZE}-bit signed {@code byte} value.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeByte8(byte)
     */
    default byte readByte8() throws IOException {
        return readByte(false, Byte.SIZE);
    }

    /**
     * Reads an unsigned {@code byte} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Byte#SIZE}
     *             (exclusive).
     * @return an unsigned {@code byte} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeByte(boolean, int, byte)
     */
    default byte readUnsignedByte(final int size) throws IOException {
        return readByte(true, size);
    }

    // -----------------------------------------------------------------------------------------------------------------

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
     * @param size the number of bits to read; between {@code 1} and {@value Integer#SIZE}, both inclusive.
     * @return a signed {@code int} value.
     * @throws IOException if an I/O error occurs.
     */
    default int readInt(final int size) throws IOException {
        return readInt(false, size);
    }

    /**
     * Reads a signed {@value Integer#SIZE}-bit {@code int} value.
     *
     * @return a signed {@value Integer#SIZE}-bit {@code int} value.
     * @throws IOException if an I/O error occurs.
     */
    default int readInt32() throws IOException {
        return readInt(Integer.SIZE);
    }

    /**
     * Reads an unsigned {@code int} value of specified bit size.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value Integer#SIZE} (exclusive).
     * @return an unsigned {@code int} value.
     * @throws IOException if an error occurs.
     */
    default int readUnsignedInt(final int size) throws IOException {
        return readInt(true, size);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a {@code long} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Long#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @return a {@code long} value of specified bit size.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeLong(boolean, int, long)
     */
    default long readLong(final boolean unsigned, int size) throws IOException {
        requireValidSizeLong(unsigned, size);
        long value = 0L;
        if (!unsigned) {
            value -= readInt(true, 1);
            if (--size > 0) {
                value <<= size;
                value |= readLong(true, size);
            }
            return value;
        }
        assert unsigned;
        if (size >= Integer.SIZE) {
            value = (readInt(false, Integer.SIZE) & 0xFFFFFFFFL);
            size -= Integer.SIZE;
        }
        assert size < Integer.SIZE : "size(" + size + ") > Integer.SIZE";
        if (size > 0) {
            value <<= size;
            value |= readInt(true, size);
        }
        return value;
    }

    /**
     * Reads a signed {@code long} value of specified bit size.
     *
     * @param size the number of bits to read; between {@code 1} and {@value Long#SIZE}, both inclusive.
     * @return a signed {@code long} value.
     * @throws IOException if an I/O error occurs.
     */
    default long readLong(final int size) throws IOException {
        return readLong(false, size);
    }

    /**
     * Reads an unsigned {@code long} value of specified bit size.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value Long#SIZE} (exclusive).
     * @return an unsigned {@code long} value.
     * @throws IOException if an error occurs.
     */
    default long readUnsignedLong(final int size) throws IOException {
        return readLong(true, size);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Skips specified number of bits by discarding bits.
     *
     * @param bits the number of bit to skip; must be positive.
     * @throws IOException if an I/O error occurs.
     */
    default void skip(int bits) throws IOException {
        if (bits <= 0) {
            throw new IllegalArgumentException("bits(" + bits + ") <= 0");
        }
        for (; bits >= Integer.SIZE; bits -= Integer.SIZE) {
            readInt(false, Integer.SIZE);
        }
        assert bits < Integer.SIZE; // TODO: 2020-04-22 remove!!!
        if (bits > 0) {
            readInt(true, bits);
            bits -= bits; // TODO: 2020-04-22 remove!!!
        }
        assert bits == 0; // TODO: 2020-04-22 remove!!!
    }

    /**
     * Aligns to specified number of bytes by discarding bits.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of bits discarded while aligning.
     * @throws IllegalArgumentException if {@code bytes} is not positive.
     * @throws IOException              if an I/O error occurs.
     * @see BitOutput#align(int)
     */
    long align(int bytes) throws IOException;

    /**
     * Aligns to a single byte by discarding bits.
     *
     * @return the number of bits discarded while aligning.
     * @throws IOException if an I/O error occurs.
     * @see #align(int)
     * @see BitOutput#align()
     */
    default long align() throws IOException {
        return align(Byte.BYTES);
    }
}
