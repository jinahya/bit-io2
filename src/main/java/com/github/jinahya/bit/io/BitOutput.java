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

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;

/**
 * An interface for writing values of an arbitrary number of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BitInput
 */
public interface BitOutput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes a {@code 1}-bit {@code boolean} value. This method writes {@code 0b1} for {@code true} and {@code 0b0} for
     * {@code false}.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeBoolean(boolean value) throws IOException {
        writeInt(true, 1, value ? 0x01 : 0x00);
    }

    /**
     * Writes an {@code int} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Integer#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     */
    void writeInt(boolean unsigned, int size, int value) throws IOException;

    /**
     * Writes a {@code long} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Long#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeLong(final boolean unsigned, int size, long value) throws IOException {
        requireValidSizeLong(unsigned, size);
        if (!unsigned) {
            writeInt(true, 1, value < 0L ? 0x01 : 0x00);
            size--;
            if (size > 0) {
                writeLong(true, size, value);
            }
            return;
        }
        assert unsigned;
        if (size >= Integer.SIZE) {
            writeInt(false, Integer.SIZE, (int) ((value >> Integer.SIZE) & 0xFFFFFFFFL));
            size -= Integer.SIZE;
        }
        if (size > 0) {
            writeInt(true, size, (int) value);
        }
    }

    /**
     * Skips specified number of bits by padding zero bits.
     *
     * @param bits the number of bit to skip; must be positive.
     * @throws IOException if an I/O error occurs.
     */
    default void skip(int bits) throws IOException {
        if (bits <= 0) {
            throw new IllegalArgumentException("bits(" + bits + ") <= 0");
        }
        for (int i = bits >> 3; i > 0; i--) {
            writeInt(true, Byte.SIZE, 0);
        }
        for (int i = (bits & 7); i > 0; i--) {
            writeInt(true, 1, 0);
        }
    }

    /**
     * Aligns to specified number of bytes by padding zero bits.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of bits padded while aligning.
     * @throws IllegalArgumentException if {@code bytes} is not positive.
     * @throws IOException              if an I/O error occurs.
     */
    long align(int bytes) throws IOException;
}
