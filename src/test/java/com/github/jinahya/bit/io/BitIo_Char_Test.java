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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BitIo_Char_Test {

    private static Stream<Arguments> sizeAndValueArgumentsStream() {
        return IntStream.range(0, 16)
                .map(i -> BitIoRandom.nextSizeForChar())
                .mapToObj(s -> Arguments.of(s, BitIoRandom.nextValueForChar(s)));
    }

    @MethodSource({"sizeAndValueArgumentsStream"})
    @ParameterizedTest
    void wr__(final int size, final char expected) throws IOException {
        final char actual = BitIoTestUtils.wr1au(o -> {
            o.writeChar(size, expected);
            return (a, i) -> {
                assertThat(a.length).isLessThanOrEqualTo(Character.BYTES);
                return i.readChar(size);
            };
        });
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void wr__SIZE() throws IOException {
        final var expected = BitIoRandom.nextValueForChar(Character.SIZE);
        final var actual = BitIoTestUtils.wr1au(o -> {
            o.writeChar(Character.SIZE, expected);
            return (a, i) -> {
                assertThat(a.length).isLessThanOrEqualTo(Character.BYTES);
                return i.readChar(Character.SIZE);
            };
        });
        assertThat(actual).isEqualTo(expected);
    }
}
