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

final class TestUtils {

    static String printBinary(final int value) {
        return String.format("%32s", Integer.toBinaryString(value));
    }

    static String printBinary(final float value) {
        return printBinary(Float.floatToRawIntBits(value));
    }

    static String printBinary(final long value) {
        return String.format("%64s", Long.toBinaryString(value));
    }

    static String printBinary(final double value) {
        return printBinary(Double.doubleToRawLongBits(value));
    }

    private TestUtils() {
        throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
