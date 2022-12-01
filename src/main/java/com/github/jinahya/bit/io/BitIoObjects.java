package com.github.jinahya.bit.io;

import java.util.Objects;

final class BitIoObjects {

    public static Object requireNonNullInput(final BitInput input) {
        return Objects.requireNonNull(input, BitIoConstants.MESSAGE_INPUT_IS_NULL);
    }

    public static Object requireNonNullOutput(final BitOutput output) {
        return Objects.requireNonNull(output, BitIoConstants.MESSAGE_OUTPUT_IS_NULL);
    }

    public static Object requireNonNullReader(final BitReader reader) {
        return Objects.requireNonNull(reader, BitIoConstants.MESSAGE_READER_IS_NULL);
    }

    public static Object requireNonNullWriter(final BitWriter writer) {
        return Objects.requireNonNull(writer, BitIoConstants.MESSAGE_WRITER_IS_NULL);
    }

    public static Object requireNonNullValue(final Object value) {
        return Objects.requireNonNull(value, BitIoConstants.MESSAGE_VALUE_IS_NULL);
    }

    public static int requireNonNegativeValue(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("negative value: " + value);
        }
        return value;
    }

    public static long requireNonNegativeValue(final long value) {
        if (value < 0L) {
            throw new IllegalArgumentException("negative value: " + value);
        }
        return value;
    }

    private BitIoObjects() {
        throw new AssertionError("instantiation is not allowed");
    }
}
