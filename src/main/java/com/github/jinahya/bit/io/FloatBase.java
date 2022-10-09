package com.github.jinahya.bit.io;

abstract class FloatBase {

    FloatBase(final int exponentSize, final int significandSize) {
        super();
        this.exponentSize = FloatConstraints.requireValidExponentSize(exponentSize);
        this.significandSize = FloatConstraints.requireValidExponentSize(significandSize);
    }

    final int exponentSize;

    final int significandSize;
}
