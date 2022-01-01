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

import static java.util.Objects.requireNonNull;

/**
 * A value adapter for reading/writing an array of {@code long}s.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class LongArrayAdapter
        implements ValueAdapter<long[]> {

    /**
     * Returns a new instance for reading/writing arrays of unsigned {@code long}s.
     *
     * @param lengthSize  the number of bits for array length.
     * @param elementSize the number of bits for array elements.
     * @return a new instance.
     */
    public static ValueAdapter<long[]> unsigned(final int lengthSize, final int elementSize) {
        return new LongArrayAdapter(new LongArrayReader.Unsigned(lengthSize, elementSize),
                                    new LongArrayWriter.Unsigned(lengthSize, elementSize));
    }

    LongArrayAdapter(final ValueReader<long[]> reader, final ValueWriter<long[]> writer) {
        super();
        this.reader = requireNonNull(reader, "reader is null");
        this.writer = requireNonNull(writer, "writer is null");
    }

    /**
     * Creates a new instance with specified arguments.
     *
     * @param lengthSize  the number of bits for the length of the array; between {@code 1} (inclusive) and {@value
     *                    Integer#SIZE} (exclusive).
     * @param elementSize the number of bits for each element in the array; between {@code 1} and {@value Byte#SIZE},
     *                    both inclusive.
     */
    public LongArrayAdapter(final int lengthSize, final int elementSize) {
        this(new LongArrayReader(lengthSize, elementSize), new LongArrayWriter(lengthSize, elementSize));
    }

    /**
     * Reads an array of bytes from specified input.
     *
     * @param input the input from which the value is read.
     * @return an array of bytes.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public long[] read(final BitInput input) throws IOException {
        requireNonNull(input, "input is null");
        return reader.read(input);
    }

    /**
     * Writes specified array to specified output.
     *
     * @param output the output to which the array is written.
     * @param value  the array to write.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void write(final BitOutput output, final long[] value) throws IOException {
        requireNonNull(output, "output is null");
        writer.write(output, value);
    }

    private ValueReader<long[]> reader;

    private ValueWriter<long[]> writer;
}
