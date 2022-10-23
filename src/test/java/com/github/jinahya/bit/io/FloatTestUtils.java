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
final class FloatTestUtils {

    static String formatBinary(final float value) {
        final int bits = Float.floatToRawIntBits(value);
        return "0b"
               + Integer.toBinaryString(bits >>> FloatConstants.SHIFT_SIGN_BIT)
               + '_'
               + String.format("%1$8s",
                               Integer.toBinaryString(
                                       (bits & FloatConstants.MASK_EXPONENT) >> FloatConstants.SIZE_SIGNIFICAND
                               ))
               + '_'
               + String.format("%1$23s", Integer.toBinaryString((bits & FloatConstants.MASK_SIGNIFICAND)))
                ;
    }

    private FloatTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
