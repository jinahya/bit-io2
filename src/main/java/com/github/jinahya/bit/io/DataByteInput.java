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

import java.io.DataInput;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A byte input reads bytes from an instance of {@link DataInput}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see DataByteOutput
 */
public class DataByteInput
        extends ByteInputAdapter<DataInput> {

    /**
     * Creates a new instance reads byte from specified data input.
     *
     * @param source the data input from which bytes are read.
     * @return a new instance.
     * @apiNote Closing the result input does not close the {@code source}.
     */
    public static DataByteInput from(final DataInput source) {
        Objects.requireNonNull(source, "source is null");
        final DataByteInput instance = new DataByteInput(BitIoUtils.emptySupplier());
        instance.source(source);
        return instance;
    }

    /**
     * Creates a new instance with specified source supplier.
     *
     * @param sourceSupplier the source supplier.
     */
    public DataByteInput(final Supplier<? extends DataInput> sourceSupplier) {
        super(sourceSupplier);
    }

    /**
     * {@inheritDoc} The {@code read(DataInput)} method of {@link DataByteInput} class invokes {@link
     * DataInput#readUnsignedByte()} method on specified data input and returns the result.
     *
     * @param source {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @see DataInput#readUnsignedByte()
     */
    @Override
    protected int read(final DataInput source) throws IOException {
        return source.readUnsignedByte();
    }
}
