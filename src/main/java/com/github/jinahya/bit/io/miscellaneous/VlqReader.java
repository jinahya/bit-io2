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

/**
 * A reader for reading VLQ-encoded values.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class VlqReader
        implements IntReader, LongReader {

    private static final class InstanceHolder {

        static final VlqReader INSTANCE = new VlqReader();

        private InstanceHolder() {
            throw new AssertionError("instantiation is not allowed");
        }
    }

    /**
     * Returns the instance of this writer. The {@code VlqReader} is singleton.
     *
     * @return the instance of this writer.
     */
    public static VlqReader getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Creates a new instance.
     */
    protected VlqReader() {
        super();
    }

    @Override
    public long readLong(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        long value = 0L;
        while (true) {
            final int last = input.readInt(true, 1);
            value <<= 7;
            value |= input.readLong(true, 7);
            if (last == 0) {
                break;
            }
        }
        return value;
    }

    @Override
    public int readInt(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        return Math.toIntExact(readLong(input));
    }
}
