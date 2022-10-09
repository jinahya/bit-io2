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

/**
 * A reader for reading {@code Double} values.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DoubleWriter
 */
public class DoubleReader
        extends DoubleBase
        implements BitReader<Double> {

    static long readZeroBits(final BitInput input) throws IOException {
        return input.readLong(true, 1) << DoubleConstants.SHIFT_SIGN_BIT;
    }

    /**
     * A reader for reading {@code ±.0d}.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     * @see DoubleWriter.Zero
     */
    public static final class Zero
            extends DoubleReader {

        private static final class Holder {

            private static final DoubleReader INSTANCE = new Zero();

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

        public static BitReader<Double> getInstance() {
            return Holder.INSTANCE;
        }

        public static BitReader<Double> getInstanceNullable() {
            return Holder.Nullable.INSTANCE;
        }

        private Zero() {
            super(DoubleConstants.SIZE_MIN_EXPONENT, DoubleConstants.SIZE_MIN_SIGNIFICAND);
        }

        /**
         * Throws an {@code UnsupportedOperationException}. Use {@link #getInstanceNullable()}.
         *
         * @return N/A
         * @see #getInstanceNullable()
         */
        @Override
        public BitReader<Double> nullable() {
            throw new UnsupportedOperationException("unsupported; see getInstanceNullable()");
        }

        @Override
        public Double read(final BitInput input) throws IOException {
            return Double.longBitsToDouble(readZeroBits(input));
        }
    }

    private static long readInfinityBits(final BitInput input) throws IOException {
        return readZeroBits(input);
    }

    static double readInfinity(final BitInput input) throws IOException {
        return Double.longBitsToDouble(readInfinityBits(input) | DoubleConstants.MASK_EXPONENT);
    }

    /**
     * A reader for reading values representing infinities.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     * @see DoubleWriter.Infinity
     */
    public static final class Infinity
            extends DoubleReader {

        private static final class Holder {

            private static final DoubleReader INSTANCE = new Infinity();

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

        public static BitReader<Double> getInstance() {
            return Holder.INSTANCE;
        }

        public static BitReader<Double> getInstanceNullable() {
            return Holder.Nullable.INSTANCE;
        }

        private Infinity() {
            super(DoubleConstants.SIZE_MIN_EXPONENT, DoubleConstants.SIZE_MIN_SIGNIFICAND);
        }

        /**
         * Throws an {@code UnsupportedOperationException}. Use {@link #getInstanceNullable()}.
         *
         * @return N/A
         * @see #getInstanceNullable()
         */
        @Override
        public BitReader<Double> nullable() {
            throw new UnsupportedOperationException("unsupported; see getInstanceNullable()");
        }

        @Override
        public Double read(final BitInput input) throws IOException {
            return readInfinity(input);
        }
    }

    static long readExponent(final BitInput input, final int size) throws IOException {
        return (input.readLong(false, size) << DoubleConstants.SIZE_SIGNIFICAND) & DoubleConstants.MASK_EXPONENT;
    }

    static long readSignificand(final BitInput input, int size) throws IOException {
        long bits = input.readLong(true, 1) << (DoubleConstants.SIZE_SIGNIFICAND - 1);
        if (--size > 0) {
            bits |= input.readLong(true, size);
        }
        return bits;
    }

    public DoubleReader(final int exponentSize, final int significandSize) {
        super(exponentSize, significandSize);
    }

    @Override
    public Double read(final BitInput input) throws IOException {
        long bits = readSignBit(input) << DoubleConstants.SHIFT_SIGN_BIT;
        bits |= readExponent(input);
        bits |= readSignificand(input);
        return Double.longBitsToDouble(bits);
    }

    protected long readSignBit(final BitInput input) throws IOException {
        return input.readLong(true, 1);
    }

    protected long readExponent(final BitInput input) throws IOException {
        return readExponent(input, exponentSize);
    }

    protected long readSignificand(final BitInput input) throws IOException {
        return readSignificand(input, significandSize);
    }
}
