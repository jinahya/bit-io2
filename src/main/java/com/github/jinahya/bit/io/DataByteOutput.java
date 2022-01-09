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

import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A byte output writes bytes to an instance of {@link DataOutput}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DataByteInput
 */
public class DataByteOutput
        extends ByteOutputAdapter<DataOutput> {

    /**
     * Creates a new instance writes bytes to specified data output.
     *
     * @param target the data output to which bytes are written.
     * @return a new instance.
     */
    public static DataByteOutput from(final DataOutput target) {
        Objects.requireNonNull(target, "target is null");
        final DataByteOutput instance = new DataByteOutput(BitIoUtils.empty());
        instance.target(target);
        return instance;
    }

    /**
     * Creates a new instance with specified target supplier.
     *
     * @param targetSupplier the target supplier.
     */
    public DataByteOutput(final Supplier<? extends DataOutput> targetSupplier) {
        super(targetSupplier);
    }

    /**
     * {@inheritDoc} The {@code write(DataOutput, int)} method of {@code DataByteOutput} class invokes {@link
     * DataOutput#writeByte(int)} method on data output with specified value.
     *
     * @param target {@inheritDoc}
     * @param value  {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @see DataOutput#write(int)
     */
    @Override
    protected void write(final DataOutput target, final int value) throws IOException {
        target.writeByte(value);
    }
}
