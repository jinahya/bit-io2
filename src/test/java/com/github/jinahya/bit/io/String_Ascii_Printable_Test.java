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
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static com.github.jinahya.bit.io.BitIoTestUtils.wr1u;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class String_Ascii_Printable_Test {

    static Stream<Arguments> randomBytesAndMaximumCharactersStream() {
        return ByteArray_Ascii_Printable_Test.randomBytesAndLengthSizeStream()
                .map(a -> {
                    final DefaultArgumentsAccessor accessor = new DefaultArgumentsAccessor(a.get());
                    final byte[] randomBytes = accessor.get(0, byte[].class);
                    final Integer lengthSize = accessor.getInteger(1);
                    final int maximumCharacters = (int) Math.pow(2, lengthSize);
                    return Arguments.of(new String(randomBytes, StandardCharsets.US_ASCII), maximumCharacters);
                });
    }

    @MethodSource({"randomBytesAndMaximumCharactersStream"})
    @ParameterizedTest
    void test(final String expected, final int maximumCharacters) throws IOException {
        wr1u(o -> {
            final var writer = StringWriter.ascii(true);
            o.writeObject(writer, expected);
            return i -> {
                final var reader = StringReader.ascii(true);
                final var actual = i.readObject(reader);
                assertThat(actual).isEqualTo(expected);
                return null;
            };
        });
    }
}
