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

    abstract static class SignBitOnly
            extends DoubleReader {

        private SignBitOnly() {
            super(DoubleConstants.SIZE_MIN_EXPONENT, DoubleConstants.SIZE_MIN_SIGNIFICAND);
        }

        /**
         * Throws an {@code UnsupportedOperationException}. Use {@code getInstanceNullable()}.
         *
         * @return N/A
         */
        @Override
        public final BitReader<Double> nullable() {
            throw new UnsupportedOperationException("unsupported; use getInstanceNullable()");
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
            extends SignBitOnly {

        private static final class Holder {

            private static final DoubleReader INSTANCE = new CompressedZero();

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
    }

    /**
     * A reader for reading values representing infinities.
     *
     * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
     * @see DoubleWriter.CompressedInfinity
     */
    public static final class CompressedInfinity
            extends SignBitOnly {

        private static final class Holder {

            private static final DoubleReader INSTANCE = new CompressedInfinity();

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
         * Returns the instance of this class which is singletone.
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
        public Double read(final BitInput input) throws IOException {
            return Double.longBitsToDouble(readBits(input) | DoubleConstants.MASK_EXPONENT);
        }
    }

    public static class CompressedNaN
            extends DoubleReader {

        private static final Map<DoubleKey, CompressedNaN> CACHED_INSTANCES = new WeakHashMap<>();

        static CompressedNaN getCachedInstance(final int significandSize) {
            return CACHED_INSTANCES.computeIfAbsent(
                    DoubleKey.withSignificandSizeOnly(significandSize),
                    k -> new CompressedNaN(k.significandSize)
            );
        }

        /**
         * Returns the instance for specified significand size.
         *
         * @param significandSize the number of bits for the significand part.
         */
        public CompressedNaN(final int significandSize) {
            super(DoubleConstants.SIZE_MIN_EXPONENT, significandSize);
        }

        @Override
        public Double read(final BitInput input) throws IOException {
            final long significandBits = readSignificandBits(input, significandSize);
            assert significandBits >= 0L;
            if (significandBits == 0L) {
                throw new IOException("significand bits are all zeros");
            }
            return Double.longBitsToDouble(significandBits | DoubleConstants.MASK_EXPONENT);
        }
    }

    private static long readExponentBits(final BitInput input, final int size) throws IOException {
        return (input.readLong(false, size) << DoubleConstants.SIZE_SIGNIFICAND) & DoubleConstants.MASK_EXPONENT;
    }

    private static long readSignificandBits(final BitInput input, int size) throws IOException {
        long bits = input.readLong(true, 1) << (DoubleConstants.SIZE_SIGNIFICAND - 1);
        if (--size > 0) {
            bits |= input.readLong(true, size);
        }
        return bits;
    }

    static double read(final BitInput input, final int exponentSize, final int significandSize) throws IOException {
        DoubleConstraints.requireValidExponentSize(exponentSize);
        DoubleConstraints.requireValidSignificandSize(significandSize);
        if (exponentSize == DoubleConstants.SIZE_EXPONENT && significandSize == DoubleConstants.SIZE_SIGNIFICAND) {
            return Double.longBitsToDouble(input.readLong(false, Long.SIZE));
        }
        long bits = input.readLong(true, 1) << DoubleConstants.SHIFT_SIGN_BIT;
        bits |= readExponentBits(input, exponentSize);
        bits |= readSignificandBits(input, significandSize);
        return Double.longBitsToDouble(bits);
    }

    private static final Map<DoubleKey, DoubleReader> CACHED_INSTANCES = new WeakHashMap<>();

    static DoubleReader getCachedInstance(final int exponentSize, final int significandSize) {
        return CACHED_INSTANCES.computeIfAbsent(
                new DoubleKey(exponentSize, significandSize),
                k -> new DoubleReader(k.exponentSize, k.significandSize)
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
