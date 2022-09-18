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
 * A byte input reads bytes from a {@link ByteBuffer}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteOutput
 */
public class BufferByteInput
        extends ByteInputAdapter<ByteBuffer> {

    /**
     * Creates a new instance with specified source supplier.
     *
     * @param source the source supplier.
     */
    public BufferByteInput(final ByteBuffer source) {
        super(source);
        if (source.capacity() == 0) {
            throw new IllegalArgumentException("source.capacity is zero");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @implNote The {@code read(ByteBuffer)} method of {@code BufferByteInput} class invokes
     * {@link ByteBuffer#get() get()} method on the {@link #source} and returns the result as an unsigned
     * {@value java.lang.Byte#SIZE}-bit {@code int} value.
     */
    @Override
    public int read() throws IOException {
        return source.get() & 0xFF; // BufferUnderflowException
    }
}
