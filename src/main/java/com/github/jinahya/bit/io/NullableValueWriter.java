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
 * A wrapper class for writing a null flag before writing values.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class NullableValueWriter<T> implements ValueWriter<T> {

    /**
     * Writes specified nullable value.
     *
     * @param output a bit output.
     * @param writer a value writer.
     * @param value  the value to write.
     * @param <V>    value type parameter
     * @throws IOException if an I/O error occurs.
     */
    static <V> void writeNullable(final BitOutput output, final ValueWriter<? super V> writer, final V value)
            throws IOException {
        requireNonNull(output, "output is null");
        requireNonNull(writer, "writer is null");
        final boolean nonnull = value != null;
        output.writeBoolean(nonnull);
        if (nonnull) {
            writer.write(output, value);
        }
    }

    /**
     * Creates a new instance with specified adapter.
     *
     * @param wrapped the adapter to be wrapped.
     */
    NullableValueWriter(final ValueWriter<? super T> wrapped) {
        super();
        this.wrapped = requireNonNull(wrapped, "wrapped is null");
    }

    @Override
    public void write(final BitOutput output, final T value) throws IOException {
        writeNullable(output, wrapped, value);
    }

    private final ValueWriter<? super T> wrapped;
}
