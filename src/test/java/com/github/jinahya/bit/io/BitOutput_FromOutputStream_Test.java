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

import java.io.OutputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing factory methods defined in {@link BitOutput} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitInput_FromInputStream_Test
 */
class BitOutput_FromOutputStream_Test {

    @Test
    void __NullOutputStream() {
        final var outputStream = OutputStream.nullOutputStream();
        final var bitOutput = BitOutput.from(outputStream);
        assertThat(bitOutput)
                .isNotNull();
    }
}
