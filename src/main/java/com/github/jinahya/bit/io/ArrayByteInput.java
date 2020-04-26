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

import java.io.IOException;
import java.util.function.Supplier;

/**
 * A byte input reads bytes from an array of bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see ArrayByteOutput
 */
public class ArrayByteInput extends ByteInputAdapter<byte[]> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with specified source supplier.
     *
     * @param sourceSupplier the source supplier.
     */
    public ArrayByteInput(final Supplier<byte[]> sourceSupplier) {
        super(sourceSupplier);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code read(byte[])} method of {@code ArrayByteInput} class returns the byte on {@code source}
     * at {@link #index} as an unsigned {@code int} and increments the value of {@link #index} by {@code 1}.
     *
     * @param source the array of byte on which the byte is read.
     * @return {@inheritDoc}
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public int read(final byte[] source) throws IOException {
        return source[index++] & 0xFF;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The next index in the {@code source} on which the byte is read.
     */
    protected int index;
}
