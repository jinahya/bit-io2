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
import java.util.Objects;

/**
 * A class for writing arrays of a specific type.
 *
 * @param <T> array type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ArrayBitReader
 */
public class ArrayBitWriter<T>
        extends FilterBitWriter<T[], T> {

    /**
     * Creates a new instance with specified number of bits for {@code length}.
     *
     * @param lengthSize the number of bits for the {@code length}.
     */
    ArrayBitWriter(final int lengthSize, final BitWriter<? super T> elementWriter) {
        super(elementWriter);
        this.lengthSize = BitIoConstraints.requireValidSizeForUnsignedInt(lengthSize);
    }

    @Override
    public void write(final BitOutput output, final T[] value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        Objects.requireNonNull(value, "value is null");
        final int length = BitIoUtils.writeCount(output, lengthSize, value.length);
        for (int i = 0; i < length; i++) {
            getWriter().write(output, value[i]);
        }
    }

    private final int lengthSize;
}
