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

/**
 * Constants for {@code float} values
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DoubleConstants
 */
final class FloatConstants {

    static final int SIZE_MIN_SIGNIFICAND = 1;

    static final int SIZE_SIGNIFICAND = 23;

    static final int MASK_SIGNIFICAND = 0b0__00000000__11111111_11111111_1111_111;

    static final int SHIFT_SIGNIFICAND_LEFT_MOST_BIT = SIZE_SIGNIFICAND - 1;

    static final int MASK_SIGNIFICAND_LEFT_MOST_BIT = 0b0__00000000__10000000_00000000_0000_000;

    static final int SIZE_MIN_EXPONENT = 1;

    static final int SIZE_EXPONENT = 8;

    static final int MASK_EXPONENT = 0b0__11111111__00000000_00000000_0000_000;

    static final int SHIFT_SIGN_BIT = SIZE_SIGNIFICAND + SIZE_EXPONENT;

    private FloatConstants() {
        throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
