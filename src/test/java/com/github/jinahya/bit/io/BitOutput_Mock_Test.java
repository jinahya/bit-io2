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
            final var size = BitIoRandom.nextSizeForByte(unsigned);
            final var value = BitIoRandom.nextValueForByte(unsigned, size);
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
            final var size = BitIoRandom.nextSizeForShort(unsigned);
            final var value = BitIoRandom.nextValueForShort(unsigned, size);
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
            final var size = BitIoRandom.nextSizeForInt(unsigned);
            final var value = BitIoRandom.nextValueForInt(unsigned, size);
            assertThatCode(() -> output.writeInt(unsigned, size, value))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class WriteLongTest {

        @ValueSource(booleans = {true, false})
        @ParameterizedTest(name = "[{index}] writeLong(unsigned: {0})")
        void writeLong__(final boolean unsigned) throws IOException {
            final var size = BitIoRandom.nextSizeForLong(unsigned);
            final var value = BitIoRandom.nextValueForLong(unsigned, size);
            assertThatCode(() -> output.writeLong(unsigned, size, value))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class WriteCharTest {

        @Test
        void writeChar__() throws IOException {
            final var size = BitIoRandom.nextSizeForChar();
            final var value = BitIoRandom.nextValueForChar(size);
            assertThatCode(() -> output.writeChar(size, value))
                    .doesNotThrowAnyException();
            verify(output, times(1))
                    .writeInt(true, size, value);
        }
    }

    @Spy
    private BitOutput output;
}
