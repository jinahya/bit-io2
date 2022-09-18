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

/**
 * An abstract class implements {@link ByteOutput} for adapting a specific type of byte target.
 *
 * @param <T> byte target type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ByteInputAdapter
 */
public abstract class ByteOutputAdapter<T>
        implements ByteOutput {

    /**
     * Creates a new instance with specified byte target.
     *
     * @param target the byte target.
     */
    protected ByteOutputAdapter(final T target) {
        super();
        this.target = Objects.requireNonNull(target, "target is null");
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
        ByteOutput.super.flush();
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
        flush();
        ByteOutput.super.close();
        if (target instanceof Closeable) {
            ((Closeable) target).close();
        }
    }

    final T target;
}
