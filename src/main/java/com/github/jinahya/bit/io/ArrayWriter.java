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
 * An abstract writer class for reading an array of primitive values.
 *
 * @param <T> array type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ArrayReader
 */
abstract class ArrayWriter<T>
        implements BitWriter<T> {

    /**
     * Creates a new instance with specified number of bits for {@code length}.
     *
     * @param lengthSize the number of bits for the {@code length}.
     */
    protected ArrayWriter(final int lengthSize) {
        super();
        this.lengthSize = BitIoConstraints.requireValidSizeForUnsignedInt(lengthSize);
    }

    /**
     * Writes specified {@code length} value.
     *
     * @param output a bit-output to which the length is written.
     * @param length the value of {@code length} to write.
     * @throws IOException if an I/O error occurs.
     */
    protected void writeLength(final BitOutput output, final int length) throws IOException {
        Objects.requireNonNull(output, "output is null");
        BitIoUtils.writeCount(output, lengthSize, length);
    }

    private final int lengthSize;
}
