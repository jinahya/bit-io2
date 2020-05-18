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
import java.nio.channels.WritableByteChannel;
import java.util.function.Supplier;

import static java.nio.ByteBuffer.allocate;
import static java.util.Objects.requireNonNull;

/**
 * A byte output writes bytes to a {@link ByteBuffer}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteInput
 */
public class BufferByteOutput extends ByteOutputAdapter<ByteBuffer> {

    /**
     * An implementation uses a single-byte-capacity buffer for writing bytes to a writable channel.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    private static class ChannelAdapter extends BufferByteOutput {

        /**
         * Creates a new instance with specified arguments.
         *
         * @param targetSupplier  a supplier for a byte buffer.
         * @param channelSupplier a supplier for a writable byte channel.
         */
        private ChannelAdapter(final Supplier<? extends ByteBuffer> targetSupplier,
                               final Supplier<? extends WritableByteChannel> channelSupplier) {
            super(targetSupplier);
            this.channelSupplier = requireNonNull(channelSupplier, "channelSupplier is null");
        }

        @Override
        public void flush() throws IOException {
            super.flush();
            if (target != null) {
                for (target.flip(); target.hasRemaining(); ) {
                    channel().write(target);
                }
                target.clear();
            }
        }

        @Override
        public void close() throws IOException {
            super.close();
            if (channel != null) {
                channel.close();
            }
        }

        @Override
        protected void write(final ByteBuffer target, final int value) throws IOException {
            while (!target.hasRemaining()) {
                target.flip(); // limit -> position, position -> zero
                channel().write(target);
                target.compact();
            }
            super.write(target, value);
        }

        private WritableByteChannel channel() {
            if (channel == null) {
                channel = channelSupplier.get();
            }
            return channel;
        }

        private final Supplier<? extends WritableByteChannel> channelSupplier;

        private WritableByteChannel channel;
    }

    /**
     * Creates a new instance which writes bytes to a writable byte channel supplied by specified supplier.
     *
     * @param channelSupplier the supplier for the writable byte channel.
     * @return a new instance.
     * @see BufferByteInput#from(Supplier)
     */
    public static BufferByteOutput from(final Supplier<? extends WritableByteChannel> channelSupplier) {
        return new ChannelAdapter(() -> allocate(1), channelSupplier) {
            @Override
            protected void write(final ByteBuffer target, final int value) throws IOException {
                super.write(target, value);
                flush();
            }
        };
    }

    /**
     * Creates a new instance with specified target supplier.
     *
     * @param targetSupplier the target supplier.
     */
    public BufferByteOutput(final Supplier<? extends ByteBuffer> targetSupplier) {
        super(targetSupplier);
    }

    /**
     * {@inheritDoc} The {@code write(ByteBuffer, int)} method of {@code BufferByteOutput} class invokes {@link
     * ByteBuffer#put(byte)} method on specified byte buffer with specified value casted as a {@code byte} value.
     *
     * @param target {@inheritDoc}
     * @param value  {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    protected void write(final ByteBuffer target, final int value) throws IOException {
        target.put((byte) value);
    }
}
