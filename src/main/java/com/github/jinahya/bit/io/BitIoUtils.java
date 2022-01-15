package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 - 2022 Jinahya, Inc.
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
import java.util.function.Supplier;

/**
 * Utilities for bit-io.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class BitIoUtils {

    // https://stackoverflow.com/a/680040/330457
    private static int log2(final int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("value(" + value + ") is not positive");
        }
        final int result = Integer.SIZE - Integer.numberOfLeadingZeros(value);
        assert result > 0;
        return result;
    }

    /**
     * Returns the number of required bits for specified value.
     *
     * @param value the value whose size is calculated.
     * @return the number of required bits for {@code value}; always positive.
     */
    public static int size(final int value) {
        if (value < 0) {
            return size(~value) + 1;
        }
        if (value == 0) {
            return 1;
        }
        return log2(value);
    }

    // https://stackoverflow.com/a/680040/330457
    private static int log2(final long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("value(" + value + ") is not positive");
        }
        final int result = Long.SIZE - Long.numberOfLeadingZeros(value);
        assert result > 0;
        return result;
    }

    /**
     * Returns the number of required bits for specified value.
     *
     * @param value the value whose size is calculated.
     * @return the number of required bits for {@code value}; always positive.
     */
    public static int size(final long value) {
        if (value < 0) {
            return size(~value) + 1;
        }
        if (value == 0L) {
            return 1;
        }
        return log2(value);
    }

    /**
     * Returns a supplier which results {@code null}.
     *
     * @param <T> result type parameter
     * @return a supplier results {@code null}.
     */
    static <T> Supplier<T> emptySupplier() {
        return () -> null;
    }

    /**
     * Reads an unsigned value of specified number of bits for a count.
     *
     * @param input a bit-input from a value is read.
     * @param size  the number of bits.
     * @return an unsigned value of {@code size} bits.
     * @throws IOException if an I/O error occurs.
     */
    static int readCount(final BitInput input, final int size) throws IOException {
        Objects.requireNonNull(input, "input is null");
        BitIoConstraints.requireValidSizeForUnsignedInt(size);
        return input.readUnsignedInt(size);
    }

    /**
     * Writes specified unsigned value of specified number of bits for a count.
     *
     * @param output a bit-output to which the value is written.
     * @param size   the number of bits.
     * @param value  an unsigned value of {@code size} bits.
     * @return the {@value} right-shifted by {@code size}.
     * @throws IOException if an I/O error occurs.
     */
    static int writeCount(final BitOutput output, final int size, final int value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        BitIoConstraints.requireValidSizeForUnsignedInt(size);
        if (value < 0) {
            throw new IllegalArgumentException("value(" + value + ") is negative");
        }
        output.writeUnsignedInt(size, value);
        return value >> (Integer.SIZE - size);
    }

    private BitIoUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
