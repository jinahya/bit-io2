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

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith({MockitoExtension.class})
class BitOutputDefaultTest {

    @DisplayName("writeBoolean")
    @Nested
    class WriteBooleanTest {

        @Test
        void readBoolean__() {
            assertThatCode(() -> output.writeBoolean(current().nextBoolean()))
                    .doesNotThrowAnyException();
        }
    }

    @DisplayName("writeByte")
    @Nested
    class WriteByteTest {

        @Test
        void writeByte_ThrowIllegalArgumentException_SizeIsNotPositive() {
            final int size = current().nextInt() | Integer.MIN_VALUE;
            assertThatThrownBy(() -> output.writeByte(size, (byte) 0))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void writeByte__Unsigned() {
            final boolean unsigned = true;
            final int size = BitIoRandom.nextSizeForByte(unsigned);
            final byte value = BitIoRandom.nextValueForByte(unsigned, size);
            assertThatCode(() -> output.writeByte(unsigned, size, value))
                    .doesNotThrowAnyException();
        }

        @Test
        void writeByte__Signed() {
            final boolean unsigned = false;
            final int size = BitIoRandom.nextSizeForByte(unsigned);
            final byte value = BitIoRandom.nextValueForByte(unsigned, size);
            assertThatCode(() -> output.writeByte(unsigned, size, value))
                    .doesNotThrowAnyException();
        }

        @Test
        void writeByte__() {
            final boolean unsigned = false;
            final int size = BitIoRandom.nextSizeForByte(unsigned);
            final byte value = BitIoRandom.nextValueForByte(unsigned, size);
            assertThatCode(() -> output.writeByte(size, value))
                    .doesNotThrowAnyException();
        }
    }

    @Spy
    private BitOutput output;
}
