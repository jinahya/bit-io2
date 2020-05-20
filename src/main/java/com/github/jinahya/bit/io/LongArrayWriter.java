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

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForLong;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForUnsignedLong;

class LongArrayWriter extends SequenceValueWriter<long[]> {

    private static class Unsigned extends LongArrayWriter {

        public Unsigned(final int lengthSize, final int elementSize) {
            super(lengthSize, requireValidSizeForUnsignedLong(elementSize));
        }

        @Override
        void writeElement(final BitOutput output, final long value) throws IOException {
            output.writeUnsignedLong(elementSize, value);
        }
    }

    public static LongArrayWriter unsigned(final int lengthSize, final int elementSize) {
        return new Unsigned(lengthSize, elementSize);
    }

    LongArrayWriter(final int lengthSize, final int elementSize) {
        super(lengthSize);
        this.elementSize = requireValidSizeForLong(elementSize);
    }

    @Override
    public void write(final BitOutput output, final long[] value) throws IOException {
        final int length = writeLength(output, value.length);
        for (int i = 0; i < length; i++) {
            writeElement(output, value[i]);
        }
    }

    void writeElement(final BitOutput output, final long value) throws IOException {
        output.writeLong(elementSize, value);
    }

    final int elementSize;
}
