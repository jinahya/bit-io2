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
import java.util.Map;
import java.util.WeakHashMap;

/**
 * A writer for writing {@code double} values.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DoubleReader
 */
public class DoubleWriter
        extends DoubleBase
        implements BitWriter<Double> {

    abstract static class SignBitOnly
            extends DoubleWriter {

        private SignBitOnly() {
            super(DoubleConstants.SIZE_MIN_EXPONENT, DoubleConstants.SIZE_MIN_SIGNIFICAND);
        }

        /**
         * Throws an {@code UnsupportedOperationException}. Use {@code getInstanceNullable()}.
         *
         * @return N/A
         */
        @Override
        public final BitWriter<Double> nullable() {
            throw new UnsupportedOperationException("unsupported; use getInstanceNullable()");
        }

        @Override
        public final void write(final BitOutput output, final Double value) throws IOException {
            output.writeLong(true, 1, Double.doubleToRawLongBits(value) >> DoubleConstants.SHIFT_SIGN_BIT);
        }
    }

    /**
     * A bit writer for writing {@code ±.0d}.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     * @see DoubleReader.CompressedZero
     */
    public static final class CompressedZero
            extends SignBitOnly {

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
    }

    /**
     * A bit writer for values representing infinities.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     * @see DoubleReader.CompressedInfinity
     */
    public static final class CompressedInfinity
            extends SignBitOnly {

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
        public static DoubleWriter getInstance() {
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
    }

    /**
     * A class for writing {@code NaN} values in a compressed manner.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public static final class CompressedNaN
            extends DoubleWriter {

        private static final Map<DoubleKey, CompressedNaN> CACHED_INSTANCES = new WeakHashMap<>();

        static CompressedNaN getCachedInstance(final int significandSize) {
            return CACHED_INSTANCES.computeIfAbsent(
                    DoubleKey.withSignificandSizeOnly(significandSize),
                    k -> new CompressedNaN(k.significandSize)
            );
        }

        /**
         * Creates a new instance with specified size of the significand part.
         *
         * @param significandSize the number of bits for the significand part; between
         *                        {@value DoubleConstants#SIZE_MIN_SIGNIFICAND} and
         *                        {@value DoubleConstants#SIZE_SIGNIFICAND}, both inclusive.
         */
        public CompressedNaN(final int significandSize) {
            super(DoubleConstants.SIZE_MIN_EXPONENT, significandSize);
            mask = DoubleConstants.MASK_SIGNIFICAND_LEFT_MOST_BIT | BitIoUtils.bitMaskDouble(super.significandSize - 1);
        }

        @Override
        public void write(final BitOutput output, final Double value) throws IOException {
            final long bits = Double.doubleToRawLongBits(value);
            if ((bits & mask) == 0L) {
                throw new IllegalArgumentException("significand of the value should be positive");
            }
            writeSignificandBits(output, significandSize, bits);
        }

        private final long mask;
    }

    private static void writeExponentBits(final BitOutput output, final int size, final long bits) throws IOException {
        output.writeLong(false, size, ((bits << 1) >> 1) >> DoubleConstants.SIZE_SIGNIFICAND);
    }

    private static void writeSignificandBits(final BitOutput output, int size, final long bits) throws IOException {
        output.writeLong(true, 1, bits >> (DoubleConstants.SIZE_SIGNIFICAND - 1));
        if (--size > 0) {
            output.writeLong(true, size, bits);
        }
    }

    static void write(final BitOutput output, final int exponentSize, final int significandSize, final double value)
            throws IOException {
        DoubleConstraints.requireValidExponentSize(exponentSize);
        DoubleConstraints.requireValidSignificandSize(significandSize);
        if (exponentSize == DoubleConstants.SIZE_EXPONENT && significandSize == DoubleConstants.SIZE_SIGNIFICAND) {
            output.writeLong(false, Long.SIZE, Double.doubleToRawLongBits(value));
            return;
        }
        final long bits = Double.doubleToRawLongBits(value);
        output.writeLong(true, 1, bits >> DoubleConstants.SHIFT_SIGN_BIT);
        writeExponentBits(output, exponentSize, bits);
        writeSignificandBits(output, significandSize, bits);
    }

    private static final Map<DoubleKey, DoubleWriter> CACHED_INSTANCES = new WeakHashMap<>();

    static DoubleWriter getCachedInstance(final int exponentSize, final int significandSize) {
        return CACHED_INSTANCES.computeIfAbsent(
                new DoubleKey(exponentSize, significandSize),
                k -> new DoubleWriter(k.exponentSize, k.significandSize)
        );
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
