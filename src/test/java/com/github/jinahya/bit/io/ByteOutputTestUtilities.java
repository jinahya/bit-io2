package com.github.jinahya.bit.io;

final class ByteOutputTestUtilities {

    static ByteOutput black() {
        return v -> {
            // does nothing
        };
    }

    private ByteOutputTestUtilities() {
        throw new AssertionError("instantiation is not allowed");
    }
}
