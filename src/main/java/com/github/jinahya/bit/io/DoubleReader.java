package com.github.jinahya.bit.io;

import java.io.IOException;

public class DoubleReader
        extends DoubleBase
        implements BitReader<Double> {

    static double readZero(final BitInput input) throws IOException {
        return Double.longBitsToDouble(
                input.readLong(true, 1) << (Long.SIZE - 1)
        );
    }

    public static final class Zero
            extends DoubleReader {

        private static final class InstanceHolder {

            private static final DoubleReader INSTANCE = new Zero();
        }

        public static DoubleReader getInstance() {
            return InstanceHolder.INSTANCE;
        }

        private Zero() {
            super(DoubleConstants.SIZE_MIN_EXPONENT, DoubleConstants.SIZE_MIN_SIGNIFICAND);
        }

        @Override
        public Double read(final BitInput input) throws IOException {
            return readZero(input);
        }
    }

    static long readExponent(final BitInput input, final int size) throws IOException {
        return (input.readLong(false, size) << DoubleConstants.SIZE_SIGNIFICAND_IEEE754) & DoubleConstants.MASK_EXPONENT;
    }

    static long readSignificand(final BitInput input, int size) throws IOException {
        long bits = input.readLong(true, 1) << (DoubleConstants.SIZE_SIGNIFICAND_IEEE754 - 1);
        if (--size > 0) {
            bits |= input.readLong(true, size);
        }
        return bits;
    }

    public DoubleReader(final int exponentSize, final int significandPrecisionSize) {
        super(exponentSize, significandPrecisionSize);
    }

    @Override
    public Double read(final BitInput input) throws IOException {
        long bits = readSignBit(input) << Long.SIZE - 1;
        bits |= readExponent(input, exponentSize);
        bits |= readSignificand(input, significandSize);
        return Double.longBitsToDouble(bits);
    }

    protected long readSignBit(final BitInput input) throws IOException {
        return input.readLong(true, 1);
    }

    protected long readExponentMask(final BitInput input) throws IOException {
        return readExponent(input, exponentSize);
    }

    protected long readSignificandPrecision(final BitInput input) throws IOException {
        return readSignificand(input, significandSize);
    }
}
