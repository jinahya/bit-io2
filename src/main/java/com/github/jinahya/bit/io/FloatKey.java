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

import java.util.Objects;

class FloatKey {

    static FloatKey of(final int exponentSize, final int significandSize) {
        return new FloatKey(exponentSize, significandSize);
    }

    static FloatKey withSignificandSizeOnly(final int significandSize) {
        return new FloatKey(FloatConstants.SIZE_MIN_EXPONENT, significandSize) {
            @Override
            int getExponentSize() {
                throw new IllegalStateException("significand size only");
            }
        };
    }

    private FloatKey(final int exponentSize, final int significandSize) {
        super();
        this.exponentSize = FloatConstraints.requireValidExponentSize(exponentSize);
        this.significandSize = FloatConstraints.requireValidSignificandSize(significandSize);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FloatKey that = (FloatKey) obj;
        return exponentSize == that.exponentSize && significandSize == that.significandSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(exponentSize, significandSize);
    }

    int getExponentSize() {
        return exponentSize;
    }

    int getSignificandSize() {
        return significandSize;
    }

    private final int exponentSize;

    private final int significandSize;
}
