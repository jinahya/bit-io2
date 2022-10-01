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
 * An abstract class for testing subclasses of {@link AbstractByteOutput} class.
 *
 * @param <T> byte output adapter type parameter
 * @param <U> byte target type parameter
 * @see ByteInputAdapterTest
 */
abstract class ByteOutputAdapterTest<T extends AbstractByteOutput<U>, U> {

    /**
     * Creates a new instance with specified classes.
     *
     * @param adapterClass a class of {@link T} to test.
     * @param targetClass  a class of {@link U} on which {@code adapterClass} is based on.
     * @see #adapterClass
     * @see #targetClass
     * @see ByteInputAdapterTest#ByteInputAdapterTest(Class, Class)
     */
    ByteOutputAdapterTest(final Class<T> adapterClass, final Class<U> targetClass) {
        super();
        this.adapterClass = requireNonNull(adapterClass, "adapterClass is null");
        this.targetClass = requireNonNull(targetClass, "targetClass is null");
    }

    final Class<T> adapterClass;

    final Class<U> targetClass;
}
