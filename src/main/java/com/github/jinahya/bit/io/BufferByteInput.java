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

    /**
     * An extended class for adapting readable byte channels.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    private static class ChannelAdapter extends BufferByteInput {

        /**
         * Creates a new instance with specified arguments.
         *
         * @param sourceSupplier  a supplier for a byte buffer.
         * @param channelSupplier a supplier for a readable byte channel from which the {@code buffer} is filled.
         */
        private ChannelAdapter(final Supplier<? extends ByteBuffer> sourceSupplier,
                               final Supplier<? extends ReadableByteChannel> channelSupplier) {
            super(sourceSupplier);
            this.channelSupplier = requireNonNull(channelSupplier, "channelSupplier is null");
        }

        @Override
        public void close() throws IOException {
            super.close();
            if (channel != null) {
                channel.close();
            }
        }

        @Override
        protected int read(final ByteBuffer source) throws IOException {
            while (!source.hasRemaining()) {
                source.clear(); // position -> zero, limit -> capacity
                if (channel().read(source) == -1) {
                    throw new EOFException("channel has reached end-of-stream");
                }
                source.flip(); // limit -> position, position -> zero
            }
            return super.read(source);
        }

        private ReadableByteChannel channel() {
            if (channel == null) {
                channel = channelSupplier.get();
            }
            return channel;
        }

        private final Supplier<? extends ReadableByteChannel> channelSupplier;

        private ReadableByteChannel channel;
    }

    /**
     * Creates a new instance which reads bytes from a readable byte channel supplied by specified supplier.
     *
     * @param channelSupplier the supplier for the readable byte channel.
     * @return a new instance.
     */
    public static BufferByteInput from(final Supplier<? extends ReadableByteChannel> channelSupplier) {
        return new ChannelAdapter(() -> (ByteBuffer) allocate(1).position(1), channelSupplier);
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
     * ByteBuffer#get()} method on specified source and returns the result as an unsigned {@code int}.
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
