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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.stream.Stream;

import static com.github.jinahya.bit.io.BitIoTestUtils.applyRandomSizeAndValueForLongUnchecked;
import static com.github.jinahya.bit.io.BitIoTestUtils.getRandomValueForLong;
import static com.github.jinahya.bit.io.BitIoTestUtils.wr1u;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class BitIo_Long_Test {

    @ValueSource(booleans = {true, false})
    @ParameterizedTest
    void test(final boolean unsigned) {
        applyRandomSizeAndValueForLongUnchecked(
                unsigned,
                s -> v -> wr1u(o -> {
                    o.writeLong(unsigned, s, v);
                    return i -> {
                        final var actual = i.readLong(unsigned, s);
                        assertThat(actual).isEqualTo(v);
                        return null;
                    };
                })
        );
    }

    private static Stream<Arguments> getUnsignedAndSizeArgumentsStream() {
        return Stream.of(
                Arguments.of(true, 1),
                Arguments.of(true, Long.SIZE - 1),
                Arguments.of(false, 1),
                Arguments.of(false, Long.SIZE)
        );
    }

    @MethodSource({"getUnsignedAndSizeArgumentsStream"})
    @ParameterizedTest
    void fixed__(final boolean unsigned, final int size) throws IOException {
        final var expected = getRandomValueForLong(unsigned, size);
        final var actual = wr1u(o -> {
            o.writeLong(unsigned, size, expected);
            return i -> i.readLong(unsigned, size);
        });
        assertThat(actual).isEqualTo(expected);
    }
}
