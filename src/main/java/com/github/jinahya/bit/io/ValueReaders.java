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

public final class ValueReaders {

    /**
     * Returns a wrapper reader which pre-reads a {@code boolean} flag indicating a {@code null} value.
     *
     * @param reader a reader to wrap.
     * @param <T>    value type parameter
     * @return a wrapper reader.
     */
    public static <T> ValueReader<T> nullable(final ValueReader<? extends T> reader) {
        return new NullableValueReader<>(reader);
    }

    private ValueReaders() {
        throw new AssertionError("instantiation is not allowed");
    }
}
