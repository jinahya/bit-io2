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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class BitIoInt32Test {

    static IntStream values() {
        return IntStream.range(0, 16)
                .map(i -> ThreadLocalRandom.current().nextInt());
    }

    @MethodSource({"values"})
    @ParameterizedTest
    void wr(final int expected) throws IOException {
        BitIoTestUtils.wr2v(o -> {
            o.writeInt32(expected);
            return i -> {
                final int actual = i.readInt32();
                assertThat(actual).isEqualTo(expected);
            };
        });
    }
}
