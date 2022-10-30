package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io2
 * %%
 * Copyright (C) 2020 Jinahya, Inc.
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

/**
 * A writer for writing {@code double} values.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DoubleReader
 */
public class DoubleWriter
        extends DoubleBase
        implements BitWriter<Double> {

    private static final class SignBitOnly
            extends DoubleWriter {

        private SignBitOnly() {
            super(DoubleConstants.SIZE_MIN_EXPONENT, DoubleConstants.SIZE_MIN_SIGNIFICAND);
        }

        private void writeBits(final BitOutput output, final long bits) throws IOException {
            output.writeLong(true, 1, bits >> DoubleConstants.SHIFT_SIGN_BIT);
        }

        private void writeBits(final BitOutput output, final double value) throws IOException {
            writeBits(output, Double.doubleToRawLongBits(value));
        }

        @Override
        public void write(final BitOutput output, final Double value) throws IOException {
            writeBits(output, value);
        }
    }

    /**
     * A bit writer for writing {@code ±.0d}.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     * @see DoubleReader.CompressedZero
     */
    public static final class CompressedZero
            implements BitWriter<Double> {

        private static final class Holder {

            private static final BitWriter<Double> INSTANCE = new CompressedZero();

            private static final class Nullable {

                private static final BitWriter<Double> INSTANCE = new FilterBitWriter.Nullable<>(Holder.INSTANCE);

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
         * @see #getInstanceNullable()
         */
        public static BitWriter<Double> getInstance() {
            return Holder.INSTANCE;
        }

        /**
         * Returns the instance handles {@code null} values.
         *
         * @return the instance handles {@code null} values.
         * @see #getInstance()
         */
        public static BitWriter<Double> getInstanceNullable() {
            return Holder.Nullable.INSTANCE;
        }

        private CompressedZero() {
            super();
        }

        @Override
        public void write(final BitOutput output, final Double value) throws IOException {
            delegate.write(output, value);
        }

        private final BitWriter<Double> delegate = new SignBitOnly();
    }

    /**
     * A bit writer for values representing infinities.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     * @see DoubleReader.CompressedInfinity
     */
    public static final class CompressedInfinity
            implements BitWriter<Double> {

        private static final class Holder {

            private static final CompressedInfinity INSTANCE = new CompressedInfinity();

            private static final class Nullable {

                private static final BitWriter<Double> INSTANCE = new FilterBitWriter.Nullable<>(Holder.INSTANCE);

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
         * @see #getInstanceNullable()
         */
        public static BitWriter<Double> getInstance() {
            return Holder.INSTANCE;
        }

        /**
         * Returns the instance handles {@code null} values.
         *
         * @return the instance handles {@code null} values.
         * @see #getInstance()
         */
        public static BitWriter<Double> getInstanceNullable() {
            return Holder.Nullable.INSTANCE;
        }

        private CompressedInfinity() {
            super();
        }

        @Override
        public BitWriter<Double> nullable() {
            return getInstanceNullable();
        }

        @Override
        public void write(final BitOutput output, final Double value) throws IOException {
            delegate.write(output, value);
        }

        private final BitWriter<Double> delegate = new SignBitOnly();
    }

    private static class SignificandOnly
            implements BitWriter<Double> {

        private SignificandOnly(final int significandSize) {
            super();
            this.significandSize = DoubleConstraints.requireValidSignificandSize(significandSize);
            shift = DoubleConstants.SIZE_SIGNIFICAND - this.significandSize;
            mask = BitIoUtils.bitMaskDouble(this.significandSize);
        }

        private void writeBits(final BitOutput output, final long bits) throws IOException {
            final long significandBits = (bits >> shift) & mask;
            if (significandBits == 0) {
                throw new IllegalArgumentException("significand bits are all zeros");
            }
            output.writeLong(true, significandSize, significandBits);
        }

        @Override
        public void write(final BitOutput output, final Double value) throws IOException {
            throw new UnsupportedOperationException("shouldn't be used");
        }

        private final int significandSize;

        private final int shift;

        private final long mask;
    }

    /**
     * A writer for writing {@code subnormal} values in a compressed manner.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public static class CompressedSubnormal
            implements BitWriter<Double> {

        public CompressedSubnormal(final int significandSize) {
            super();
            this.significandOnly = new SignificandOnly(significandSize);
        }

        @Override
        public void write(final BitOutput output, final Double value) throws IOException {
            final long bits = Double.doubleToRawLongBits(value);
            signBitOnly.writeBits(output, bits);
            significandOnly.writeBits(output, bits);
        }

        private final SignBitOnly signBitOnly = new SignBitOnly();

        private final SignificandOnly significandOnly;
    }

    /**
     * A class for writing {@code NaN}s in a compressed manner.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public static class CompressedNaN
            implements BitWriter<Double> {

        /**
         * Creates a new instance with specified size of the significand part.
         *
         * @param significandSize the number of bits for the significand part; between
         *                        {@value DoubleConstants#SIZE_MIN_SIGNIFICAND} and
         *                        {@value DoubleConstants#SIZE_SIGNIFICAND}, both inclusive.
         */
        public CompressedNaN(final int significandSize) {
            super();
            compressedSubnormal = new CompressedSubnormal(significandSize);
        }

        @Override
        public void write(final BitOutput output, final Double value) throws IOException {
            if (significandOnly) {
                compressedSubnormal.significandOnly.write(output, value);
                return;
            }
            compressedSubnormal.write(output, value);
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
         * Invokes the {@link #setSignificandOnly(boolean)} method with specified argument and returns this object.
         *
         * @param significandOnly the value for the {@code significand} argument.
         * @return this object.
         */
        public CompressedNaN significandOnly(final boolean significandOnly) {
            setSignificandOnly(significandOnly);
            return this;
        }

        final CompressedSubnormal compressedSubnormal;

        private boolean significandOnly;
    }

    static void write(final BitOutput output, final int exponentSize, final int significandSize, final double value)
            throws IOException {
        if (exponentSize == DoubleConstants.SIZE_EXPONENT && significandSize == DoubleConstants.SIZE_SIGNIFICAND) {
            output.writeLong(false, Long.SIZE, Double.doubleToRawLongBits(value));
            return;
        }
        final long bits = Double.doubleToRawLongBits(value);
        output.writeLong(true, 1, bits >> DoubleConstants.SHIFT_SIGN_BIT);
        output.writeLong(true, exponentSize,
                         (bits & DoubleConstants.MASK_EXPONENT) >> DoubleConstants.SIZE_SIGNIFICAND);
        output.writeLong(true, significandSize, bits >> (DoubleConstants.SIZE_SIGNIFICAND - significandSize));
    }

    /**
     * Creates a new instance with specified size of the exponent part and the significand part, respectively.
     *
     * @param exponentSize    the number of bits for the export part; between {@value DoubleConstants#SIZE_MIN_EXPONENT}
     *                        and {@value DoubleConstants#SIZE_EXPONENT}, both inclusive.
     * @param significandSize the number of bits for the significand part; between
     *                        {@value DoubleConstants#SIZE_SIGNIFICAND} and {@value DoubleConstants#SIZE_SIGNIFICAND},
     *                        both inclusive.
     */
    public DoubleWriter(final int exponentSize, final int significandSize) {
        super(exponentSize, significandSize);
    }

    @Override
    public void write(final BitOutput output, final Double value) throws IOException {
        write(output, exponentSize, significandSize, value);
    }
}
