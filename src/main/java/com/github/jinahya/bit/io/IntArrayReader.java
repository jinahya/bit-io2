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

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForInt;

class IntArrayReader extends SequenceValueReader<int[]> {

    public static class Unsigned extends IntArrayReader {

        public Unsigned(final int lengthSize, final int elementSize) {
            super(lengthSize, requireValidSizeForInt(true, elementSize));
        }

        @Override
        int readElement(final BitInput input) throws IOException {
            return input.readUnsignedInt(elementSize);
        }
    }

    IntArrayReader(final int lengthSize, final int elementSize) {
        super(lengthSize);
        this.elementSize = requireValidSizeForInt(false, elementSize);
    }

    @Override
    public int[] read(final BitInput input) throws IOException {
        final int length = readLength(input);
        final int[] value = new int[length];
        for (int i = 0; i < value.length; i++) {
            value[i] = readElement(input);
        }
        return value;
    }

    int readElement(final BitInput input) throws IOException {
        return input.readInt(elementSize);
    }

    final int elementSize;
}
