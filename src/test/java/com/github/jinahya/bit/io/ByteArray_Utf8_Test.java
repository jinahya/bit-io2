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
import org.apache.commons.text.RandomStringGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ByteArray_Utf8_Test {

    static byte[] randomBytes() {
        final int length = ThreadLocalRandom.current().nextInt(128);
        return new RandomStringGenerator.Builder().build().generate(length).getBytes(StandardCharsets.UTF_8);
    }

    static Stream<byte[]> randomBytesStream() {
        return IntStream.range(0, 16)
                .mapToObj(i -> randomBytes());
    }

    static Stream<Arguments> randomBytesAndLengthSizeStream() {
        return randomBytesStream()
                .map(b -> Arguments.of(b, BitIoUtils.size(b.length)));
    }

    @MethodSource({"randomBytesAndLengthSizeStream"})
    @ParameterizedTest
    void utf8__(final byte[] expected, final int lengthSize) throws IOException {
        final var baos = new ByteArrayOutputStream();
        final var output = ByteOutputAdapter.from(baos);
        final var writer = ByteArrayWriter.utf8(lengthSize);
        writer.write(output, expected);
        final var padded = output.align(1);
        log.debug("given: {}, written: {}, rate: {}", expected.length, baos.size(),
                  (baos.size() / (double) expected.length) * 100.0d);
        final var bais = new ByteArrayInputStream(baos.toByteArray());
        final var input = ByteInputAdapter.from(bais);
        final var reader = ByteArrayReader.utf8(lengthSize);
        final var actual = reader.read(input);
        final var discarded = input.align(1);
        assertThat(actual).isEqualTo(expected);
        assertThat(discarded).isEqualTo(padded);
    }
}
