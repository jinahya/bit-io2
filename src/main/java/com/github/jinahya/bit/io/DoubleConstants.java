package com.github.jinahya.bit.io;

final class DoubleConstants {

    // -----------------------------------------------------------------------------------------------------------------
    static final int SIZE_SIGNIFICAND_IEEE754 = 52;

    static final int SIZE_MIN_SIGNIFICAND = 1;

    static final long MASK_SIGNIFICAND = 0b0__00000000_000__11111111_11111111_11111111_11111111_11111111_11111111_1111L;

    // -----------------------------------------------------------------------------------------------------------------
    static final int SIZE_EXPONENT_IEEE754 = 11;

    static final int SIZE_MIN_EXPONENT = 1;

    static final long MASK_EXPONENT = 0b0__11111111_111__00000000_00000000_00000000_00000000_00000000_00000000_0000L;

    static final int SHIFT_EXPONENT = SIZE_SIGNIFICAND_IEEE754;

    // -----------------------------------------------------------------------------------------------------------------
    static final int SIZE_SIGN_BIT = 1;

    static final long MASK_SIGN_BIT = 0b1_00000000_000__00000000_00000000_00000000_00000000_00000000_00000000_0000L;

    static final int SHIFT_SIGN_BIT = Long.SIZE - SIZE_SIGN_BIT;

    private DoubleConstants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
