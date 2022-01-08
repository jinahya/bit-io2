package com.github.jinahya.bit.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class BitIoCharTest {

    @DisplayName("unsigned")
    @Nested
    class UnsignedTest {

        @RepeatedTest(16)
        void wr__() {
            BitIoRandom.applyNextChar_v(s -> e -> {
                return BitIoTestUtils.wr2v(o -> {
                    o.writeChar(s, (char) e.intValue());
                    return i -> {
                        final char actual = i.readChar(s);
                        assertThat(actual).isEqualTo((char) e.intValue());
                    };
                });
            });
        }
    }

    @DisplayName("16")
    @Nested
    class Char16Test {

        @Test
        void wr_MIN_VALUE() throws IOException {
            final char expected = Character.MIN_VALUE;
            final char actual = BitIoTestUtils.wr1v(o -> {
                o.writeChar16(expected);
                return BitInput::readChar16;
            });
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void wr_MAX_VALUE() throws IOException {
            final char expected = Character.MAX_VALUE;
            final char actual = BitIoTestUtils.wr1v(o -> {
                o.writeChar16(expected);
                return BitInput::readChar16;
            });
            assertThat(actual).isEqualTo(expected);
        }

        @RepeatedTest(16)
        void wr_random() {
            BitIoRandom.applyNextValueForChar_v(Character.SIZE, e -> {
                return BitIoTestUtils.wr2v(o -> {
                    o.writeChar16((char) e.intValue());
                    return i -> {
                        final char actual = i.readChar16();
                        assertThat(actual).isEqualTo((char) e.intValue());
                    };
                });
            });
        }
    }
}
