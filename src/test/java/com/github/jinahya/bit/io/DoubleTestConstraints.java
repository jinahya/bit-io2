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

import static org.assertj.core.api.Assertions.assertThat;

final class DoubleTestConstraints {

    static void assertSameSignBit(final double value1, final double value2) {
        assertThat(Double.doubleToRawLongBits(value1) >> DoubleConstants.SHIFT_SIGN_BIT)
                .isEqualTo(Double.doubleToRawLongBits(value2) >> DoubleConstants.SHIFT_SIGN_BIT);
    }

    static long requireExponentZero(final long bits) {
        final var masked = bits & DoubleConstants.MASK_EXPONENT;
        if (masked > 0) {
            throw new IllegalArgumentException("value.exponent is not zero: " + TestUtils.printBinary(bits));
        }
        return bits;
    }

    static double requireExponentZero(final double value) {
        return Double.longBitsToDouble(requireExponentZero(Double.doubleToRawLongBits(value)));
    }

    static long requireExponentNotZero(final long bits) {
        final var masked = bits & DoubleConstants.MASK_EXPONENT;
        if (masked == 0) {
            throw new IllegalArgumentException("value.exponent is zero: " + TestUtils.printBinary(bits));
        }
        return bits;
    }

    static double requireExponentNotZero(final double value) {
        return Double.longBitsToDouble(requireExponentNotZero(Double.doubleToRawLongBits(value)));
    }

    static long requireSignificandZero(final long bits) {
        final var masked = bits & DoubleConstants.MASK_SIGNIFICAND;
        if (masked > 0) {
            throw new IllegalArgumentException("value.significand is not zero: " + TestUtils.printBinary(bits));
        }
        return bits;
    }

    static double requireSignificandZero(final double value) {
        return Double.longBitsToDouble(requireSignificandZero(Double.doubleToRawLongBits(value)));
    }

    static long requireSignificandNotZero(final long bits) {
        final var masked = bits & DoubleConstants.MASK_SIGNIFICAND;
        if (masked == 0) {
            throw new IllegalArgumentException("value.significand is zero: " + TestUtils.printBinary(bits));
        }
        return bits;
    }

    static double requireSignificandNotZero(final double value) {
        return Double.longBitsToDouble(requireSignificandNotZero(Double.doubleToRawLongBits(value)));
    }

    static double requireZero(final double value) {
        requireExponentZero(value);
        requireSignificandZero(value);
        return value;
    }

    static double requireSubnormal(final double value) {
        requireExponentZero(value);
        requireSignificandNotZero(value);
        return value;
    }

    private DoubleTestConstraints() {
        throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
