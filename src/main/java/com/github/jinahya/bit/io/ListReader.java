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
import java.util.List;
import java.util.Objects;

/**
 * A reader for reading lists of specific element type.
 *
 * @param <T> element type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ListWriter
 */
public class ListReader<T>
        implements BitReader<List<T>> {

    /**
     * Creates a new instance for reading lists of specified element type.
     *
     * @param elementReader the reader for reading each element.
     */
    public ListReader(final BitReader<? extends T> elementReader) {
        super();
        this.elementReader = Objects.requireNonNull(elementReader, "elementReader is null");
    }

    @Override
    public List<T> read(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        final int count = BitIoUtils.readCountCompressed(input);
        final List<T> value = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            value.add(elementReader.read(input));
        }
        return value;
    }

    /**
     * The reader for reading each element.
     */
    private final BitReader<? extends T> elementReader;
}
