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
import java.io.Flushable;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * An abstract class implements {@link ByteOutput} for adapting a specific type of byte-target.
 *
 * @param <T> byte target parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ByteInputAdapter
 */
public abstract class ByteOutputAdapter<T>
        implements ByteOutput {

    /**
     * Creates a new instance with specified target supplier.
     *
     * @param targetSupplier the target supplier.
     */
    protected ByteOutputAdapter(final Supplier<? extends T> targetSupplier) {
        super();
        this.targetSupplier = Objects.requireNonNull(targetSupplier, "targetSupplier is null");
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException {@inheritDoc}
     * @implNote The {@code flush()} method of {@code ByteOutputAdapter} class invokes {@link Flushable#flush()} method
     * on the byte target which may not have been initialized yet in which case this method does nothing.
     */
    @Override
    public void flush() throws IOException {
        ByteOutput.super.flush(); // does nothing.
        final T target = target(false);
        if (target instanceof Flushable) {
            ((Flushable) target).flush();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException {@inheritDoc}
     * @implNote The {@code close()} method of {@code ByteOutputAdapter} class invokes {@link Closeable#close()} method
     * on the byte-target which may not have been initialized yet in which case this method does nothing.
     */
    @Override
    public void close() throws IOException {
        ByteOutput.super.close(); // does nothing.
        if (closeTarget) {
            final T target = target(false);
            if (target instanceof Closeable) {
                ((Closeable) target).close();
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @implNote The {@code write(int)} method of {@code ByteOutputAdapter} class invokes {@link #write(Object, int)}
     * method with a byte target and specified value.
     */
    @Override
    public void write(final int value) throws IOException {
        write(target(true), value);
    }

    /**
     * Writes specified {@value java.lang.Byte#SIZE}-bit unsigned {@code int} value to specified target.
     *
     * @param target the target to which the {@code value} is written.
     * @param value  the {@value java.lang.Byte#SIZE}-bit unsigned {@code int} value to write; between {@code 0} and
     *               {@code 255}, both inclusive.
     * @throws IOException if an I/O error occurs.
     */
    protected abstract void write(T target, int value) throws IOException;

    T target(final boolean get) {
        if (get) {
            if (target(false) == null) {
                target(targetSupplier.get());
                closeTarget = true;
            }
            return target(false);
        }
        return target;
    }

    void target(final T target) {
        if (target(false) != null) {
            throw new IllegalStateException("target already has been supplied");
        }
        this.target = Objects.requireNonNull(target, "target is null");
    }

    private final Supplier<? extends T> targetSupplier;

    private T target = null;

    private boolean closeTarget = false;
}
