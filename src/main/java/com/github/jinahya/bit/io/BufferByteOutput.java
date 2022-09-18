package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 Jinahya, Inc.
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

/**
 * A byte output writes bytes to a {@link ByteBuffer}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteInput
 */
public class BufferByteOutput
        extends ByteOutputAdapter<ByteBuffer> {

    /**
     * Creates a new instance with specified target buffer.
     *
     * @param target the target buffer.
     */
    public BufferByteOutput(final ByteBuffer target) {
        super(target);
        if (target.capacity() == 0) {
            throw new IllegalArgumentException("target.capacity is zero");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @implNote The {@code write(ByteBuffer, int)} method of {@code BufferByteOutput} class invokes
     * {@link ByteBuffer#put(byte)} method on specified byte buffer with specified value as a {@code byte} value.
     */
    @Override
    public void write(final int value) throws IOException {
        target.put((byte) value); // BufferOverflowException, ReadOnlyBufferException
    }
}
