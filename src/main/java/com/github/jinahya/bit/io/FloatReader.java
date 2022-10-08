package com.github.jinahya.bit.io;

import java.io.IOException;
import java.util.Objects;

public class FloatReader
        extends FloatBase
        implements BitReader<Float> {

    public static class Zero
            extends FloatReader {

        public static final Zero POSITIVE = new Zero(1, 1) {
            @Override
            protected int readSignBit(BitInput input) throws IOException {
                return 0;
            }
        };

        public static final Zero NEGATIVE = new Zero(1, 1) {
            @Override
            protected int readSignBit(BitInput input) throws IOException {
                return 1;
            }
        };

        protected Zero(int exponentSize, int significandPrecisionSize) {
            super(exponentSize, significandPrecisionSize);
        }

        @Override
        protected int readSignBit(final BitInput input) throws IOException {
            return super.readSignBit(input);
        }

        @Override
        protected int readExponent(final BitInput input) throws IOException {
            return 0;
        }

        @Override
        protected int readSignificandPrecision(final BitInput input) throws IOException {
            return 0;
        }
    }

    static float read(final BitInput input, final int exponentSize, final int significandPrecisionSize)
            throws IOException {
        Objects.requireNonNull(input, "input is null");
        FloatConstraints.requireValidExponentSize(exponentSize);
        FloatConstraints.requireValidSignificandPrecisionSize(significandPrecisionSize);
        return .0f;
    }

    static int readExponent(final BitInput input, final int exponentSize) throws IOException {
        return input.readInt(false, exponentSize)
               & BitIoUtils.bitMask(FloatConstants.IEEE754_EXPONENT_MASK);
    }

    static int readExponentMask(final BitInput input, final int exponentSize) throws IOException {
        return readExponent(input, exponentSize)
               << FloatConstants.IEEE754_SIGNIFICAND_PRECISION_SIZE;
    }

    static int readSignificandPrecisionMask(final BitInput input, final int significandPrecisionSize)
            throws IOException {
        return input.readInt(true, 1)
               << (FloatConstants.IEEE754_SIGNIFICAND_PRECISION_SIZE - 1)
               | input.readInt(true, significandPrecisionSize - 1);
    }

    public FloatReader(final int exponentSize, final int significandPrecisionSize) {
        super(exponentSize, significandPrecisionSize);
    }

    @Override
    public Float read(final BitInput input) throws IOException {
        int bits = 0;
        bits |= readSignBit(input);
        bits <<= Integer.SIZE - 1;
        bits |= (readExponent(input) & BitIoUtils.bitMask(exponentSize)) << FloatConstants.IEEE754_SIGNIFICAND_PRECISION_SIZE;
        bits |= (readSignificandPrecision(input) & BitIoUtils.bitMask(FloatConstants.IEEE754_SIGNIFICAND_PRECISION_SIZE));
        return Float.intBitsToFloat(bits);
    }

    protected int readSignBit(final BitInput input) throws IOException {
        return input.readInt(true, 1);
    }

    protected int readExponent(final BitInput input) throws IOException {
        return 0;
    }

    protected int readSignificandPrecision(final BitInput input) throws IOException {
        return 0;
    }
}
