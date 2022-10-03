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

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoTestUtils.applyRandomValueForCharUnchecked;
import static com.github.jinahya.bit.io.BitIoTestUtils.wr1u;
import static com.github.jinahya.bit.io.BitIoTestUtils.wr2u;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

class BitIo_Char_Test {

    @Test
    void test() {
        applyRandomValueForCharUnchecked(
                s -> v -> wr1u(o -> {
                    final var expected = (char) v.intValue();
                    o.writeChar(s, expected);
                    return i -> {
                        final var actual = i.readChar(s);
                        assertThat(actual).isEqualTo(expected);
                        return null;
                    };
                })
        );
    }

    @Test
    void __SIZE() throws IOException {
        wr2u(o -> {
            final var expected = (char) current().nextInt(Character.MAX_VALUE + 1);
            o.writeChar(Character.SIZE, expected);
            return i -> {
                final var actual = i.readChar(Character.SIZE);
                assertThat(actual).isEqualTo(expected);
            };
        });
    }
}
