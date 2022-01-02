package com.github.jinahya.bit.io;

import java.io.IOException;
import java.util.Objects;

/**
 * A utility class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class BitIoUtils {

    private static int log2(final int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("value(" + value + ") is not positive");
        }
        return Integer.SIZE - Integer.numberOfLeadingZeros(value);
    }

    /**
     * Returns the number of required bits for specified unsigned value.
     *
     * @param value the unsigned value whose size is calculated.
     * @return the number of required bits for {@code value}; always positive.
     */
    static int size(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("value(" + value + ") is negative");
        }
        if (value == 0) {
            return 1;
        }
        return log2(value);
    }

    static int readCount(final BitInput input, final int size) throws IOException {
        Objects.requireNonNull(input, "input is null");
        BitIoConstraints.requireValidSizeForInt(true, size);
        return input.readUnsignedInt(size);
    }

    static void writeCount(final BitOutput output, final int size, final int value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        BitIoConstraints.requireValidSizeForInt(true, size);
        output.writeUnsignedInt(size, value);
    }

    private BitIoUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
