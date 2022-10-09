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

final class DoubleConstants {

    // -----------------------------------------------------------------------------------------------------------------
    static final int SIZE_SIGNIFICAND = 52;

    static final int SIZE_MIN_SIGNIFICAND = 2;

//    static final long MASK_SIGNIFICAND = 0b0__00000000_000__11111111_11111111_11111111_11111111_11111111_11111111_1111L;

    // -----------------------------------------------------------------------------------------------------------------
    static final int SIZE_EXPONENT = 11;

    static final int SIZE_MIN_EXPONENT = 1;

    static final long MASK_EXPONENT = 0b0__11111111_111__00000000_00000000_00000000_00000000_00000000_00000000_0000L;

//    static final int SHIFT_EXPONENT = SIZE_SIGNIFICAND;

    // -----------------------------------------------------------------------------------------------------------------
    static final int SHIFT_SIGN_BIT = Long.SIZE - 1;

    private DoubleConstants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
