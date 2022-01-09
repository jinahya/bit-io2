package com.github.jinahya.bit.io;

import static java.util.concurrent.ThreadLocalRandom.current;

final class ByteInputTestUtilities {

    static ByteInput white() {
        return () -> current().nextInt(255);
    }

    private ByteInputTestUtilities() {
        throw new AssertionError("instantiation is not allowed");
    }
}
