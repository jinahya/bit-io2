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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@ExtendWith({MockitoExtension.class})
class BitOutput_Reset_Test {

    @Test
    void _IllegalStateException_NotAligned() throws ReflectiveOperationException {
        final Field field = ByteOutputAdapter.class.getDeclaredField("available");
        assert !field.canAccess(output);
        field.setAccessible(true);
        field.set(output, ThreadLocalRandom.current().nextInt(Byte.SIZE));
        assertThatThrownBy(output::reset).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void _DoesNotThrow_Aligned() throws ReflectiveOperationException {
        final Field field = ByteOutputAdapter.class.getDeclaredField("available");
        assert !field.canAccess(output);
        field.setAccessible(true);
        field.set(output, Byte.SIZE);
        assertThatCode(output::reset).doesNotThrowAnyException();
    }

    private final ByteOutputAdapter output = new ByteOutputAdapter(mock(ByteOutput.class));
}
