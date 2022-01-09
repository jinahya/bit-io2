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
 * An interface for writing objects of a specific type.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitReader
 */
public interface BitWriter<T> {

    /**
     * Returns a writer writes a {@code 1}-bit {@code null}-flag and writes non-{@code null} values only.
     *
     * @param writer the writer for writing values.
     * @param <T>    value type parameter
     * @return a writer for nullable values.
     * @see BitReader#nullable(BitReader)
     */
    static <T> BitWriter<T> nullable(final BitWriter<? super T> writer) {
        return new FilterBitWriter<T, T>(writer) {
            @Override
            public void write(final BitOutput output, final T value) throws IOException {
                Objects.requireNonNull(output, "output is null");
                final int flag = value == null ? 0 : 1;
                output.writeUnsignedInt(1, flag);
                if (flag == 0) {
                    return;
                }
                super.writer.write(output, value);
            }
        };
    }

    /**
     * Writes specified value to specified output.
     *
     * @param output the output to which the value is written.
     * @param value  the value to write.
     * @throws IOException if an I/O error occurs.
     */
    void write(BitOutput output, T value) throws IOException;
}
