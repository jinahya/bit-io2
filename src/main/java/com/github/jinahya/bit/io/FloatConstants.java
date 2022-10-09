package com.github.jinahya.bit.io;

final class FloatConstants {

    static final int SIZE_SIGNIFICAND = 23;

    static final int SIZE_MIN_SIGNIFICAND = 1;

    static final int MASK_SIGNIFICAND = 0b0__00000000__11111111_11111111_1111_111;

    // -----------------------------------------------------------------------------------------------------------------
    static final int SIZE_EXPONENT = 8;

    static final int SIZE_MIN_EXPONENT = 1;

    static final int MASK_EXPONENT = 0b0__11111111__00000000_00000000_0000_000;

    static final int SHIFT_EXPONENT = SIZE_SIGNIFICAND;

    // -----------------------------------------------------------------------------------------------------------------
    static final int SIZE_SIGN_BIT = 1;

    static final int MASK_SIGN_BIT = 0b1__00000000__00000000_00000000_0000_000;

    static final int SHIFT_SIGN_BIT = Integer.SIZE - SIZE_SIGN_BIT;

    private FloatConstants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
