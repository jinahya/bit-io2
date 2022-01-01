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

class LongArrayReader
        extends SequenceValueReader<long[]> {

    public static class Unsigned
            extends LongArrayReader {

        public Unsigned(final int lengthSize, final int elementSize) {
            super(lengthSize, requireValidSizeForUnsignedLong(elementSize));
        }

        @Override
        long readElement(final BitInput input) throws IOException {
            return input.readUnsignedLong(elementSize);
        }
    }

    public LongArrayReader(final int lengthSize, final int elementSize) {
        super(lengthSize);
        this.elementSize = requireValidSizeForLong(elementSize);
    }

    @Override
    public long[] read(final BitInput input) throws IOException {
        final int length = readLength(input);
        final long[] value = new long[length];
        for (int i = 0; i < value.length; i++) {
            value[i] = readElement(input);
        }
        return value;
    }

    long readElement(final BitInput input) throws IOException {
        return input.readLong(elementSize);
    }

    final int elementSize;
}
