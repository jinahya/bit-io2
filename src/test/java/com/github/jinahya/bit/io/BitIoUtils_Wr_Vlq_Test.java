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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class BitIoUtils_Wr_Vlq_Test {

    private static IntStream getIntStream() {
        return IntStream.concat(
                IntStream.of(0, 1, Integer.MAX_VALUE - 1, Integer.MAX_VALUE),
                IntStream.range(0, 16).map(i -> ThreadLocalRandom.current().nextInt() & Integer.MAX_VALUE)
        );
    }

    private static LongStream getLongStream() {
        return LongStream.concat(
                LongStream.of(0L, 1L, Long.MAX_VALUE - 1, Long.MAX_VALUE),
                IntStream.range(0, 16).mapToLong(i -> ThreadLocalRandom.current().nextLong() & Long.MAX_VALUE)
        );
    }

    @Test
    void vlq__106903() throws IOException {
        final int expected = 106903;
        final int actual = BitIoTestUtils.wr1au(o -> {
            BitIoUtils.writeVlq(o, expected);
            return (a, i) -> {
                return BitIoUtils.readVlq(i);
            };
        });
        assertThat(actual).isEqualTo(expected);
    }

    @MethodSource({"getIntStream"})
    @ParameterizedTest
    void vlq__(final int expected) throws IOException {
        final int actual = BitIoTestUtils.wr1au(o -> {
            BitIoUtils.writeVlq(o, expected);
            return (a, i) -> {
                return BitIoUtils.readVlq(i);
            };
        });
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void vlqLong__106903() throws IOException {
        final long expected = 106903L;
        final long actual = BitIoTestUtils.wr1au(o -> {
            BitIoUtils.writeVlqLong(o, expected);
            return (a, i) -> {
                return BitIoUtils.readVlqLong(i);
            };
        });
        assertThat(actual).isEqualTo(expected);
    }

    @MethodSource({"getLongStream"})
    @ParameterizedTest
    void vlqLong__(final long expected) throws IOException {
        final long actual = BitIoTestUtils.wr1au(o -> {
            BitIoUtils.writeVlqLong(o, expected);
            return (a, i) -> {
                return BitIoUtils.readVlqLong(i);
            };
        });
        assertThat(actual).isEqualTo(expected);
    }
}
