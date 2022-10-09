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

@Slf4j
final class DoubleTestConstants {

    static final long POSITIVE_ZERO_BITS
            = 0b0__00000000_000__00000000_00000000_00000000_00000000_00000000_00000000_0000L;

    static {
        assert POSITIVE_ZERO_BITS == Double.doubleToRawLongBits(.0d);
    }

    static final long NEGATIVE_ZERO_BITS
            = 0b1__00000000_000__00000000_00000000_00000000_00000000_00000000_00000000_0000L;

    static {
        assert NEGATIVE_ZERO_BITS == Double.doubleToRawLongBits(.0d);
    }

    static final long POSITIVE_INFINITY_BITS
            = 0b0__11111111_111__00000000_00000000_00000000_00000000_00000000_00000000_0000L;

    static {
        assert POSITIVE_INFINITY_BITS == Double.doubleToRawLongBits(Double.POSITIVE_INFINITY);
    }

    static final long NEGATIVE_INFINITY_BITS
            = 0b1__11111111_111__00000000_00000000_00000000_00000000_00000000_00000000_0000L;

    static {
        assert NEGATIVE_INFINITY_BITS == Double.doubleToRawLongBits(Double.NEGATIVE_INFINITY);
    }

    private DoubleTestConstants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
