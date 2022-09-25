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
 * A byte output writes bytes to an instance of {@link java.io.RandomAccessFile}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see RandomAccessFileByteInput
 */
class RandomAccessFileByteOutput
        extends ByteOutputAdapter<RandomAccessFile> {

    /**
     * Creates a new instance on top of specified random access file.
     *
     * @param target the random access file to which bytes are written.
     */
    public RandomAccessFileByteOutput(final RandomAccessFile target) {
        super(target);
    }

    /**
     * {@inheritDoc}
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @implNote The {@code write(RandomAccessFile, int)} method of {@code RandomAccessFileByteOutput} class invokes
     * {@link RandomAccessFile#write(int)} method on {@link #target} with {@code value}.
     */
    @Override
    public void write(final int value) throws IOException {
        target.write(value);
    }
}
