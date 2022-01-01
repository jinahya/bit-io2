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

public final class ValueWriters<T> {

    /**
     * Returns a wrapper which pre-writes a {@code boolean} flag indicating whether the value is {@code null}.
     *
     * @param wrapped a writer to wrap.
     * @param <T>     value type parameter
     * @return a wrapper writer.
     */
    public static <T> ValueWriter<T> nullable(final ValueWriter<? super T> wrapped) {
        return new NullableValueWriter<>(wrapped);
    }

    private ValueWriters() {
        throw new AssertionError("instantiation is not allowed");
    }
}
