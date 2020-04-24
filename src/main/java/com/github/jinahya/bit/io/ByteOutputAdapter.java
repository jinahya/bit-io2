package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
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
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public abstract class ByteOutputAdapter<T> implements ByteOutput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with specified target supplier.
     *
     * @param targetSupplier the target supplier.
     */
    public ByteOutputAdapter(final Supplier<? extends T> targetSupplier) {
        super();
        this.targetSupplier = requireNonNull(targetSupplier, "targetSupplier is null");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void write(final int value) throws IOException {
        write(target(), value);
    }

    /**
     * Writes specified unsigned {@value Byte#SIZE}-bit integer to specified target.
     *
     * @param target the target.
     * @param value  the unsigned {@value Byte#SIZE}-bit integer to write.
     * @throws IOException if an I/O error occurs.
     */
    protected abstract void write(T target, int value) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------
    private T target() {
        if (target == null) {
            target = targetSupplier.get();
        }
        return target;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final Supplier<? extends T> targetSupplier;

    private transient T target;
}
