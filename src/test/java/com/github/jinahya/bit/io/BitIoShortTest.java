package com.github.jinahya.bit.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class BitIoShortTest {

    @DisplayName("signed")
    @Nested
    class SignedTest {

        @Test
        void wr_MinValue() throws IOException {
            final short expected = Short.MIN_VALUE;
            final short actual = BitIoTestUtils.wr1v(o -> {
                o.writeShort(Short.SIZE, expected);
                return i -> {
                    return i.readShort(Short.SIZE);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void wr_NegativeOne() throws IOException {
            final short expected = -1;
            final short actual = BitIoTestUtils.wr1v(o -> {
                o.writeShort(1, expected);
                return i -> {
                    return i.readShort(1);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void wr_Zero() throws IOException {
            final short expected = 0;
            final short actual = BitIoTestUtils.wr1v(o -> {
                o.writeShort(1, expected);
                return i -> {
                    return i.readShort(1);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void wr_PositiveOne() throws IOException {
            final short expected = 1;
            final short actual = BitIoTestUtils.wr1v(o -> {
                o.writeShort(2, expected);
                return i -> {
                    return i.readShort(2);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void wr_MaxValue() throws IOException {
            final short expected = Short.MAX_VALUE;
            final short actual = BitIoTestUtils.wr1v(o -> {
                o.writeShort(Short.SIZE, expected);
                return i -> {
                    return i.readShort(Short.SIZE);
                };
            });
            assertThat(actual).isEqualTo(expected);
        }

        @RepeatedTest(16)
        void wr_random() {
            final boolean unsigned = false;
            BitIoRandom.applyNextShort_v(unsigned, s -> e -> {
                return BitIoTestUtils.wr2v(o -> {
                    o.writeShort(unsigned, s, e.shortValue());
                    return i -> {
                        final short actual = i.readShort(unsigned, s);
                        assertThat(actual).isEqualTo(e.shortValue());
                    };
                });
            });
        }
    }

    @DisplayName("unsigned")
    @Nested
    class UnsignedTest {

        @RepeatedTest(16)
        void short__() {
            final boolean unsigned = true;
            BitIoRandom.applyNextShort_v(unsigned, s -> e -> {
                return BitIoTestUtils.wr2v(o -> {
                    o.writeShort(unsigned, s, e.shortValue());
                    return i -> {
                        final short actual = i.readShort(unsigned, s);
                        assertThat(actual).isEqualTo(e.shortValue());
                    };
                });
            });
        }

        @RepeatedTest(16)
        void unsignedShort__() {
            BitIoRandom.applyNextShort_v(true, s -> e -> {
                return BitIoTestUtils.wr2v(o -> {
                    o.writeUnsignedShort(s, e.shortValue());
                    return i -> {
                        final short actual = i.readUnsignedShort(s);
                        assertThat(actual).isEqualTo(e.shortValue());
                    };
                });
            });
        }
    }

    @DisplayName("8")
    @Nested
    class Short8Test {

        @RepeatedTest(16)
        void wr_random() {
            final boolean unsigned = false;
            final int size = Short.SIZE;
            BitIoRandom.applyNextValueForShort_v(unsigned, size, e -> {
                return BitIoTestUtils.wr2v(o -> {
                    o.writeShort16(e.shortValue());
                    return i -> {
                        final short actual = i.readShort16();
                        assertThat(actual).isEqualTo(e.shortValue());
                    };
                });
            });
        }
    }
}
