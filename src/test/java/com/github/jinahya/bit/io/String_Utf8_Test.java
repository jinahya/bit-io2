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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.DefaultArgumentsAccessor;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static com.github.jinahya.bit.io.BitIoTestUtils.wr2u;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class String_Utf8_Test {

    static Stream<String> randomValueStream() {
        return ByteArray_Utf8_Test.randomBytesAndLengthSizeStream()
                .map(a -> new DefaultArgumentsAccessor(a.get()).get(0, byte[].class))
                .map(v -> new String(v, StandardCharsets.UTF_8))
                ;
    }

    @MethodSource({"randomValueStream"})
    @ParameterizedTest
    void test(final String expected) throws IOException {
        wr2u(o -> {
            final var writer = StringWriter.utf8();
            o.writeObject(writer, expected);
            return i -> {
                final var reader = StringReader.utf8();
                final var actual = i.readObject(reader);
                assertThat(actual).isEqualTo(expected);
            };
        });
    }
}
