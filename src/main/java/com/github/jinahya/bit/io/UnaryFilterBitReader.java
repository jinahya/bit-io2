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

/**
 * A reader class for filtering other readers.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see UnaryFilterBitWriter
 */
public abstract class UnaryFilterBitReader<T>
        extends FilterBitReader<T, T> {

    /**
     * Creates a new instance with specified reader.
     *
     * @param reader the reader to filter.
     */
    protected UnaryFilterBitReader(final BitReader<? extends T> reader) {
        super(reader);
    }

    /**
     * {@inheritDoc}
     *
     * @param input {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @implNote The {@code read(BitInput)} method of {@code FilterBitReader} class invokes {@link #read(BitInput)}
     * method on {@link #reader} and returns the result.
     */
    @Override
    public T read(final BitInput input) throws IOException {
        return reader.read(input);
    }
}
