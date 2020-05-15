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

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForChar;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForLong;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForShort;
import static java.lang.Float.floatToRawIntBits;
import static java.util.Objects.requireNonNull;

/**
 * An interface for writing values of an arbitrary number of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BitInput
 */
public interface BitOutput extends OctetConsumerAttachable {

    /**
     * Writes specified {@code 1}-bit {@code boolean} value. This method writes {@code 0b1} for {@code true} and {@code
     * 0b0} for {@code false}.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readBoolean()
     */
    default void writeBoolean(boolean value) throws IOException {
        writeInt(true, 1, value ? 0x01 : 0x00);
    }

    /**
     * Writes specified {@code byte} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Byte#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeByte(final boolean unsigned, final int size, final byte value) throws IOException {
        writeInt(unsigned, requireValidSizeForByte(unsigned, size), value);
    }

    /**
     * Writes specified signed {@code byte} value of specified number of bits.
     *
     * @param size  the number of bits to write; between {@code 1} and {@value java.lang.Byte#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeByte(final int size, final byte value) throws IOException {
        writeByte(false, size, value);
    }

    /**
     * Writes specified signed {@value java.lang.Byte#SIZE}-bit signed {@code byte} value.
     *
     * @param value the {@value java.lang.Byte#SIZE}-bit signed {@code byte} value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeByte8(final byte value) throws IOException {
        writeByte(Byte.SIZE, value);
    }

    /**
     * Writes specified unsigned {@code byte} value of specified bit size.
     *
     * @param size  the number of lower bits to write; between {@code 1} (inclusive) and {@value java.lang.Byte#SIZE}
     *              (exclusive).
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeUnsignedByte(final int size, final byte value) throws IOException {
        writeByte(true, size, value);
    }

    /**
     * Writes specified {@code short} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Short#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeShort(final boolean unsigned, final int size, final short value) throws IOException {
        writeInt(unsigned, requireValidSizeForShort(unsigned, size), value);
    }

    /**
     * Writes specified signed {@code short} value of specified number of bits.
     *
     * @param size  the number of bits to write; between {@code 1} and ({@value java.lang.Short#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeShort(final int size, final short value) throws IOException {
        writeShort(false, size, value);
    }

    /**
     * Writes specified signed {@value java.lang.Short#SIZE}-bit signed {@code short} value.
     *
     * @param value the {@value java.lang.Short#SIZE}-bit signed {@code short} value to write.
     * @throws IOException if an I/O error occurs.
     * @see #writeShort(int, short)
     */
    default void writeShort16(final short value) throws IOException {
        writeShort(Short.SIZE, value);
    }

    /**
     * Writes specified signed {@value java.lang.Short#SIZE}-bit signed {@code short} value in little endian byte
     * order.
     *
     * @param value the {@value java.lang.Short#SIZE}-bit signed {@code short} value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeShort16Le(final short value) throws IOException {
        writeByte8((byte) value);
        writeByte8((byte) (value >> Byte.SIZE));
    }

    /**
     * Writes specified unsigned {@code short} value of specified bit size.
     *
     * @param size  the number of bits to write; between {@code 1} (inclusive) and {@value java.lang.Short#SIZE}
     *              (exclusive).
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeUnsignedShort(final int size, final short value) throws IOException {
        writeShort(true, size, value);
    }

    /**
     * Writes specified {@code int} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Integer#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     */
    void writeInt(boolean unsigned, int size, int value) throws IOException;

    /**
     * Writes specified signed {@code int} value of specified bit size.
     *
     * @param size  the number of bits to write; between {@code 1} and {@value java.lang.Integer#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeInt(final int size, final int value) throws IOException {
        writeInt(false, size, value);
    }

    /**
     * Writes specified signed {@value java.lang.Integer#SIZE}-bit value.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeInt32(final int value) throws IOException {
        writeInt(Integer.SIZE, value);
    }

    /**
     * Writes specified signed {@value java.lang.Integer#SIZE}-bit value in little endian byte order.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeInt32Le(final int value) throws IOException {
        writeShort16Le((short) value);
        writeShort16Le((short) (value >> Short.SIZE));
    }

    /**
     * Writes specified unsigned {@code int} value of specified bit size.
     *
     * @param size  the number of bits to write; between {@code 1} (inclusive) and {@value java.lang.Integer#SIZE}
     *              (exclusive).
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeUnsignedInt(final int size, final int value) throws IOException {
        writeInt(true, size, value);
    }

    /**
     * Writes specified {@code long} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Long#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeLong(final boolean unsigned, int size, final long value) throws IOException {
        requireValidSizeForLong(unsigned, size);
        if (!unsigned) {
            writeInt(true, 1, value < 0L ? 0x01 : 0x00);
            if (--size > 0) {
                writeLong(true, size, value);
            }
            return;
        }
        if (size >= Integer.SIZE) {
            writeInt(false, Integer.SIZE, (int) (value >> (size - Integer.SIZE)));
            size -= Integer.SIZE;
        }
        if (size > 0) {
            writeInt(true, size, (int) value);
        }
    }

    /**
     * Writes specified signed {@code long} value of specified bit size.
     *
     * @param size  the number of bits to write; between {@code 1} and {@value java.lang.Long#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeLong(final int size, final long value) throws IOException {
        writeLong(false, size, value);
    }

    /**
     * Writes specified signed {@value java.lang.Long#SIZE}-bit {@code long} value.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeLong64(final long value) throws IOException {
        writeLong(Long.SIZE, value);
    }

    /**
     * Writes specified signed {@value java.lang.Long#SIZE}-bit {@code long} value in little endian byte order.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeLong64Le(final long value) throws IOException {
        writeInt32Le((int) value);
        writeInt32Le((int) (value >> Integer.SIZE));
    }

    /**
     * Writes specified unsigned {@code long} value of specified bit size.
     *
     * @param size  the number of bits to write; between {@code 1} (inclusive) and {@value java.lang.Long#SIZE}
     *              (exclusive).
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeUnsignedLong(final int size, final long value) throws IOException {
        writeLong(true, size, value);
    }

    /**
     * Writes specified {@code char} value of specified bit size.
     *
     * @param size  the number of bits to write.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @see #writeUnsignedInt(int, int)
     * @see #writeChar16(char)
     */
    default void writeChar(final int size, final char value) throws IOException {
        writeInt(true, requireValidSizeForChar(size), value);
    }

    /**
     * Writes specified {@value java.lang.Character#SIZE}-bit {@code char} value.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @see #writeChar(int, char)
     */
    default void writeChar16(final char value) throws IOException {
        writeChar(Character.SIZE, value);
    }

    /**
     * Writes specified {@value java.lang.Float#SIZE}-bit {@code float} value. The {@code writeFloat32(float)} method of
     * {@code BitOutput} interface writes a {@value java.lang.Integer#SIZE}-bit {@link Float#floatToRawIntBits(float)
     * raw int bits} of specified {@code float} value.
     *
     * @param value the {@code float} value to write.
     * @throws IOException if an I/O error occurs.
     * @see Float#floatToRawIntBits(float)
     * @see #writeInt32(int)
     */
    default void writeFloat32(final float value) throws IOException {
        writeInt(false, Integer.SIZE, floatToRawIntBits(value));
    }

    /**
     * Writes specified {@value java.lang.Double#SIZE}-bit {@code double} value. The {@code writeDouble64(double)}
     * method of {@code BitOutput} interface writes a {@value java.lang.Long#SIZE}-bit {@link
     * Double#doubleToRawLongBits(double) raw long bits} of specified {@code double} value.
     *
     * @param value the {@code double} value to write.
     * @throws IOException if an I/O error occurs.
     * @see Double#doubleToRawLongBits(double)
     * @see #writeLong64(long)
     */
    default void writeDouble64(final double value) throws IOException {
        writeLong(false, Long.SIZE, Double.doubleToRawLongBits(value));
    }

    /**
     * Writes specified value using specified adapter. The {@code writeValue(ValueAdapter, T)} method of {@code
     * BitOutput} interface invokes {@link ValueAdapter#write(BitOutput, Object)} with {@code this} and specified
     * value.
     *
     * @param adapter the adapter.
     * @param value   the value to write.
     * @param <T>     value type parameter
     * @throws IOException if an I/O error occurs.
     */
    default <T> void writeValue(final ValueAdapter<? super T> adapter, final T value) throws IOException {
        requireNonNull(adapter, "adapter is null").write(this, value);
    }

    /**
     * Skips specified number of bits by padding zero bits.
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
            writeInt(false, Integer.SIZE, 0);
        }
        if (bits > 0) {
            writeInt(true, bits, 0);
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

    /**
     * Aligns to a single byte by padding zero bits.
     *
     * @return the number of bits padded while aligning.
     * @throws IOException if an I/O error occurs.
     * @see #align(int)
     */
    default long align() throws IOException {
        return align(Byte.BYTES);
    }
}
