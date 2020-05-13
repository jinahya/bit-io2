package com.github.jinahya.bit.io;

final class BitIoConstants {

    private static final int[] MASKS = new int[8];

    static {
        int p = 2;
        for (int i = 0; i < MASKS.length; i++) {
            MASKS[i] = p - 1;
            p <<= 1;
        }
    }

    static int mask(final int size) {
        return MASKS[size - 1];
    }

    private BitIoConstants() {
        throw new AssertionError("initialization is not allowed");
    }
}
