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
import java.nio.channels.WritableByteChannel;
import java.util.function.Supplier;

import static java.nio.ByteBuffer.allocate;
import static java.util.Objects.requireNonNull;

/**
 * A byte output writes bytes to a writable byte channel.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteInput
 * @deprecated Use {@link ChannelByteOutput}.
 */
@Deprecated
class ChannelByteOutput extends BufferByteOutput {

    // -----------------------------------------------------------------------------------------------------------------
    public static ChannelByteOutput of(final Supplier<? extends WritableByteChannel> channelSupplier) {
        if (channelSupplier == null) {
            throw new NullPointerException("channelSupplier is null");
        }
        return new ChannelByteOutput(() -> allocate(1), channelSupplier);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public ChannelByteOutput(final Supplier<? extends ByteBuffer> targetSupplier,
                             final Supplier<? extends WritableByteChannel> channelSupplier) {
        super(targetSupplier);
        this.channelSupplier = requireNonNull(channelSupplier, "channelSupplier is null");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     *
     * @param target {@inheritDoc}
     * @param value  {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    protected void write(final ByteBuffer target, final int value) throws IOException {
        while (!target.hasRemaining()) {
            target.flip(); // limit -> position, position -> zero
            final int written = channel().write(target);
            target.compact();
        }
        super.write(value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private WritableByteChannel channel() {
        if (channel == null) {
            channel = channelSupplier.get();
        }
        return channel;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final Supplier<? extends WritableByteChannel> channelSupplier;

    private transient WritableByteChannel channel;
}
