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
import java.util.Arrays;
import java.util.Objects;

/**
 * An abstract reader class for reading an array of primitive values.
 *
 * @param <E> element type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ArrayWriter
 */
class ArrayReader<E>
        implements BitReader<Object> {

    /**
     * Creates a new instance with specified arguments.
     *
     * @param dimensionSizes the number of bits for the {@code length}.
     * @param elementReader  a bit-reader for reading each element.
     */
    protected ArrayReader(final int[] dimensionSizes, final Class<E> componentType,
                          final BitReader<? extends E> elementReader) {
        super();
        this.dimensionSizes = Arrays.copyOf(Objects.requireNonNull(dimensionSizes, "dimensionSizes is null"),
                                            dimensionSizes.length);
        if (this.dimensionSizes.length == 0) {
            throw new IllegalArgumentException("dimensionSizes.length is zero");
        }
        if (this.dimensionSizes.length > 255) {
            throw new IllegalArgumentException("dimensionSizes.length(" + this.dimensionSizes.length + ") > 255");
        }
        for (final int dimensionSize : this.dimensionSizes) {
            BitIoConstraints.requireValidSizeForUnsignedInt(dimensionSize);
        }
        this.componentType = Objects.requireNonNull(componentType, "componentType is null");
        this.elementReader = Objects.requireNonNull(elementReader, "elementReader is null");
    }

    private Object read(final BitInput input, final Object array, final int... dimensions) throws IOException {
        Objects.requireNonNull(input, "input is null");
        Objects.requireNonNull(array, "array is null");
        Objects.requireNonNull(dimensions, "dimensions is null");
        if (dimensions.length == 0) {
            throw new IllegalArgumentException("dimensions.length is zero");
        }
        if (dimensions.length == 1) {

        }
        final int[] dimensions = new int[dimensionSizes.length];
        for (int i = 0; i < dimensions.length; i++) {
            dimensions[i] = input.readUnsignedInt(dimensionSizes[i]);
        }
        final Object value = Array.newInstance(componentType, dimensions);
        for (int i = 1; i < dimensions.length; i++) {
        }
        return value;
    }

    @Override
    public Object read(final BitInput input) throws IOException {
        final int[] dimensions = new int[dimensionSizes.length];
        for (int i = 0; i < dimensions.length; i++) {
            dimensions[i] = input.readUnsignedInt(dimensionSizes[i]);
        }
        final Object value = Array.newInstance(componentType, dimensions);
        for (int i = 1; i < dimensions.length; i++) {
        }
        return value;
    }

    private final int[] dimensionSizes;

    private final Class<E> componentType;

    protected final BitReader<? extends E> elementReader;
}
