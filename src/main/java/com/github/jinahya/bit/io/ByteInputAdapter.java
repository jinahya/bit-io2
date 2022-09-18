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

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * An abstract class implements {@link ByteInput} for adapting a specific type of byte source.
 *
 * @param <T> byte source type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ByteOutputAdapter
 */
public abstract class ByteInputAdapter<T>
        implements ByteInput {

    /**
     * Creates a new instance with specified byte-source supplier.
     *
     * @param supplier the byte-source supplier.
     */
    protected ByteInputAdapter(final Supplier<? extends T> supplier) {
        super();
        this.supplier = Objects.requireNonNull(supplier, "supplier is null");
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException {@inheritDoc}
     * @implNote The {@code close()} method of {@code ByteInputAdapter} class invokes {@link Closeable#close()} method
     * on the byte source which may not have been initialized yet in which case the method does nothing.
     */
    @Override
    public void close() throws IOException {
        ByteInput.super.close();
        if (close) {
            final T source = source(false);
            if (source instanceof Closeable) {
                ((Closeable) source).close();
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @implNote The {@code read()} method of {@code ByteInputAdapter} class invokes {@link #read(Object)} with a byte
     * source and returns the result.
     */
    @Override
    public int read() throws IOException {
        return read(source(true));
    }

    /**
     * Reads an {@value java.lang.Byte#SIZE}-bit <em>unsigned</em> {@code int} value from specified source.
     *
     * @param source the source from which an {@value java.lang.Byte#SIZE}-bit <em>unsigned</em> {@code int} value is
     *               read.
     * @return an {@value java.lang.Byte#SIZE}-bit <em>unsigned</em> {@code int} value read from the {@code source};
     * between {@code 0} and {@code 255}, both inclusive.
     * @throws IOException if an I/O error occurs.
     */
    protected abstract int read(T source) throws IOException;

    /**
     * Returns the byte source.
     *
     * @param get a flag for getting it from the {@link #supplier} when it's not already set; {@code true} for getting
     *            it from the {@link #supplier}, {@code false} for returning the value as is, in which case the result
     *            may be {@code null}.
     * @return the byte source; {@code null} when it's not set yet.
     */
    T source(final boolean get) {
        if (get) {
            if (source(false) == null) {
                source(supplier.get());
                close = true;
            }
            return source(false);
        }
        return source;
    }

    /**
     * Sets specified byte source.
     *
     * @param source the byte source.
     * @throws IllegalStateException then there is already a byte source set.
     */
    void source(final T source) {
        if (source(false) != null) {
            throw new IllegalStateException("source already has been set");
        }
        this.source = Objects.requireNonNull(source, "source is null");
    }

    private final Supplier<? extends T> supplier;

    private T source = null;

    /**
     * A flag for closing the {@link #source} within the {@link #close()} method; indicates that the {@link #source} is
     * supplied from the {@link #supplier}, not directly set via {@link #source(Object)}  method.
     */
    private boolean close = false;
}
