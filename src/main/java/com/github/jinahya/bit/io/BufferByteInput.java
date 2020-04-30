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

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.function.Supplier;

import static java.nio.ByteBuffer.allocate;
import static java.util.Objects.requireNonNull;

/**
 * A byte input reads bytes from a {@link ByteBuffer}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteOutput
 */
public class BufferByteInput extends ByteInputAdapter<ByteBuffer> {

    private static class ChannelAdapter extends BufferByteInput {

        ChannelAdapter(final Supplier<? extends ReadableByteChannel> channelSupplier) {
            super(() -> (ByteBuffer) allocate(1).position(1));
            this.channelSupplier = requireNonNull(channelSupplier, "channelSupplier is null");
        }

        @Override
        protected int read(final ByteBuffer source) throws IOException {
            while (!source.hasRemaining()) {
                source.clear(); // position -> zero, limit -> capacity
                final ReadableByteChannel channel = channel();
                if (channel().read(source) == -1) {
                    throw new EOFException("channel has reached end-of-stream");
                }
                source.flip(); // limit -> position, position -> zero
            }
            return super.read(source);
        }

        ReadableByteChannel channel() {
            if (channel == null) {
                channel = channelSupplier.get();
            }
            return channel;
        }

        private final Supplier<? extends ReadableByteChannel> channelSupplier;

        private transient ReadableByteChannel channel;
    }

    /**
     * Creates a new instance reads bytes from a readable byte channel.
     *
     * @param channelSupplier a supplier for the readable byte channel.
     * @return a new instance.
     */
    public static ByteInput of(final Supplier<? extends ReadableByteChannel> channelSupplier) {
        return new ChannelAdapter(channelSupplier);
    }

    /**
     * Creates a new instance reads bytes from specified readable byte channel.
     *
     * @param channel the readable byte channel.
     * @return a new instance.
     */
    public static ByteInput of(final ReadableByteChannel channel) {
        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        return new ChannelAdapter(() -> null) {
            @Override
            ReadableByteChannel channel() {
                return channel;
            }
        };
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
     * {@inheritDoc}
     *
     * @param source {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    protected int read(final ByteBuffer source) throws IOException {
        return source.get() & 0xFF;
    }
}
