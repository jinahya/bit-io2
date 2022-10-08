package com.github.jinahya.bit.io;

final class FloatConstraints {

    static int requireValidExponentSize(final int exponentSize) {
        if (exponentSize < FloatConstants.MIN_EXPONENT_SIZE) {
            throw new IllegalArgumentException(
                    "exponentSize(" + exponentSize + ") < " + FloatConstants.MIN_EXPONENT_SIZE);
        }
        if (exponentSize > FloatConstants.MAX_EXPONENT_SIZE) {
            throw new IllegalArgumentException(
                    "exponentSize(" + exponentSize + ") > " + FloatConstants.MAX_EXPONENT_SIZE);
        }
        return exponentSize;
    }

    static int requireValidSignificandPrecisionSize(final int significandPrecisionSize) {
        if (significandPrecisionSize < FloatConstants.MIN_SIGNIFICAND_PRECISION_SIZE) {
            throw new IllegalArgumentException(
                    "significandPrecisionSize(" + significandPrecisionSize + ") < "
                    + FloatConstants.MIN_SIGNIFICAND_PRECISION_SIZE);
        }
        if (significandPrecisionSize > FloatConstants.MAX_SIGNIFICAND_PRECISION_SIZE) {
            throw new IllegalArgumentException(
                    "significandPrecisionSize(" + significandPrecisionSize + ") > "
                    + FloatConstants.MAX_SIGNIFICAND_PRECISION_SIZE);
        }
        return significandPrecisionSize;
    }

    private FloatConstraints() {
        throw new AssertionError("instantiation is not allowed");
    }
}
