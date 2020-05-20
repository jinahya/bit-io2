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
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see NullableValueWriter
 */
final class NullableValueReader<T> implements ValueReader<T> {

    /**
     * Creates a new instance with specified adapter.
     *
     * @param wrapped the adapter to be wrapped.
     */
    NullableValueReader(final ValueReader<? extends T> wrapped) {
        super();
        this.wrapped = requireNonNull(wrapped, "wrapped is null");
    }

    /**
     * {@inheritDoc} The {@code read(BitInput)} method of {@code NullableValueReader} class reads a {@code 1}-bit {@code
     * boolean} flag and reads a value if and only if the flag is {@code true}.
     *
     * @param input {@inheritDoc}
     * @return the value read; maybe {@code null} if the flag is not {@code true}.
     * @throws IOException {@inheritDoc}
     */
    @Override
    public T read(final BitInput input) throws IOException {
        requireNonNull(input, "input is null");
        final boolean nonnull = input.readBoolean();
        if (nonnull) {
            return wrapped.read(input);
        }
        return null;
    }

    private final ValueReader<? extends T> wrapped;
}
