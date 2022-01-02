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
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A byte input reads bytes from a {@link ByteBuffer}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteOutput
 */
public class BufferByteInput
        extends ByteInputAdapter<ByteBuffer> {

    /**
     * Creates a new instance which reads bytes from the channel supplied by specified supplier.
     *
     * @param channelSupplier the supplier of the channel.
     * @return a new instance.
     */
    public static BufferByteInput adapting(final Supplier<? extends ReadableByteChannel> channelSupplier) {
        return new BufferByteInputChannelAdapter(() -> (ByteBuffer) ByteBuffer.allocate(1).position(1),
                                                 channelSupplier);
    }

    /**
     * Creates a new instance which reads bytes from specified channel.
     *
     * @param channel the channel from which bytes are read.
     * @return a new instance.
     */
    public static BufferByteInput adapting(final ReadableByteChannel channel) {
        Objects.requireNonNull(channel, "channel is null");
        return adapting(() -> channel);
    }

    /**
     * Creates a new instance which reads bytes from specified buffer.
     *
     * @param source the byte buffer which bytes are read.
     * @return a new instance.
     */
    public static ByteInput of(final ByteBuffer source) {
        Objects.requireNonNull(source, "source is null");
        final ByteInputAdapter<ByteBuffer> instance = new BufferByteInput(empty());
        instance.source(source);
        return instance;
    }

    /**
     * Creates a new instance with specified source supplier.
     *
     * @param sourceSupplier the source supplier.
     */
    public BufferByteInput(final Supplier<? extends ByteBuffer> sourceSupplier) {
        super(sourceSupplier);
    }

    /**
     * {@inheritDoc} The {@code read(ByteBuffer)} method of {@code BufferByteInput} class invokes {@link
     * ByteBuffer#get()} method on specified byte buffer and returns the result as an unsigned {@code int}.
     *
     * @param source {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    protected int read(final ByteBuffer source) throws IOException {
        return source.get() & 0xFF;
    }

    @Override
    void source(final ByteBuffer source) {
        super.source(source);
        if (source(false).capacity() == 0) {
            throw new IllegalArgumentException("source.capacity is zero");
        }
    }
}
