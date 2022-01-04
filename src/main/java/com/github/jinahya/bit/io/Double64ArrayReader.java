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
import java.util.function.UnaryOperator;

class Double64ArrayReader
        extends PrimitiveArrayReader<double[]> {

    public static double[] readFrom(final BitInput input, final UnaryOperator<BitReader<double[]>> operator)
            throws IOException {
        Objects.requireNonNull(input, "input is null");
        Objects.requireNonNull(operator, "operator is null");
        return operator.apply(new Double64ArrayReader(31)).read(input);
    }

    public static double[] readFrom(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        return readFrom(input, UnaryOperator.identity());
    }

    public static double[] readNullableFrom(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        return readFrom(input, BitReader::nullable);
    }

    /**
     * Creates a new instance with specified length-size.
     *
     * @param lengthSize the number of bits for the {@code length} of an array.
     */
    public Double64ArrayReader(final int lengthSize) {
        super(lengthSize);
    }

    @Override
    public double[] read(final BitInput input) throws IOException {
        final int length = readLength(input);
        final double[] value = new double[length];
        readElements(input, value);
        return value;
    }

    void readElements(final BitInput input, final double[] elements) throws IOException {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = readElement(input);
        }
    }

    double readElement(final BitInput input) throws IOException {
        return input.readDouble64();
    }
}
