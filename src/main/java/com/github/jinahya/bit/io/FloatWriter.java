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
import java.util.Objects;

public class FloatWriter
        extends FloatBase
        implements BitWriter<Float> {

    abstract static class SingleBitOnly
            extends FloatWriter {

        private SingleBitOnly() {
            super(FloatConstants.SIZE_MIN_EXPONENT, FloatConstants.SIZE_MIN_SIGNIFICAND);
        }

        /**
         * Throws an {@code UnsupportedOperationException}. Use {@code getInstanceNullable()}.
         *
         * @return N/A
         */
        @Override
        public final BitWriter<Float> nullable() {
            throw new UnsupportedOperationException("unsupported; see getInstanceNullable()");
        }

        @Override
        public final void write(final BitOutput output, final Float value) throws IOException {
            Objects.requireNonNull(value, "value is null");
            output.writeInt(true, 1, Float.floatToRawIntBits(value) >> FloatConstants.SHIFT_SIGN_BIT);
        }
    }

    /**
     * A bit writer for writing {@code ±0.0f}.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public static final class Zero
            extends SingleBitOnly {

        private static final class Holder {

            private static final BitWriter<Float> INSTANCE = new Zero();

            private static final class Nullable {

                private static final BitWriter<Float> INSTANCE = new FilterBitWriter.Nullable<>(Holder.INSTANCE);

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
        public static BitWriter<Float> getInstance() {
            return Holder.INSTANCE;
        }

        /**
         * Returns the instance handles {@code null} values.
         *
         * @return the instance handles {@code null} values.
         * @see #getInstance()
         */
        public static BitWriter<Float> getInstanceNullable() {
            return Holder.Nullable.INSTANCE;
        }

        private Zero() {
            super();
        }
    }

    /**
     * A bit writer for writing {@code ±INFINITY}.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public static final class Infinity
            extends SingleBitOnly {

        private static final class Holder {

            private static final FloatWriter INSTANCE = new Infinity();

            private static final class Nullable {

                private static final BitWriter<Float> INSTANCE = new FilterBitWriter.Nullable<>(Holder.INSTANCE);

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
        public static FloatWriter getInstance() {
            return Holder.INSTANCE;
        }

        /**
         * Returns the instance handles {@code null} values.
         *
         * @return the instance handles {@code null} values.
         * @see #getInstance()
         */
        public static BitWriter<Float> getInstanceNullable() {
            return Holder.Nullable.INSTANCE;
        }

        private Infinity() {
            super();
        }
    }

    public static final class NaN
            extends FloatWriter {

        static NaN getInstance(final int significandSize) {
            return new NaN(significandSize);
        }

        public NaN(final int significandSize) {
            super(FloatConstants.SIZE_MIN_EXPONENT, significandSize);
            mask = FloatConstants.MASK_SIGNIFICAND_LEFT_MOST_BIT | BitIoUtils.bitMaskSingle(super.significandSize - 1);
        }

        @Override
        public void write(final BitOutput output, final Float value) throws IOException {
            final int bits = Float.floatToRawIntBits(value);
            if ((bits & mask) == 0) {
                throw new IllegalArgumentException("significand of the value should be positive");
            }
            writeSignificandBits(output, significandSize, bits);
        }

        private final int mask;
    }

    static FloatWriter getInstance(final int exponentSize, final int significandSize) {
        return new FloatWriter(exponentSize, significandSize);
    }

    static void writeExponentBits(final BitOutput output, final int size, final int bits) throws IOException {
        output.writeInt(false, size, ((bits << 1) >> 1) >> FloatConstants.SIZE_SIGNIFICAND);
    }

    static void writeSignificandBits(final BitOutput output, final int size, final int bits) throws IOException {
        output.writeInt(true, 1, bits >> FloatConstants.SHIFT_SIGNIFICAND_LEFT_MOST_BIT);
        output.writeInt(true, size - 1, bits);
    }

    /**
     * Creates a new instance with specified exponent size and significand size.
     *
     * @param exponentSize    the number of bits for exponent.
     * @param significandSize the number of bits for significand.
     */
    public FloatWriter(final int exponentSize, final int significandSize) {
        super(exponentSize, significandSize);
    }

    @Override
    public void write(final BitOutput output, final Float value) throws IOException {
        final int bits = Float.floatToRawIntBits(value);
        output.writeInt(true, 1, bits >> FloatConstants.SHIFT_SIGN_BIT);
        writeExponentBits(output, exponentSize, bits);
        writeSignificandBits(output, significandSize, bits);
    }
}
