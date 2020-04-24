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
import java.util.function.Supplier;

import static java.nio.ByteBuffer.allocate;
import static java.util.Objects.requireNonNull;

/**
 * A byte input reads bytes from a readable byte channel.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteOutput
 * @deprecated Use {@link ChannelByteInput2}.
 */
@Deprecated
class ChannelByteInput extends BufferByteInput {

    // -----------------------------------------------------------------------------------------------------------------
    public static ChannelByteInput of(final Supplier<? extends ReadableByteChannel> channelSupplier) {
        if (channelSupplier == null) {
            throw new NullPointerException("channelSupplier is null");
        }
        return new ChannelByteInput(() -> (ByteBuffer) allocate(1).position(1), channelSupplier);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public ChannelByteInput(final Supplier<? extends ByteBuffer> sourceSupplier,
                            final Supplier<? extends ReadableByteChannel> channelSupplier) {
        super(sourceSupplier);
        this.channelSupplier = requireNonNull(channelSupplier, "channelSupplier is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public int read(final ByteBuffer source) throws IOException {
        while (!source.hasRemaining()) {
            source.clear(); // position -> zero, limit -> capacity
            channel().read(source);
            source.flip(); // limit -> position, position -> zero
        }
        return super.read(source);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ReadableByteChannel channel() {
        if (channel == null) {
            channel = channelSupplier.get();
        }
        return channel;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final Supplier<? extends ReadableByteChannel> channelSupplier;

    private transient ReadableByteChannel channel;
}
