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
import java.nio.channels.WritableByteChannel;
import java.util.function.Supplier;

/**
 * A byte output which writes bytes to a writable byte channel.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteInput
 */
class ChannelByteOutput extends ByteOutputAdapter<WritableByteChannel> {

    public ChannelByteOutput(final Supplier<? extends WritableByteChannel> targetSupplier) {
        super(targetSupplier);
    }

    @Override
    protected void write(final WritableByteChannel target, final int value) throws IOException {
        delegate(target).write(value);
    }

    private ByteOutput delegate(final WritableByteChannel channel) {
        if (delegate == null) {
            delegate = BufferByteOutput.of(channel);
        }
        return delegate;
    }

    private ByteOutput delegate;
}
