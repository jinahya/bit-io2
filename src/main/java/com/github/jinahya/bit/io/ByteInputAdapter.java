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
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;

/**
 * An implementation of {@link BitInput} reads octets from an instance of {@link ByteInput}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ByteOutputAdapter
 */
public class ByteInputAdapter
        implements BitInput {

    /**
     * Creates a new instance on top of specified stream.
     *
     * @param stream the stream from which bytes are read.
     * @return a new instance.
     */
    static BitInput from(final InputStream stream) {
        return new ByteInputAdapter(new StreamByteInput(stream));
    }

    /**
     * Creates a new instance on top of specified input.
     *
     * @param input the input from which bytes are read.
     * @return a new instance.
     */
    static BitInput from(final DataInput input) {
        return new ByteInputAdapter(new DataByteInput(input));
    }

    /**
     * Creates a new instance on top of specified file.
     *
     * @param file the file from which bytes are read.
     * @return a new instance.
     */
    static BitInput from(final RandomAccessFile file) {
        return new ByteInputAdapter(new RandomAccessByteInput(file));
    }

    /**
     * Creates a new instance on top of specified buffer.
     *
     * @param buffer the buffer from which bytes are read.
     * @return a new instance.
     */
    static BitInput from(final ByteBuffer buffer) {
        return new ByteInputAdapter(new BufferByteInput(buffer));
    }

    /**
     * Creates a new instance on top of specified channel.
     *
     * @param channel the channel from which bytes are read.
     * @return a new instance.
     */
    static BitInput from(final ReadableByteChannel channel) {
        return new ByteInputAdapter(new ChannelByteInput(channel));
    }

    /**
     * Creates a new instance on top of specified byte input.
     *
     * @param input the byte input.
     */
    public ByteInputAdapter(final ByteInput input) {
        super();
        this.input = Objects.requireNonNull(input, "input is null");
    }

    @Override
    public int readInt(final boolean unsigned, int size) throws IOException {
        BitIoConstraints.requireValidSizeForInt(unsigned, size);
        int value = 0;
        if (!unsigned) {
            value -= readInt(true, 1);
            if (--size > 0) {
                value <<= size;
                value |= readInt(true, size);
            }
            return value;
        }
        for (; size >= Byte.SIZE; size -= Byte.SIZE) {
            value <<= Byte.SIZE;
            value |= unsigned8(Byte.SIZE);
        }
        if (size > 0) {
            value <<= size;
            value |= unsigned8(size);
        }
        return value;
    }

    @Override
    public long align(final int bytes) throws IOException {
        if (bytes <= 0) {
            throw new IllegalArgumentException("bytes(" + bytes + ") is not positive");
        }
        long bits = 0L; // the number of discarded bits
        if (available > 0) {
            bits += available; // must be prior to the below
            readInt(true, available);
        }
        assert available == 0;
        if (bytes == 1) {
            return bits;
        }
        for (int i = (bytes - (int) (this.count % bytes)); i > 0L; i--) {
            readInt(true, Byte.SIZE);
            bits += Byte.SIZE;
        }
        assert (count % bytes) == 0L;
        return bits;
    }

    /**
     * Reads an unsigned {@code int} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Byte#SIZE}, both inclusive.
     * @return an unsigned {@code int} value.
     * @throws IOException if an I/O error occurs.
     */
    private int unsigned8(final int size) throws IOException {
        assert size > 0 && size <= Byte.SIZE;
        if (available == 0) {
            octet = input.read();
            assert octet >= 0 && octet < 256;
            count++;
            available = Byte.SIZE;
        }
        if (available == Byte.SIZE && size == Byte.SIZE) { // return, directly, a full 8-bit octet
            available = 0;
            return octet;
        }
        final int required = size - available;
        if (required > 0) {
            return (unsigned8(available) << required) | unsigned8(required);
        }
        available -= size;
        return (octet >> available) & BitIoUtils.bitMaskSingle(size);
    }

    private final ByteInput input;

    /**
     * The current octet.
     */
    private int octet;

    /**
     * The number of available bits in {@link #octet}.
     */
    private int available = 0;

    /**
     * The number of bytes read, from the {@link #input}, so far.
     */
    private long count;
}
