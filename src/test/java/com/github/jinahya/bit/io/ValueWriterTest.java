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

import java.util.Objects;

/**
 * An abstract class for testing subclasses {@link ValueWriter} class.
 *
 * @param <T> writer type parameter
 * @param <U> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
abstract class ValueWriterTest<T extends ValueWriter<U>, U> {

    protected ValueWriterTest(final Class<T> writerClass, final Class<U> valueClass) {
        super();
        this.writerClass = Objects.requireNonNull(writerClass, "writerClass is null");
        this.valueClass = Objects.requireNonNull(valueClass, "valueClass is null");
    }

    protected final Class<T> writerClass;

    protected final Class<U> valueClass;
}
