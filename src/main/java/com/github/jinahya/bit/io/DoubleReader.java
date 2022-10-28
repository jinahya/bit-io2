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
 * A reader for reading {@code Double} values.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DoubleWriter
 */
public class DoubleReader
        extends DoubleBase
        implements BitReader<Double> {

    private static final class SignBitOnly
            extends DoubleReader {

        private SignBitOnly() {
            super(DoubleConstants.SIZE_MIN_EXPONENT, DoubleConstants.SIZE_MIN_SIGNIFICAND);
        }

        long readBits(final BitInput input) throws IOException {
            return input.readLong(true, 1) << DoubleConstants.SHIFT_SIGN_BIT;
        }

        @Override
        public Double read(final BitInput input) throws IOException {
            return Double.longBitsToDouble(readBits(input));
        }
    }

    /**
     * A reader for reading {@code ±.0d}.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     * @see DoubleWriter.CompressedZero
     */
    public static final class CompressedZero
            implements BitReader<Double> {

        private static final class Holder {

            private static final BitReader<Double> INSTANCE = new CompressedZero();

            private static final class Nullable {

                private static final BitReader<Double> INSTANCE = new FilterBitReader.Nullable<>(Holder.INSTANCE);

                private Nullable() {
                    throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
                }
            }

            private Holder() {
                throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
            }
        }

        /**
         * Returns the instance of this class which is singleton.
         *
         * @return the instance.
         */
        public static BitReader<Double> getInstance() {
            return Holder.INSTANCE;
        }

        /**
         * Returns the instance handles {@code null} values.
         *
         * @return the instance handles {@code null} values.
         */
        public static BitReader<Double> getInstanceNullable() {
            return Holder.Nullable.INSTANCE;
        }

        private CompressedZero() {
            super();
        }

        @Override
        public BitReader<Double> nullable() {
            return getInstanceNullable();
        }

        @Override
        public Double read(final BitInput input) throws IOException {
            return delegate.read(input);
        }

        private final BitReader<Double> delegate = new SignBitOnly();
    }

    /**
     * A reader for reading values representing infinities.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     * @see DoubleWriter.CompressedInfinity
     */
    public static final class CompressedInfinity
            implements BitReader<Double> {

        private static final class Holder {

            private static final BitReader<Double> INSTANCE = new CompressedInfinity();

            private static final class Nullable {

                private static final BitReader<Double> INSTANCE = new FilterBitReader.Nullable<>(Holder.INSTANCE);

                private Nullable() {
                    throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
                }
            }

            private Holder() {
                throw new AssertionError(BitIoConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
            }
        }

        /**
         * Returns the instance of this class which is singleton.
         *
         * @return the instance.
         */
        public static BitReader<Double> getInstance() {
            return Holder.INSTANCE;
        }

        /**
         * Returns the instance handles {@code null} values.
         *
         * @return the instance handles {@code null} values.
         */
        public static BitReader<Double> getInstanceNullable() {
            return Holder.Nullable.INSTANCE;
        }

        private CompressedInfinity() {
            super();
        }

        @Override
        public BitReader<Double> nullable() {
            return getInstanceNullable();
        }

        @Override
        public Double read(final BitInput input) throws IOException {
            return Double.longBitsToDouble(delegate.readBits(input) | DoubleConstants.MASK_EXPONENT);
        }

        private final SignBitOnly delegate = new SignBitOnly();
    }

    private static final class SignificandOnly
            implements BitReader<Double> {

        /**
         * Returns the instance for specified significand size.
         *
         * @param significandSize the number of bits for the significand part.
         */
        private SignificandOnly(final int significandSize) {
            super();
            this.significandSize = DoubleConstraints.requireValidSignificandSize(significandSize);
            shift = DoubleConstants.SIZE_SIGNIFICAND - this.significandSize;
        }

        private long readBits(final BitInput input) throws IOException {
            final long significandBits = input.readLong(true, significandSize) << shift;
            if (significandBits == 0) {
                throw new IOException("significand bits are all zeros");
            }
            return significandBits;
        }

        @Override
        public Double read(final BitInput input) throws IOException {
            throw new UnsupportedOperationException("shouldn't be used");
        }

        private final int significandSize;

        private final int shift;
    }

    public static class CompressedSubnormal
            implements BitReader<Double> {

        private static final Map<DoubleCacheKey, BitReader<Double>> CACHED_INSTANCES = new WeakHashMap<>();

        private static final Map<DoubleCacheKey, BitReader<Double>> CACHED_INSTANCES_NULLABLE = new WeakHashMap<>();

        static BitReader<Double> getCachedInstance(final int significandSize) {
            return CACHED_INSTANCES.computeIfAbsent(
                    DoubleCacheKey.of(significandSize),
                    k -> new CompressedSubnormal(k.getSignificandSize()) {
                        @Override
                        public BitReader<Double> nullable() {
                            return CACHED_INSTANCES_NULLABLE.computeIfAbsent(
                                    DoubleCacheKey.copyOf(k),
                                    k2 -> super.nullable()
                            );
                        }
                    }
            );
        }

        /**
         * Returns the instance for specified significand size.
         *
         * @param significandSize the number of bits for the significand part.
         */
        public CompressedSubnormal(final int significandSize) {
            super();
            significandOnly = new SignificandOnly(significandSize);
        }

        private long readBits(final BitInput input) throws IOException {
            final long signBits = signBitReader.readBits(input);
            final long significandBits = significandOnly.readBits(input);
            return signBits | significandBits;
        }

        @Override
        public Double read(final BitInput input) throws IOException {
            final long bits = readBits(input);
            return Double.longBitsToDouble(bits);
        }

        private final SignBitOnly signBitReader = new SignBitOnly();

        private final SignificandOnly significandOnly;
    }

    public static class CompressedNaN
            implements BitReader<Double> {

        private static final Map<DoubleCacheKey, BitReader<Double>> CACHED_INSTANCES = new WeakHashMap<>();

        private static final Map<DoubleCacheKey, BitReader<Double>> CACHED_INSTANCES_NULLABLE = new WeakHashMap<>();

        static BitReader<Double> getCachedInstance(final int significandSize) {
            return CACHED_INSTANCES.computeIfAbsent(
                    DoubleCacheKey.of(significandSize),
                    k -> new CompressedNaN(k.getSignificandSize()) {
                        @Override
                        public BitReader<Double> nullable() {
                            return CACHED_INSTANCES_NULLABLE.computeIfAbsent(
                                    DoubleCacheKey.copyOf(k),
                                    k2 -> super.nullable()
                            );
                        }
                    }
            );
        }

        /**
         * Returns the instance for specified significand size.
         *
         * @param significandSize the number of bits for the significand part.
         */
        public CompressedNaN(final int significandSize) {
            super();
            compressedSubnormal = new CompressedSubnormal(significandSize);
        }

        @Override
        public Double read(final BitInput input) throws IOException {
            if (significandOnly) {
                return Double.longBitsToDouble(
                        compressedSubnormal.significandOnly.readBits(input) | DoubleConstants.MASK_EXPONENT);
            }
            return Double.longBitsToDouble(compressedSubnormal.readBits(input) | DoubleConstants.MASK_EXPONENT);
        }

        /**
         * Returns current value of {@code significandOnly} property.
         *
         * @return current value of {@code significandOnly} property.
         * @apiNote initial value of the property is {@code false}.
         */
        public boolean isSignificandOnly() {
            return significandOnly;
        }

        /**
         * Replaces current value of {@code significandOnly} property with specified value.
         *
         * @param significandOnly new value for the {@code significandOnly} property; {@code true} for not reading the
         *                        sign bit; {@code false} for reading the sign bit.
         */
        public void setSignificandOnly(final boolean significandOnly) {
            this.significandOnly = significandOnly;
        }

        /**
         * Invokes {@link #setSignificandOnly(boolean)} method with specified arguments and returns this object.
         *
         * @param readSignBit the argument to passed to the {@link #setSignificandOnly(boolean)} method.
         * @return this object.
         */
        public CompressedNaN significandOnly(final boolean readSignBit) {
            setSignificandOnly(readSignBit);
            return this;
        }

        final CompressedSubnormal compressedSubnormal;

        private boolean significandOnly;
    }

    static double read(final BitInput input, final int exponentSize, final int significandSize) throws IOException {
        if (exponentSize == DoubleConstants.SIZE_EXPONENT && significandSize == DoubleConstants.SIZE_SIGNIFICAND) {
            return Double.longBitsToDouble(input.readLong(false, Long.SIZE));
        }
        return Double.longBitsToDouble(
                (input.readLong(true, 1) << DoubleConstants.SHIFT_SIGN_BIT)
                | (input.readLong(true, exponentSize) << DoubleConstants.SIZE_SIGNIFICAND)
                | (input.readLong(true, significandSize) << (DoubleConstants.SIZE_SIGNIFICAND - significandSize))
        );
    }

    private static final Map<DoubleCacheKey, BitReader<Double>> CACHED_INSTANCES = new WeakHashMap<>();

    private static final Map<DoubleCacheKey, BitReader<Double>> CACHED_INSTANCES_NULLABLE = new WeakHashMap<>();

    static BitReader<Double> getCachedInstance(final int exponentSize, final int significandSize) {
        return CACHED_INSTANCES.computeIfAbsent(
                DoubleCacheKey.of(exponentSize, significandSize),
                k -> new DoubleReader(k.getExponentSize(), k.getSignificandSize()) {
                    @Override
                    public BitReader<Double> nullable() {
                        return CACHED_INSTANCES_NULLABLE.computeIfAbsent(DoubleCacheKey.copyOf(k), k2 -> super.nullable());
                    }
                }
        );
    }

    /**
     * Creates a new instance specified sizes of the exponent part and significand part, respectively.
     *
     * @param exponentSize    the number of bits for the exponent part.
     * @param significandSize the number of bits for the significand part.
     */
    public DoubleReader(final int exponentSize, final int significandSize) {
        super(exponentSize, significandSize);
    }

    @Override
    public Double read(final BitInput input) throws IOException {
        return read(input, exponentSize, significandSize);
    }
}
