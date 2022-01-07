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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith({MockitoExtension.class})
@Slf4j
public class BitOutputTest {

    // ---------------------------------------------------------------------------------------------------- writeBoolean

    /**
     * Tests {@link BitOutput#writeBoolean(boolean)} method.
     *
     * @throws IOException if an I/O error occurs.
     * @see BitInputTest#testReadBoolean()
     */
    @Test
    void testWriteBoolean() throws IOException {
        output.writeBoolean(ThreadLocalRandom.current().nextBoolean());
    }

    // ------------------------------------------------------------------------------------------------------- writeByte
    @DisplayName("writeByte(false, size) throws IllegalArgumentException when size is illegal")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTestParameters#invalidSizesForByte"})
    @ParameterizedTest
    void assertWriteByteSignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> output.writeByte(false, size, (byte) 0));
    }

    @DisplayName("writeByte(true, size) throws IllegalArgumentException when size is illegal")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTestParameters#invalidSizesForUnsignedByte"})
    @ParameterizedTest
    void assertWriteByteUnsignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> output.writeByte(true, size, (byte) 0));
    }

    // ------------------------------------------------------------------------------------------------------ writeShort
    @DisplayName("writeShort(false, size) throws IllegalArgumentException when size is illegal")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTestParameters#invalidSizesForShort"})
    @ParameterizedTest
    void assertWriteShortSignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> output.writeShort(false, size, (short) 0));
    }

    @DisplayName("writeShort(true, size) throws IllegalArgumentException when size is illegal")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTestParameters#invalidSizesForUnsignedShort"})
    @ParameterizedTest
    void assertWriteShortUnsignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> output.writeShort(true, size, (short) 0));
    }

    // ------------------------------------------------------------------------------------------------------- writeLong
    @DisplayName("writeLong(false, size) throws IllegalArgumentException when size is illegal")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTestParameters#illegalSizesForLong"})
    @ParameterizedTest
    void assertWriteLongSignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> output.writeLong(false, size, 0L));
    }

    @DisplayName("writeLong(true, size) throws IllegalArgumentException when size is illegal")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTestParameters#illegalSizesForUnsignedLong"})
    @ParameterizedTest
    void assertWriteLongUnsignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> output.writeLong(true, size, 0L));
    }

    // -------------------------------------------------------------------------------------------------------- writeChar

    /**
     * Asserts {@link BitOutput#writeChar(int, char)} method throws an {@link IllegalArgumentException} when the {@code
     * size} argument is invalid.
     *
     * @param size an invalid value for {@code size} argument.
     * @see BitInputTest#assertReadCharSignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(int)
     */
    @DisplayName("writeChar(size, value) throws IllegalArgumentException when size is illegal")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTestParameters#invalidSizesForChar"})
    @ParameterizedTest
    void assertWriteCharSignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> output.writeChar(size, (char) 0));
    }

    @DisplayName("writeChar(size, value)")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTestParameters#sizesForChar"})
    @ParameterizedTest
    void testReadChar(final int size) throws IOException {
        output.writeChar(size, (char) 0);
    }

    // ------------------------------------------------------------------------------------------------------------ skip

    /**
     * Asserts {@link BitOutput#skip(int)} method throws {@link IllegalArgumentException} when {@code bits} argument is
     * not positive.
     *
     * @see BitInputTest#assertSkipThrowsIllegalArgumentExceptionWhenBitsIsNotLegal(int)
     */
    @DisplayName("skip(bits) throws IllegalArgumentException when bits is not positive")
    @MethodSource({"com.github.jinahya.bit.io.BitIoTestParameters#illegalBitsForSkip"})
    @ParameterizedTest
    void assertSkipThrowsIllegalArgumentExceptionWhenBitsIsNotLegal(final int bits) {
        assertThrows(IllegalArgumentException.class, () -> output.skip(bits));
    }

    /**
     * Tests {@link BitOutput#skip(int)} method.
     *
     * @throws IOException if an I/O error occurs.
     * @see BitInputTest#testSkip()
     */
    @Test
    void testSkip() throws IOException {
        output.skip(ThreadLocalRandom.current().nextInt(1, 128));
    }

    // ----------------------------------------------------------------------------------------------------------- align
    @Test
    void testAlignWithBytes() throws IOException {
        output.align(ThreadLocalRandom.current().nextInt(1, 128));
    }

    @Test
    void testAlign() throws IOException {
        output.align();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Spy
    private BitOutput output;
}
