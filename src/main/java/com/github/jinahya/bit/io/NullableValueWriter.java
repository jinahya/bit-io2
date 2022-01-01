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

/**
 * A writer class for writing a {@code null} flag before writing a value.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class NullableValueWriter<T>
        extends FilterValueWriter<T> {

    /**
     * Creates a new instance on top of specified writer.
     *
     * @param writer the writer to wrap.
     */
    NullableValueWriter(final ValueWriter<? super T> writer) {
        super(writer);
    }

    /**
     * Writes specified value to specified output. The {@code write(BitOutput, Object)} method of {@code
     * NullableValueWriter} class writes a {@code 1}-bit {@code int} flag indicates a nullability of given value and
     * writes the value if and only if the value is not {@code null}.
     *
     * @param output {@inheritDoc}
     * @param value  {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void write(final BitOutput output, final T value) throws IOException {
        requireNonNull(output, "output is null");
        final int flag = value == null ? 0 : 1;
        output.writeUnsignedInt(1, flag);
        if (flag == 0) {
            return;
        }
        super.write(output, value);
    }
}
