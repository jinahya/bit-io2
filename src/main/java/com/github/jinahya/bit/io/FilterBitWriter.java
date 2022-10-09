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
 * A value writer for writing filtered values.
 *
 * @param <T> original value type parameter
 * @param <U> filtered value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see FilterBitReader
 */
public abstract class FilterBitWriter<T, U>
        implements BitWriter<T> {

    /**
     * Creates a new instance which writes filtered values.
     *
     * @param delegate a value writer for writing filtered values.
     * @param mapper   a mapper for mapping original values.
     * @param <T>      original value type parameter
     * @param <U>      filtered value type parameter
     * @return a new instance.
     * @see FilterBitReader#mapping(BitReader, Function)
     */
    public static <T, U> FilterBitWriter<T, U> mapping(final BitWriter<? super U> delegate,
                                                       final Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return new FilterBitWriter<T, U>(delegate) {
            @Override
            protected U filter(final T value) {
                return mapper.apply(value);
            }
        };
    }

    static final class Nullable<T>
            extends FilterBitWriter<T, T> {

        Nullable(final BitWriter<? super T> delegate) {
            super(delegate);
        }

        @Override
        public BitWriter<T> nullable() {
            throw new UnsupportedOperationException(BitIoConstants.MESSAGE_UNSUPPORTED_ALREADY_NULLABLE);
        }

        @Override
        public void write(final BitOutput output, final T value) throws IOException {
            final boolean nonnull = value != null;
            output.writeBoolean(nonnull);
            if (nonnull) {
                super.write(output, value);
            }
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
    protected FilterBitWriter(final BitWriter<? super U> delegate) {
        super();
        this.delegate = Objects.requireNonNull(delegate, "delegate is null");
    }

    @Override
    public void write(final BitOutput output, final T value) throws IOException {
        delegate.write(output, filter(value));
    }

    /**
     * Filters specified original value for writing to the {@link #delegate}.
     *
     * @param value the value to filter.
     * @return a filter value.
     */
    protected abstract U filter(final T value);

    /**
     * The writer for writing filtered values.
     */
    protected final BitWriter<? super U> delegate;
}
