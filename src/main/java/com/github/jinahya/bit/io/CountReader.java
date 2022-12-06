package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 - 2022 Jinahya, Inc.
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

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.ToIntFunction;

/**
 * An interface for readers need to read a number of sub-values.
 *
 * @param <T> self type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see CountWriter
 */
public interface CountReader<T extends CountReader<T>> {

    /**
     * Configures to use specified function for reading the {@code count} of elements.
     *
     * @param countReader the function applies with an {@code input} and reads the {@code count}.
     */
    @SuppressWarnings({
            "java:S1874", // isAccessible
            "java:S3011" // setAccessible, set
    })
    default void setCountReader(final ToIntFunction<? super BitInput> countReader) {
        Objects.requireNonNull(countReader, "countReader is null");
        try {
            final Field field = getClass().getDeclaredField("countReader");
            if (!field.isAccessible()) { // NOSONAR
                field.setAccessible(true); // NOSONAR
            }
            field.set(this, countReader); // NOSONAR
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException("failed to set 'countReader", roe); // NOSONAR
        }
    }

    /**
     * Configures to use specified function for reading the {@code count} of elements, and returns this object.
     *
     * @param countReader the function applies with an {@code input} and reads the {@code count}.
     * @return this object.
     */
    @SuppressWarnings({"unchecked"})
    default T countReader(final ToIntFunction<? super BitInput> countReader) {
        setCountReader(countReader);
        return (T) this;
    }
}
