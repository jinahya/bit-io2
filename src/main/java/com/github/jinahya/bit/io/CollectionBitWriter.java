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
import java.util.List;
import java.util.Objects;

/**
 * A class for writing specific type of collections of a specific element type.
 *
 * @param <T> collection type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see CollectionBitReader
 */
public class CollectionBitWriter<T extends Collection<U>, U>
        extends FilterBitWriter<T, U> {

    /**
     * Creates a new instance writes lists of specified type.
     *
     * @param sizeSize      the number of bits for the {@code size} of lists.
     * @param elementWriter a writer for writing elements.
     * @param <T>           element type parameter
     * @return a new instance.
     */
    public static <T> CollectionBitWriter<List<T>, T> ofList(final int sizeSize, final BitWriter<T> elementWriter) {
        return new CollectionBitWriter<>(sizeSize, elementWriter);
    }

    /**
     * Creates a new instance with specified number of bits for {@code length}.
     *
     * @param sizeSize the number of bits for the {@code length}.
     */
    public CollectionBitWriter(final int sizeSize, final BitWriter<U> elementWriter) {
        super(elementWriter);
        this.sizeSize = BitIoConstraints.requireValidSizeForUnsignedInt(sizeSize);
    }

    @Override
    public void write(final BitOutput output, final T value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        Objects.requireNonNull(value, "value is null");
        final int count = BitIoUtils.writeCount(output, sizeSize, value.size());
        final Iterator<U> iterator = value.iterator();
        for (int i = 0; i < count; i++) {
            writer.write(output, iterator.next());
        }
    }

    private final int sizeSize;
}
