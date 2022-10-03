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
        extends AbstractByteOutput<ByteBuffer> {

    /**
     * Creates a new instance with specified byte buffer.
     *
     * @param target the byte buffer on which bytes are put.
     */
    public BufferByteOutput(final ByteBuffer target) {
        super(target);
    }

    /**
     * {@inheritDoc}
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @apiNote This method may throw an {@link java.nio.BufferOverflowException} when the
     * {@link #target target buffer}'s current position is not smaller than its limit.
     * @implSpec The {@code write(int)} method of {@code BufferByteOutput} class invokes {@link ByteBuffer#put(byte)}
     * method on the {@link #target} with specified value cast as a {@code byte} value.
     * @see ByteBuffer#put(byte)
     */
    @Override
    public void write(final int value) throws IOException {
        target.put((byte) value); // BufferOverflowException, ReadOnlyBufferException
    }
}
