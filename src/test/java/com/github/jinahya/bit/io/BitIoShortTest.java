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
import org.junit.jupiter.api.RepeatedTest;

import static org.assertj.core.api.Assertions.assertThat;

class BitIoShortTest {

    @DisplayName("signed")
    @Nested
    class SignedTest {

//        @Test
//        void wr_MinValue() throws IOException {
//            final short expected = Short.MIN_VALUE;
//            final short actual = BitIoTestUtils.wr1v(o -> {
//                o.writeShort(Short.SIZE, expected);
//                return i -> {
//                    return i.readShort(Short.SIZE);
//                };
//            });
//            assertThat(actual).isEqualTo(expected);
//        }
//
//        @Test
//        void wr_NegativeOne() throws IOException {
//            final short expected = -1;
//            final short actual = BitIoTestUtils.wr1v(o -> {
//                o.writeShort(1, expected);
//                return i -> {
//                    return i.readShort(1);
//                };
//            });
//            assertThat(actual).isEqualTo(expected);
//        }
//
//        @Test
//        void wr_Zero() throws IOException {
//            final short expected = 0;
//            final short actual = BitIoTestUtils.wr1v(o -> {
//                o.writeShort(1, expected);
//                return i -> {
//                    return i.readShort(1);
//                };
//            });
//            assertThat(actual).isEqualTo(expected);
//        }
//
//        @Test
//        void wr_PositiveOne() throws IOException {
//            final short expected = 1;
//            final short actual = BitIoTestUtils.wr1v(o -> {
//                o.writeShort(2, expected);
//                return i -> {
//                    return i.readShort(2);
//                };
//            });
//            assertThat(actual).isEqualTo(expected);
//        }
//
//        @Test
//        void wr_MaxValue() throws IOException {
//            final short expected = Short.MAX_VALUE;
//            final short actual = BitIoTestUtils.wr1v(o -> {
//                o.writeShort(Short.SIZE, expected);
//                return i -> {
//                    return i.readShort(Short.SIZE);
//                };
//            });
//            assertThat(actual).isEqualTo(expected);
//        }

        @RepeatedTest(16)
        void wr_random() {
            final boolean unsigned = false;
            BitIoTestUtils.applyRandomSizeAndValueForShortUnchecked(unsigned, s -> e -> {
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
            BitIoTestUtils.applyRandomSizeAndValueForShortUnchecked(unsigned, s -> e -> {
                return BitIoTestUtils.wr2v(o -> {
                    o.writeShort(unsigned, s, e.shortValue());
                    return i -> {
                        final short actual = i.readShort(unsigned, s);
                        assertThat(actual).isEqualTo(e.shortValue());
                    };
                });
            });
        }

//        @RepeatedTest(16)
//        void unsignedShort__() {
//            BitIoTestUtils.applyNextShort_v(true, s -> e -> {
//                return BitIoTestUtils.wr2v(o -> {
//                    o.writeUnsignedShort(s, e.shortValue());
//                    return i -> {
//                        final short actual = i.readUnsignedShort(s);
//                        assertThat(actual).isEqualTo(e.shortValue());
//                    };
//                });
//            });
//        }
    }

    @DisplayName("8")
    @Nested
    class ShortTest {

//        @RepeatedTest(16)
//        void wr_random() {
//            final boolean unsigned = false;
//            final int size = Short.SIZE;
//            BitIoTestUtils.applyNextValueForShort_v(unsigned, size, e -> {
//                return BitIoTestUtils.wr2v(o -> {
//                    o.writeShort(e.shortValue());
//                    return i -> {
//                        final short actual = i.readShort();
//                        assertThat(actual).isEqualTo(e.shortValue());
//                    };
//                });
//            });
//        }
    }
}
