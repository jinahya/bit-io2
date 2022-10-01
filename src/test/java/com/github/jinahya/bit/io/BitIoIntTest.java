package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 Jinahya, Inc.
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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.RepeatedTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class BitIoIntTest {

    @RepeatedTest(1)
    void unsigned() {
        final boolean unsigned = true;
        BitIoTestUtils.applyNextInt_v(unsigned, s -> e -> {
            return BitIoTestUtils.wr2v(o -> {
                o.writeInt(unsigned, s, e);
                return i -> {
                    final int actual = i.readInt(unsigned, s);
                    assertThat(actual).isEqualTo(e);
                };
            });
        });
    }

    @RepeatedTest(1)
    void signed() {
        final boolean unsigned = false;
        BitIoTestUtils.applyNextInt_v(unsigned, s -> e -> {
            return BitIoTestUtils.wr2v(o -> {
                o.writeInt(unsigned, s, e);
                return i -> {
                    final int actual = i.readInt(unsigned, s);
                    assertThat(actual).isEqualTo(e);
                };
            });
        });
    }
}
