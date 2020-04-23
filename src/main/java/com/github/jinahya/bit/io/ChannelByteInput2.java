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
 * A byte input which reads bytes from a readable byte channel.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteInput
 * @see ChannelByteOutput2
 */
class ChannelByteInput2 extends ByteInputAdapter<ReadableByteChannel> {

    // -----------------------------------------------------------------------------------------------------------------
    public static ChannelByteInput2 of(final Supplier<? extends ReadableByteChannel> channelSupplier) {
        if (channelSupplier == null) {
            throw new NullPointerException("channelSupplier is null");
        }
        return new ChannelByteInput2(channelSupplier, () -> (ByteBuffer) allocate(1).position(1));
    }

    // -----------------------------------------------------------------------------------------------------------------
    public ChannelByteInput2(final Supplier<? extends ReadableByteChannel> sourceSupplier,
                             final Supplier<? extends ByteBuffer> bufferSupplier) {
        super(sourceSupplier);
        this.bufferSupplier = requireNonNull(bufferSupplier, "bufferSupplier is null");
    }

    @Override
    public int read(final ReadableByteChannel source) throws IOException {
        final ByteBuffer buffer = buffer();
        while (!buffer.hasRemaining()) {
            buffer.clear(); // position -> zero, limit -> capacity
            if (source.read(buffer) == -1) {
                throw new EOFException("reached to an end");
            }
            buffer.flip(); // limit -> position, position -> zero
        }
        return buffer.get() & 0xFF;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ByteBuffer buffer() {
        if (buffer == null) {
            buffer = bufferSupplier.get();
        }
        if (buffer == null) {
            throw new RuntimeException("null buffer supplied");
        }
        if (buffer.capacity() == 0) {
            throw new RuntimeException("zero-capacity buffer supplied");
        }
        return buffer;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final Supplier<? extends ByteBuffer> bufferSupplier;

    private transient ByteBuffer buffer;
}
