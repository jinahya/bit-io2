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
 * A value adapter for reading/writing an array of bytes.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class ByteArrayAdapter
        implements ValueAdapter<byte[]> {

    public static ByteArrayAdapter unsigned(final int lengthSize, final int elementSize) {
        return new ByteArrayAdapter(new ByteArrayReaderUnsigned(lengthSize, elementSize),
                                    new ByteArrayWriterUnsigned(lengthSize, elementSize));
    }

    ByteArrayAdapter(final ValueReader<byte[]> reader, final ValueWriter<byte[]> writer) {
        super();
        this.reader = requireNonNull(reader, "reader is null");
        this.writer = requireNonNull(writer, "writer is null");
    }

    /**
     * Creates a new instance with specified arguments.
     *
     * @param lengthSize  the number of bits for the length of the array; between {@code 1} (inclusive) and {@value
     *                    java.lang.Integer#SIZE} (exclusive).
     * @param elementSize the number of bits for each element in the array; between {@code 1} and {@value
     *                    java.lang.Byte#SIZE}, both inclusive.
     */
    public ByteArrayAdapter(final int lengthSize, final int elementSize) {
        this(new ByteArrayReader(lengthSize, elementSize),
             new ByteArrayWriter(lengthSize, elementSize));
    }

    /**
     * Reads an array of bytes from specified input.
     *
     * @param input the input from which the value is read.
     * @return an array of bytes.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public byte[] read(final BitInput input) throws IOException {
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
    public void write(final BitOutput output, final byte[] value) throws IOException {
        requireNonNull(output, "output is null");
        writer.write(output, value);
    }

    private final ValueReader<byte[]> reader;

    private final ValueWriter<byte[]> writer;
}
