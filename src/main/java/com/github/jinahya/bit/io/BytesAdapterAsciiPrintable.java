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

/**
 * A value adapter for reading/writing an array of printable ascii characters.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
class BytesAdapterAsciiPrintable
        extends BytesAdapterAscii {

    BytesAdapterAsciiPrintable(final int lengthSize) {
        super(lengthSize);
    }

    @Override
    byte readByte(final BitInput input) throws IOException {
        final int e = input.readUnsignedInt(1);
        if (e == 0b0) {
            return (byte) (input.readUnsignedInt(6) + 0x20);
        }
        return (byte) (input.readUnsignedInt(5) + 0x60);
    }

    @Override
    void writeByte(final BitOutput output, final byte value) throws IOException {
        if (value < 0x60) {
            output.writeUnsignedInt(1, 0);
            output.writeUnsignedInt(6, value - 0x20);
            return;
        }
        output.writeUnsignedInt(1, 1);
        output.writeUnsignedInt(5, value - 0x60);
    }
}
