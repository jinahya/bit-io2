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
 * A class for reading arrays of a specific type.
 *
 * @param <T> the type of array elements.
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ArrayBitWriter
 */
public class ArrayBitReader<T>
        extends FilterBitReader<T[], T> {

    /**
     * Creates a new instance for reading array of specified type.
     *
     * @param lengthSize    the number of bits for the {@code length} of arrays.
     * @param elementReader a reader for reading each array element.
     * @param componentType the type of array elements.
     * @param <T>           element type parameter
     * @return a new instance.
     */
    public static <T> ArrayBitReader<T> of(final int lengthSize, final BitReader<T> elementReader,
                                           final Class<T> componentType) {
        Objects.requireNonNull(componentType, "componentType is null");
        @SuppressWarnings({"unchecked"})
        final IntFunction<T[]> arrayCreator = l -> (T[]) Array.newInstance(componentType, l);
        return new ArrayBitReader<>(lengthSize, elementReader, arrayCreator);
    }

    /**
     * Creates a new instance with specified number of bits for {@code length}.
     *
     * @param lengthSize    the number of bits for the {@code length} of arrays.
     * @param elementReader a reader for reading each array element.
     * @param arrayCreator  a function for creating {@code T[]}.
     */
    ArrayBitReader(final int lengthSize, final BitReader<? extends T> elementReader,
                   final IntFunction<? extends T[]> arrayCreator) {
        super(elementReader);
        this.lengthSize = BitIoConstraints.requireValidSizeForUnsignedInt(lengthSize);
        this.arrayCreator = Objects.requireNonNull(arrayCreator, "arrayCreator is null");
    }

    @Override
    public T[] read(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        final int length = BitIoUtils.readCount(input, lengthSize);
        final T[] value = arrayCreator.apply(length);
        for (int i = 0; i < value.length; i++) {
            value[i] = getReader().read(input);
        }
        return value;
    }

    private final int lengthSize;

    private final IntFunction<? extends T[]> arrayCreator;
}
