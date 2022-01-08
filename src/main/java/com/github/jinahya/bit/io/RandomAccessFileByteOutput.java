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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A byte output writes bytes to an instance of {@link java.io.RandomAccessFile}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see RandomAccessFileByteInput
 */
class RandomAccessFileByteOutput
        extends ByteOutputAdapter<RandomAccessFile> {

    /**
     * Returns a new instance which writes bytes to specified file.
     *
     * @param file the file to which bytes are written.
     * @return a new instance.
     * @see RandomAccessFileByteInput#from(File)
     */
    static RandomAccessFileByteOutput from(final File file) {
        Objects.requireNonNull(file, "file is null");
        return new RandomAccessFileByteOutput(() -> {
            try {
                return new RandomAccessFile(file, "rwd");
            } catch (final IOException ioe) {
                throw new RuntimeException(ioe);
            }
        });
    }

    /**
     * Creates a new byte output which reads bytes from specified random access file.
     *
     * @param target the random access file to which bytes are written.
     * @return a new instance.
     * @see RandomAccessFileByteInput#of(RandomAccessFile)
     */
    public static RandomAccessFileByteOutput of(final RandomAccessFile target) {
        Objects.requireNonNull(target, "target is null");
        final RandomAccessFileByteOutput instance = new RandomAccessFileByteOutput(BitIoUtils.empty());
        instance.target(target);
        return instance;
    }

    /**
     * Creates a new instance with specified target supplier.
     *
     * @param targetSupplier the target supplier.
     */
    public RandomAccessFileByteOutput(final Supplier<? extends RandomAccessFile> targetSupplier) {
        super(targetSupplier);
    }

    /**
     * {@inheritDoc}
     *
     * @param target {@inheritDoc}
     * @param value  {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @implSpec The {@code write(RandomAccessFile, int)} method of {@code RandomAccessFileByteOutput} class invokes
     * {@link RandomAccessFile#write(int)} method on {@code target} with {@code value}.
     */
    @Override
    protected void write(final RandomAccessFile target, final int value) throws IOException {
        target.write(value);
    }
}
