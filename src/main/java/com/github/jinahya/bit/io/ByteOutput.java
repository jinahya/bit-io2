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
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * An interface for writing bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see ByteInput
 */
@FunctionalInterface
public interface ByteOutput
        extends Flushable, Closeable {

    /**
     * Returns a new instance which writes bytes to specified file.
     *
     * @param file the file to which bytes are written.
     * @return a new instance.
     */
    static ByteOutput from(final File file) {
        return StreamByteOutput.from(file);
    }

    /**
     * Returns a new instance which writes bytes to specified random access file.
     *
     * @param file the file to which bytes are written.
     * @return a new instance.
     */
    static ByteOutput of(final RandomAccessFile file) {
        return RandomAccessFileByteOutput.of(file);
    }

    /**
     * Returns a new instance which writes bytes to specified path.
     *
     * @param path    the path to which bytes are written.
     * @param options an array of open options.
     * @return a new instance.
     */
    static ByteOutput from(final Path path, final OpenOption... options) {
        Objects.requireNonNull(path, "path is null");
        Objects.requireNonNull(options, "options is null");
        return BufferByteOutput.adapting(() -> {
            try {
                return FileChannel.open(path, options);
            } catch (final IOException ioe) {
                throw new RuntimeException("failed to open " + path, ioe);
            }
        });
    }

    /**
     * Returns a new instance which writes bytes to specified path.
     *
     * @param path    the path to which bytes are written.
     * @return a new instance.
     */
    static ByteOutput from(final Path path) {
        return from(path, StandardOpenOption.WRITE);
    }

    /**
     * Flushes this output by writing any buffered output to the underlying output.
     *
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation does nothing.
     */
    @Override
    default void flush() throws IOException {
        // does nothing.
    }

    /**
     * Closes this output and releases any system resources associated with it.
     *
     * @throws IOException if an I/O error occurs.
     * @implNote The default implementation does nothing.
     */
    @Override
    default void close() throws IOException {
        // does nothing.
    }

    /**
     * Writes specified unsigned {@value java.lang.Byte#SIZE}-bit value.
     *
     * @param value the unsigned {@value java.lang.Byte#SIZE}-bit value to write; between {@code 0} and {@code 255},
     *              both inclusive.
     * @throws IOException if an I/O error occurs.
     */
    void write(int value) throws IOException;
}
