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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoTestUtils.nextSizeForByte;
import static com.github.jinahya.bit.io.BitIoTestUtils.nextSizeForChar;
import static com.github.jinahya.bit.io.BitIoTestUtils.nextSizeForInt;
import static com.github.jinahya.bit.io.BitIoTestUtils.nextSizeForLong;
import static com.github.jinahya.bit.io.BitIoTestUtils.nextSizeForShort;
import static com.github.jinahya.bit.io.BitIoTestUtils.nextValueForByte;
import static com.github.jinahya.bit.io.BitIoTestUtils.nextValueForChar;
import static com.github.jinahya.bit.io.BitIoTestUtils.nextValueForInt;
import static com.github.jinahya.bit.io.BitIoTestUtils.nextValueForLong;
import static com.github.jinahya.bit.io.BitIoTestUtils.nextValueForShort;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
class BitOutputMockTest {

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

        @Test
        void writeByte__() throws IOException {
            final boolean unsigned = false;
            final int size = nextSizeForByte(unsigned);
            final byte value = nextValueForByte(unsigned, size);
            assertThatCode(() -> output.writeByte(false, size, value))
                    .doesNotThrowAnyException();
            verify(output, times(1))
                    .writeByte(unsigned, size, value);
        }

        @Test
        void writeUnsignedByte__() throws IOException {
            final boolean unsigned = true;
            final int size = nextSizeForByte(unsigned);
            final byte value = nextValueForByte(unsigned, size);
            assertThatCode(() -> output.writeByte(true, size, value))
                    .doesNotThrowAnyException();
            verify(output, times(1))
                    .writeByte(unsigned, size, value);
        }
    }

    @Nested
    class WriteShortTest {

        @Test
        void writeShort__() throws IOException {
            final boolean unsigned = false;
            final int size = nextSizeForShort(unsigned);
            final short value = nextValueForShort(unsigned, size);
            assertThatCode(() -> output.writeShort(unsigned, size, value))
                    .doesNotThrowAnyException();
            verify(output, times(1))
                    .writeShort(unsigned, size, value);
        }

        @Test
        void writeUnsignedShort__() throws IOException {
            final boolean unsigned = true;
            final int size = nextSizeForShort(unsigned);
            final short value = nextValueForShort(unsigned, size);
            assertThatCode(() -> output.writeShort(true, size, value))
                    .doesNotThrowAnyException();
            verify(output, times(1))
                    .writeShort(unsigned, size, value);
        }
    }

    @Nested
    class WriteIntTest {

        @Test
        void writeInt__() throws IOException {
            final boolean unsigned = false;
            final int size = nextSizeForInt(unsigned);
            final int value = nextValueForInt(unsigned, size);
            assertThatCode(() -> output.writeInt(false, size, value))
                    .doesNotThrowAnyException();
            verify(output, times(1))
                    .writeInt(unsigned, size, value);
        }

        @Test
        void writeUnsignedInt__() throws IOException {
            final boolean unsigned = true;
            final int size = nextSizeForInt(unsigned);
            final int value = nextValueForInt(unsigned, size);
            assertThatCode(() -> output.writeInt(true, size, value))
                    .doesNotThrowAnyException();
            verify(output, times(1))
                    .writeInt(unsigned, size, value);
        }
    }

    @Nested
    class WriteLongTest {

        @Test
        void writeLong__() throws IOException {
            final boolean unsigned = false;
            final int size = nextSizeForLong(unsigned);
            final long value = nextValueForLong(unsigned, size);
            assertThatCode(() -> output.writeLong(true, size, value))
                    .doesNotThrowAnyException();
            verify(output, times(1))
                    .writeLong(unsigned, size, value);
        }

        @Test
        void writeUnsignedLong__() throws IOException {
            final boolean unsigned = true;
            final int size = nextSizeForLong(unsigned);
            final long value = nextValueForLong(unsigned, size);
            assertThatCode(() -> output.writeLong(true, size, value))
                    .doesNotThrowAnyException();
            verify(output, times(1))
                    .writeLong(unsigned, size, value);
        }
    }

    @Nested
    class WriteCharTest {

        @Test
        void writeChar__() throws IOException {
            final int size = nextSizeForChar();
            final char value = nextValueForChar(size);
            assertThatCode(() -> output.writeChar(size, value))
                    .doesNotThrowAnyException();
            verify(output, times(1))
                    .writeInt(true, size, value);
        }
    }

    @Spy
    private BitOutput output;
}
