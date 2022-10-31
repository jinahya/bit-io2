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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
class BitInput_Mock_Test {

    @Nested
    class ReadBooleanTest {

        @Test
        void readBoolean__() {
            assertThatCode(() -> input.readBoolean())
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class ReadByteTest {

        @ValueSource(booleans = {true, false})
        @ParameterizedTest(name = "[{index}] readByte(unsigned: {0})")
        void readByte__(final boolean unsigned) throws IOException {
            final var size = BitIoRandom.nextSizeForByte(unsigned);
            final var value = input.readByte(unsigned, size);
            verify(input, times(1))
                    .readInt(unsigned, size);
        }
    }

    @Nested
    class ReadShortTest {

        @ValueSource(booleans = {true, false})
        @ParameterizedTest(name = "[{index}] readShort(unsigned: {0})")
        void readShort__(final boolean unsigned) throws IOException {
            final var size = BitIoRandom.nextSizeForShort(unsigned);
            assertThatCode(() -> input.readShort(unsigned, size))
                    .doesNotThrowAnyException();
            verify(input, times(1))
                    .readInt(unsigned, size);
        }
    }

    @Nested
    class ReadIntTest {

        @ValueSource(booleans = {true, false})
        @ParameterizedTest(name = "[{index}] readInt(unsigned: {0})")
        void readInt__(final boolean unsigned) {
            final var size = BitIoRandom.nextSizeForInt(unsigned);
            assertThatCode(() -> input.readInt(unsigned, size))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class ReadLongTest {

        @ValueSource(booleans = {true, false})
        @ParameterizedTest(name = "[{index}] readLong(unsigned: {0})")
        void readLong__(final boolean unsigned) {
            final var size = BitIoRandom.nextSizeForLong(unsigned);
            assertThatCode(() -> input.readLong(unsigned, size))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class ReadCharTest {

        @Test
        void readChar__() throws IOException {
            final var size = BitIoRandom.nextSizeForChar();
            assertThatCode(() -> input.readChar(size))
                    .doesNotThrowAnyException();
            verify(input, times(1))
                    .readInt(true, size);
        }
    }

    @Spy
    private BitInput input;
}
