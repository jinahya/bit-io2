package com.github.jinahya.bit.io;

import java.util.Objects;

final class FloatKey {

    static FloatKey withSignificandSizeOnly(final int significandSize) {
        return new FloatKey(FloatConstants.SIZE_MIN_EXPONENT, significandSize);
    }

    FloatKey(final int exponentSize, final int significandSize) {
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

    final int exponentSize;

    final int significandSize;
}
