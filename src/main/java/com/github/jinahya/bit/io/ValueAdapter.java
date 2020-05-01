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
 * An interface for readable/writable unit of bit data.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public interface ValueAdapter<T> {

    /**
     * Returns a value adapter which pre-read/write a signal of nullability of the value.
     *
     * @param wrapped the unit to be wrapped.
     * @param <T>     value type parameter
     * @return a bit unit of nullable values.
     */
    static <T> ValueAdapter<T> nullable(final ValueAdapter<T> wrapped) {
        return new NullableAdapter<>(wrapped);
    }

    /**
     * Writes specified value to specified output.
     *
     * @param output the output to which the value is written.
     * @param value  the value to write.
     * @throws IOException if an I/O error occurs.
     */
    void write(BitOutput output, T value) throws IOException;

    /**
     * Reads a value from specified input.
     *
     * @param input the input from which the value is read.
     * @return a value.
     * @throws IOException if an I/O error occurs.
     */
    T read(BitInput input) throws IOException;
}
