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

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;

/**
 * A byte input reads bytes from a readable byte channel.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteOutput
 */
public class ChannelByteInput
        extends ByteInputAdapter<ReadableByteChannel> {

    private static class DelegatingBufferByteInput
            extends BufferByteInput {

        DelegatingBufferByteInput(final ByteBuffer source, final ReadableByteChannel channel) {
            super(source);
            this.channel = Objects.requireNonNull(channel, "channel is null");
        }

        @Override
        public void close() throws IOException {
            super.close(); // effectively, does nothing.
            channel.close();
        }

        @Override
        public int read() throws IOException {
            if (!source.hasRemaining()) {
                source.clear();
                while (source.position() == 0) {
                    if (channel.read(source) == -1) {
                        throw new EOFException("reached to an end");
                    }
                }
                source.flip();
            }
            return super.read();
        }

        private final ReadableByteChannel channel;
    }

    /**
     * Creates a new instance with specified arguments.
     *
     * @param source a source for lazily opening a channel.
     * @param buffer a non-zero capacity buffer to use.
     */
    public ChannelByteInput(final ReadableByteChannel source, final ByteBuffer buffer) {
        super(source);
        if (Objects.requireNonNull(buffer, "buffer is null").capacity() == 0) {
            throw new IllegalArgumentException("buffer.capacity is zero");
        }
        this.delegate = new DelegatingBufferByteInput(buffer, source);
    }

    @Override
    public void close() throws IOException {
        delegate.close();
        super.close();
    }

    @Override
    public int read() throws IOException {
        return delegate.read();
    }

    private final ByteInput delegate;
}
