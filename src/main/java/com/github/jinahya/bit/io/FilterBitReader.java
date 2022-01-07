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
import java.util.Objects;

/**
 * A reader class for filtering other readers.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see FilterBitWriter
 */
public abstract class FilterBitReader<T>
        implements BitReader<T> {

    /**
     * Creates a new instance with specified reader.
     *
     * @param reader the reader to filter.
     */
    protected FilterBitReader(final BitReader<? extends T> reader) {
        super();
        this.reader = Objects.requireNonNull(reader, "reader is null");
    }

    /**
     * {@inheritDoc}
     *
     * @param input {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @implNote The {@code read(BitInput)} method of {@code FilterBitReader} class invokes {@link #read(BitInput)}
     * method on {@link #reader}.
     */
    @Override
    public T read(final BitInput input) throws IOException {
        return reader.read(input);
    }

    /**
     * The reader wrapped by this reader.
     */
    protected final BitReader<? extends T> reader;
}
