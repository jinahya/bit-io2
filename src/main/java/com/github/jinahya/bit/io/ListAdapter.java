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

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForInt;
import static com.github.jinahya.bit.io.ValueReader.readCollection;
import static com.github.jinahya.bit.io.ValueWriter.writeCollection;
import static java.util.Objects.requireNonNull;

/**
 * A value adapter for reading/writing a list.
 *
 * @param <T> element type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class ListAdapter<T> implements ValueAdapter<List<T>> {

    /**
     * Creates a new instance with specified arguments.
     *
     * @param lengthSize     the number of bits for reading/writing the number of elements.
     * @param elementAdapter a value adapter for reading/writing list elements.
     */
    public ListAdapter(final int lengthSize, final ValueAdapter<T> elementAdapter) {
        super();
        this.lengthSize = requireValidSizeForInt(true, lengthSize);
        this.elementAdapter = requireNonNull(elementAdapter, "elementAdapter is null");
    }

    @Override
    public List<T> read(final BitInput input) throws IOException {
        return readCollection(input, lengthSize, elementAdapter, new ArrayList<>());
    }

    @Override
    public void write(final BitOutput output, final List<T> value) throws IOException {
        writeCollection(output, lengthSize, elementAdapter, value);
    }

    private final int lengthSize;

    private final ValueAdapter<T> elementAdapter;
}
