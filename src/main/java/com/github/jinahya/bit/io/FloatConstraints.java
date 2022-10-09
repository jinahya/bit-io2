package com.github.jinahya.bit.io;

final class FloatConstraints {

    static int requireValidExponentSize(final int exponentSize) {
        if (exponentSize < FloatConstants.SIZE_MIN_EXPONENT) {
            throw new IllegalArgumentException(
                    "exponentSize(" + exponentSize + ") < " + FloatConstants.SIZE_MIN_EXPONENT);
        }
        if (exponentSize > FloatConstants.SIZE_EXPONENT) {
            throw new IllegalArgumentException(
                    "exponentSize(" + exponentSize + ") > " + FloatConstants.SIZE_EXPONENT);
        }
        return exponentSize;
    }

    static int requireValidSignificandSize(final int significandSize) {
        if (significandSize < FloatConstants.SIZE_MIN_SIGNIFICAND) {
            throw new IllegalArgumentException(
                    "significandSize(" + significandSize + ") < " + FloatConstants.SIZE_MIN_SIGNIFICAND);
        }
        if (significandSize > FloatConstants.SIZE_SIGNIFICAND) {
            throw new IllegalArgumentException(
                    "significandSize(" + significandSize + ") > " + FloatConstants.SIZE_SIGNIFICAND);
        }
        return significandSize;
    }

    private FloatConstraints() {
        throw new AssertionError("instantiation is not allowed");
    }
}
