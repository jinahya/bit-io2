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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BitOutputAdapterTest {

    // -------------------------------------------------------------------------------------------------------- writeInt
    @DisplayName("writeInt(false, size, value) throws IllegalArgumentException when size is illegal")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTests#illegalSizeForIntSigned"})
    @ParameterizedTest
    void assertWriteIntSignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> adapter.writeInt(false, size, 0));
    }

    @DisplayName("writeInt(true, size, value) throws IllegalArgumentException when size is illegal")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTests#illegalSizeForIntUnsigned"})
    @ParameterizedTest
    void assertWriteIntUnsignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> adapter.writeInt(true, size, 0));
    }

    @DisplayName("writeInt(false, size, value)")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTests#sizeAndValueForIntSigned"})
    @ParameterizedTest
    void testWriteIntSigned(final int size, final int value) throws IOException {
        adapter.writeInt(false, size, value);
    }

    @DisplayName("writeInt(true, size)")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTests#sizeAndValueForIntUnsigned"})
    @ParameterizedTest
    void testWriteIntUnsigned(final int size, final int value) throws IOException {
        adapter.writeInt(true, size, value);
    }

    // ------------------------------------------------------------------------------------------------------- writeLong
    @DisplayName("writeLong(false, size)")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTests#sizeAndValueForLongSigned"})
    @ParameterizedTest
    void testWriteLongSigned(final int size, final long value) throws IOException {
        adapter.writeLong(false, size, value);
    }

    @DisplayName("writeLong(true, size)")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTests#sizeAndValueForLongUnsigned"})
    @ParameterizedTest
    void testWriteLongUnsigned(final int size, final long value) throws IOException {
        adapter.writeLong(true, size, value);
    }

    // ----------------------------------------------------------------------------------------------------------- align
    @DisplayName("align(bytes) throws IllegalArgumentException when bytes is not positive ")
    @Test
    void assertAlignThrowsIllegalArgumentExceptionWhenBytesIsNotPositive() {
        assertThrows(IllegalArgumentException.class, () -> adapter.align(0));
        assertThrows(IllegalArgumentException.class, () -> adapter.align(current().nextInt() | Integer.MIN_VALUE));
    }

    /**
     * Tests {@link BitOutputAdapter#align(int)} method.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void testAlign() throws IOException {
        adapter.align(current().nextInt(1, 128));
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final BitOutputAdapter adapter = new BitOutputAdapter(() -> v -> {
    });
}
