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

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing {@link FloatWriter#getCachedInstance(int, int)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class FloatWriter_CashedInstance_Test {

    private static Stream<Arguments> allSizesAndArgumentsStream() {
        return FloatTestParameters.allSizesArgumentsStream();
    }

    @MethodSource({"allSizesAndArgumentsStream"})
    @ParameterizedTest
    void test__(final int exponentSize, final int significandSize) {
        final BitWriter<Float> instance = FloatWriter.getCachedInstance(exponentSize, significandSize);
        assertThat(instance.nullable())
                .isSameAs(FloatWriter.getCachedInstance(exponentSize, significandSize).nullable());
    }
}
