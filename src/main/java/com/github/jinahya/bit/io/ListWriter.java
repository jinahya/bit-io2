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
import java.util.Iterator;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class ListWriter<T>
        extends SequenceValueWriter<List<T>> {

    public ListWriter(final int lengthSize, final ValueAdapter<? super T> elementWriter) {
        super(lengthSize);
        this.elementWriter = requireNonNull(elementWriter, "elementWriter is null");
    }

    @Override
    public void write(final BitOutput output, final List<T> value) throws IOException {
        final int length = writeLength(output, value.size());
        final Iterator<? extends T> iterator = value.iterator();
        for (int i = 0; i < length; i++) {
            elementWriter.write(output, iterator.next());
        }
    }

    private final ValueWriter<? super T> elementWriter;
}
