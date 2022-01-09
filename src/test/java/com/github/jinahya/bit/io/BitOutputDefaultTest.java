package com.github.jinahya.bit.io;

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
