package com.github.jinahya.bit.io;

abstract class FloatBase {

    FloatBase(final int exponentSize, final int significandPrecisionSize) {
        super();
        this.exponentSize = FloatConstraints.requireValidExponentSize(exponentSize);
        this.significandPrecisionSize = FloatConstraints.requireValidExponentSize(significandPrecisionSize);
    }

    final int exponentSize;

    final int significandPrecisionSize;
}
