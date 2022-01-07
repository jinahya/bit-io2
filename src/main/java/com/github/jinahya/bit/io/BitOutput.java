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
import java.io.Flushable;
import java.io.IOException;
import java.util.Objects;

/**
 * An interface for writing values of an arbitrary number of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BitInput
 */
public interface BitOutput
        extends Flushable, Closeable {

    /**
     * Flushes this output by writing any buffered output to the underlying output.
     *
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation does nothing.
     */
    @Override
    default void flush() throws IOException {
        // does nothing.
    }

    /**
     * Closes this output and releases any system resources associated with it.
     *
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation does nothing.
     */
    @Override
    default void close() throws IOException {
        // does nothing.
    }

    /**
     * Writes a {@code 1}-bit <em>unsigned</em> {@code int} value({@code 0b1} for {@code true} and {@code 0b0} for
     * {@code false}) for specified {@code boolean} value.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeBoolean(final boolean value) throws IOException {
        writeUnsignedInt(1, value ? 0b01 : 0b00);
    }

    /**
     * Writes specified {@code byte} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Byte#SIZE} - ({@code
     *                 unsigned ? 1: 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #writeInt(boolean, int, int)} with given arguments.
     * @see #writeInt(boolean, int, int)
     */
    default void writeByte(final boolean unsigned, final int size, final byte value) throws IOException {
        writeInt(unsigned, BitIoConstraints.requireValidSizeForByte(unsigned, size), value);
    }

    /**
     * Writes specified <em>signed</em> {@code byte} value of specified number of bits.
     *
     * @param size  the number of bits to write; between {@code 1} and {@value java.lang.Byte#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invoke {@link #writeByte(boolean, int, byte)} method with {@code false} and
     * given arguments.
     * @see #writeByte(boolean, int, byte)
     */
    default void writeByte(final int size, final byte value) throws IOException {
        writeByte(false, size, value);
    }

    /**
     * Writes specified {@value java.lang.Byte#SIZE}-bit {@code byte} value.
     *
     * @param value the {@value java.lang.Byte#SIZE}-bit {@code byte} value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #writeByte(int, byte)} method with {@link
     * java.lang.Byte#SIZE} and {@code value}.
     * @see #writeByte(int, byte)
     */
    default void writeByte8(final byte value) throws IOException {
        writeByte(Byte.SIZE, value);
    }

    /**
     * Writes specified <em>unsigned</em> {@code byte} value of specified number of bits.
     *
     * @param size  the number of lower bits to write; between {@code 1} (inclusive) and {@value java.lang.Byte#SIZE}
     *              (exclusive).
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #writeByte(boolean, int, byte)} method with {@code true} and
     * given arguments.
     * @see #writeByte(boolean, int, byte)
     */
    default void writeUnsignedByte(final int size, final byte value) throws IOException {
        writeByte(true, size, value);
    }

    /**
     * Writes specified {@code short} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Short#SIZE} - ({@code
     *                 unsigned ? 1 : 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #writeInt(boolean, int, int)} method with given arguments.
     * @see #writeInt(boolean, int, int)
     */
    default void writeShort(final boolean unsigned, final int size, final short value) throws IOException {
        writeInt(unsigned, BitIoConstraints.requireValidSizeForShort(unsigned, size), value);
    }

    /**
     * Writes specified <em>signed</em> {@code short} value of specified number of bits.
     *
     * @param size  the number of bits to write; between {@code 1} and ({@value java.lang.Short#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #writeShort(boolean, int, short)} method with {@code false}
     * and given arguments.
     * @see #writeShort(boolean, int, short)
     */
    default void writeShort(final int size, final short value) throws IOException {
        writeShort(false, size, value);
    }

    /**
     * Writes specified {@value java.lang.Short#SIZE}-bit {@code short} value.
     *
     * @param value the {@value java.lang.Short#SIZE}-bit {@code short} value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #writeShort(int, short)} method with {@value
     * java.lang.Short#SIZE} and {@code value}.
     * @see #writeShort(int, short)
     */
    default void writeShort16(final short value) throws IOException {
        writeShort(Short.SIZE, value);
    }

    /**
     * Writes specified <em>unsigned</em> {@code short} value of specified number of bits.
     *
     * @param size  the number of bits to write; between {@code 1} (inclusive) and {@value java.lang.Short#SIZE}
     *              (exclusive).
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #writeShort(boolean, int, short)} with {@code true} and given
     * arguments.
     * @see #writeShort(boolean, int, short)
     */
    default void writeUnsignedShort(final int size, final short value) throws IOException {
        writeShort(true, size, value);
    }

    /**
     * Writes specified {@code int} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Integer#SIZE} - ({@code
     *                 unsigned ? 1 : 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     */
    void writeInt(boolean unsigned, int size, int value) throws IOException;

    /**
     * Writes specified <em>signed</em> {@code int} value of specified number of bits.
     *
     * @param size  the number of bits to write; between {@code 1} and {@value java.lang.Integer#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #writeInt(boolean, int, int)} method with {@code false} and
     * given arguments.
     * @see #writeInt(boolean, int, int)
     */
    default void writeInt(final int size, final int value) throws IOException {
        writeInt(false, size, value);
    }

    /**
     * Writes specified {@value java.lang.Integer#SIZE}-bit int value.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #writeInt(int, int)} method with {@link
     * java.lang.Integer#SIZE} and {@code value}.
     * @see #writeInt(int, int)
     */
    default void writeInt32(final int value) throws IOException {
        writeInt(Integer.SIZE, value);
    }

    /**
     * Writes specified <em>unsigned</em> {@code int} value of specified number of bits.
     *
     * @param size  the number of bits to write; between {@code 1} (inclusive) and {@value java.lang.Integer#SIZE}
     *              (exclusive).
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #writeInt(boolean, int, int)} with {@code true} and given
     * arguments.
     * @see #writeInt(boolean, int, int)
     */
    default void writeUnsignedInt(final int size, final int value) throws IOException {
        writeInt(true, size, value);
    }

    /**
     * Writes specified {@code long} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Long#SIZE} - ({@code
     *                 unsigned ? 1 : 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeLong(final boolean unsigned, int size, final long value) throws IOException {
        BitIoConstraints.requireValidSizeForLong(unsigned, size);
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
     * Writes specified <em>signed</em> {@code long} value of specified number of bits.
     *
     * @param size  the number of bits to write; between {@code 1} and {@value java.lang.Long#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #writeLong(boolean, int, long)} method with {@code false} and
     * given arguments.
     * @see #writeLong(boolean, int, long)
     */
    default void writeLong(final int size, final long value) throws IOException {
        writeLong(false, size, value);
    }

    /**
     * Writes specified {@value java.lang.Long#SIZE}-bit {@code long} value.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #writeLong(int, long)} method with {@link
     * java.lang.Long#SIZE} and {@code value}.
     * @see #writeLong(int, long)
     */
    default void writeLong64(final long value) throws IOException {
        writeLong(Long.SIZE, value);
    }

    /**
     * Writes specified <em>unsigned</em> {@code long} value of specified number of bits.
     *
     * @param size  the number of bits to write; between {@code 1} (inclusive) and {@value java.lang.Long#SIZE}
     *              (exclusive).
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #writeLong(boolean, int, long)} method with {@code true} and
     * given arguments.
     * @see #writeLong(boolean, int, long)
     */
    default void writeUnsignedLong(final int size, final long value) throws IOException {
        writeLong(true, size, value);
    }

    /**
     * Writes specified {@code char} value of specified number of bits.
     *
     * @param size  the number of bits to write; between {@code 1} and {@value java.lang.Character#SIZE}, both
     *              inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #writeUnsignedInt(int, int)} method with given arguments.
     * @see #writeUnsignedInt(int, int)
     */
    default void writeChar(final int size, final char value) throws IOException {
        writeUnsignedInt(BitIoConstraints.requireValidSizeForChar(size), value);
    }

    /**
     * Writes specified {@value java.lang.Character#SIZE}-bit {@code char} value.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #writeChar(int, char)} method with {@link
     * java.lang.Character#SIZE} and {@code value}.
     * @see #writeChar(int, char)
     */
    default void writeChar16(final char value) throws IOException {
        writeChar(Character.SIZE, value);
    }

    /**
     * Writes specified {@value java.lang.Float#SIZE}-bit {@code float} value.
     *
     * @param value the {@code float} value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation writes specified value as a {@value java.lang.Integer#SIZE}-bit {@code int}
     * value converted with {@link Float#floatToRawIntBits(float)} method.
     * @see Float#floatToRawIntBits(float)
     * @see #writeInt32(int)
     */
    default void writeFloat32(final float value) throws IOException {
        writeInt32(Float.floatToRawIntBits(value));
    }

    /**
     * Writes specified {@value java.lang.Double#SIZE}-bit {@code double} value.
     *
     * @param value the {@code double} value to write.
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation writes specified value as a {@value java.lang.Long#SIZE}-bit {@code long}
     * value converted with {@link Double#doubleToRawLongBits(double)} method.
     * @see Double#doubleToRawLongBits(double)
     * @see #writeLong64(long)
     */
    default void writeDouble64(final double value) throws IOException {
        writeLong64(Double.doubleToRawLongBits(value));
    }

    /**
     * Writes specified value using specified writer.
     *
     * @param writer the writer.
     * @param value  the value to write.
     * @param <T>    value type parameter
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link BitWriter#write(BitOutput, Object)} with {@code this} and
     * {@code value}.
     */
    default <T> void writeObject(final BitWriter<? super T> writer, final T value) throws IOException {
        Objects.requireNonNull(writer, "writer is null");
        writer.write(this, value);
    }

    default <T> void writeArray(final T[] array, final int offset, final int length, final BitWriter<? super T> writer)
            throws IOException {
        Objects.requireNonNull(array, "array is null");
        if (offset < 0) {
            throw new IllegalArgumentException("offset(" + offset + ") is negative");
        }
        if (offset + length > array.length) {
            throw new ArrayIndexOutOfBoundsException(array.length);
        }
        Objects.requireNonNull(writer, "reader is null");
        final int limit = offset + length;
        for (int i = offset; i < limit; i++) {
            writer.write(this, array[i]);
        }
    }

    /**
     * Writes specified number of zero-bits.
     *
     * @param bits the number of bit to skip; must be positive.
     * @throws IllegalArgumentException if {@code bits} is not positive.
     * @throws IOException              if an I/O error occurs.
     */
    default void skip(int bits) throws IOException {
        if (bits <= 0) {
            throw new IllegalArgumentException("bits(" + bits + ") is not positive");
        }
        for (; bits >= Integer.SIZE; bits -= Integer.SIZE) {
            writeInt(false, Integer.SIZE, 0);
        }
        if (bits > 0) {
            writeInt(true, bits, 0);
        }
    }

    /**
     * Aligns to specified number of <em>bytes</em> by padding required number of zero-bits.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of zero-bits padded while aligning.
     * @throws IllegalArgumentException if {@code bytes} is not positive.
     * @throws IOException              if an I/O error occurs.
     */
    long align(int bytes) throws IOException;

    /**
     * Aligns to a single <em>byte</em> by padding required number of zero-bits.
     *
     * @return the number of zero-bits padded while aligning; between {@code 0} (inclusive) and {@value
     * java.lang.Byte#SIZE} (exclusive).
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation invokes {@link #align(int)} method with {@value java.lang.Byte#BYTES}.
     * @see #align(int)
     */
    default long align() throws IOException {
        final long p = align(Byte.BYTES);
        assert p >= 0L;
        assert p < Byte.SIZE;
        return p;
    }
}
