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

/**
 * A byte output writes bytes to a writable byte channel.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteInput
 */
public class ChannelByteOutput
        extends AbstractByteOutput<WritableByteChannel> {

    /**
     * Creates a new instance on top of specified channel.
     *
     * @param channel the channel to which bytes are written.
     */
    public ChannelByteOutput(final WritableByteChannel channel) {
        super(channel);
        delegate = new BufferByteOutput(ByteBuffer.allocate(1)) {
            @Override
            public void write(final int value) throws IOException {
                {
                    assert target.capacity() == 1;
                    assert target.limit() == 1;
                    assert target.position() == 0;
                    assert target.remaining() == 1;
                }
                super.write(value);
                {
                    assert target.capacity() == 1;
                    assert target.limit() == 1;
                    assert target.position() == 1;
                    assert target.remaining() == 0;
                }
                for (target.flip(); target.hasRemaining(); ) {
                    ChannelByteOutput.this.target.write(target);
                }
                {
                    assert target.capacity() == 1;
                    assert target.limit() == 1;
                    assert target.position() == 1;
                    assert target.remaining() == 0;
                }
            }
        };
    }

    @Override
    public void write(final int value) throws IOException {
        delegate.write(value);
    }

    private final BufferByteOutput delegate;
}
