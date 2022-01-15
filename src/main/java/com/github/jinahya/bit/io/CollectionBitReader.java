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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.IntFunction;

/**
 * A class for reading specific type of collections of a specific element type.
 *
 * @param <T> the type of elements.
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see CollectionBitWriter
 */
public class CollectionBitReader<T extends Collection<U>, U>
        extends FilterBitReader<T, U> {

    /**
     * Creates a new instance reads lists of specified type.
     *
     * @param sizeSize      the number of bits for the {@code size} of lists.
     * @param elementReader a reader for reading elements.
     * @param <T>           element type parameter
     * @return a new instance.
     */
    public static <T> CollectionBitReader<List<T>, T> ofList(
            final int sizeSize, final BitReader<? extends T> elementReader) {
        return new CollectionBitReader<>(sizeSize, elementReader, ArrayList::new);
    }

    /**
     * Creates a new instance with specified number of bits for {@code length}.
     *
     * @param sizeSize          the number of bits for the {@code size} of collections.
     * @param elementReader     a reader for reading each element.
     * @param collectionCreator a function for creating an instance of {@link T}.
     */
    public CollectionBitReader(final int sizeSize, final BitReader<? extends U> elementReader,
                               final IntFunction<? extends T> collectionCreator) {
        super(elementReader);
        this.sizeSize = BitIoConstraints.requireValidSizeForUnsignedInt(sizeSize);
        this.collectionCreator = Objects.requireNonNull(collectionCreator, "collectionCreator is null");
    }

    @Override
    public T read(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        final int capacity = BitIoUtils.readCount(input, sizeSize);
        final T value = collectionCreator.apply(capacity);
        for (int i = 0; i < capacity; i++) {
            value.add(getReader().read(input));
        }
        return value;
    }

    private final int sizeSize;

    private final IntFunction<? extends T> collectionCreator;
}
