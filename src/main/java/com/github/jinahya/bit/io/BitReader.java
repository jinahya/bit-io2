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
 * An interface for reading objects of a specific type.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitWriter
 */
public interface BitReader<T> {

    /**
     * Returns a reader which reads a {@code 1}-bit {@code null}-flag and reads non-{@code null} values only.
     *
     * @param reader a reader for reading values.
     * @param <T>    value type parameter
     * @return a reader for nullable values.
     * @see BitWriter#nullable(BitWriter)
     */
    static <T> BitReader<T> nullable(final BitReader<? extends T> reader) {
        return new FilterBitReader<T>(reader) {
            @Override
            public T read(final BitInput input) throws IOException {
                Objects.requireNonNull(input, "input is null");
                final int flag = input.readUnsignedInt(1);
                if (flag == 0) {
                    return null;
                }
                return super.read(input);
            }
        };
    }

    /**
     * Reads a value from specified input.
     *
     * @param input the input from which the value is read.
     * @return a value read.
     * @throws IOException if an I/O error occurs.
     */
    T read(BitInput input) throws IOException;
}