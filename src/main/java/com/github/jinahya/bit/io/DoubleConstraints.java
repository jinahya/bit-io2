package com.github.jinahya.bit.io;

final class DoubleConstraints {

    static int requireValidExponentSize(final int exponentSize) {
        if (exponentSize < DoubleConstants.SIZE_MIN_EXPONENT) {
            throw new IllegalArgumentException(
                    "exponentSize(" + exponentSize + ") < " + DoubleConstants.SIZE_MIN_EXPONENT);
        }
        if (exponentSize > DoubleConstants.SIZE_EXPONENT_IEEE754) {
            throw new IllegalArgumentException(
                    "exponentSize(" + exponentSize + ") > " + DoubleConstants.SIZE_EXPONENT_IEEE754);
        }
        return exponentSize;
    }

    static int requireValidSignificandSize(final int significandSize) {
        if (significandSize < DoubleConstants.SIZE_MIN_SIGNIFICAND) {
            throw new IllegalArgumentException(
                    "significandSize(" + significandSize + ") < " + DoubleConstants.SIZE_MIN_SIGNIFICAND);
        }
        if (significandSize > DoubleConstants.SIZE_SIGNIFICAND_IEEE754) {
            throw new IllegalArgumentException(
                    "significandSize(" + significandSize + ") > " + DoubleConstants.SIZE_SIGNIFICAND_IEEE754);
        }
        return significandSize;
    }

    private DoubleConstraints() {
        throw new AssertionError("instantiation is not allowed");
    }
}
