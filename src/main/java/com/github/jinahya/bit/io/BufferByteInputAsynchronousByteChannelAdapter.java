package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 - 2022 Jinahya, Inc.
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
import java.nio.channels.AsynchronousByteChannel;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * An implementation uses a single-byte-capacity buffer for reading bytes from an asynchronous byte channel.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
class BufferByteInputAsynchronousByteChannelAdapter
        extends BufferByteInput {

    /**
     * Creates a new instance with specified arguments.
     *
     * @param sourceSupplier  a supplier for a byte buffer.
     * @param channelSupplier a supplier for a readable byte channel from which the {@code buffer} is filled.
     */
    BufferByteInputAsynchronousByteChannelAdapter(final Supplier<? extends ByteBuffer> sourceSupplier,
                                                  final Supplier<? extends AsynchronousByteChannel> channelSupplier) {
        super(sourceSupplier);
        this.channelSupplier = Objects.requireNonNull(channelSupplier, "channelSupplier is null");
    }

    @Override
    public void close() throws IOException {
        super.close();
        final AsynchronousByteChannel channel = channel(false);
        if (channel != null) {
            channel.close();
        }
    }

    @Override
    protected int read(final ByteBuffer source) throws IOException {
        while (!source.hasRemaining()) {
            source.clear(); // position -> zero, limit -> capacity
            try {
                while (channel(true).read(source).get() == 0) {
                }
            } catch (InterruptedException | ExecutionException e) {
                final Throwable cause = e.getCause();
                if (cause instanceof IOException) {
                    throw (IOException) cause;
                }
                throw new RuntimeException(cause);
            }
            source.flip();
        }
        return super.read(source);
    }

    private AsynchronousByteChannel channel(final boolean get) {
        if (get) {
            if (channel(false) == null) {
                channel(channelSupplier.get());
            }
            return channel(false);
        }
        return channel;
    }

    private void channel(final AsynchronousByteChannel channel) {
        if (channel(false) != null) {
            throw new IllegalStateException("channel already has been supplied");
        }
        this.channel = Objects.requireNonNull(channel, "channel is null");
    }

    private final Supplier<? extends AsynchronousByteChannel> channelSupplier;

    private AsynchronousByteChannel channel;
}
