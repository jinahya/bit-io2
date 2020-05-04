package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
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
import java.io.OutputStream;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * A byte input reads bytes from an instance of {@link InputStream}.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 * @see StreamByteOutput
 */
public class StreamByteInput extends ByteInputAdapter<InputStream> {

    /**
     * Creates a new instance which reads bytes directly from specified source.
     *
     * @param source the source from which bytes are read.
     * @return a new instance.
     * @see StreamByteOutput#from(OutputStream)
     */
    public static StreamByteInput from(final InputStream source) {
        requireNonNull(source, "source is null");
        return new StreamByteInput(nullSourceSupplier()) {
            @Override
            InputStream source() {
                return source;
            }
        };
    }

    /**
     * Creates a new instance with specified source supplier.
     *
     * @param sourceSupplier the source supplier.
     * @see StreamByteOutput#StreamByteOutput(Supplier)
     */
    public StreamByteInput(final Supplier<? extends InputStream> sourceSupplier) {
        super(sourceSupplier);
    }

    @Override
    protected int read(final InputStream source) throws IOException {
        final int value = source.read();
        if (value == -1) {
            throw new EOFException("reached to an end");
        }
        return value;
    }
}
