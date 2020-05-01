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

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A class for testing {@link BitInput} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitOutputTest
 */
@ExtendWith({MockitoExtension.class})
@Slf4j
public class BitInputTest {

    // ----------------------------------------------------------------------------------------------------- readBoolean

    /**
     * Tests {@link BitInput#readBoolean()} method.
     *
     * @throws IOException if an I/O error occurs.
     * @see BitOutputTest#testWriteBoolean()
     */
    @Test
    void testReadBoolean() throws IOException {
        final boolean value = input.readBoolean();
    }

    // -------------------------------------------------------------------------------------------------------- readByte
    @DisplayName("readByte(false, size) throws IllegalArgumentException when size is illegal")
    @MethodSource({"com.github.jinahya.bit.io.BitIoParameters#illegalSizeForByte"})
    @ParameterizedTest
    void assertReadByteSignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> input.readByte(false, size));
    }

    @DisplayName("readByte(true, size) throws IllegalArgumentException when size is illegal")
    @MethodSource({"com.github.jinahya.bit.io.BitIoParameters#illegalSizeForUnsignedByte"})
    @ParameterizedTest
    void assertReadByteUnsignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> input.readByte(true, size));
    }

    // ------------------------------------------------------------------------------------------------------- readShort
    @DisplayName("readShort(false, size) throws IllegalArgumentException when size is illegal")
    @MethodSource({"com.github.jinahya.bit.io.BitIoParameters#illegalSizeForShort"})
    @ParameterizedTest
    void assertReadShortSignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> input.readShort(false, size));
    }

    @DisplayName("readShort(true, size) throws IllegalArgumentException when size is illegal")
    @MethodSource({"com.github.jinahya.bit.io.BitIoParameters#illegalSizeForUnsignedShort"})
    @ParameterizedTest
    void assertReadShortUnsignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> input.readShort(true, size));
    }

    // -------------------------------------------------------------------------------------------------------- readLong
    @DisplayName("readLong(false, size) throws IllegalArgumentException when size is illegal")
    @MethodSource({"com.github.jinahya.bit.io.BitIoParameters#illegalSizeForLong"})
    @ParameterizedTest
    void assertReadLongSignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> input.readLong(false, size));
    }

    @DisplayName("readLong(true, size) throws IllegalArgumentException when size is illegal")
    @MethodSource({"com.github.jinahya.bit.io.BitIoParameters#illegalSizeForUnsignedLong"})
    @ParameterizedTest
    void assertReadLongUnsignedThrowsIllegalArgumentExceptionWhenSizeIsIllegal(final int size) {
        assertThrows(IllegalArgumentException.class, () -> input.readLong(true, size));
    }

    // ------------------------------------------------------------------------------------------------------------ skip

    /**
     * Asserts {@link BitInput#skip(int)} method throws {@link IllegalArgumentException} when {@code bits} argument is
     * not positive.
     */
    @DisplayName("skip(bits) throws IllegalArgumentException when bits is not positive")
    @MethodSource({"com.github.jinahya.bit.io.BitIoParameters#illegalBitsForSkip"})
    @ParameterizedTest
    void assertSkipThrowsIllegalArgumentExceptionWhenBitsIsNotPositive() {
        assertThrows(IllegalArgumentException.class, () -> input.skip(current().nextInt() | Integer.MIN_VALUE));
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Spy
    private BitInput input;
}
