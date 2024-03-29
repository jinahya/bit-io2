package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 Jinahya, Inc.
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for testing {@link DoubleReader.CompressedZero}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class DoubleReader_CompressedZero_Test
        extends BitReaderTest<DoubleReader.CompressedZero, Double> {

    DoubleReader_CompressedZero_Test() {
        super(DoubleReader.CompressedZero.class, Double.class);
    }

    @Test
    void nullable__() {
        assertThat(DoubleReader.CompressedZero.getInstance().nullable())
                .isNotNull()
                .isSameAs(DoubleReader.CompressedZero.getInstanceNullable());
    }
}
