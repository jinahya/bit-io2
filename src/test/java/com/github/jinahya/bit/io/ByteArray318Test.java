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

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ByteArray318Test {

    @Test
    void of318__empty() throws IOException {
        final byte[] expected = new byte[0];
        final byte[] actual = BitIoTestUtils.wr1v(o -> {
            ByteArrayWriter.of318().write(o, expected);
            return i -> ByteArrayReader.of318().read(i);
        });
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void of318__() throws IOException {
        final byte[] expected = new byte[current().nextInt(16)];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = BitIoTestUtils.getRandomValueForByte(false, Byte.SIZE);
        }
        final byte[] actual = BitIoTestUtils.wr1v(o -> {
            ByteArrayWriter.of318().write(o, expected);
            return i -> ByteArrayReader.of318().read(i);
        });
        assertThat(actual).isEqualTo(expected);
    }
}
