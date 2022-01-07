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

class IntArrayWriter
        extends PrimitiveArrayWriter<int[]> {

    static class Unsigned
            extends IntArrayWriter {

        Unsigned(final int lengthSize, final int elementSize) {
            super(lengthSize, BitIoConstraints.requireValidSizeForUnsignedInt(elementSize));
        }

        @Override
        void writeElement(final BitOutput output, final int value) throws IOException {
            output.writeUnsignedInt(elementSize, value);
        }
    }

    public IntArrayWriter(final int lengthSize, final int elementSize) {
        super(lengthSize);
        this.elementSize = BitIoConstraints.requireValidSizeForInt(elementSize);
    }

    @Override
    public void write(final BitOutput output, final int[] value) throws IOException {
        writeLength(output, value.length);
        writeElements(output, value);
    }

    void writeElements(final BitOutput output, final int[] elements) throws IOException {
        for (final int element : elements) {
            writeElement(output, element);
        }
    }

    void writeElement(final BitOutput output, final int element) throws IOException {
        output.writeInt(elementSize, element);
    }

    final int elementSize;
}
