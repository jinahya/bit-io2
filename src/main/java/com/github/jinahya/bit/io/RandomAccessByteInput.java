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
import java.io.RandomAccessFile;

/**
 * A byte input reads bytes from an instance of {@link java.io.RandomAccessFile}.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 * @see RandomAccessByteOutput
 */
class RandomAccessByteInput
        extends AbstractByteInput<RandomAccessFile> {

    /**
     * Creates a new instance with specified source supplier.
     *
     * @param source the source supplier.
     */
    RandomAccessByteInput(final RandomAccessFile source) {
        super(source);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @implSpec The {@code read()} method of {@code RandomAccessFileByteInput} class invokes
     * {@link RandomAccessFile#readUnsignedByte() readUnsignedByte()} method on the {@link #source}, and returns the
     * result.
     * @see RandomAccessFile#readUnsignedByte()
     */
    @Override
    public int read() throws IOException {
        return source.readUnsignedByte();
    }
}
