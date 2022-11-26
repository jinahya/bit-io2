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

import com.github.jinahya.bit.io.BitOutput;
import com.github.jinahya.bit.io.IntWriter;
import com.github.jinahya.bit.io.LongWriter;

import java.io.IOException;
import java.util.Objects;

public abstract class Leb128Writer
        implements IntWriter, LongWriter {

    public static class OfUnsigned
            extends Leb128Writer {

        /**
         * Creates a new instance.
         */
        public OfUnsigned() {
            super();
        }

        @Override
        public void writeLong(final BitOutput output, long value) throws IOException {
            Objects.requireNonNull(output, "output is null");
            if (value < 0L) {
                throw new IllegalArgumentException("negative value: " + value);
            }
            while (true) {
                final int group = (int) (value & 0x7FL);
                final boolean last = (value >>= 7) == 0L;
                output.writeInt(true, 1, last ? 0 : 1);
                output.writeInt(true, 7, group);
                if (last) {
                    break;
                }
            }
        }
    }

    public static class OfSigned
            extends Leb128Writer {

        /**
         * Creates a new instance.
         */
        public OfSigned() {
            super();
        }

        @Override
        public void writeLong(final BitOutput output, long value) throws IOException {
            Objects.requireNonNull(output, "output is null");
            while (true) {
                final int group = (int) (value & 0x7FL);
                value >>= 7;
                final boolean clear = (group & 0x40) == 0;
                if ((value == 0 && clear) || (value == -1 && !clear)) {
                    output.writeInt(true, Byte.SIZE, group);
                    return;
                }
                output.writeInt(true, Byte.SIZE, (group | 0x80));
            }
        }
    }

    protected Leb128Writer() {
        super();
    }

    @Override
    public void writeInt(final BitOutput output, final int value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        writeLong(output, value);
    }
}
