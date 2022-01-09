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

/**
 * A writer class for filtering other writers.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see UnaryFilterBitReader
 */
public abstract class UnaryFilterBitWriter<T>
        extends FilterBitWriter<T, T> {

    /**
     * Creates a new instance on top of specified writer.
     *
     * @param writer the writer to filter.
     */
    protected UnaryFilterBitWriter(final BitWriter<? super T> writer) {
        super(writer);
    }

    /**
     * {@inheritDoc}
     *
     * @param output {@inheritDoc}
     * @param value  {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @implNote The {@code write(BitOutput, Object)} method of {@code FilterBitWriter} class invokes {@link
     * BitWriter#write(BitOutput, Object)} method on the wrapped {@code writer} with {@code output} and {@code value}.
     */
    @Override
    public void write(final BitOutput output, final T value) throws IOException {
        writer.write(output, value);
    }
}
