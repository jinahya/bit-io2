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

public class ListReader<T>
        extends SequenceValueReader<List<T>> {

    public ListReader(final int lengthSize, final ValueReader<? extends T> elementAdapter) {
        super(lengthSize);
        this.elementAdapter = Objects.requireNonNull(elementAdapter, "elementAdapter is null");
    }

    @Override
    public List<T> read(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        final int length = readLength(input);
        final List<T> value = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            value.add(elementAdapter.read(input));
        }
        return value;
    }

    private final ValueReader<? extends T> elementAdapter;
}
