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
class ByteArrayUtf8Test {

    static byte[] randomBytes() {
        final int length = ThreadLocalRandom.current().nextInt(128);
        return new RandomStringGenerator.Builder().build().generate(length).getBytes(StandardCharsets.UTF_8);
    }

    static Stream<Arguments> randomBytesAndLengthSizeStream() {
        return IntStream.range(0, 8)
                .mapToObj(i -> {
                    final byte[] randomBytes = randomBytes();
                    final int lengthSize = BitIoUtils.size(randomBytes.length);
                    return Arguments.of(randomBytes, lengthSize);
                });
    }

    @MethodSource({"randomBytesAndLengthSizeStream"})
    @ParameterizedTest
    void wr__(final byte[] expected, final int lengthSize) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = BitOutputAdapter.of(StreamByteOutput.of(baos));
        final BitWriter<byte[]> writer = ByteArrayWriter.utf8(lengthSize);
        writer.write(output, expected);
        final long padded = output.align();
        output.flush();
        log.debug("given: {}, written: {}, rate: {}", expected.length, baos.size(),
                  (baos.size() / (double) expected.length) * 100.0d);
        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        final BitInput input = BitInputAdapter.of(StreamByteInput.of(bais));
        final BitReader<byte[]> reader = ByteArrayReader.utf8(lengthSize);
        final byte[] actual = reader.read(input);
        final long discarded = input.align();
        assertThat(actual).isEqualTo(expected);
        assertThat(discarded).isEqualTo(padded);
    }
}
