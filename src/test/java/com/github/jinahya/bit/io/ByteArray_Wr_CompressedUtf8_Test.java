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
import org.junit.jupiter.api.Test;
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
class ByteArray_CompressedUtf8_Wr_Test {

    static byte[] randomBytes() {
        final int length = ThreadLocalRandom.current().nextInt(1024);
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
    void wr__(final byte[] expected, final int lengthSize) throws IOException {
        final var baos = new ByteArrayOutputStream();
        final var output = BitOutputFactory.from(baos);
        final var writer = ByteArrayWriter.compressedUtf8();
        writer.write(output, expected);
        final var padded = output.align(1);
        if (false) {
            final var given = expected.length + Integer.BYTES;
            log.debug("given: {}, written: {}, rate: {}", given, baos.size(), (baos.size() / (double) given) * 100.0d);
        }
        final var bais = new ByteArrayInputStream(baos.toByteArray());
        final var input = BitInputFactory.from(bais);
        final var reader = ByteArrayReader.compressedUtf8();
        final var actual = reader.read(input);
        final var discarded = input.align(1);
        assertThat(actual).isEqualTo(expected);
        assertThat(discarded).isEqualTo(padded);
    }

    @Test
    void wr__nullable() throws IOException {
        final int lengthSize = Integer.SIZE - 1;
        final var baos = new ByteArrayOutputStream();
        final var output = BitOutputFactory.from(baos);
        final var writer = ByteArrayWriter.compressedUtf8().nullable();
        writer.write(output, null);
        final var padded = output.align(1);
        final var bais = new ByteArrayInputStream(baos.toByteArray());
        final var input = BitInputFactory.from(bais);
        final var reader = ByteArrayReader.compressedUtf8().nullable();
        final var actual = reader.read(input);
        final var discarded = input.align(1);
        assertThat(actual).isNull();
        assertThat(discarded).isEqualTo(padded);
    }
}
