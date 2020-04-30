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
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
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

    private static class ChanelAdapter extends BufferByteOutput {

        ChanelAdapter(final Supplier<? extends WritableByteChannel> channelSupplier) {
            super(() -> allocate(1));
            this.channelSupplier = requireNonNull(channelSupplier, "channelSupplier is null");
        }

        @Override
        protected void write(final ByteBuffer target, final int value) throws IOException {
            super.write(target, value);
            while (!target.hasRemaining()) {
                target.flip(); // limit -> position, position -> zero
                final int written = channel().write(target);
                target.compact();
            }
        }

        WritableByteChannel channel() {
            if (channel == null) {
                channel = channelSupplier.get();
            }
            return channel;
        }

        private final Supplier<? extends WritableByteChannel> channelSupplier;

        private transient WritableByteChannel channel;
    }

    /**
     * Creates a new instance writes bytes to a writable byte channel.
     *
     * @param channelSupplier a supplier for the writable byte channel.
     * @return a new instance.
     * @see #of(WritableByteChannel)
     * @see BufferByteInput#of(Supplier)
     */
    public static ByteOutput of(final Supplier<? extends WritableByteChannel> channelSupplier) {
        return new ChanelAdapter(channelSupplier);
    }

    /**
     * Creates a new instance writes bytes to specified writable byte channel.
     *
     * @param channel the writable byte channel to which bytes are written.
     * @return a new instance.
     * @see #of(Supplier)
     * @see BufferByteInput#of(ReadableByteChannel)
     */
    public static ByteOutput of(final WritableByteChannel channel) {
        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        return new ChanelAdapter(() -> null) {
            @Override
            WritableByteChannel channel() {
                return channel;
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
     * {@inheritDoc}
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
