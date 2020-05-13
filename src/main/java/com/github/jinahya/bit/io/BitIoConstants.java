package com.github.jinahya.bit.io;

/**
 * Defines constants for bit-io.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;\
 */
final class BitIoConstants {

    /**
     * An array of pre-initialized bit masks.
     */
    private static final int[] MASKS = new int[8];

    static {
        int p = 2;
        for (int i = 0; i < MASKS.length; i++) {
            MASKS[i] = p - 1;
            p <<= 1;
        }
    }

    /**
     * Returns a bit mask for specified bit size.
     *
     * @param size the number of bits to mask.
     * @return a bit mask.
     */
    static int mask(final int size) {
        return MASKS[size - 1];
    }

    /**
     * Creates a new instance.
     */
    private BitIoConstants() {
        throw new AssertionError("initialization is not allowed");
    }
}
