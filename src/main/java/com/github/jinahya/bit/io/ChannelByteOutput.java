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

/**
 * A byte output writes bytes from a writable byte channel.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteInput
 */
public class ChannelByteOutput
        extends ByteOutputAdapter<WritableByteChannel> {

    private static class DelegatingBufferByteOutput
            extends BufferByteOutput {

        DelegatingBufferByteOutput(final ByteBuffer target, final WritableByteChannel channel) {
            super(target);
            this.channel = Objects.requireNonNull(channel, "channel is null");
        }

        @Override
        public void flush() throws IOException {
            super.flush();
            for (target.flip(); target.hasRemaining(); ) {
                channel.write(target);
            }
            target.clear();
        }

        @Override
        public void close() throws IOException {
            super.close();
            channel.close();
        }

        @Override
        public void write(final int value) throws IOException {
            if (!target.hasRemaining()) {
                target.flip();
                while (target.position() == 0) {
                    final int written = channel.write(target);
                }
                target.compact();
            }
            super.write(value);
        }

        private final WritableByteChannel channel;
    }

    /**
     * Creates a new instance on top of specified channel which uses specified buffer.
     *
     * @param target the channel to which bytes are written.
     * @param buffer a buffer to use; must have a non-zero capacity.
     */
    public ChannelByteOutput(final WritableByteChannel target, final ByteBuffer buffer) {
        super(target);
        this.delegate = new DelegatingBufferByteOutput(buffer, target);
    }

    @Override
    public void flush() throws IOException {
        delegate.flush();
        super.flush();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
        super.close();
    }

    @Override
    public void write(final int value) throws IOException {
        delegate.write(value);
    }

    private final BufferByteOutput delegate;
}
