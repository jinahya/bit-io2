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
import java.util.Iterator;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForInt;
import static java.util.Objects.requireNonNull;

/**
 * An interface for writing non-primitive object references.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ValueReader
 * @see ValueAdapter
 */
public interface ValueWriter<T> {

    /**
     * Writes specified unsigned {@code int} value of specified bit-size for a <i>length</i> of subsequent elements.
     *
     * @param output a bit-output to which the value is written.
     * @param size   the number of bits to write.
     * @param value  the value whose lower {@code size} bits are written.
     * @return an actual written value of the lower specified bits of specified value.
     * @throws IOException if an I/O error occurs.
     */
    static int writeLength(final BitOutput output, final int size, final int value) throws IOException {
        requireNonNull(output, "output is null");
        requireValidSizeForInt(true, size);
        final int length = value & ((1 << size) - 1);
        output.writeUnsignedInt(size, length);
        return length;
    }

    /**
     * Writes elements in specified collection using specified adapter.
     *
     * @param output     a bit output.
     * @param size       the number of bits for the {@code size} of the collection.
     * @param adapter    the value adapter for writing elements.
     * @param collection the collection whose elements are written.
     * @param <U>        element type parameter
     * @throws IOException if an I/O error occurs.
     */
    static <U> void writeCollection(final BitOutput output, final int size, final ValueAdapter<? super U> adapter,
                                    final Collection<? extends U> collection)
            throws IOException {
        requireNonNull(adapter, "adapter is null");
        requireNonNull(collection, "collection is null");
        final int length = writeLength(output, size, collection.size());
        final Iterator<? extends U> iterator = collection.iterator();
        for (int i = 0; i < length; i++) {
            adapter.write(output, iterator.next());
        }
    }

    /**
     * Returns an adapter which pre-writes a {@code boolean} flag for indicating a nullability of the value.
     *
     * @param wrapped the adapter to be wrapped.
     * @param <T>     value type parameter
     * @return an adapter wraps specified adapter.
     */
    static <T> ValueWriter<T> nullable(final ValueWriter<? super T> wrapped) {
        return new NullableValueWriter<>(wrapped);
    }

    /**
     * Writes specified value to specified output.
     *
     * @param output the output to which the value is written.
     * @param value  the value to write.
     * @throws IOException if an I/O error occurs.
     */
    void write(BitOutput output, T value) throws IOException;
}
