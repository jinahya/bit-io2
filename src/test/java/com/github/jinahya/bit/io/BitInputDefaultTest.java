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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoTestUtils.nextSizeForByte;
import static com.github.jinahya.bit.io.BitIoTestUtils.nextSizeForInt;
import static com.github.jinahya.bit.io.BitIoTestUtils.nextSizeForLong;
import static com.github.jinahya.bit.io.BitIoTestUtils.nextSizeForShort;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
class BitInputDefaultTest {

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

        @Test
        void readByte__() throws IOException {
            final var unsigned = false;
            final var size = nextSizeForByte(unsigned);
            assertThatCode(() -> input.readByte(size))
                    .doesNotThrowAnyException();
            verify(input, times(1))
                    .readByte(unsigned, size);
        }

        @Test
        void readUnsignedByte__() throws IOException {
            final var unsigned = true;
            final var size = nextSizeForByte(unsigned);
            assertThatCode(() -> input.readUnsignedByte(size))
                    .doesNotThrowAnyException();
            verify(input, times(1))
                    .readByte(unsigned, size);
        }
    }

    @Nested
    class ReadShortTest {

        @Test
        void readShort__() throws IOException {
            final var unsigned = false;
            final var size = nextSizeForShort(unsigned);
            assertThatCode(() -> input.readShort(size))
                    .doesNotThrowAnyException();
            verify(input, times(1))
                    .readShort(unsigned, size);
        }

        @Test
        void readUnsignedShort__() throws IOException {
            final var unsigned = true;
            final var size = nextSizeForShort(unsigned);
            assertThatCode(() -> input.readUnsignedShort(size))
                    .doesNotThrowAnyException();
            verify(input, times(1))
                    .readShort(unsigned, size);
        }
    }

    @Nested
    class ReadIntTest {

        @Test
        void readInt__() throws IOException {
            final var unsigned = false;
            final var size = nextSizeForInt(unsigned);
            assertThatCode(() -> input.readInt(size))
                    .doesNotThrowAnyException();
            verify(input, times(1))
                    .readInt(unsigned, size);
        }

        @Test
        void readUnsignedInt__() throws IOException {
            final var unsigned = true;
            final var size = nextSizeForInt(unsigned);
            assertThatCode(() -> input.readUnsignedInt(size))
                    .doesNotThrowAnyException();
            verify(input, times(1))
                    .readInt(unsigned, size);
        }
    }

    @Nested
    class ReadLongTest {

        @Test
        void readLong__() throws IOException {
            final var unsigned = false;
            final var size = nextSizeForLong(unsigned);
            assertThatCode(() -> input.readLong(size))
                    .doesNotThrowAnyException();
            verify(input, times(1))
                    .readLong(unsigned, size);
        }

        @Test
        void readUnsignedLong__() throws IOException {
            final var unsigned = true;
            final var size = nextSizeForLong(unsigned);
            assertThatCode(() -> input.readUnsignedLong(size))
                    .doesNotThrowAnyException();
            verify(input, times(1))
                    .readLong(unsigned, size);
        }
    }

    @Spy
    private BitInput input;
}
