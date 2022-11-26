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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class FilterBitWriter_Nullable_Test {

    @Test
    void create__() {
        @SuppressWarnings({"unchecked"})
        final var nullable = new FilterBitWriter.Nullable<Object>(mock(BitWriter.class));
        assertThat(nullable).isNotNull();
    }

    @Test
    void nullable_Throw_AlreadyNullable() {
        @SuppressWarnings({"unchecked"})
        final var nullable = new FilterBitWriter.Nullable<Object>(mock(BitWriter.class));
        assertThatThrownBy(nullable::nullable)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage(BitIoConstants.MESSAGE_UNSUPPORTED_ALREADY_NULLABLE);
    }
}
