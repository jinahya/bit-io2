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

public class FloatReader
        extends FloatBase
        implements BitReader<Float> {

    abstract static class SignBitOnly
            extends FloatReader {

        private SignBitOnly() {
            super(FloatConstants.SIZE_MIN_EXPONENT, FloatConstants.SIZE_MIN_SIGNIFICAND);
        }

        /**
         * Throws an {@code UnsupportedOperationException}. Use {@code getInstanceNullable()}.
         *
         * @return N/A
         */
        @Override
        public final BitReader<Float> nullable() {
            throw new UnsupportedOperationException("unsupported; see getInstanceNullable()");
        }

        int readBits(final BitInput input) throws IOException {
            return input.readInt(true, 1) << FloatConstants.SHIFT_SIGN_BIT;
        }

        @Override
        public Float read(final BitInput input) throws IOException {
            return Float.intBitsToFloat(readBits(input));
        }
    }

    public static final class Zero
            extends SignBitOnly {

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
            super();
        }
    }

    public static final class Infinity
            extends SignBitOnly {

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
            super();
        }

        @Override
        public Float read(final BitInput input) throws IOException {
            return Float.intBitsToFloat(readBits(input) | FloatConstants.MASK_EXPONENT);
        }
    }

    public static class NaN
            extends FloatReader {

        static NaN getInstance(final int significandSize) {
            return new NaN(significandSize);
        }

        public NaN(int significandSize) {
            super(FloatConstants.SIZE_MIN_EXPONENT, significandSize);
        }

        @Override
        public Float read(final BitInput input) throws IOException {
            final int bits = FloatReader.readSignificandBits(input, significandSize);
            assert bits >= 0;
            if (bits == 0) {
                throw new IOException("significand bits are all zeros");
            }
            return Float.intBitsToFloat(bits | FloatConstants.MASK_EXPONENT);
        }
    }

    static FloatReader getInstance(final int exponentSize, final int significandSize) {
        return new FloatReader(exponentSize, significandSize);
    }

    static int readExponentBits(final BitInput input, final int size) throws IOException {
        return (input.readInt(false, size) << FloatConstants.SIZE_SIGNIFICAND) & FloatConstants.MASK_EXPONENT;
    }

    static int readSignificandBits(final BitInput input, final int size) throws IOException {
        return input.readInt(true, 1) << (FloatConstants.SIZE_SIGNIFICAND - 1)
               | input.readInt(true, size - 1);
    }

    /**
     * Creates a new instance with specified exponent size and significand size.
     *
     * @param exponentSize    the number of bits for exponent.
     * @param significandSize the number of bits for significand.
     */
    public FloatReader(final int exponentSize, final int significandSize) {
        super(exponentSize, significandSize);
    }

    @Override
    public Float read(final BitInput input) throws IOException {
        int bits = input.readInt(true, 1) << FloatConstants.SHIFT_SIGN_BIT;
        bits |= readExponentBits(input, exponentSize);
        bits |= readSignificandBits(input, significandSize);
        return Float.intBitsToFloat(bits);
    }
}
