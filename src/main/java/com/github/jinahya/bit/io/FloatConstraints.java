package com.github.jinahya.bit.io;

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
