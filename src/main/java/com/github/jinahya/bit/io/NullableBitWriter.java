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
 * A class for writing nullable values.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class NullableBitWriter<T>
        extends FilterBitWriter<T> {

    /**
     * Creates a new instance on top of specified writer.
     *
     * @param writer the writer to filter.
     */
    NullableBitWriter(final BitWriter<? super T> writer) {
        super(writer);
    }

    /**
     * {@inheritDoc}
     *
     * @param output {@inheritDoc}
     * @param value  {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @implSpec The overridden implementation writes a {@code 1}-bit {@code int} value({@code 0b0} for {@code null},
     * {@code 0b1} for non-{@code null}) and writes {@code value} if and only if the value is not {@code null}.
     */
    @Override
    public void write(final BitOutput output, final T value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        final int flag = value == null ? 0 : 1;
        output.writeUnsignedInt(1, flag);
        if (flag == 0) {
            return;
        }
        super.write(output, value);
    }
}
