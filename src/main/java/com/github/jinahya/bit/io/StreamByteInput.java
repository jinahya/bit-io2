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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A byte input reads bytes from an instance of {@link InputStream}.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 * @see StreamByteOutput
 */
public class StreamByteInput
        extends ByteInputAdapter<InputStream> {

    /**
     * Creates a new instance which read byte from specified Input stream.
     *
     * @param source the Input stream to which bytes are written.
     * @return a new instance.
     * @apiNote Closing the result does not close the {@code source}.
     * @see StreamByteOutput#from(OutputStream)
     */
    public static StreamByteInput from(final InputStream source) {
        Objects.requireNonNull(source, "source is null");
        final StreamByteInput instance = new StreamByteInput(BitIoUtils.empty());
        instance.source(source);
        return instance;
    }

    /**
     * Returns a new instance which reads bytes from specified file.
     *
     * @param file the file from which bytes are read.
     * @return a new instance.
     */
    static StreamByteInput from(final File file) {
        Objects.requireNonNull(file, "file is null");
        return new StreamByteInput(() -> {
            try {
                return new FileInputStream(file);
            } catch (final IOException ioe) {
                throw new RuntimeException("failed to open " + file, ioe);
            }
        });
    }

    /**
     * Creates a new instance with specified source supplier.
     *
     * @param sourceSupplier the source supplier.
     */
    public StreamByteInput(final Supplier<? extends InputStream> sourceSupplier) {
        super(sourceSupplier);
    }

    /**
     * {@inheritDoc} The {@code read(InputStream)} method of {@code StreamByteInput} class invokes {@link
     * InputStream#read()} method on specified input stream and returns the result.
     *
     * @param source {@inheritDoc}
     * @return {@inheritDoc}
     * @throws EOFException if the {@link InputStream#read()} operation returns {@code -1}.
     * @throws IOException  {@inheritDoc}
     */
    @Override
    protected int read(final InputStream source) throws IOException {
        final int value = source.read();
        if (value == -1) {
            throw new EOFException("reached to an end");
        }
        return value;
    }
}
