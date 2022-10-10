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
 * A writer for writing {@code Double} values.
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
         * Throws an {@code UnsupportedOperationException}.
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
     * @see DoubleReader.Zero
     */
    public static final class Zero
            extends SignBitOnly {

        private static final class Holder {

            private static final BitWriter<Double> INSTANCE = new Zero();

            private static final class Nullable {

                private static final BitWriter<Double> INSTANCE = new FilterBitWriter.Nullable<>(Holder.INSTANCE);

                private Nullable() {
                    throw new AssertionError("instantiation is not allowed");
                }
            }

            private Holder() {
                throw new AssertionError("instantiation is not allowed");
            }
        }

        /**
         * Returns the instance of this class.
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

        private Zero() {
            super();
        }
    }

    /**
     * A bit writer for values representing infinities.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     * @see DoubleReader.Infinity
     */
    public static final class Infinity
            extends SignBitOnly {

        private static final class Holder {

            private static final Infinity INSTANCE = new Infinity();

            private static final class Nullable {

                private static final BitWriter<Double> INSTANCE = new FilterBitWriter.Nullable<>(Holder.INSTANCE);

                private Nullable() {
                    throw new AssertionError("instantiation is not allowed");
                }
            }

            private Holder() {
                throw new AssertionError("instantiation is not allowed");
            }
        }

        /**
         * Returns the instance of this class.
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

        private Infinity() {
            super();
        }
    }

    public static final class NaN
            extends DoubleWriter {

        static NaN getInstance(final int significandSize) {
            return new NaN(significandSize);
        }

        public NaN(final int significandSize) {
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

    static DoubleWriter getInstance(final int exponentSize, final int significandSize) {
        return new DoubleWriter(exponentSize, significandSize);
    }

    static void writeExponentBits(final BitOutput output, final int size, final long bits) throws IOException {
        output.writeLong(false, size, ((bits << 1) >> 1) >> DoubleConstants.SIZE_SIGNIFICAND);
    }

    static void writeSignificandBits(final BitOutput output, int size, final long bits) throws IOException {
        output.writeLong(true, 1, bits >> (DoubleConstants.SIZE_SIGNIFICAND - 1));
        if (--size > 0) {
            output.writeLong(true, size, bits);
        }
    }

    public DoubleWriter(final int exponentSize, final int significandSize) {
        super(exponentSize, significandSize);
    }

    @Override
    public void write(final BitOutput output, final Double value) throws IOException {
        final long bits = Double.doubleToRawLongBits(value);
        writeSignBit(output, bits);
        writeExponent(output, bits);
        writeSignificand(output, bits);
    }

    protected void writeSignBit(final BitOutput output, final long bits) throws IOException {
        output.writeLong(true, 1, bits >> DoubleConstants.SHIFT_SIGN_BIT);
    }

    protected void writeExponent(final BitOutput output, final long bits) throws IOException {
        writeExponentBits(output, exponentSize, bits);
    }

    protected void writeSignificand(final BitOutput output, final long bits) throws IOException {
        writeSignificandBits(output, significandSize, bits);
    }
}
