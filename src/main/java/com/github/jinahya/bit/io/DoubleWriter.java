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

public class DoubleWriter
        extends DoubleBase
        implements BitWriter<Double> {

    static void writeZero(final BitOutput output, final double value) throws IOException {
        output.writeLong(true, 1, Double.doubleToRawLongBits(value) >> (Integer.SIZE - 1));
    }

    static void writeExponent(final BitOutput output, final int size, final long bits) throws IOException {
        output.writeLong(false, size, ((bits << 1) >> 1) >> DoubleConstants.SIZE_SIGNIFICAND_IEEE754);
    }

    static void writeSignificand(final BitOutput output, int size, final long bits) throws IOException {
        output.writeLong(true, 1, bits >> (DoubleConstants.SIZE_SIGNIFICAND_IEEE754 - 1));
        if (--size > 0) {
            output.writeLong(true, size, bits);
        }
    }

    public DoubleWriter(final int exponentSize, final int significandPrecisionSize) {
        super(exponentSize, significandPrecisionSize);
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
        writeExponent(output, exponentSize, bits);
    }

    protected void writeSignificand(final BitOutput output, final long bits) throws IOException {
        writeSignificand(output, significandSize, bits);
    }
}
