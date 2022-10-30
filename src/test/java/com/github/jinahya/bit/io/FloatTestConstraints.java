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

final class FloatTestConstraints {

    static void assertSignBitsAreSame(final float value1, final float value2) {
        assertThat(Float.floatToRawIntBits(value1) >> FloatConstants.SHIFT_SIGN_BIT)
                .isEqualTo(Float.floatToRawIntBits(value2) >> FloatConstants.SHIFT_SIGN_BIT);
    }

    static int requireExponentZero(final int bits) {
        final var masked = bits & FloatConstants.MASK_EXPONENT;
        if (masked > 0) {
            throw new IllegalArgumentException("value.exponent is not zero: " + TestUtils.printBinary(bits));
        }
        return bits;
    }

    static float requireExponentZero(final float value) {
        return Float.intBitsToFloat(requireExponentZero(Float.floatToRawIntBits(value)));
    }

    static int requireExponentNotZero(final int bits) {
        final var masked = bits & FloatConstants.MASK_EXPONENT;
        if (masked == 0) {
            throw new IllegalArgumentException("value.exponent is zero: " + TestUtils.printBinary(bits));
        }
        return bits;
    }

    static float requireExponentNotZero(final float value) {
        return Float.intBitsToFloat(requireExponentNotZero(Float.floatToRawIntBits(value)));
    }

    static int requireSignificandZero(final int bits) {
        final var masked = bits & FloatConstants.MASK_SIGNIFICAND;
        if (masked > 0) {
            throw new IllegalArgumentException("value.significand is not zero: " + TestUtils.printBinary(bits));
        }
        return bits;
    }

    static float requireSignificandZero(final float value) {
        return Float.intBitsToFloat(requireSignificandZero(Float.floatToRawIntBits(value)));
    }

    static int requireSignificandNotZero(final int bits) {
        final var masked = bits & FloatConstants.MASK_SIGNIFICAND;
        if (masked == 0) {
            throw new IllegalArgumentException("value.significand is zero: " + TestUtils.printBinary(bits));
        }
        return bits;
    }

    static float requireSignificandNotZero(final float value) {
        return Float.intBitsToFloat(requireSignificandNotZero(Float.floatToRawIntBits(value)));
    }

    static float requireZero(final float value) {
        requireExponentZero(value);
        requireSignificandZero(value);
        return value;
    }

    static float requireSubnormal(final float value) {
        requireExponentZero(value);
        requireSignificandNotZero(value);
        return value;
    }

    private FloatTestConstraints() {
        throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
