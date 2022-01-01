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

import static java.util.Objects.requireNonNull;

/**
 * A reader class wraps another reader.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see NullableValueWriter
 */
public abstract class FilterValueReader<T>
        implements ValueReader<T> {

    /**
     * Creates a new instance with specified reader.
     *
     * @param reader the reader to filter.
     */
    protected FilterValueReader(final ValueReader<? extends T> reader) {
        super();
        this.reader = requireNonNull(reader, "reader is null");
    }

    /**
     * {@inheritDoc} The {@code read(BitInput)} method of {@code FilterValueReader} class invokes {@link
     * #read(BitInput)} method on {@link #reader}.
     *
     * @param input {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public T read(final BitInput input) throws IOException {
        return reader.read(input);
    }

    /**
     * The reader wrapped by this reader.
     */
    protected final ValueReader<? extends T> reader;
}
