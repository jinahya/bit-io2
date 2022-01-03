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

class SingleDimensionArrayWriter<E>
        implements BitWriter<E[]> {

    SingleDimensionArrayWriter(final int lengthSize, final Class<E> componentType,
                               final BitWriter<? super E> elementWriter) {
        super();
        this.lengthSize = BitIoConstraints.requireValidSizeForUnsignedInt(lengthSize);
        this.componentType = Objects.requireNonNull(componentType, "componentType is null");
        this.elementWriter = Objects.requireNonNull(elementWriter, "elementWriter is null");
    }

    @Override
    public void write(final BitOutput output, final E[] value) throws IOException {
        BitIoUtils.writeCount(output, lengthSize, value.length);
        for (final E element : value) {
            elementWriter.write(output, element);
        }
    }

    private final int lengthSize;

    private final Class<E> componentType;

    private final BitWriter<? super E> elementWriter;
}
