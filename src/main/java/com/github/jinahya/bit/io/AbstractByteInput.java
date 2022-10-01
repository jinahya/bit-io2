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

import java.util.Objects;

/**
 * An abstract byte input from reading bytes from a byte source of specific type.
 *
 * @param <T> byte source type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see AbstractByteOutput
 */
public abstract class AbstractByteInput<T>
        implements ByteInput {

    /**
     * Creates a new instance on top of specified byte source.
     *
     * @param source the byte source.
     */
    protected AbstractByteInput(final T source) {
        super();
        this.source = Objects.requireNonNull(source, "source is null");
    }

    /**
     * The byte source from which bytes are read.
     */
    protected final T source;
}
