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

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

class BitIoTest {

    @DisplayName("skip")
    @Nested
    class SkipTest {

        @Test
        void skip__() throws IOException {
            BitIoTestUtils.wr2v(o -> {
                final int bits = current().nextInt(1, 128);
                o.skip(bits);
                return i -> {
                    i.skip(bits);
                };
            });
        }
    }

    @DisplayName("align")
    @Nested
    class AlignTest {

        @RepeatedTest(16)
        void align__single() throws IOException {
            BitIoTestUtils.wr2v(o -> {
                final int bits = current().nextInt(1, 128);
                final boolean skip = current().nextBoolean();
                if (skip) {
                    o.skip(bits);
                }
                final long padded = o.align();
                return i -> {
                    if (skip) {
                        i.skip(bits);
                    }
                    final long discarded = i.align();
                    assertThat(discarded).isEqualTo(padded);
                };
            });
        }

        @RepeatedTest(16)
        void align__() throws IOException {
            BitIoTestUtils.wr2v(o -> {
                final int bits = current().nextInt(1, 128);
                final boolean skip = current().nextBoolean();
                if (skip) {
                    o.skip(bits);
                }
                final int bytes = current().nextInt(1, 16);
                final long padded = o.align(bytes);
                return i -> {
                    if (skip) {
                        i.skip(bits);
                    }
                    final long discarded = i.align(bytes);
                    assertThat(discarded).isEqualTo(padded);
                };
            });
        }
    }
}
