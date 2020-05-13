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

import static java.util.Objects.requireNonNull;

final class CompositeValueAdapter<T> implements ValueAdapter<T> {

    CompositeValueAdapter(final ValueReader<? extends T> reader, final ValueWriter<? super T> writer) {
        super();
        this.reader = requireNonNull(reader, "reader is null");
        this.writer = requireNonNull(writer, "writer is null");
    }

    @Override
    public T read(final BitInput input) throws IOException {
        return reader.read(input);
    }

    @Override
    public void write(final BitOutput output, final T value) throws IOException {
        writer.write(output, value);
    }

    private final ValueReader<? extends T> reader;

    private final ValueWriter<? super T> writer;
}
