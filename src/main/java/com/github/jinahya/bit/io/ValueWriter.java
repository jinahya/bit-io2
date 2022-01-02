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
 * An interface for writing non-primitive object references.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ValueReader
 */
public interface ValueWriter<T> {

    /**
     * Writes specified unsigned {@code int} value of specified bit-size for a <i>length</i> of subsequent elements.
     *
     * @param output a bit-output to which the value is written.
     * @param size   the number of bits to write.
     * @param value  the value whose lower {@code size} bits are written.
     * @return an actual written value of the lower specified bits of specified value.
     * @throws IOException if an I/O error occurs.
     */
    static int writeLength(final BitOutput output, final int size, final int value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        BitIoConstraints.requireValidSizeForInt(true, size);
        if (value < 0) {
            throw new IllegalArgumentException("length(" + value + " is negative");
        }
        final int length = value & ((1 << size) - 1);
        output.writeUnsignedInt(size, length);
        return length;
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
