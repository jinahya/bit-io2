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

/**
 * A utility class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class BitIoUtils {

    private static int log2(final int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("value(" + value + ") is not positive");
        }
        return Integer.SIZE - Integer.numberOfLeadingZeros(value);
    }

    /**
     * Returns the number of required bits for specified unsigned value.
     *
     * @param value the unsigned value whose size is calculated.
     * @return the number of required bits for {@code value}; always positive.
     */
    static int size(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("value(" + value + ") is negative");
        }
        if (value == 0) {
            return 1;
        }
        return log2(value);
    }

    static int readCount(final BitInput input, final int size) throws IOException {
        Objects.requireNonNull(input, "input is null");
        BitIoConstraints.requireValidSizeForInt(true, size);
        return input.readUnsignedInt(size);
    }

    static void writeCount(final BitOutput output, final int size, final int value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        BitIoConstraints.requireValidSizeForInt(true, size);
        output.writeUnsignedInt(size, value);
    }

    private BitIoUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
