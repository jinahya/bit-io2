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

import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * An interface for reading bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see ByteOutput
 */
@FunctionalInterface
public interface ByteInput
        extends Closeable {

    /**
     * Returns a new instance which reads bytes from specified file.
     *
     * @param file the file from which bytes are read.
     * @return a new instance.
     */
    static ByteInput from(final File file) {
        return StreamByteInput.from(file);
    }

    /**
     * Returns a new instance which reads bytes from specified path.
     *
     * @param path    the path from which bytes are read.
     * @param options an array of open options.
     * @return a new instance.
     */
    static ByteInput from(final Path path, final OpenOption... options) {
        Objects.requireNonNull(path, "path is null");
        Objects.requireNonNull(options, "options is null");
        return BufferByteInput.adapting(() -> {
            try {
                return FileChannel.open(path, options);
            } catch (final IOException ioe) {
                throw new RuntimeException("failed to open " + path, ioe);
            }
        });
    }

    /**
     * Returns a new instance which reads bytes from specified path.
     *
     * @param path the path from which bytes are read.
     * @return a new instance.
     */
    static ByteInput from(final Path path) {
        return from(path, StandardOpenOption.READ);
    }

    /**
     * Returns a new instance which reads bytes from specified random access file.
     *
     * @param file the file from which bytes are read.
     * @return a new instance.
     */
    static ByteInput of(final RandomAccessFile file) {
        return RandomAccessFileByteInput.of(file);
    }

    /**
     * Closes this input and releases any system resources associated with it.
     *
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation does nothing.
     */
    @Override
    default void close() throws IOException {
        // does nothing.
    }

    /**
     * Reads an unsigned {@value java.lang.Byte#SIZE}-bit value.
     *
     * @return an unsigned {@value java.lang.Byte#SIZE}-bit value; between {@code 0} and {@code 255}, both inclusive.
     * @throws EOFException if reached to an end.
     * @throws IOException  if an I/O error occurs.
     */
    int read() throws IOException;
}
