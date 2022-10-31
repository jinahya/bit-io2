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
import java.util.List;
import java.util.Objects;
import java.util.function.ObjIntConsumer;

/**
 * A writer for writing lists of specific element type.
 *
 * @param <T> element type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ListReader
 */
public class ListWriter<T>
        implements BitWriter<List<T>>,
                   WritesCount<ListWriter<T>> {

    /**
     * Creates a new instance for writing lists of specified element type using specified element writer.
     *
     * @param elementWriter the writer for reading elements.
     */
    public ListWriter(final BitWriter<? super T> elementWriter) {
        super();
        this.elementWriter = Objects.requireNonNull(elementWriter, "elementWriter is null");
    }

    @Override
    public void write(final BitOutput output, final List<T> value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        Objects.requireNonNull(value, "value is null");
        countWriter.accept(output, value.size());
        for (final T element : value) {
            elementWriter.write(output, element);
        }
    }

    @Override
    public void setCountWriter(final ObjIntConsumer<? super BitOutput> countWriter) {
        this.countWriter = Objects.requireNonNull(countWriter, "countWriter is null");
    }

    /**
     * The writer for writing elements
     */
    private final BitWriter<? super T> elementWriter;

    private ObjIntConsumer<? super BitOutput> countWriter = BitIoConstants.COUNT_WRITER_COMPRESSED;
}
