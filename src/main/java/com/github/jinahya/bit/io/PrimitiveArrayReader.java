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
 * An abstract reader class for reading an array of primitive values.
 *
 * @param <T> array type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see PrimitiveArrayWriter
 */
abstract class PrimitiveArrayReader<T>
        implements BitReader<T> {

    /**
     * Creates a new instance with specified number of bits for {@code length}.
     *
     * @param lengthSize the number of bits for the {@code length}.
     */
    protected PrimitiveArrayReader(final int lengthSize) {
        super();
        this.lengthSize = BitIoConstraints.requireValidSizeForUnsignedInt(lengthSize);
    }

    /**
     * Reads {@code length}.
     *
     * @param input a bit-input from which the length is read.
     * @return a value of {@code length}.
     * @throws IOException if an I/O error occurs.
     */
    protected int readLength(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        return BitIoUtils.readCount(input, lengthSize);
    }

    private final int lengthSize;
}
