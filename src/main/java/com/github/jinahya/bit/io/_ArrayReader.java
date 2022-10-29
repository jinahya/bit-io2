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
import java.lang.reflect.Array;
import java.util.Objects;
import java.util.function.IntFunction;

/**
 * A reader for reading arrays of bytes.
 *
 * @param <T> element type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see _ArrayWriter
 */
class _ArrayReader<T>
        implements BitReader<T[]> {

    /**
     * Creates a new instance for reading array of specified element type using specified element reader.
     *
     * @param elementReader the reader for reading each element.
     * @param arrayCreator  a function for creating an array of {@code T}.
     */
    public _ArrayReader(final BitReader<? extends T> elementReader, final IntFunction<? extends T[]> arrayCreator) {
        super();
        this.elementReader = Objects.requireNonNull(elementReader, "elementReader is null");
        this.arrayCreator = Objects.requireNonNull(arrayCreator, "arrayCreator is null");
    }

    /**
     * Creates a new instance for reading array of specified element type using specified element reader.
     *
     * @param elementReader the reader for reading each element.
     * @param elementClass  the type of elements in arrays.
     */
    @SuppressWarnings({"unchecked"})
    public _ArrayReader(final BitReader<? extends T> elementReader, final Class<T> elementClass) {
        this(elementReader, l -> (T[]) Array.newInstance(elementClass, l));
    }

    @Override
    public T[] read(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        final int length = BitIoUtils.readCountCompressed(input);
        final T[] value = arrayCreator.apply(length);
        for (int i = 0; i < value.length; i++) {
            value[i] = elementReader.read(input);
        }
        return value;
    }

    /**
     * The reader for reading array elements.
     */
    private final BitReader<? extends T> elementReader;

    /**
     * A function for creating an array of given length.
     */
    private final IntFunction<? extends T[]> arrayCreator;
}
