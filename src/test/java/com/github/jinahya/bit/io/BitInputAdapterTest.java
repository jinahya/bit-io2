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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BitInputAdapterTest {

    // --------------------------------------------------------------------------------------------------------- readInt
    @DisplayName("readInt(false, size) throws IllegalArgumentException when size is illegal")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTests#illegalSizeForIntSigned"})
    @ParameterizedTest
    void assertReadIntSignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> adapter.readInt(false, size));
    }

    @DisplayName("readInt(true, size) throws IllegalArgumentException when size is illegal")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTests#illegalSizeForIntUnsigned"})
    @ParameterizedTest
    void assertReadIntUnsignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> adapter.readInt(true, size));
    }

    @DisplayName("readInt(false, size)")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTests#sizeForIntSigned"})
    @ParameterizedTest
    void testReadIntSigned(final int size) throws IOException {
        final int value = adapter.readInt(false, size);
        assertEquals(value < 0 ? -1 : 0, value >> (size - 1));
    }

    @DisplayName("readInt(true, size)")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTests#sizeForIntUnsigned"})
    @ParameterizedTest
    void testReadIntUnsigned(final int size) throws IOException {
        final int value = adapter.readInt(true, size);
        assertTrue(value >= 0);
    }

    // -------------------------------------------------------------------------------------------------------- readLong
    @DisplayName("readLong(false, size)")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTests#sizeForLongSigned"})
    @ParameterizedTest
    void testReadLongSigned(final int size) throws IOException {
        final long value = adapter.readLong(false, size);
        assertEquals(value < 0L ? -1L : 0L, value >> (size - 1));
    }

    @DisplayName("readLong(true, size)")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTests#sizeForLongUnsigned"})
    @ParameterizedTest
    void testReadLongUnsigned(final int size) throws IOException {
        final long value = adapter.readLong(true, size);
        assertTrue(value >= 0L);
    }

    // ----------------------------------------------------------------------------------------------------------- align
    @DisplayName("align(bytes) throws IllegalArgumentException when bytes is not positive ")
    @Test
    void assertAlignThrowsIllegalArgumentExceptionWhenBytesIsNotPositive() {
        assertThrows(IllegalArgumentException.class, () -> adapter.align(0));
        assertThrows(IllegalArgumentException.class, () -> adapter.align(current().nextInt() | Integer.MIN_VALUE));
    }

    /**
     * Tests {@link BitInputAdapter#align(int)} method.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void testAlign() throws IOException {
        adapter.align(current().nextInt(1, 128));
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final BitInputAdapter adapter = new BitInputAdapter(() -> () -> current().nextInt(0, 256));
}
