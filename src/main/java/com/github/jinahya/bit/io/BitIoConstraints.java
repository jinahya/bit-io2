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

/**
 * Constraints for bit-io.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class BitIoConstraints {

    private static int requirePositive(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size(" + size + ") <= 0");
        }
        return size;
    }

    static int requireValidSizeForByte(final boolean unsigned, final int size) {
        if (requirePositive(size) > Byte.SIZE) {
            throw new IllegalArgumentException("size(" + size + ") > " + Byte.SIZE);
        }
        if (unsigned && size == Byte.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") for unsigned byte");
        }
        return size;
    }

    static int requireValidSizeForShort(final boolean unsigned, final int size) {
        if (requirePositive(size) > Short.SIZE) {
            throw new IllegalArgumentException("size(" + size + ") > " + Short.SIZE);
        }
        if (unsigned && size == Short.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") for unsigned short");
        }
        return size;
    }

    static int requireValidSizeForInt(final boolean unsigned, final int size) {
        if (requirePositive(size) > Integer.SIZE) {
            throw new IllegalArgumentException("size(" + size + ") > " + Integer.SIZE);
        }
        if (unsigned && size == Integer.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") for unsigned int");
        }
        return size;
    }

    static int requireValidSizeForLong(final boolean unsigned, final int size) {
        if (requirePositive(size) > Long.SIZE) {
            throw new IllegalArgumentException("size(" + size + ") > " + Long.SIZE);
        }
        if (unsigned && size == Long.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") for unsigned long");
        }
        return size;
    }

    /**
     * Tests whether specified bit size is valid for a {@code char} value.
     *
     * @param size the bit size to test.
     * @return specified {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid for a {@code char} value.
     */
    static int requireValidSizeForChar(final int size) {
        if (requirePositive(size) > Character.SIZE) {
            throw new IllegalArgumentException("size(" + size + ") > " + Character.SIZE);
        }
        return size;
    }

    /**
     * Creates a new instance.
     */
    private BitIoConstraints() {
        throw new AssertionError("instantiation is not allowed");
    }
}
