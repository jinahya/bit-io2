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
import java.util.stream.Stream;

import static com.github.jinahya.bit.io.BitIoTestUtils.wr1u;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing {@link FloatWriter} and {@link FloatReader}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class Float_Wr_CachedInstance_Test {

    static Stream<Arguments> sizes() {
        return Float_Wr_Test.sizes();
    }

    static Stream<Arguments> sizesAndValues() {
        return Float_Wr_Test.sizesAndValuesArgumentsStream();
    }

    @MethodSource({"sizesAndValues"})
    @ParameterizedTest
    void rw__(final int exponentSize, final int significandSize, final Float value) throws IOException {
        final var actual = wr1u(o -> {
            FloatWriter.getCachedInstance(exponentSize, significandSize).write(o, value);
            return i -> FloatReader.getCachedInstance(exponentSize, significandSize).read(i);
        });
        if (value.isNaN()) {
            assertThat(actual).isNaN();
            return;
        }
        assertThat(actual).isEqualTo(value);
    }

    @MethodSource({"sizesAndValues"})
    @ParameterizedTest
    void wr__(final int exponentSize, final int significandSize, final Float value) throws IOException {
        final var actual = wr1u(o -> {
            FloatWriter.getCachedInstance(exponentSize, significandSize).nullable().write(o, value);
            return i -> FloatReader.getCachedInstance(exponentSize, significandSize).nullable().read(i);
        });
        if (value.isNaN()) {
            assertThat(actual).isNaN();
            return;
        }
        assertThat(actual).isEqualTo(value);
    }

    @MethodSource({"sizes"})
    @ParameterizedTest
    void wr_Null_Null(final int exponentSize, final int significandSize) throws IOException {
        final var actual = wr1u(o -> {
            FloatWriter.getCachedInstance(exponentSize, significandSize).nullable().write(o, null);
            return i -> FloatReader.getCachedInstance(exponentSize, significandSize).nullable().read(i);
        });
        assertThat(actual).isNull();
    }
}
