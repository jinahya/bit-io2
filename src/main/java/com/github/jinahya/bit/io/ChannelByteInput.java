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
import java.nio.channels.ReadableByteChannel;
import java.util.function.Supplier;

/**
 * A byte input which reads bytes from a readable byte channel.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteOutput
 */
class ChannelByteInput extends ByteInputAdapter<ReadableByteChannel> {

    /**
     * Creates new instance with specified source supplier.
     *
     * @param sourceSupplier the source supplier.
     */
    public ChannelByteInput(final Supplier<? extends ReadableByteChannel> sourceSupplier) {
        super(sourceSupplier);
    }

    @Override
    protected int read(final ReadableByteChannel source) throws IOException {
        return delegate(source).read();
    }

    private ByteInput delegate(final ReadableByteChannel channel) {
        if (delegate == null) {
            delegate = BufferByteInput.of(channel);
        }
        return delegate;
    }

    private ByteInput delegate;
}
