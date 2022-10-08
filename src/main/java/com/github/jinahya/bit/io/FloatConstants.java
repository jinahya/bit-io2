package com.github.jinahya.bit.io;

final class FloatConstants {

    static final int IEEE754_EXPONENT_SIZE = 8;

    static final int IEEE754_EXPONENT_MASK = BitIoUtils.bitMask(IEEE754_EXPONENT_SIZE);

    static final int MIN_EXPONENT_SIZE = 1;

    static final int MAX_EXPONENT_SIZE = IEEE754_EXPONENT_SIZE;

    static final int IEEE754_SIGNIFICAND_PRECISION_SIZE = 23;

    static final int IEEE754_SIGNIFICAND_PRECISION_MASK = BitIoUtils.bitMask(IEEE754_SIGNIFICAND_PRECISION_SIZE);

    static final int MIN_SIGNIFICAND_PRECISION_SIZE = 1;

    static final int MAX_SIGNIFICAND_PRECISION_SIZE = IEEE754_SIGNIFICAND_PRECISION_SIZE;

    private FloatConstants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
