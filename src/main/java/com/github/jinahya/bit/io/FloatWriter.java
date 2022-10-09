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

    static void writeZeroBits(final BitOutput output, final int bits) throws IOException {
        output.writeInt(true, 1, bits >= 0 ? 0 : 1);
    }

    /**
     * A bit writer for writing {@code ±0.0f}.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public static final class Zero
            extends FloatWriter {

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
            super(FloatConstants.SIZE_MIN_EXPONENT, FloatConstants.SIZE_MIN_SIGNIFICAND);
        }

        @Override
        public BitWriter<Float> nullable() {
            throw new UnsupportedOperationException("unsupported; see getInstanceNullable()");
        }

        @Override
        public void write(final BitOutput output, final Float value) throws IOException {
            Objects.requireNonNull(value, "value is null");
            writeZeroBits(output, Float.floatToRawIntBits(value));
        }
    }

    static void writeInfinityBits(final BitOutput output, final int bits) throws IOException {
        writeZeroBits(output, bits);
    }

    /**
     * A bit writer for writing {@code ±Infinity}.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     */
    public static final class Infinity
            extends FloatWriter {

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
            super(FloatConstants.SIZE_MIN_EXPONENT, FloatConstants.SIZE_MIN_SIGNIFICAND);
        }

        @Override
        public BitWriter<Float> nullable() {
            throw new UnsupportedOperationException("unsupported; see getInstanceNullable()");
        }

        @Override
        public void write(final BitOutput output, final Float value) throws IOException {
            Objects.requireNonNull(value, "value is null");
            writeInfinityBits(output, Float.floatToRawIntBits(value));
        }
    }

    static void writeExponentBits(final BitOutput output, final int size, final int bits) throws IOException {
        output.writeInt(false, size, ((bits << 1) >> 1) >> FloatConstants.SIZE_SIGNIFICAND);
    }

    static void writeSignificandBits(final BitOutput output, int size, final int bits) throws IOException {
        output.writeInt(true, 1, bits >> (FloatConstants.SIZE_SIGNIFICAND - 1));
        if (--size > 0) {
            output.writeInt(true, size, bits);
        }
    }

    public FloatWriter(final int exponentSize, final int significandSize) {
        super(exponentSize, significandSize);
    }

    @Override
    public void write(final BitOutput output, final Float value) throws IOException {
        final int bits = Float.floatToRawIntBits(value);
        writeSignBit(output, bits);
        writeExponent(output, bits);
        writeSignificand(output, bits);
    }

    protected void writeSignBit(final BitOutput output, final int bits) throws IOException {
        output.writeInt(true, 1, bits >> FloatConstants.SHIFT_SIGN_BIT);
    }

    protected void writeExponent(final BitOutput output, final int bits) throws IOException {
        writeExponentBits(output, exponentSize, bits);
    }

    protected void writeSignificand(final BitOutput output, final int bits) throws IOException {
        writeSignificandBits(output, significandSize, bits);
    }
}
