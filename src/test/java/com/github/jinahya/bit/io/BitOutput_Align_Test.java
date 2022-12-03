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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({MockitoExtension.class})
class BitOutput_Align_Test {

    @Test
    void _IllegalArgumentException_BytesZero() throws IOException {
        assertThatThrownBy(() -> output.align(0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void _IllegalArgumentException_BytesNegative() throws IOException {
        final var bytes = ThreadLocalRandom.current().nextInt() | Integer.MIN_VALUE;
        assertThatThrownBy(() -> output.align(0)).isInstanceOf(IllegalArgumentException.class);
    }

    @ValueSource(ints = {1, 2, 4, 8, 16})
    @ParameterizedTest
    void __(final int bytes) throws IOException {
        output.align(bytes);
    }

    private final ByteOutputAdapter output = new ByteOutputAdapter(mock(ByteOutput.class));
}
