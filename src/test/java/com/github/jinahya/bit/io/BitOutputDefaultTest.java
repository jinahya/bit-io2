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

    @DisplayName("readBoolean")
    @Nested
    class ReadBooleanTest {

        @Test
        void readBoolean__() {
            assertThatCode(() -> output.writeBoolean(current().nextBoolean()))
                    .doesNotThrowAnyException();
        }
    }

    @DisplayName("readByte")
    @Nested
    class ReadByteTest {

        @Test
        void readByte_ThrowIllegalArgumentException_SizeIsNotPositive() {
            final int size = current().nextInt() | Integer.MIN_VALUE;
            assertThatThrownBy(() -> output.writeByte(size, (byte) 0))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Spy
    private BitOutput output;
}
