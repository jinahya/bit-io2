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
final class FloatTestConstants {

    static final int POSITIVE_ZERO_BITS = 0b0__00000000___00000000_00000000_0000_000;

    static {
        assert POSITIVE_ZERO_BITS == Float.floatToIntBits(+.0f);
    }

    static final int NEGATIVE_ZERO_BITS = 0b1__00000000___00000000_00000000_0000_000;

    static {
        assert NEGATIVE_ZERO_BITS == Float.floatToIntBits(-.0f);
    }

    static final int POSITIVE_INFINITY_BITS = 0b0__11111111___00000000_00000000_0000_000;

    static {
        assert POSITIVE_INFINITY_BITS == Float.floatToRawIntBits(Float.POSITIVE_INFINITY);
    }

    static final int NEGATIVE_INFINITY_BITS = 0b1__11111111___00000000_00000000_0000_000;

    static {
        assert NEGATIVE_INFINITY_BITS == Float.floatToRawIntBits(Float.NEGATIVE_INFINITY);
    }

    private FloatTestConstants() {
        throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
