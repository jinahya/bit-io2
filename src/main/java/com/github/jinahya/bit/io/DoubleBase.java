package com.github.jinahya.bit.io;

abstract class DoubleBase {

    DoubleBase(final int exponentSize, final int significandSize) {
        super();
        this.exponentSize = DoubleConstraints.requireValidExponentSize(exponentSize);
        this.significandSize = DoubleConstraints.requireValidExponentSize(significandSize);
    }

    final int exponentSize;

    final int significandSize;
}
