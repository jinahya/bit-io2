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
import org.junit.jupiter.api.Nested;

/**
 * A class for testing {@link DoubleReader.CompressedNaN}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class DoubleReader_CompressedNaN_SignificandOnly_Test
        extends BitReaderTest<DoubleReader.CompressedNaN, Double> {

    DoubleReader_CompressedNaN_SignificandOnly_Test() {
        super(DoubleReader.CompressedNaN.class, Double.class);
    }

    @Nested
    class IsSignificandOnlyTest {

//        @IntRangeSource(from = DoubleConstants.SIZE_MIN_SIGNIFICAND, to = DoubleConstants.SIZE_SIGNIFICAND)
//        @ParameterizedTest
//        void isSignificandOnly_False_NewInstance(final int significandSize) {
//            assertThat(new DoubleReader.CompressedNaN(significandSize).isSignificandOnly()).isFalse();
//        }
    }

    @Nested
    class SetSignificandOnlyTest {

//        @IntRangeSource(from = DoubleConstants.SIZE_MIN_SIGNIFICAND, to = DoubleConstants.SIZE_SIGNIFICAND)
//        @ParameterizedTest(name = "[{index}] ({0})setSignificandOnly(true)")
//        void setSignificandOnly__True(final int significandSize) {
//            new DoubleReader.CompressedNaN(significandSize).significandOnly(true);
//        }
//
//        @IntRangeSource(from = DoubleConstants.SIZE_MIN_SIGNIFICAND, to = DoubleConstants.SIZE_SIGNIFICAND)
//        @ParameterizedTest(name = "[{index}] ({0})setSignificandOnly(false)")
//        void setSignificandOnly__False(final int significandSize) {
//            new DoubleReader.CompressedNaN(significandSize).significandOnly(false);
//        }
    }
}
