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
import java.nio.channels.WritableByteChannel;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * An implementation uses a single-byte-capacity buffer for writing bytes to a channel.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
class BufferByteOutputChannelAdapter
        extends BufferByteOutput {

    /**
     * Creates a new instance with specified arguments.
     *
     * @param targetSupplier  a supplier for a byte buffer.
     * @param channelSupplier a supplier for a writable byte channel.
     */
    BufferByteOutputChannelAdapter(final Supplier<? extends ByteBuffer> targetSupplier,
                                   final Supplier<? extends WritableByteChannel> channelSupplier) {
        super(targetSupplier);
        this.channelSupplier = Objects.requireNonNull(channelSupplier, "channelSupplier is null");
    }

    @Override
    public void flush() throws IOException {
        super.flush();
        final ByteBuffer target = target(false);
        if (target != null) {
            for (target.flip(); target.hasRemaining(); ) {
                channel(true).write(target);
            }
            target.clear();
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        final WritableByteChannel channel = channel(false);
        if (channel != null) {
            channel.close();
        }
    }

    @Override
    protected void write(final ByteBuffer target, final int value) throws IOException {
        while (!target.hasRemaining()) {
            target.flip(); // limit -> position, position -> zero
            while (target.position() == 0) {
                channel(true).write(target);
            }
            target.compact();
        }
        super.write(target, value);
    }

    private WritableByteChannel channel(final boolean get) {
        if (get) {
            if (channel(false) == null) {
                channel(channelSupplier.get());
            }
            return channel(false);
        }
        return channel;
    }

    private void channel(final WritableByteChannel channel) {
        if (channel(false) != null) {
            throw new IllegalStateException("channel already has been supplied");
        }
        this.channel = Objects.requireNonNull(channel, "channel is null");
    }

    private final Supplier<? extends WritableByteChannel> channelSupplier;

    private WritableByteChannel channel;
}