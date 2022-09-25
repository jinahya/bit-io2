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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * A byte input reads bytes from an instance of {@link InputStream}.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 * @see StreamByteOutput
 */
public class StreamByteInput
        extends ByteInputAdapter<InputStream> {

    /**
     * Creates a new instance on top of specified input stream.
     *
     * @param source the input stream from which bytes are read.
     */
    public StreamByteInput(final InputStream source) {
        super(source);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @throws EOFException if the {@link InputStream#read()} operation returns {@code -1}.
     * @throws IOException  {@inheritDoc}
     * @implNote The {@code read(InputStream)} method of {@code StreamByteInput} class invokes
     * {@link InputStream#read()} method on {@link #source} and returns the result.
     */
    @Override
    public int read() throws IOException {
        final int value = source.read();
        if (value == -1) {
            throw new EOFException("reached to an end");
        }
        return value;
    }
}
