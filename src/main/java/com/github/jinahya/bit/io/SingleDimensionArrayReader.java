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
import java.lang.reflect.Array;
import java.util.Objects;

class SingleDimensionArrayReader<E>
        implements BitReader<E[]> {

    SingleDimensionArrayReader(final int lengthSize, final Class<E> componentType,
                               final BitReader<? extends E> elementReader) {
        super();
        this.lengthSize = BitIoConstraints.requireValidSizeForUnsignedInt(lengthSize);
        this.componentType = Objects.requireNonNull(componentType, "componentType is null");
        this.elementReader = Objects.requireNonNull(elementReader, "elementReader is null");
    }

    @Override
    public E[] read(final BitInput input) throws IOException {
        final E[] value;
        final int length = BitIoUtils.readCount(input, lengthSize);
        value = (E[]) Array.newInstance(componentType, length);
        for (int i = 0; i < value.length; i++) {
            value[i] = elementReader.read(input);
        }
        return value;
    }

    private final int lengthSize;

    private final Class<E> componentType;

    private final BitReader<? extends E> elementReader;
}
