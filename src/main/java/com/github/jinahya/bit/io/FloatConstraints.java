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

final class FloatConstraints {

    static int requireValidExponentSize(final int size) {
        if (size < FloatConstants.SIZE_MIN_EXPONENT) {
            throw new IllegalArgumentException("size(" + size + ") < " + FloatConstants.SIZE_MIN_EXPONENT);
        }
        if (size > FloatConstants.SIZE_EXPONENT) {
            throw new IllegalArgumentException("size(" + size + ") > " + FloatConstants.SIZE_EXPONENT);
        }
        return size;
    }

    static int requireValidSignificandSize(final int size) {
        if (size < FloatConstants.SIZE_MIN_SIGNIFICAND) {
            throw new IllegalArgumentException("size(" + size + ") < " + FloatConstants.SIZE_MIN_SIGNIFICAND);
        }
        if (size > FloatConstants.SIZE_SIGNIFICAND) {
            throw new IllegalArgumentException("size(" + size + ") > " + FloatConstants.SIZE_SIGNIFICAND);
        }
        return size;
    }

    private FloatConstraints() {
        throw new AssertionError("instantiation is not allowed");
    }
}
