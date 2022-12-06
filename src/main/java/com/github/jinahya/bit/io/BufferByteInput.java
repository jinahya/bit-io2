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
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * A byte input reads bytes from a {@link ByteBuffer}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteOutput
 */
public class BufferByteInput
        extends AbstractByteInput<ByteBuffer> {

    /**
     * Creates a new instance with specified byte buffer.
     *
     * @param source the byte buffer from which byte are gotten.
     */
    public BufferByteInput(final ByteBuffer source) {
        super(source);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @implSpec Default implementation invokes {@link ByteBuffer#get() get()} method on the {@link #source}, and
     * returns the result cast as an unsigned {@value java.lang.Byte#SIZE}-bit {@code int} value;
     * @implNote Default implementation throws an {@link IOException} when the {@link ByteBuffer#get()} method throws a
     * {@link BufferUnderflowException}.
     * @see ByteBuffer#get()
     */
    @Override
    public int read() throws IOException {
        try {
            return source.get() & 0xFF;
        } catch (final BufferUnderflowException bue) {
            throw new IOException("no bytes in the buffer", bue);
        }
    }
}
