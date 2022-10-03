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
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.stream.Stream;

import static com.github.jinahya.bit.io.BitIoTestUtils.applyRandomSizeAndValueForShortUnchecked;
import static com.github.jinahya.bit.io.BitIoTestUtils.getRandomValueForShort;
import static com.github.jinahya.bit.io.BitIoTestUtils.wr1u;
import static org.assertj.core.api.Assertions.assertThat;

class BitIo_Short_Test {

    @ValueSource(booleans = {true, false})
    @ParameterizedTest
    void random__(final boolean unsigned) {
        applyRandomSizeAndValueForShortUnchecked(
                unsigned,
                s -> v -> wr1u(o -> {
                    final var expected = v.shortValue();
                    o.writeShort(unsigned, s, expected);
                    return i -> {
                        final var actual = i.readShort(unsigned, s);
                        assertThat(actual).isEqualTo(expected);
                        return null;
                    };
                })
        );
    }

    private static Stream<Arguments> getUnsignedAndSizeArgumentsStream() {
        return Stream.of(
                Arguments.of(true, 1),
                Arguments.of(true, Short.SIZE - 1),
                Arguments.of(false, 1),
                Arguments.of(false, Short.SIZE)
        );
    }

    @MethodSource({"getUnsignedAndSizeArgumentsStream"})
    @ParameterizedTest
    void fixed__(final boolean unsigned, final int size) throws IOException {
        final var expected = getRandomValueForShort(unsigned, size);
        final var actual = wr1u(o -> {
            o.writeShort(unsigned, size, expected);
            return i -> i.readShort(unsigned, size);
        });
        assertThat(actual).isEqualTo(expected);
    }
}
