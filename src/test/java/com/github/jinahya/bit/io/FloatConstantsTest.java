package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FloatConstantsTest {

    @Test
    void test1() {
        assertThat(FloatConstants.MASK_SIGNIFICAND | FloatConstants.MASK_EXPONENT)
                .isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    void test2() {
        assertThat(FloatConstants.MASK_SIGNIFICAND | FloatConstants.MASK_EXPONENT | FloatConstants.MASK_SIGN_BIT)
                .isEqualTo(-1);
    }
}
