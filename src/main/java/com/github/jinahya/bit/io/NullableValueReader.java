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
 * A wrapper class for reading a null flag before reading values.
 *
 * @param <T> value type parameter.
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see NullableValueWriter
 */
final class NullableValueReader<T> implements ValueReader<T> {

    /**
     * Reads a nullable value.
     *
     * @param input  a bit input.
     * @param reader a value reader.
     * @param <V>    value type parameter
     * @return a value read; maybe {@code null}.
     * @throws IOException if an I/O error occurs.
     * @see NullableValueWriter#writeNullable(BitOutput, ValueWriter, Object)
     */
    static <V> V readNullable(final BitInput input, final ValueReader<? extends V> reader) throws IOException {
        requireNonNull(input, "input is null");
        requireNonNull(reader, "reader is null");
        final boolean nonnull = input.readBoolean();
        if (nonnull) {
            return reader.read(input);
        }
        return null;
    }

    /**
     * Creates a new instance with specified adapter.
     *
     * @param wrapped the adapter to be wrapped.
     */
    NullableValueReader(final ValueReader<? extends T> wrapped) {
        super();
        this.wrapped = requireNonNull(wrapped, "wrapped is null");
    }

    @Override
    public T read(final BitInput input) throws IOException {
        return readNullable(input, wrapped);
    }

    private final ValueReader<? extends T> wrapped;
}
