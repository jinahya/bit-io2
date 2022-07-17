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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A byte output writes bytes to an instance of {@link OutputStream}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see StreamByteInput
 */
public class StreamByteOutput
        extends ByteOutputAdapter<OutputStream> {

    /**
     * Creates a new instance which writes bytes to output stream.
     *
     * @param target the output stream to which bytes are written.
     * @return a new instance.
     * @apiNote Closing the result does not close the {@code target}.
     */
    public static StreamByteOutput from(final OutputStream target) {
        Objects.requireNonNull(target, "target is null");
        final StreamByteOutput instance = new StreamByteOutput(BitIoUtils.emptySupplier());
        instance.target(target);
        return instance;
    }

    /**
     * Returns a new instance which writes bytes to specified file.
     *
     * @param file the file to which bytes are written.
     * @return a new instance.
     */
    static StreamByteOutput from(final File file) {
        Objects.requireNonNull(file, "file is null");
        return new StreamByteOutput(() -> {
            try {
                return new FileOutputStream(file);
            } catch (final IOException ioe) {
                throw new RuntimeException("failed to open " + file, ioe);
            }
        });
    }

    /**
     * Creates a new instance with specified target supplier.
     *
     * @param targetSupplier the target supplier.
     */
    public StreamByteOutput(final Supplier<? extends OutputStream> targetSupplier) {
        super(targetSupplier);
    }

    /**
     * {@inheritDoc} The {@code write(OutputStream, int)} method of {@code StreamByteOutput} class invokes
     * {@link OutputStream#write(int)} method on specified output stream with specified value.
     *
     * @param target {@inheritDoc}
     * @param value  {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    protected void write(final OutputStream target, final int value) throws IOException {
        target.write(value);
    }
}
