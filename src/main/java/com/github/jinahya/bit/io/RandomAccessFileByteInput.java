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
 * A byte input reads bytes from an instance of {@link java.io.RandomAccessFile}.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 * @see RandomAccessFileByteOutput
 */
class RandomAccessFileByteInput
        extends ByteInputAdapter<RandomAccessFile> {

    /**
     * Returns a new instance which reads bytes from specified file.
     *
     * @param file the file from which bytes are read.
     * @return a new instance.
     * @see RandomAccessFileByteOutput#from(File)
     */
    static RandomAccessFileByteInput from(final File file) {
        Objects.requireNonNull(file, "file is null");
        return new RandomAccessFileByteInput(() -> {
            try {
                return new RandomAccessFile(file, "r");
            } catch (final IOException ioe) {
                throw new RuntimeException(ioe);
            }
        });
    }

    /**
     * Creates a new byte input which reads bytes from specified random access file.
     *
     * @param source the random access file from which bytes are read.
     * @return a new instance.
     * @see RandomAccessFileByteOutput#of(RandomAccessFile)
     */
    public static RandomAccessFileByteInput of(final RandomAccessFile source) {
        Objects.requireNonNull(source, "source is null");
        final RandomAccessFileByteInput instance = new RandomAccessFileByteInput(BitIoUtils.empty());
        instance.source(source);
        return instance;
    }

    /**
     * Creates a new instance with specified source supplier.
     *
     * @param sourceSupplier the source supplier.
     */
    RandomAccessFileByteInput(final Supplier<? extends RandomAccessFile> sourceSupplier) {
        super(sourceSupplier);
    }

    /**
     * {@inheritDoc}
     *
     * @param source {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @implNote The {@code read(InputStream)} method of {@code RandomAccessFileByteInput} class invokes {@link
     * RandomAccessFile#readUnsignedByte()} method on specified source and returns the result.
     */
    @Override
    protected int read(final RandomAccessFile source) throws IOException {
        return source.readUnsignedByte();
    }
}
