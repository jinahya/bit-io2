package com.github.jinahya.bit.io;

import java.util.Objects;

final class BitIoObjects {

    static <T extends BitInput> T requireNonNullInput(final T input) {
        return Objects.requireNonNull(input, BitIoConstants.MESSAGE_INPUT_IS_NULL);
    }

    static <T extends BitOutput> T requireNonNullOutput(final T output) {
        return Objects.requireNonNull(output, BitIoConstants.MESSAGE_OUTPUT_IS_NULL);
    }

    static <T extends ByteInput> T requireNonNullInput(final T input) {
        return Objects.requireNonNull(input, BitIoConstants.MESSAGE_INPUT_IS_NULL);
    }

    static <T extends ByteOutput> T requireNonNullOutput(final T output) {
        return Objects.requireNonNull(output, BitIoConstants.MESSAGE_OUTPUT_IS_NULL);
    }

    static <T extends BitReader<?>> T requireNonNullReader(final T reader) {
        return Objects.requireNonNull(reader, BitIoConstants.MESSAGE_READER_IS_NULL);
    }

    static <T extends BitWriter<?>> T requireNonNullWriter(final T writer) {
        return Objects.requireNonNull(writer, BitIoConstants.MESSAGE_WRITER_IS_NULL);
    }

    static Object requireNonNullValue(final Object value) {
        return Objects.requireNonNull(value, BitIoConstants.MESSAGE_VALUE_IS_NULL);
    }

    private BitIoObjects() {
        throw new AssertionError("instantiation is not allowed");
    }
}
