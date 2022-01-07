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

class Double64ArrayWriter
        extends PrimitiveArrayWriter<double[]> {

    public static void writeTo(final BitOutput output, final UnaryOperator<BitWriter<double[]>> operator,
                               final double[] value)
            throws IOException {
        Objects.requireNonNull(output, "output is null");
        operator.apply(new Double64ArrayWriter(31)).write(output, value);
    }

    public static void writeTo(final BitOutput output, final double[] value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        Objects.requireNonNull(value, "value is null");
        writeTo(output, UnaryOperator.identity(), value);
    }

    public static void writeNullableTo(final BitOutput output, final double[] value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        writeTo(output, BitWriter::nullable, value);
    }

    /**
     * Creates a new instance with specified length-size.
     *
     * @param lengthSize the number of bits for the {@code length} of an array.
     */
    public Double64ArrayWriter(final int lengthSize) {
        super(lengthSize);
    }

    @Override
    public void write(final BitOutput output, final double[] value) throws IOException {
        writeLength(output, value.length);
        writeElements(output, value);
    }

    void writeElements(final BitOutput output, final double[] elements) throws IOException {
        for (final double element : elements) {
            writeElement(output, element);
        }
    }

    void writeElement(final BitOutput output, final double element) throws IOException {
        output.writeDouble64(element);
    }
}