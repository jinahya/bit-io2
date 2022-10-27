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
 * An abstract class for testing subclasses of {@link AbstractByteInput} class.
 *
 * @param <T> subclass type parameter
 * @param <U> source type parameter
 */
abstract class ByteInputAdapterTest<T extends AbstractByteInput<U>, U> {

    /**
     * Creates a new instance with specified arguments.
     *
     * @param adapterClass a class of {@link T} to test.
     * @param sourceClass  a class of {@link U} on which {@code adapterClass} is based.
     * @see #adapterClass
     * @see #sourceClass
     */
    ByteInputAdapterTest(final Class<T> adapterClass, final Class<U> sourceClass) {
        super();
        this.adapterClass = requireNonNull(adapterClass, "adapterClass is null");
        this.sourceClass = requireNonNull(sourceClass, "sourceClass is null");
    }

    final Class<T> adapterClass;

    final Class<U> sourceClass;
}
