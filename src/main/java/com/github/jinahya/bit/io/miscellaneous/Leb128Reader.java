package com.github.jinahya.bit.io.miscellaneous;

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

import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.IntReader;
import com.github.jinahya.bit.io.LongReader;

import java.io.IOException;
import java.util.Objects;

public abstract class Leb128Reader
        implements IntReader, LongReader {

    public static class OfUnsigned
            extends Leb128Reader {

        /**
         * Creates a new instance.
         */
        public OfUnsigned() {
            super();
        }

        @Override
        public long readLong(final BitInput input) throws IOException {
            Objects.requireNonNull(input, "input is null");
            long value = 0L;
            for (int shift = 0; ; shift += 7) {
                final int group = input.readInt(true, Byte.SIZE);
                value |= ((group & 0x7FL) << shift);
                if ((group & 0x80) == 0) {
                    break;
                }
            }
            return value;
        }
    }

    public static class OfSigned
            extends Leb128Reader {

        /**
         * Creates a new instance.
         */
        public OfSigned() {
            super();
        }

        @Override
        public long readLong(final BitInput input) throws IOException {
            Objects.requireNonNull(input, "input is null");
            long value = 0L;
            for (int shift = 0; ; ) {
                final int group = input.readInt(true, Byte.SIZE);
                value |= ((group & 0x7FL) << shift);
                shift += 7;
                if ((group & 0x80) == 0) {
                    if (shift < Long.SIZE && (group & 0x40) != 0) {
                        value |= (~0L << shift);
                    }
                    break;
                }
            }
            return value;
        }
    }

    protected Leb128Reader() {
        super();
    }

    @Override
    public int readInt(final BitInput input) throws IOException {
        return Math.toIntExact(readLong(input));
    }
}
