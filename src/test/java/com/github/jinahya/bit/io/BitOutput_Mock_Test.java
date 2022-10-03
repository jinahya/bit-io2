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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoTestUtils.getRandomSizeForByte;
import static com.github.jinahya.bit.io.BitIoTestUtils.getRandomSizeForChar;
import static com.github.jinahya.bit.io.BitIoTestUtils.getRandomSizeForInt;
import static com.github.jinahya.bit.io.BitIoTestUtils.getRandomSizeForLong;
import static com.github.jinahya.bit.io.BitIoTestUtils.getRandomSizeForShort;
import static com.github.jinahya.bit.io.BitIoTestUtils.getRandomValueForByte;
import static com.github.jinahya.bit.io.BitIoTestUtils.getRandomValueForChar;
import static com.github.jinahya.bit.io.BitIoTestUtils.getRandomValueForInt;
import static com.github.jinahya.bit.io.BitIoTestUtils.getRandomValueForLong;
import static com.github.jinahya.bit.io.BitIoTestUtils.getRandomValueForShort;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
class BitOutput_Mock_Test {

    @DisplayName("writeBoolean")
    @Nested
    class WriteBooleanTest {

        @Test
        void readBoolean__() {
            assertThatCode(() -> output.writeBoolean(current().nextBoolean()))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class WriteByteTest {

        @ValueSource(booleans = {true, false})
        @ParameterizedTest(name = "[{index}] writeBoolean(unsigned: {0})")
        void writeByte__(final boolean unsigned) throws IOException {
            final var size = getRandomSizeForByte(unsigned);
            final var value = getRandomValueForByte(unsigned, size);
            assertThatCode(() -> output.writeByte(unsigned, size, value))
                    .doesNotThrowAnyException();
            verify(output, times(1))
                    .writeInt(unsigned, size, value);
        }
    }

    @Nested
    class WriteShortTest {

        @ValueSource(booleans = {true, false})
        @ParameterizedTest(name = "[{index}] writeShort(unsigned: {0})")
        void writeShort__(final boolean unsigned) throws IOException {
            final var size = getRandomSizeForShort(unsigned);
            final var value = getRandomValueForShort(unsigned, size);
            assertThatCode(() -> output.writeShort(unsigned, size, value))
                    .doesNotThrowAnyException();
            verify(output, times(1))
                    .writeInt(unsigned, size, value);
        }
    }

    @Nested
    class WriteIntTest {

        @ValueSource(booleans = {true, false})
        @ParameterizedTest(name = "[{index}] writeInt(unsigned: {0})")
        void writeInt__(final boolean unsigned) throws IOException {
            final var size = getRandomSizeForInt(unsigned);
            final var value = getRandomValueForInt(unsigned, size);
            assertThatCode(() -> output.writeInt(false, size, value))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class WriteLongTest {

        @ValueSource(booleans = {true, false})
        @ParameterizedTest(name = "[{index}] writeLong(unsigned: {0})")
        void writeLong__(final boolean unsigned) throws IOException {
            final var size = getRandomSizeForLong(unsigned);
            final var value = getRandomValueForLong(unsigned, size);
            assertThatCode(() -> output.writeLong(true, size, value))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class WriteCharTest {

        @Test
        void writeChar__() throws IOException {
            final var size = getRandomSizeForChar();
            final var value = getRandomValueForChar(size);
            assertThatCode(() -> output.writeChar(size, value))
                    .doesNotThrowAnyException();
            verify(output, times(1))
                    .writeInt(true, size, value);
        }
    }

    @Spy
    private BitOutput output;
}
