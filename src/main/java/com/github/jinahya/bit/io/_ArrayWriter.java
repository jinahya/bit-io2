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
 * A writer for reading arrays of bytes.
 *
 * @param <T> element type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ArrayReader
 */
class ArrayWriter<T>
        implements BitWriter<T[]> {

    /**
     * Creates a new instance for reading array of specified element type using specified element writer.
     *
     * @param elementWriter the writer for reading each element.
     */
    public ArrayWriter(final BitWriter<? super T> elementWriter) {
        super();
        this.elementWriter = Objects.requireNonNull(elementWriter, "elementWriter is null");
    }

    @Override
    public void write(final BitOutput output, final T[] value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        Objects.requireNonNull(value, "value is null");
        BitIoUtils.writeCountCompressed(output, value.length);
        for (final T element : value) {
            elementWriter.write(output, element);
        }
    }

    /**
     * The writer for writing array elements
     */
    private final BitWriter<? super T> elementWriter;
}
