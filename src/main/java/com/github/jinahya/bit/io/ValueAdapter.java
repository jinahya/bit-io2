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

import static java.util.Objects.requireNonNull;

/**
 * An interface for reading/writing non-primitive object references.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public interface ValueAdapter<T> extends ValueReader<T>, ValueWriter<T> {

    /**
     * Creates a new adapter which pre-reads/writes a {@code boolean} value indicating the nullability of the value.
     *
     * @param wrapped the adapter to be wrapped.
     * @param <T>     value type parameter
     * @return an adapter wraps specified adapter.
     */
    static <T> ValueAdapter<T> nullable(final ValueAdapter<T> wrapped) {
        return new NullableValueAdapter<>(requireNonNull(wrapped, "wrapped is null"));
    }

    /**
     * Creates new adapter composing specified arguments.
     *
     * @param reader an adapter for reading values.
     * @param writer an adapter for writing values.
     * @param <T>    value type parameter
     * @return a new instance.
     */
    static <T> ValueAdapter<T> compose(final ValueReader<? extends T> reader, final ValueWriter<? super T> writer) {
        return new CompositeValueAdapter<>(reader, writer);
    }
}
