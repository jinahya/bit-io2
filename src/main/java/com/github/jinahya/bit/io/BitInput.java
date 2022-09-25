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

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;

/**
 * An interface for reading values of an arbitrary number of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BitOutput
 */
public interface BitInput {

    /**
     * Creates a new instance on top of specified stream.
     *
     * @param stream the stream from which bytes are read.
     * @return a new instance.
     * @see BitOutput#from(java.io.OutputStream)
     */
    static BitInput from(final InputStream stream) {
        Objects.requireNonNull(stream, "stream is null");
        return new BitInputAdapter(new StreamByteInput(stream));
    }

    /**
     * Creates a new instance on top of specified input.
     *
     * @param input the input from which bytes are read.
     * @return a new instance.
     * @see BitOutput#from(java.io.DataOutput)
     */
    static BitInput from(final DataInput input) {
        Objects.requireNonNull(input, "input is null");
        return new BitInputAdapter(new DataByteInput(input));
    }

    /**
     * Creates a new instance on top of specified buffer.
     *
     * @param buffer the buffer from which bytes are read.
     * @return a new instance.
     * @see BitOutput#from(ByteBuffer)
     */
    static BitInput from(final ByteBuffer buffer) {
        Objects.requireNonNull(buffer, "buffer is null");
        return new BitInputAdapter(new BufferByteInput(buffer));
    }

    /**
     * Creates a new instance on top of specified channel.
     *
     * @param channel the channel from which bytes are read.
     * @return a new instance.
     * @see BitOutput#from(java.nio.channels.WritableByteChannel)
     */
    static BitInput from(final ReadableByteChannel channel) {
        Objects.requireNonNull(channel, "channel is null");
        return new BitInputAdapter(new ChannelByteInput(channel));
    }

    /**
     * Reads a {@code boolean} value.
     *
     * @return the value read.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation reads a {@code 1}-bit <em>unsigned</em> {@code int} value and returns
     * {@code true} for {@code 0b1} and {@code false} for {@code 0b0}
     */
    default boolean readBoolean() throws IOException {
        return readUnsignedInt(1) == 0x01;
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
        return (byte) readInt(unsigned, BitIoConstraints.requireValidSizeForByte(unsigned, size));
    }

    /**
     * Reads a <em>signed</em> {@code byte} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Byte#SIZE}, both inclusive.
     * @return a signed {@code byte} value of specified {@code size}.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #readByte(boolean, int)} method with {@code false} and
     * {@code size}, and returns the result.
     */
    default byte readByte(final int size) throws IOException {
        return readByte(false, size);
    }

    /**
     * Reads a <em>signed</em> {@value java.lang.Byte#SIZE}-bit {@code byte} value.
     *
     * @return a signed {@value java.lang.Byte#SIZE}-bit {@code byte} value.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #readByte(int)} method with {@value java.lang.Byte#SIZE}, and
     * returns the result.
     */
    default byte readByte() throws IOException {
        return readByte(Byte.SIZE);
    }

    /**
     * Reads an <em>unsigned</em> {@code byte} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Byte#SIZE}
     *             (exclusive).
     * @return an unsigned {@code byte} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #readByte(boolean, int)} method with {@code true} and given
     * {@code size}, and returns the result.
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
     * @implSpec The default implementation invokes {@link #readInt(boolean, int)} method with given arguments, and
     * returns the result as a {@code short} value.
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
     * @implSpec The default implementation invokes {@link #readShort(boolean, int)} method with {@code false} and
     * {@code size}, and returns the result.
     * @see #readShort(boolean, int)
     */
    default short readShort(final int size) throws IOException {
        return readShort(false, size);
    }

    /**
     * Reads a <em>signed</em> {@value java.lang.Short#SIZE}-bit {@code short} value.
     *
     * @return a {@value java.lang.Short#SIZE}-bit {@code short} value read.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #readShort(int)} method with {@value java.lang.Short#SIZE},
     * and returns the result.
     */
    default short readShort() throws IOException {
        return readShort(Short.SIZE);
    }

    /**
     * Reads an <em>unsigned</em> {@code short} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Short#SIZE}
     *             (exclusive).
     * @return an unsigned {@code short} value of specified bit {@code size}.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #readShort(boolean, int)} method with {@code true} and
     * {@code size}, and returns the result.
     */
    default short readUnsignedShort(final int size) throws IOException {
        return readShort(true, size);
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
     * Reads a <em>signed</em> {@code int} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Integer#SIZE}, both inclusive.
     * @return a signed {@code int} value.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #readInt(boolean, int)} method with {@code false} and given
     * {@code size}, and returns the result.
     */
    default int readInt(final int size) throws IOException {
        return readInt(false, size);
    }

    /**
     * Reads a <em>signed</em> {@value java.lang.Integer#SIZE}-bit {@code int} value.
     *
     * @return a {@value java.lang.Integer#SIZE}-bit {@code int} value read.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #readInt(int)} method with {@value java.lang.Integer#SIZE},
     * and returns the result.
     */
    default int readInt() throws IOException {
        return readInt(Integer.SIZE);
    }

    /**
     * Reads an <em>unsigned</em> {@code int} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Integer#SIZE}
     *             (exclusive).
     * @return an unsigned {@code int} value.
     * @throws IOException if an error occurs.
     * @implSpec The default implementation invokes {@link #readInt(boolean, int)} method with {@code true} and
     * {@code size}, and returns the result.
     */
    default int readUnsignedInt(final int size) throws IOException {
        return readInt(true, size);
    }

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
        assert unsigned; // NOSONAR
        if (size >= Integer.SIZE) {
            value = readInt() & 0xFFFFFFFFL;
            size -= Integer.SIZE;
        }
        if (size > 0) {
            value <<= size;
            value |= readUnsignedInt(size);
        }
        return value;
    }

    /**
     * Reads a <em>signed</em> {@code long} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Long#SIZE}, both inclusive.
     * @return a signed {@code long} value.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #readLong(boolean, int)} method with {@code false} and given
     * {@code size}, and returns the result.
     */
    default long readLong(final int size) throws IOException {
        return readLong(false, size);
    }

    /**
     * Reads a <em>signed</em> {@value java.lang.Long#SIZE}-bit {@code long} value.
     *
     * @return a {@value java.lang.Long#SIZE}-bit {@code long} value.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #readLong(int)} method with {@value java.lang.Long#SIZE}, and
     * returns the result.
     */
    default long readLong() throws IOException {
        return readLong(Long.SIZE);
    }

    /**
     * Reads an <em>unsigned</em> {@code long} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} (inclusive) and {@value java.lang.Long#SIZE}
     *             (exclusive).
     * @return an unsigned {@code long} value.
     * @throws IOException if an error occurs.
     * @implSpec The default implementation invokes {@link #readLong(boolean, int)} method with {@code true} and given
     * {@code size}, and returns the result.
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
     * @implSpec The default implementation invokes {@link #readUnsignedInt(int)} method with given {@code size}, and
     * returns the result as a {@code char}.
     */
    default char readChar(final int size) throws IOException {
        return (char) readUnsignedInt(BitIoConstraints.requireValidSizeForChar(size));
    }

    /**
     * Reads a {@value java.lang.Character#SIZE}-bit {@code char} value.
     *
     * @return a {@code char} value.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #readChar(int)} method with
     * {@value java.lang.Character#SIZE}, and returns the result.
     */
    default char readChar() throws IOException {
        return readChar(Character.SIZE);
    }

    /**
     * Reads a {@value java.lang.Float#SIZE}-bit {@code float} value.
     *
     * @return a {@value java.lang.Float#SIZE}-bit {@code float} value
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation reads a {@value java.lang.Integer#SIZE}-bit signed {@code int} value, and
     * returns the value as a {@code float} value converted using {@link Float#intBitsToFloat(int)} method.
     */
    default float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    /**
     * Reads a {@value java.lang.Double#SIZE}-bit {@code double} value.
     *
     * @return a {@value java.lang.Double#SIZE}-bit {@code double} value
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation reads a {@value java.lang.Long#SIZE}-bit signed {@code long} value, and
     * returns the value as a {@code double} value converted using {@link Double#longBitsToDouble(long)} method.
     * @see #readLong()
     */
    default double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    /**
     * Reads a value using specified reader.
     *
     * @param reader the reader.
     * @param <T>    value type parameter
     * @return a value read.
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link BitReader#read(BitInput)} method on specified {@code reader}
     * with {@code this}, and returns the result.
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
            readInt();
        }
        if (bits > 0) {
            readUnsignedInt(bits);
        }
    }

    /**
     * Aligns to specified number of <em>bytes</em> by reading (and discarding) required number of bits.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of bits discarded; non-negative, always.
     * @throws IllegalArgumentException if {@code bytes} is not positive.
     * @throws IOException              if an I/O error occurs.
     */
    long align(int bytes) throws IOException;

    /**
     * Aligns to a single <em>byte</em> by reading (and discarding) required number of bits.
     *
     * @return the number of bits discarded; between {@code 0} (inclusive) and {@value java.lang.Byte#SIZE} (exclusive).
     * @throws IOException if an I/O error occurs.
     * @implSpec The default implementation invokes {@link #align(int)} method with {@value java.lang.Byte#BYTES}, and
     * returns the result.
     */
    default long align() throws IOException {
        final long discarded = align(Byte.BYTES);
        assert discarded >= 0L;
        assert discarded < Byte.SIZE;
        return discarded;
    }
}
