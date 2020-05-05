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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Supplier;

/**
 * A byte output writes bytes to an instance of {@link OutputStream}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see StreamByteInput
 */
public class StreamByteOutput extends ByteOutputAdapter<OutputStream> {

    /**
     * Creates a new instance with specified target supplier.
     *
     * @param targetSupplier the target supplier.
     * @see StreamByteInput#StreamByteInput(Supplier)
     */
    public StreamByteOutput(final Supplier<? extends OutputStream> targetSupplier) {
        super(targetSupplier);
    }

    /**
     * {@inheritDoc} The {@code write(OutputStream, int)} method of {@code StreamByteOutput} class invokes {@link
     * OutputStream#write(int)} method on specified {@code target} with specified {@code value}.
     *
     * @param target {@inheritDoc}
     * @param value  {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @see StreamByteInput#read(InputStream)
     */
    @Override
    protected void write(final OutputStream target, final int value) throws IOException {
        target.write(value);
    }
}
