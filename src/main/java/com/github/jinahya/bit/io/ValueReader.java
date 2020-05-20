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
import java.util.Collection;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForInt;
import static java.util.Objects.requireNonNull;

/**
 * An interface for reading non-primitive object references.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ValueWriter
 * @see ValueAdapter
 */
public interface ValueReader<T> {

    /**
     * Reads an unsigned {@code int} value of specified bit size for a <i>length</i> of subsequent elements.
     *
     * @param input a bit input from which the value is read.
     * @param size  the number of bits to read for the value.
     * @return a value read.
     * @throws IOException if an I/O error occurs.
     */
    static int readLength(final BitInput input, final int size) throws IOException {
        requireNonNull(input, "input is null");
        requireValidSizeForInt(true, size);
        return input.readUnsignedInt(size);
    }

    /**
     * Reads a sequence of elements of specified type and add them to specified collection.
     *
     * @param input      a bit input.
     * @param size       the number of bits for a <i>length</i> of the elements.
     * @param adapter    an adapter for reading elements.
     * @param collection the collection to which elements are added.
     * @param <U>        collection type parameter
     * @param <V>        element type parameter
     * @return given collection.
     * @throws IOException if an I/O error occurs.
     */
    static <U extends Collection<? super V>, V> U readCollection(final BitInput input, final int size,
                                                                 final ValueAdapter<? extends V> adapter,
                                                                 final U collection)
            throws IOException {
        requireNonNull(adapter, "adapter is null");
        requireNonNull(collection, "collection is null");
        final int length = readLength(input, size);
        for (int i = 0; i < length; i++) {
            collection.add(adapter.read(input));
        }
        return collection;
    }

    /**
     * Returns an adapter which pre-reads a {@code boolean} flag for indicating a nullability of the value.
     *
     * @param wrapped the adapter to be wrapped.
     * @param <T>     value type parameter
     * @return an adapter wraps specified adapter.
     */
    static <T> ValueReader<T> nullable(final ValueReader<? extends T> wrapped) {
        return new NullableValueReader<>(wrapped);
    }

    /**
     * Reads a value from specified input.
     *
     * @param input the input from which the value is read.
     * @return a value.
     * @throws IOException if an I/O error occurs.
     */
    T read(BitInput input) throws IOException;
}
