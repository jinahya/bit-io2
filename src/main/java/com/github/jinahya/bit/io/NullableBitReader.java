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
 * A wrapper class for reading a null flag before reading values.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see NullableBitWriter
 */
final class NullableBitReader<T>
        extends FilterBitReader<T> {

    /**
     * Creates a new instance on top of specified reader.
     *
     * @param reader the reader to wrap.
     */
    NullableBitReader(final BitReader<? extends T> reader) {
        super(reader);
    }

    /**
     * Reads a value from specified input. The {@code read(BitInput)} method of {@code NullableValueReader} class reads
     * a {@code 1}-bit {@code int} value and reads a value if and only if the value is {@code 1}.
     *
     * @param input {@inheritDoc}
     * @return the value read; maybe {@code null} if the flag is not {@code 0}.
     * @throws IOException {@inheritDoc}
     */
    @Override
    public T read(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        final int flag = input.readUnsignedInt(1);
        if (flag == 0) {
            return null;
        }
        return super.read(input);
    }
}
