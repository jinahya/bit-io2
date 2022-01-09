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
class BitInputDefaultTest {

    @DisplayName("readBoolean")
    @Nested
    class ReadBooleanTest {

        @Test
        void readBoolean__() {
            assertThatCode(() -> input.readBoolean())
                    .doesNotThrowAnyException();
        }
    }

    @DisplayName("readByte")
    @Nested
    class ReadByteTest {

        @Test
        void readByte_ThrowIllegalArgumentException_SizeIsNotPositive() {
            final int size = current().nextInt() | Integer.MIN_VALUE;
            assertThatThrownBy(() -> input.readByte(size))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void readByte__Unsigned() {
            final boolean unsigned = true;
            final int size = BitIoRandom.nextSizeForByte(unsigned);
            assertThatCode(() -> input.readByte(unsigned, size))
                    .doesNotThrowAnyException();
        }

        @Test
        void readByte__Signed() {
            final boolean unsigned = false;
            final int size = BitIoRandom.nextSizeForByte(unsigned);
            assertThatCode(() -> input.readByte(unsigned, size))
                    .doesNotThrowAnyException();
        }

        @Test
        void readByte__() {
            final boolean unsigned = false;
            final int size = BitIoRandom.nextSizeForByte(unsigned);
            assertThatCode(() -> input.readByte(size))
                    .doesNotThrowAnyException();
        }
    }

    @Spy
    private BitInput input;
}
