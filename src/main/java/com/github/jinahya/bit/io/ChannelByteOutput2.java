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
 * A byte output which writes bytes to a writable byte channel.
 * <p>
 * Note that a flushing might be required when the {@code buffer}'s capacity is greater than {@code 1}.
 * <blockquote><pre>{@code
 * final WritableByteChannel channel = getTarget();
 * final ByteBuffer buffer = getBuffer();
 * for (buffer.flip(); buffer.hasRemaining(); ) {
 *     channel.write(buffer);
 * }
 * }</pre></blockquote>
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteOutput
 * @see ChannelByteInput2
 */
class ChannelByteOutput2 extends ByteOutputAdapter<WritableByteChannel> {

    // -----------------------------------------------------------------------------------------------------------------
    public static ChannelByteOutput2 of(final Supplier<? extends WritableByteChannel> targetSupplier) {
        if (targetSupplier == null) {
            throw new NullPointerException("targetSupplier is null");
        }
        return new ChannelByteOutput2(targetSupplier, () -> allocate(1));
    }

    // -----------------------------------------------------------------------------------------------------------------
    public ChannelByteOutput2(final Supplier<? extends WritableByteChannel> targetSupplier,
                              final Supplier<? extends ByteBuffer> bufferSupplier) {
        super(targetSupplier);
        this.bufferSupplier = requireNonNull(bufferSupplier, "bufferSupplier is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    protected void write(final WritableByteChannel target, final int value) throws IOException {
        final ByteBuffer buffer = buffer();
        while (!buffer.hasRemaining()) {
            buffer.flip(); // limit -> position, position -> zero
            final int written = target.write(buffer);
            buffer.compact();
        }
        buffer.put((byte) value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ByteBuffer buffer() {
        if (buffer == null) {
            buffer = bufferSupplier.get();
        }
        return buffer;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final Supplier<? extends ByteBuffer> bufferSupplier;

    private transient ByteBuffer buffer;
}
