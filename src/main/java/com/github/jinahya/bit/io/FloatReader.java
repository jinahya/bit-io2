package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 - 2022 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * A reader for reading {@code float} values.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class FloatReader
        extends FloatBase
        implements BitReader<Float> {

    private static final class SignBitOnly
            extends FloatReader {

        private SignBitOnly() {
            super(FloatConstants.SIZE_MIN_EXPONENT, FloatConstants.SIZE_MIN_SIGNIFICAND);
        }

        int readBits(final BitInput input) throws IOException {
            return input.readInt(true, 1) << FloatConstants.SHIFT_SIGN_BIT;
        }

        @Override
        public Float read(final BitInput input) throws IOException {
            return Float.intBitsToFloat(readBits(input));
        }
    }

    /**
     * A reader for reading either {@code +.0f} or {@code -.0f} in a compressed manner.
     *
     * @implSpec The implementation read only {@code 1} bit for the <em>sign bit</em> and returns either
     * <code>0b<strong>0</strong>__00000000__00000000_00000000_0000_000<sub>2</sub></code> or
     * <code>0b<strong>1</strong>__00000000__00000000_00000000_0000_000<sub>2</sub></code>.
     */
    public static final class CompressedZero
            implements BitReader<Float> {

        private static final class Holder {

            private static final BitReader<Float> INSTANCE = new CompressedZero();

            private static final class Nullable {

                private static final BitReader<Float> INSTANCE = new FilterBitReader.Nullable<>(Holder.INSTANCE);

                private Nullable() {
                    throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
                }
            }

            private Holder() {
                throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
            }
        }

        /**
         * Returns the instance.
         *
         * @return the instance.
         */
        public static BitReader<Float> getInstance() {
            return Holder.INSTANCE;
        }

        /**
         * Returns the instance handles {@code null} values.
         *
         * @return the instance handles {@code null} values.
         */
        public static BitReader<Float> getInstanceNullable() {
            return Holder.Nullable.INSTANCE;
        }

        private CompressedZero() {
            super();
        }

        @Override
        public BitReader<Float> nullable() {
            return getInstanceNullable();
        }

        @Override
        public Float read(final BitInput input) throws IOException {
            return delegate.read(input);
        }

        private final BitReader<Float> delegate = new SignBitOnly();
    }

    /**
     * A reader for reading either {@link Float#NEGATIVE_INFINITY} or {@link Float#POSITIVE_INFINITY} in a compresses
     * manner.
     *
     * @implSpec This reader reads {@code 1} bit for the <em>sign bit</em>, and returns either
     * <code>0b<strong>0</strong>__11111111__00000000_00000000_0000_000<sub>2</sub></code> or
     * <code>0b<strong>1</strong>__11111111__00000000_00000000_0000_000<sub>2</sub></code>.
     */
    public static final class CompressedInfinity
            implements BitReader<Float> {

        private static final class Holder {

            private static final BitReader<Float> INSTANCE = new CompressedInfinity();

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

        /**
         * Returns the instance.
         *
         * @return the instance.
         */
        public static BitReader<Float> getInstance() {
            return Holder.INSTANCE;
        }

        /**
         * Returns the instance handles {@code null} values.
         *
         * @return the instance handles {@code null} values.
         */
        public static BitReader<Float> getInstanceNullable() {
            return Holder.Nullable.INSTANCE;
        }

        private CompressedInfinity() {
            super();
        }

        @Override
        public BitReader<Float> nullable() {
            return getInstanceNullable();
        }

        @Override
        public Float read(final BitInput input) throws IOException {
            return Float.intBitsToFloat(delegate.readBits(input) | FloatConstants.MASK_EXPONENT);
        }

        private final SignBitOnly delegate = new SignBitOnly();
    }

    /**
     * A reader for reading {@code NaN} values in a compressed manner.
     */
    public static class CompressedNaN
            implements BitReader<Float> {

        private static final Map<FloatKey, BitReader<Float>> CACHED_INSTANCES = new WeakHashMap<>();

        private static final Map<FloatKey, BitReader<Float>> CACHED_INSTANCES_NULLABLE = new WeakHashMap<>();

        static BitReader<Float> getCachedInstance(final int significandSize) {
            return CACHED_INSTANCES.computeIfAbsent(
                    FloatKey.withSignificandSizeOnly(significandSize),
                    k -> new CompressedNaN(k.getSignificandSize()) {
                        @Override
                        public BitReader<Float> nullable() {
                            return CACHED_INSTANCES_NULLABLE.computeIfAbsent(
                                    FloatKey.copyOf(k),
                                    k2 -> super.nullable()
                            );
                        }
                    }
            );
        }

        /**
         * Creates a new instance with specified number bits for the {@code significand} part.
         *
         * @param significandSize a number of bits for the significand part; between
         *                        {@value FloatConstants#SIZE_MIN_SIGNIFICAND} and
         *                        {@value FloatConstants#SIZE_SIGNIFICAND}, both inclusive.
         */
        public CompressedNaN(int significandSize) {
            super();
            this.significandSize = FloatConstraints.requireValidSignificandSize(significandSize);
        }

        @Override
        public Float read(final BitInput input) throws IOException {
//            final int significandBits = readSignificandBits(input, significandSize);
            final int significandBits = (input.readInt(true, 1) << (FloatConstants.SIZE_SIGNIFICAND - 1))
                                        | input.readInt(true, significandSize - 1);
            if (significandBits == 0) {
                throw new IOException("significand bits are all zeros");
            }
            return Float.intBitsToFloat(significandBits | FloatConstants.MASK_EXPONENT);
        }

        private final int significandSize;
    }

    /**
     * A reader for reading {@code subnormal} values in a compressed manner.
     */
    public static class CompressedSubnormal
            implements BitReader<Float> {

        private static final Map<FloatKey, BitReader<Float>> CACHED_INSTANCES = new WeakHashMap<>();

        private static final Map<FloatKey, BitReader<Float>> CACHED_INSTANCES_NULLABLE = new WeakHashMap<>();

        static BitReader<Float> getCachedInstance(final int significandSize) {
            return CACHED_INSTANCES.computeIfAbsent(
                    FloatKey.withSignificandSizeOnly(significandSize),
                    k -> new CompressedSubnormal(k.getSignificandSize()) {
                        @Override
                        public BitReader<Float> nullable() {
                            return CACHED_INSTANCES_NULLABLE.computeIfAbsent(
                                    FloatKey.copyOf(k),
                                    k2 -> super.nullable()
                            );
                        }
                    }
            );
        }

        /**
         * Creates a new instance with specified number bits for the {@code significand} part.
         *
         * @param significandSize a number of bits for the significand part; between
         *                        {@value FloatConstants#SIZE_MIN_SIGNIFICAND} and
         *                        {@value FloatConstants#SIZE_SIGNIFICAND}, both inclusive.
         */
        public CompressedSubnormal(int significandSize) {
            super();
            this.significandSize = FloatConstraints.requireValidSignificandSize(significandSize);
            shift = FloatConstants.SIZE_SIGNIFICAND - this.significandSize;
        }

        @Override
        public Float read(final BitInput input) throws IOException {
            final int signBits = signBitReader.readBits(input);
            final int significandBits = input.readInt(true, significandSize) << shift;
            if (significandBits == 0) {
                throw new IOException("significand bits are all zeros");
            }
            return Float.intBitsToFloat(signBits | significandBits);
        }

        private final SignBitOnly signBitReader = new SignBitOnly();

        private final int significandSize;

        private final int shift;
    }

    private static int readExponentBits(final BitInput input, final int size) throws IOException {
        return (input.readInt(false, size) << FloatConstants.SIZE_SIGNIFICAND) & FloatConstants.MASK_EXPONENT;
    }

    private static int readSignificandBits(final BitInput input, final int size) throws IOException {
        return (input.readInt(true, 1) << (FloatConstants.SIZE_SIGNIFICAND - 1))
               | input.readInt(true, size - 1);
    }

    static float read(final BitInput input, final int exponentSize, final int significandSize) throws IOException {
        if (exponentSize == FloatConstants.SIZE_EXPONENT && significandSize == FloatConstants.SIZE_SIGNIFICAND) {
            return Float.intBitsToFloat(input.readInt(false, Integer.SIZE));
        }
        int bits = input.readInt(true, 1) << FloatConstants.SHIFT_SIGN_BIT;
        bits |= readExponentBits(input, exponentSize);
        bits |= readSignificandBits(input, significandSize);
        return Float.intBitsToFloat(bits);
    }

    private static final Map<FloatKey, BitReader<Float>> CACHED_INSTANCE = new WeakHashMap<>();

    private static final Map<FloatKey, BitReader<Float>> CACHED_INSTANCE_NULLABLE = new WeakHashMap<>();

    /**
     * Returns a cached instance for specified sizes of exponent part and significand part, respectively.
     *
     * @param exponentSize    the number of bits for the exponent part; between
     *                        {@value FloatConstants#SIZE_MIN_EXPONENT} and {@value FloatConstants#SIZE_EXPONENT}, both
     *                        inclusive.
     * @param significandSize the number of bits for the significand part; between
     *                        {@value FloatConstants#SIZE_MIN_SIGNIFICAND} and {@value FloatConstants#SIZE_SIGNIFICAND},
     *                        both inclusive.
     * @return a cached instance.
     */
    static BitReader<Float> getCachedInstance(final int exponentSize, final int significandSize) {
        return CACHED_INSTANCE.computeIfAbsent(
                FloatKey.of(exponentSize, significandSize),
                k -> new FloatReader(k.getExponentSize(), k.getSignificandSize()) {
                    @Override
                    public BitReader<Float> nullable() {
                        return CACHED_INSTANCE_NULLABLE.computeIfAbsent(FloatKey.copyOf(k), k2 -> super.nullable());
                    }
                }
        );
    }

    /**
     * Creates a new instance with specified sizes of the exponent part and the significand part.
     *
     * @param exponentSize    the number of bits for the exponent part; between
     *                        {@value FloatConstants#SIZE_MIN_EXPONENT} and {@value FloatConstants#SIZE_EXPONENT}, both
     *                        inclusive.
     * @param significandSize the number of bits for the significand part; between
     *                        {@value FloatConstants#SIZE_MIN_SIGNIFICAND} and {@value FloatConstants#SIZE_SIGNIFICAND},
     *                        both inclusive.
     */
    public FloatReader(final int exponentSize, final int significandSize) {
        super(exponentSize, significandSize);
    }

    @Override
    public Float read(final BitInput input) throws IOException {
        return read(input, exponentSize, significandSize);
    }
}
