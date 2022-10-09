package com.github.jinahya.bit.io;

import java.io.IOException;

public class FloatReader
        extends FloatBase
        implements BitReader<Float> {

    static int readZeroBits(final BitInput input) throws IOException {
        return input.readInt(true, 1) << FloatConstants.SHIFT_SIGN_BIT;
    }

    public static final class Zero
            extends FloatReader {

        private static final class Holder {

            private static final FloatReader INSTANCE = new Zero();

            private static final class Nullable {

                private static final BitReader<Float> INSTANCE = new FilterBitReader.Nullable<>(Holder.INSTANCE);

                private Nullable() {
                    throw new AssertionError("instantiation is not allowed");
                }
            }

            private Holder() {
                throw new AssertionError("instantiation is not allowed");
            }
        }

        public static BitReader<Float> getInstance() {
            return Holder.INSTANCE;
        }

        public static BitReader<Float> getInstanceNullable() {
            return Holder.Nullable.INSTANCE;
        }

        private Zero() {
            super(FloatConstants.SIZE_MIN_EXPONENT, FloatConstants.SIZE_MIN_SIGNIFICAND);
        }

        @Override
        public BitReader<Float> nullable() {
            throw new UnsupportedOperationException("unsupported; see getInstanceNullable()");
        }

        @Override
        public Float read(final BitInput input) throws IOException {
            return Float.intBitsToFloat(readZeroBits(input));
        }
    }

    private static int readInfinityBits(final BitInput input) throws IOException {
        return readZeroBits(input);
    }

    static float readInfinity(final BitInput input) throws IOException {
        return Float.intBitsToFloat(readInfinityBits(input) | FloatConstants.MASK_EXPONENT);
    }

    public static final class Infinity
            extends FloatReader {

        private static final class Holder {

            private static final FloatReader INSTANCE = new Infinity();

            private static final class Nullable {

                private static final BitReader<Float> INSTANCE = new FilterBitReader.Nullable<>(Holder.INSTANCE);

                private Nullable() {
                    throw new AssertionError("instantiation is not allowed");
                }
            }

            private Holder() {
                throw new AssertionError("instantiation is not allowed");
            }
        }

        public static BitReader<Float> getInstance() {
            return Holder.INSTANCE;
        }

        public static BitReader<Float> getInstanceNullable() {
            return Holder.Nullable.INSTANCE;
        }

        private Infinity() {
            super(FloatConstants.SIZE_MIN_EXPONENT, FloatConstants.SIZE_MIN_SIGNIFICAND);
        }

        @Override
        public BitReader<Float> nullable() {
            throw new UnsupportedOperationException("unsupported; see getInstanceNullable()");
        }

        @Override
        public Float read(final BitInput input) throws IOException {
            return readInfinity(input);
        }
    }

    static int readExponent(final BitInput input, final int size) throws IOException {
        return (input.readInt(false, size) << FloatConstants.SIZE_SIGNIFICAND) & FloatConstants.MASK_EXPONENT;
    }

    static int readSignificand(final BitInput input, int size) throws IOException {
        int bits = input.readInt(true, 1) << (FloatConstants.SIZE_SIGNIFICAND - 1);
        if (--size > 0) {
            bits |= input.readInt(true, size);
        }
        return bits;
    }

    public FloatReader(final int exponentSize, final int significandSize) {
        super(exponentSize, significandSize);
    }

    @Override
    public Float read(final BitInput input) throws IOException {
        int bits = readSignBit(input) << FloatConstants.SHIFT_SIGN_BIT;
        bits |= readExponent(input);
        bits |= readSignificand(input);
        return Float.intBitsToFloat(bits);
    }

    protected int readSignBit(final BitInput input) throws IOException {
        return input.readInt(true, 1);
    }

    protected int readExponent(final BitInput input) throws IOException {
        return readExponent(input, exponentSize);
    }

    protected int readSignificand(final BitInput input) throws IOException {
        return readSignificand(input, significandSize);
    }
}
