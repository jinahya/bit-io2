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

class BitIoByteTest {

    @DisplayName("signed")
    @Nested
    class SignedTest {

//        @Test
//        void wr__MIN_VALUE() throws IOException {
//            final byte expected = Byte.MIN_VALUE;
//            final byte actual = BitIoTestUtils.wr1v(o -> {
//                o.writeByte(Byte.SIZE, expected);
//                return i -> {
//                    return i.readByte(Byte.SIZE);
//                };
//            });
//            assertThat(actual).isEqualTo(expected);
//        }

//        @Test
//        void wr__NegativeOne() throws IOException {
//            final byte expected = -1;
//            final byte actual = BitIoTestUtils.wr1v(o -> {
//                o.writeByte(1, expected);
//                return i -> {
//                    return i.readByte(1);
//                };
//            });
//            assertThat(actual).isEqualTo(expected);
//        }

//        @Test
//        void wr__Zero() throws IOException {
//            final byte expected = 0;
//            final byte actual = BitIoTestUtils.wr1v(o -> {
//                o.writeByte(1, expected);
//                return i -> {
//                    return i.readByte(1);
//                };
//            });
//            assertThat(actual).isEqualTo(expected);
//        }

//        @Test
//        void wr__PositiveOne() throws IOException {
//            final byte expected = 1;
//            final byte actual = BitIoTestUtils.wr1v(o -> {
//                o.writeByte(2, expected);
//                return i -> {
//                    return i.readByte(2);
//                };
//            });
//            assertThat(actual).isEqualTo(expected);
//        }

//        @Test
//        void wr__MAX_VALUE() throws IOException {
//            final byte expected = Byte.MAX_VALUE;
//            final byte actual = BitIoTestUtils.wr1v(o -> {
//                o.writeByte(Byte.SIZE, expected);
//                return i -> {
//                    return i.readByte(Byte.SIZE);
//                };
//            });
//            assertThat(actual).isEqualTo(expected);
//        }

        @RepeatedTest(16)
        void wr__random() {
            final boolean unsigned = false;
            BitIoTestUtils.applyRandomSizeAndValueForByteUnchecked(unsigned, s -> e -> {
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
            BitIoTestUtils.applyRandomSizeAndValueForByteUnchecked(unsigned, s -> e -> {
                return BitIoTestUtils.wr2v(o -> {
                    o.writeByte(unsigned, s, e.byteValue());
                    return i -> {
                        final byte actual = i.readByte(unsigned, s);
                        assertThat(actual).isEqualTo(e.byteValue());
                    };
                });
            });
        }

//        @RepeatedTest(16)
//        void we_unsigned__random() {
//            BitIoTestUtils.applyNextByte_v(true, s -> e -> {
//                return BitIoTestUtils.wr2v(o -> {
//                    o.writeUnsignedByte(s, e.byteValue());
//                    return i -> {
//                        final byte actual = i.readUnsignedByte(s);
//                        assertThat(actual).isEqualTo(e.byteValue());
//                    };
//                });
//            });
//        }
    }

    @DisplayName("8")
    @Nested
    class ByteTest {

//        @RepeatedTest(16)
//        void byte8_random() {
//            final boolean unsigned = false;
//            final int size = Byte.SIZE;
//            BitIoTestUtils.applyNextValueForByte_v(unsigned, size, e -> {
//                return BitIoTestUtils.wr2v(o -> {
//                    o.writeByte(e.byteValue());
//                    return i -> {
//                        final byte actual = i.readByte();
//                        assertThat(actual).isEqualTo(e.byteValue());
//                    };
//                });
//            });
//        }
    }
}
