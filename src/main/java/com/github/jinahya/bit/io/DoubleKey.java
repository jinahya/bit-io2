package com.github.jinahya.bit.io;

import java.util.Objects;

final class DoubleKey {

    static DoubleKey withSignificandSizeOnly(final int significandSize) {
        return new DoubleKey(DoubleConstants.SIZE_MIN_EXPONENT, significandSize);
    }

    DoubleKey(final int exponentSize, final int significandSize) {
        super();
        this.exponentSize = DoubleConstraints.requireValidExponentSize(exponentSize);
        this.significandSize = DoubleConstraints.requireValidSignificandSize(significandSize);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DoubleKey that = (DoubleKey) obj;
        return exponentSize == that.exponentSize && significandSize == that.significandSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(exponentSize, significandSize);
    }

    final int exponentSize;

    final int significandSize;
}
