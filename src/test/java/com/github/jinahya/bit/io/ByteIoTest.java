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
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
abstract class ByteIoTest<O extends ByteOutput, I extends ByteInput> {

    protected abstract O newOutput(final int bytes);

    protected abstract I newInput(final byte[] bytes);

    @Test
    void writeAndRead() throws IOException {
        final int count = ThreadLocalRandom.current().nextInt(1024);
        final byte[] expected = new byte[count];
        for (int i = 0; i < count; i++) {
            expected[i] = (byte) (ThreadLocalRandom.current().nextInt() & 0xFF);
        }
        final O output = newOutput(expected.length);
        for (final byte b : expected) {
            output.write(b & 0xFF);
        }
        final I input = newInput(expected);
        final byte[] actual = new byte[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = (byte) input.read();
        }
        assertThat(actual).isEqualTo(expected);
    }
}
