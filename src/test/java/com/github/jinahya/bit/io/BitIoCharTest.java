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
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class BitIoCharTest {

    @DisplayName("unsigned")
    @Nested
    class UnsignedTest {

        @RepeatedTest(16)
        void wr__() {
            BitIoTestUtils.applyNextChar_v(s -> e -> {
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
                o.writeChar(Character.SIZE, expected);
                return i -> i.readChar(Character.SIZE);
            });
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void wr_MAX_VALUE() throws IOException {
            final char expected = Character.MAX_VALUE;
            final char actual = BitIoTestUtils.wr1v(o -> {
                o.writeChar(Character.SIZE, expected);
                return i -> i.readChar(Character.SIZE);
            });
            assertThat(actual).isEqualTo(expected);
        }

        @RepeatedTest(16)
        void wr_random() {
            BitIoTestUtils.applyNextValueForChar_v(Character.SIZE, e -> {
                return BitIoTestUtils.wr2v(o -> {
                    o.writeChar(Character.SIZE, (char) e.intValue());
                    return i -> {
                        final char actual = i.readChar(Character.SIZE);
                        assertThat(actual).isEqualTo((char) e.intValue());
                    };
                });
            });
        }
    }
}
