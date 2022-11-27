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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FilterBitReader_Nullable_Test {

    @Test
    void nullable__() {
        final BitReader<String> nullable = FilterBitReader.nullable(StringReader.compressedAscii(true));
        assertThatThrownBy(nullable::nullable)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage(BitIoConstants.MESSAGE_UNSUPPORTED_ALREADY_NULLABLE);
        assertThatThrownBy(() -> FilterBitReader.nullable(nullable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
