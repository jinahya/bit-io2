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
import java.util.Objects;

/**
 * A writer for writing iterables of specific element type.
 *
 * @param <E> element type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see IterableReader
 */
class IterableWriter<E>
        implements BitWriter<Iterable<? extends E>> {

    /**
     * Creates a new instance for writing lists of specified element type using specified element writer.
     *
     * @param elementWriter the writer for reading elements.
     */
    public IterableWriter(final BitWriter<? super E> elementWriter) {
        super();
        this.elementWriter = Objects.requireNonNull(elementWriter, "elementWriter is null");
    }

    @Override
    public void write(final BitOutput output, final Iterable<? extends E> value) throws IOException {
        BitIoObjects.requireNonNullOutput(output);
        BitIoObjects.requireNonNullValue(value);
        for (final E e : value) {
            output.writeInt(true, 1, 1);
            elementWriter.write(output, e);
        }
        output.writeInt(true, 1, 0);
    }

    private final BitWriter<? super E> elementWriter;
}
