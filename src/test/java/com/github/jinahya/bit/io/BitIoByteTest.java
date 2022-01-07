package com.github.jinahya.bit.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class BitIoByteTest {

    @DisplayName("signed")
    @Nested
    class SignedTest {

        @Test
        void wr__MIN_VALUE() throws IOException {
            final byte expected = Byte.MIN_VALUE;
            final byte actual = BitIoTestUtils.wr1v(o -> {
                o.writeByte(Byte.SIZE, expected);
                return i -> {
                    return i.readByte(Byte.SIZE);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void wr__NegativeOne() throws IOException {
            final byte expected = -1;
            final byte actual = BitIoTestUtils.wr1v(o -> {
                o.writeByte(1, expected);
                return i -> {
                    return i.readByte(1);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void wr__Zero() throws IOException {
            final byte expected = 0;
            final byte actual = BitIoTestUtils.wr1v(o -> {
                o.writeByte(1, expected);
                return i -> {
                    return i.readByte(1);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void wr__PositiveOne() throws IOException {
            final byte expected = 1;
            final byte actual = BitIoTestUtils.wr1v(o -> {
                o.writeByte(2, expected);
                return i -> {
                    return i.readByte(2);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void wr__MAX_VALUE() throws IOException {
            final byte expected = Byte.MAX_VALUE;
            final byte actual = BitIoTestUtils.wr1v(o -> {
                o.writeByte(Byte.SIZE, expected);
                return i -> {
                    return i.readByte(Byte.SIZE);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @RepeatedTest(16)
        void wr__random() {
            final boolean unsigned = false;
            BitIoRandom.applyNextByte_v(unsigned, s -> e -> {
                return BitIoTestUtils.wr2v(o -> {
                    o.writeByte(unsigned, s, e.byteValue());
                    return i -> {
                        final byte actual = i.readByte(unsigned, s);
                        assertThat(actual).isEqualTo(e.byteValue());
                    };
                });
            });
        }
    }

    @DisplayName("unsigned")
    @Nested
    class UnsignedTest {

        @RepeatedTest(16)
        void wr__random() {
            final boolean unsigned = true;
            BitIoRandom.applyNextByte_v(unsigned, s -> e -> {
                return BitIoTestUtils.wr2v(o -> {
                    o.writeByte(unsigned, s, e.byteValue());
                    return i -> {
                        final byte actual = i.readByte(unsigned, s);
                        assertThat(actual).isEqualTo(e.byteValue());
                    };
                });
            });
        }

        @RepeatedTest(16)
        void we_unsigned__random() {
            BitIoRandom.applyNextByte_v(true, s -> e -> {
                return BitIoTestUtils.wr2v(o -> {
                    o.writeUnsignedByte(s, e.byteValue());
                    return i -> {
                        final byte actual = i.readUnsignedByte(s);
                        assertThat(actual).isEqualTo(e.byteValue());
                    };
                });
            });
        }
    }

    @DisplayName("8")
    @Nested
    class Byte8Test {

        @RepeatedTest(16)
        void byte8_random() {
            final boolean unsigned = false;
            final int size = Byte.SIZE;
            BitIoRandom.applyNextValueForByte_v(unsigned, size, e -> {
                return BitIoTestUtils.wr2v(o -> {
                    o.writeByte8(e.byteValue());
                    return i -> {
                        final byte actual = i.readByte8();
                        assertThat(actual).isEqualTo(e.byteValue());
                    };
                });
            });
        }
    }
}
