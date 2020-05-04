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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static java.util.Objects.requireNonNull;

/**
 * A value adapter for reading/writing a list.
 *
 * @param <T> element type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class ListAdapter<T> implements ValueAdapter<List<T>> {

    /**
     * Creates a new instance which can read/write a list whose {@code size} can be, in maximum, expressed as {@value
     * java.lang.Byte#SIZE}-bit unsigned int.
     *
     * @param elementAdapter a value adapter for reading/writing list elements.
     * @param <T>            element type parameter
     * @return a new instance.
     */
    public static <T> ListAdapter<T> listAdapter8(final ValueAdapter<T> elementAdapter) {
        return new ListAdapter<>(Byte.SIZE, elementAdapter);
    }

    /**
     * Creates a new instance which can read/write a list whose {@code size} can be, in maximum, expressed as {@value
     * java.lang.Short#SIZE}-bit unsigned int.
     *
     * @param elementAdapter a value adapter for reading/writing list elements.
     * @param <T>            element type parameter
     * @return a new instance.
     */
    public static <T> ListAdapter<T> listAdapter16(final ValueAdapter<T> elementAdapter) {
        return new ListAdapter<>(Short.SIZE, elementAdapter);
    }

    /**
     * Creates a new instance which can read/write a list whose {@code size} can be, in maximum, expressed as {@code
     * 24}-bit unsigned int.
     *
     * @param elementAdapter a value adapter for reading/writing list elements.
     * @param <T>            element type parameter
     * @return a new instance.
     */
    public static <T> ListAdapter<T> listAdapter24(final ValueAdapter<T> elementAdapter) {
        return new ListAdapter<>(Byte.SIZE + Short.SIZE, elementAdapter);
    }

    /**
     * Creates a new instance which can read/write a list whose {@code size} can be, in maximum, expressed as {@code
     * 31}-bit unsigned int.
     *
     * @param elementAdapter a value adapter for reading/writing list elements.
     * @param <T>            element type parameter
     * @return a new instance.
     */
    public static <T> ListAdapter<T> listAdapter31(final ValueAdapter<T> elementAdapter) {
        return new ListAdapter<>(Integer.SIZE - 1, elementAdapter);
    }

    /**
     * Creates a new instance with specified arguments.
     *
     * @param lengthSize     the number of bits for reading/writing the number of elements.
     * @param elementAdapter a value adapter for reading/writing list elements.
     */
    public ListAdapter(final int lengthSize, final ValueAdapter<T> elementAdapter) {
        super();
        this.lengthSize = requireValidSizeInt(true, lengthSize);
        this.elementAdapter = requireNonNull(elementAdapter, "elementAdapter is null");
    }

    @Override
    public void write(final BitOutput output, final List<T> value) throws IOException {
        final int length = value.size() & ((1 << lengthSize) - 1);
        assert length <= value.size();
        output.writeUnsignedInt(lengthSize, length);
        final Iterator<T> i = value.iterator();
        for (int l = 0; l < length; l++) {
            elementAdapter.write(output, i.next());
        }
    }

    @Override
    public List<T> read(final BitInput input) throws IOException {
        final int length = input.readUnsignedInt(lengthSize);
        final List<T> value = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            value.add(elementAdapter.read(input));
        }
        return value;
    }

    private final int lengthSize;

    private final ValueAdapter<T> elementAdapter;
}
