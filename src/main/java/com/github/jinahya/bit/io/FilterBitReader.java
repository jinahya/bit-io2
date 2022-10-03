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
import java.util.function.Function;

/**
 * A value reader for reading filtered values.
 *
 * @param <T> filtered value type parameter
 * @param <U> original value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see FilterBitWriter
 */
public abstract class FilterBitReader<T, U>
        implements BitReader<T> {

    /**
     * Creates a new instance which reads filtered values.
     *
     * @param delegate a reader for reading original values.
     * @param mapper   a mapper for filtering values.
     * @param <T>      filtered value type parameter
     * @param <U>      original value type parameter
     * @return a new instance.
     * @see FilterBitWriter#mapping(BitWriter, Function)
     */
    public static <T, U> FilterBitReader<T, U> mapping(final BitReader<? extends U> delegate,
                                                       final Function<? super U, ? extends T> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return new FilterBitReader<T, U>(delegate) {
            @Override
            protected T filter(final U value) {
                return mapper.apply(value);
            }
        };
    }

    static final class Nullable<T>
            extends FilterBitReader<T, T> {

        Nullable(final BitReader<? extends T> delegate) {
            super(delegate);
        }

        @Override
        public T read(final BitInput input) throws IOException {
            Objects.requireNonNull(input, "input is null");
            final boolean nonnull = input.readBoolean();
            if (nonnull) {
                return super.read(input);
            }
            return null;
        }

        @Override
        protected T filter(final T value) {
            return value;
        }
    }

    /**
     * Creates a new instance which wraps specified delegate.
     *
     * @param delegate the delegate to wrap.
     */
    protected FilterBitReader(final BitReader<? extends U> delegate) {
        super();
        this.delegate = Objects.requireNonNull(delegate, "delegate is null");
    }

    @Override
    public T read(final BitInput input) throws IOException {
        return filter(delegate.read(input));
    }

    /**
     * Maps specified original value read from the {@link #delegate}.
     *
     * @param value the value to map.
     * @return a mapped value.
     */
    protected abstract T filter(final U value);

    /**
     * The reader for reading original values.
     */
    protected final BitReader<? extends U> delegate;
}
