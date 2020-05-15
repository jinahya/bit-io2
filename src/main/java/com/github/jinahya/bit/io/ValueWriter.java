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

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForInt;
import static java.util.Objects.requireNonNull;

/**
 * An interface for writing non-scalar values.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public interface ValueWriter<T> {

    /**
     * Returns an adapter which pre-writes a {@code boolean} value indicating the nullability of the value.
     *
     * @param wrapped the adapter to be wrapped.
     * @param <T>     value type parameter
     * @return an adapter wraps specified adapter.
     */
    static <T> ValueWriter<T> nullable(final ValueWriter<? super T> wrapped) {
        return new NullableValueWriter<>(requireNonNull(wrapped, "wrapped is null"));
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
     * Writes specified {@code length} value as an unsigned {@code int} of specified bit size.
     *
     * @param output a bit output from which the value is read.
     * @param size   the number bits to read.
     * @param value  the value whose lower {@code size} bits are written.
     * @return an actual written {@code length} value.
     * @throws IOException if an I/O error occurs.
     * @apiNote. This method is for writing the {@code length} of subsequent non-singular value.
     */
    default int writeLength(final BitOutput output, final int size, final int value) throws IOException {
        if (value < 0) {
            throw new IllegalArgumentException("value(" + value + ") < 0");
        }
        final int length = value & ((1 << requireValidSizeForInt(true, size)) - 1);
        output.writeUnsignedInt(size, length);
        return length;
    }
}
