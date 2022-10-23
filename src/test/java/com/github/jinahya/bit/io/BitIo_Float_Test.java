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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.stream.Stream;

import static com.github.jinahya.bit.io.BitIoTestUtils.wr1u;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

@Slf4j
class BitIo_Float_Test {

    private static Stream<Arguments> sizesArgumentsStream() {
        return FloatTestParameters.sizesArgumentsStream();
    }

    private static Stream<Arguments> sizesAndValuesArgumentsStream() {
        return FloatTestParameters.sizesAndValuesArgumentsStream();
    }

    @MethodSource({"sizesArgumentsStream"})
    @ParameterizedTest
    void wr__(final int exponentSize, final int significandSize) throws IOException {
        try (MockedStatic<FloatConstraints> floatConstraints
                     = Mockito.mockStatic(FloatConstraints.class, Mockito.CALLS_REAL_METHODS)) {
            final var expected = BitIoRandom.nextValueForFloat(exponentSize, significandSize);
            final var actual = wr1u(o -> {
                o.writeFloat(exponentSize, significandSize, expected);
                return i -> i.readFloat(exponentSize, significandSize);
            });
            if (Float.isNaN(expected)) {
                assertThat(actual).isNaN();
                return;
            }
            assertThat(actual).isEqualTo(expected);
            floatConstraints.verify(() -> FloatConstraints.requireValidExponentSize(exponentSize), times(2));
            floatConstraints.verify(() -> FloatConstraints.requireValidSignificandSize(significandSize), times(2));
        }
    }

    @MethodSource({"sizesAndValuesArgumentsStream"})
    @ParameterizedTest
    void wr__(final int exponentSize, final int significandSize, final float expected) throws IOException {
        final var actual = wr1u(o -> {
            o.writeFloat(exponentSize, significandSize, expected);
            return i -> i.readFloat(exponentSize, significandSize);
        });
        if (Float.isNaN(expected)) {
            assertThat(actual).isNaN();
            return;
        }
        assertThat(actual).isEqualTo(expected);
    }
}
