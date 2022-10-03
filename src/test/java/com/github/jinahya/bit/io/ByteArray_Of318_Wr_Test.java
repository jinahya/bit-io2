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

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoTestUtils.wr1u;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ByteArray_Of318_Wr_Test {

    @Test
    void of318__empty() throws IOException {
        final var expected = new byte[0];
        final var actual = wr1u(o -> {
            ByteArrayWriter.of318().write(o, expected);
            return i -> ByteArrayReader.of318().read(i);
        });
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void of318__() throws IOException {
        final var expected = new byte[current().nextInt(16)];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = BitIoTestUtils.getRandomValueForByte(false, Byte.SIZE);
        }
        final var actual = wr1u(o -> {
            ByteArrayWriter.of318().write(o, expected);
            return i -> ByteArrayReader.of318().read(i);
        });
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void of318__nullable() throws IOException {
        final var actual = wr1u(o -> {
            ByteArrayWriter.of318().nullable().write(o, null);
            return i -> ByteArrayReader.of318().nullable().read(i);
        });
        assertThat(actual).isNull();
    }
}
