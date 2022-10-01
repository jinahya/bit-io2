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

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.Objects;

/**
 * An interface for writing values of an arbitrary number of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BitInput
 */
public interface BitOutput {

    /**
     * Creates a new instance on top of specified stream.
     *
     * @param stream the stream to which bytes are written.
     * @return a new instance.
     */
    static BitOutput from(final OutputStream stream) {
        return new BitOutputAdapter(ByteOutput.of(stream));
    }

    /**
     * Creates a new instance on top of specified output.
     *
     * @param output the output to which bytes are written.
     * @return a new instance.
     */
    static BitOutput from(final DataOutput output) {
        return new BitOutputAdapter(ByteOutput.of(output));
    }

    /**
     * Creates a new instance on top of specified buffer.
     *
     * @param buffer the buffer to which bytes are written.
     * @return a new instance.
     */
    static BitOutput from(final ByteBuffer buffer) {
        return new BitOutputAdapter(ByteOutput.of(buffer));
    }

    /**
     * Creates a new instance on top of specified channel.
     *
     * @param channel the channel to which bytes are written.
     * @return a new instance.
     */
    static BitOutput from(final WritableByteChannel channel) {
        return new BitOutputAdapter(ByteOutput.of(channel));
    }

    /**
     * Writes specified {@code boolean} value.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation writes a {@code 1}-bit unsigned {@code int} value({@code 0b1} for
     * {@code true}, {@code 0b0} for {@code false}) for the {@code value}.
     */
    default void writeBoolean(final boolean value) throws IOException {
        writeUnsignedInt(1, value ? 0b01 : 0b00);
    }

    /**
     * Writes specified {@code byte} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Byte#SIZE} -
     *                 ({@code unsigned ? 1: 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #writeInt(boolean, int, int)} with given arguments.
     */
    default void writeByte(final boolean unsigned, final int size, final byte value) throws IOException {
        writeInt(unsigned, BitIoConstraints.requireValidSizeForByte(unsigned, size), value);
    }

    /**
     * Writes specified signed {@code byte} value of specified number of bits.
     *
     * @param size  the number of bits to write; between {@code 1} and {@value java.lang.Byte#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invoke {@link #writeByte(boolean, int, byte)} method with {@code false},
     * {@code size}, and {@code value}.
     */
    default void writeByte(final int size, final byte value) throws IOException {
        writeByte(false, size, value);
    }

    /**
     * Writes specified {@value java.lang.Byte#SIZE}-bit {@code byte} value.
     *
     * @param value the {@value java.lang.Byte#SIZE}-bit {@code byte} value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #writeByte(int, byte)} method with
     * {@value java.lang.Byte#SIZE} and {@code value}.
     */
    default void writeByte(final byte value) throws IOException {
        writeByte(Byte.SIZE, value);
    }

    /**
     * Writes specified {@code byte} value of specified number of bits as unsigned.
     *
     * @param size  the number of lower bits to write; between {@code 1} (inclusive) and {@value java.lang.Byte#SIZE}
     *              (exclusive).
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #writeByte(boolean, int, byte)} method with {@code true},
     * {@code size}, and {@code value}.
     */
    default void writeUnsignedByte(final int size, final byte value) throws IOException {
        writeByte(true, size, value);
    }

    /**
     * Writes specified {@code short} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Short#SIZE} -
     *                 ({@code unsigned ? 1 : 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #writeInt(boolean, int, int)} method with given arguments.
     */
    default void writeShort(final boolean unsigned, final int size, final short value) throws IOException {
        BitIoConstraints.requireValidSizeForShort(unsigned, size);
        writeInt(unsigned, size, value);
    }

    /**
     * Writes specified {@code short} value, of specified number of bits, as a signed value.
     *
     * @param size  the number of bits to write; between {@code 1} and {@value java.lang.Short#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #writeShort(boolean, int, short)} method with {@code false},
     * {@code size}, and {@code value}.
     */
    default void writeShort(final int size, final short value) throws IOException {
        writeShort(false, size, value);
    }

    /**
     * Writes specified {@value java.lang.Short#SIZE}-bit {@code short} value.
     *
     * @param value the {@value java.lang.Short#SIZE}-bit {@code short} value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #writeShort(int, short)} method with
     * {@value java.lang.Short#SIZE} and {@code value}.
     */
    default void writeShort(final short value) throws IOException {
        writeShort(Short.SIZE, value);
    }

    /**
     * Writes specified {@code short} value, of specified number of bits, as an unsigned value.
     *
     * @param size  the number of lower bits to write; between {@code 1} (inclusive) and {@value java.lang.Short#SIZE}
     *              (exclusive).
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #writeShort(boolean, int, short)} with {@code true},
     * {@code size}, and {@code value}.
     */
    default void writeUnsignedShort(final int size, final short value) throws IOException {
        writeShort(true, size, value);
    }

    /**
     * Writes specified {@code int} value of specified number of bits.
     *
     * @param unsigned a flag for indicating an unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Integer#SIZE} -
     *                 ({@code unsigned ? 1 : 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     */
    void writeInt(boolean unsigned, int size, int value) throws IOException;

    /**
     * Writes specified {@code int} value, of specified number of bits, as a signed value.
     *
     * @param size  the number of bits to write; between {@code 1} and {@value java.lang.Integer#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #writeInt(boolean, int, int)} method with {@code false},
     * {@code size}, and {@code value}.
     */
    default void writeInt(final int size, final int value) throws IOException {
        writeInt(false, size, value);
    }

    /**
     * Writes specified {@value java.lang.Integer#SIZE}-bit signed {@code int} value.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #writeInt(int, int)} method with
     * {@value java.lang.Integer#SIZE} and {@code value}.
     */
    default void writeInt(final int value) throws IOException {
        writeInt(Integer.SIZE, value);
    }

    /**
     * Writes specified {@code int} value, of specified number of bits, as an unsigned value.
     *
     * @param size  the number of bits to write; between {@code 1} (inclusive) and {@value java.lang.Integer#SIZE}
     *              (exclusive).
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #writeInt(boolean, int, int)} with {@code true},
     * {@code size}, and {@code value}.
     */
    default void writeUnsignedInt(final int size, final int value) throws IOException {
        writeInt(true, size, value);
    }

    /**
     * Writes specified {@code long} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Long#SIZE} -
     *                 ({@code unsigned ? 1 : 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     */
    default void writeLong(final boolean unsigned, int size, final long value) throws IOException {
        BitIoConstraints.requireValidSizeForLong(unsigned, size);
        if (!unsigned) {
            writeUnsignedInt(1, value < 0L ? 0x01 : 0x00); // the most significant (sign) bit
            if (--size > 0) {
                writeUnsignedLong(size, value);
            }
            return;
        }
        assert unsigned; // NOSONAR
        if (size >= Integer.SIZE) {
            writeInt((int) (value >> (size - Integer.SIZE)));
            size -= Integer.SIZE;
        }
        if (size > 0) {
            writeUnsignedInt(size, (int) value);
        }
    }

    /**
     * Writes specified signed {@code long} value of specified number of bits.
     *
     * @param size  the number of bits to write; between {@code 1} and {@value java.lang.Long#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #writeLong(boolean, int, long)} method with {@code false} and
     * given arguments.
     */
    default void writeLong(final int size, final long value) throws IOException {
        writeLong(false, size, value);
    }

    /**
     * Writes specified {@value java.lang.Long#SIZE}-bit {@code long} value.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #writeLong(int, long)} method with
     * {@value java.lang.Long#SIZE} and {@code value}.
     * @see #writeLong(int, long)
     */
    default void writeLong(final long value) throws IOException {
        writeLong(Long.SIZE, value);
    }

    /**
     * Writes specified unsigned {@code long} value of specified number of bits.
     *
     * @param size  the number of bits to write; between {@code 1} (inclusive) and {@value java.lang.Long#SIZE}
     *              (exclusive).
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #writeLong(boolean, int, long)} method with {@code true} and
     * given arguments.
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
     * @implSpec The default implementation invokes {@link #writeUnsignedInt(int, int)} method with given arguments.
     */
    default void writeChar(final int size, final char value) throws IOException {
        writeUnsignedInt(BitIoConstraints.requireValidSizeForChar(size), value);
    }

    /**
     * Writes specified {@value java.lang.Character#SIZE}-bit {@code char} value.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #writeChar(int, char)} method with
     * {@value java.lang.Character#SIZE} and {@code value}.
     */
    default void writeChar16(final char value) throws IOException {
        writeChar(Character.SIZE, value);
    }

    /**
     * Writes specified {@value java.lang.Float#SIZE}-bit {@code float} value.
     *
     * @param value the {@code float} value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation writes specified value as a {@value java.lang.Integer#SIZE}-bit {@code int}
     * value converted with {@link Float#floatToRawIntBits(float)} method.
     */
    default void writeFloat(final float value) throws IOException {
        writeInt(Float.floatToRawIntBits(value));
    }

    /**
     * Writes specified {@value java.lang.Double#SIZE}-bit {@code double} value.
     *
     * @param value the {@code double} value to write.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation writes specified value as a {@value java.lang.Long#SIZE}-bit {@code long}
     * value converted with {@link Double#doubleToRawLongBits(double)} method.
     */
    default void writeDouble(final double value) throws IOException {
        writeLong(Double.doubleToRawLongBits(value));
    }

    /**
     * Writes specified value using specified writer.
     *
     * @param writer the writer.
     * @param value  the value to write.
     * @param <T>    value type parameter
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link BitWriter#write(BitOutput, Object)} with {@code this} and
     * {@code value}.
     */
    default <T> void writeObject(final BitWriter<? super T> writer, final T value) throws IOException {
        Objects.requireNonNull(writer, "writer is null");
        writer.write(this, value);
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
     * Aligns to specified number of bytes by padding required number of zero-bits.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of zero-bits padded while aligning; non-negative, always.
     * @throws IllegalArgumentException if {@code bytes} is not positive.
     * @throws IOException              if an I/O error occurs.
     */
    long align(int bytes) throws IOException;

    /**
     * Aligns to a single byte by padding required number of zero-bits.
     *
     * @return the number of zero-bits padded while aligning; between {@code 0} (inclusive) and
     * {@value java.lang.Byte#SIZE} (exclusive).
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #align(int)} method with {@value java.lang.Byte#BYTES}.
     * @see #align(int)
     */
    default long align() throws IOException {
        final long padded = align(Byte.BYTES);
        assert padded >= 0L;
        assert padded < Byte.SIZE;
        return padded;
    }
}
