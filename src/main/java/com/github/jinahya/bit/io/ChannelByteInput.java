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

/**
 * A byte input reads bytes from a readable byte channel.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteOutput
 */
public class ChannelByteInput
        extends AbstractByteInput<ReadableByteChannel> {

    /**
     * Creates a new instance with specified channel.
     *
     * @param channel the channel from which bytes are read.
     */
    public ChannelByteInput(final ReadableByteChannel channel) {
        super(channel);
        final ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.position(buffer.capacity());
        delegate = new BufferByteInput(buffer) {
            @Override
            public int read() throws IOException {
                if (!source.hasRemaining()) {
                    for (source.clear(); source.position() == 0; ) {
                        if (ChannelByteInput.this.source.read(source) == -1) {
                            throw new EOFException("reached to an end");
                        }
                    }
                }
                source.flip(); // limit -> position, position -> zero
                return super.read();
            }
        };
    }

    @Override
    public int read() throws IOException {
        return delegate.read();
    }

    private final ByteInput delegate;
}
