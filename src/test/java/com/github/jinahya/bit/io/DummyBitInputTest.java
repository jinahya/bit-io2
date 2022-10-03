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
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith({DummyBitInputParameterResolver.class})
@Slf4j
class DummyBitInputTest {

    @Test
    void readBoolean__(final BitInput input) throws IOException {
        final var value = input.readBoolean();
        verify(input, times(1)).readBoolean();
        verify(input, times(1)).readInt(true, 1);
        verify(input, times(1)).readInt(true, 1);
        verifyNoMoreInteractions(input);
    }

//    @Test
//    void readByte__(final BitInput input) throws IOException {
//        final var size = BitIoRandomValues.randomSizeForByte();
//        final var value = input.readByte(size);
//        verify(input, times(1)).readByte(size);
//        verify(input, times(1)).readByte(false, size);
//        verify(input, times(1)).readInt(false, size);
//        verifyNoMoreInteractions(input);
//    }
}
