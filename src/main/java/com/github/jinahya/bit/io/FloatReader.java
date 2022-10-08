package com.github.jinahya.bit.io;

import java.io.IOException;

public class FloatReader
        extends FloatBase
        implements BitReader<Float> {

    static float readZero(final BitInput input) throws IOException {
        return Float.intBitsToFloat(
                input.readInt(true, 1) << (Integer.SIZE - 1)
        );
    }

    public static final class Zero
            extends FloatReader {

        private static final class InstanceHolder {

            private static final FloatReader INSTANCE = new Zero();
        }

        public static FloatReader getInstance() {
            return InstanceHolder.INSTANCE;
        }

        private Zero() {
            super(FloatConstants.SIZE_MIN_EXPONENT, FloatConstants.SIZE_MIN_SIGNIFICAND);
        }

        @Override
        public Float read(final BitInput input) throws IOException {
            return readZero(input);
        }
    }

    static int readExponent(final BitInput input, final int size) throws IOException {
        return (input.readInt(false, size) << FloatConstants.SIZE_SIGNIFICAND_IEEE754) & FloatConstants.MASK_EXPONENT;
    }

    static int readSignificand(final BitInput input, int size) throws IOException {
        int bits = input.readInt(true, 1) << (FloatConstants.SIZE_SIGNIFICAND_IEEE754 - 1);
        if (--size > 0) {
            bits |= input.readInt(true, size);
        }
        return bits;
    }

    public FloatReader(final int exponentSize, final int significandPrecisionSize) {
        super(exponentSize, significandPrecisionSize);
    }

    @Override
    public Float read(final BitInput input) throws IOException {
        int bits = readSignBit(input) << Integer.SIZE - 1;
        bits |= readExponent(input, exponentSize);
        bits |= readSignificand(input, significandSize);
        return Float.intBitsToFloat(bits);
    }

    protected int readSignBit(final BitInput input) throws IOException {
        return input.readInt(true, 1);
    }

    protected int readExponentMask(final BitInput input) throws IOException {
        return readExponent(input, exponentSize);
    }

    protected int readSignificandPrecision(final BitInput input) throws IOException {
        return readSignificand(input, significandSize);
    }
}
