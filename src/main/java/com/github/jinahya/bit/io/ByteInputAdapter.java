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
import java.util.function.IntPredicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * An abstract class implements {@link ByteInput} adapting a specific type of byte source.
 *
 * @param <T> byte source parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ByteOutputAdapter
 */
public abstract class ByteInputAdapter<T> implements ByteInput {

    /**
     * Creates a new instance with specified source supplier.
     *
     * @param sourceSupplier the source supplier.
     */
    public ByteInputAdapter(final Supplier<? extends T> sourceSupplier) {
        super();
        this.sourceSupplier = requireNonNull(sourceSupplier, "sourceSupplier is null");
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        ByteInput.super.close(); // does nothing.
        if (source instanceof Closeable) {
            ((Closeable) source).close();
        }
    }

    /**
     * {@inheritDoc} The {@code read()} method of {@code ByteInputAdapter} class invokes {@link #read(Object)} with a
     * byte source and returns the result.
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        final int octet = read(source());
        octetConsumerAttachable.consumeOctet(octet);
        return octet;
    }

    /**
     * Reads an {@value java.lang.Byte#SIZE}-bit unsigned {@code int} value from specified source.
     *
     * @param source the source from which an {@value java.lang.Byte#SIZE}-bit unsigned {@code int} value is read.
     * @return an {@value java.lang.Byte#SIZE}-bit unsigned {@code int} value read from the {@code source}.
     * @throws IOException if an I/O error occurs.
     */
    protected abstract int read(T source) throws IOException;

    private T source() {
        if (source == null) {
            source = sourceSupplier.get();
        }
        return source;
    }

    @Override
    public boolean attachOctetConsumer(final IntPredicate octetConsumer) {
        return octetConsumerAttachable.attachOctetConsumer(octetConsumer);
    }

    @Override
    public boolean detachOctetConsumer(final IntPredicate octetConsumer) {
        return octetConsumerAttachable.detachOctetConsumer(octetConsumer);
    }

    private final Supplier<? extends T> sourceSupplier;

    private T source;

    private final SimpleOctetConsumerAttachable octetConsumerAttachable = new SimpleOctetConsumerAttachable();
}
