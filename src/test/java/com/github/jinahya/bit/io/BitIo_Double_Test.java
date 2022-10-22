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

import java.io.IOException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.jinahya.bit.io.BitIoTestUtils.wr1u;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class BitIo_Double_Test {

    private static Stream<Arguments> sizes() {
        return Stream.concat(
                IntStream.range(0, 16)
                        .mapToObj(i -> Arguments.of(BitIoRandom.nextExponentSizeForDouble(),
                                                    BitIoRandom.nextSignificandSizeForDouble())),
                Stream.of(Arguments.of(DoubleConstants.SIZE_MAX_EXPONENT, DoubleConstants.SIZE_MAX_SIGNIFICAND)
                ));
    }

    @MethodSource({"sizes"})
    @ParameterizedTest
    void wr__(final int exponentSize, final int significandSize) throws IOException {
        final var expected = BitIoRandom.nextValueForDouble(exponentSize, significandSize);
        final var actual = wr1u(o -> {
            o.writeDouble(exponentSize, significandSize, expected);
            return i -> i.readDouble(exponentSize, significandSize);
        });
        if (Double.isNaN(expected)) {
            assertThat(actual).isNaN();
            return;
        }
        assertThat(actual).isEqualTo(expected);
    }
}
