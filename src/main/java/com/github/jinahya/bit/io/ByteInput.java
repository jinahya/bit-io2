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

/**
 * An interface for reading bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see ByteOutput
 */
@FunctionalInterface
public interface ByteInput {

    /**
     * Reads an unsigned {@value java.lang.Byte#SIZE}-bit byte value.
     *
     * @return an unsigned {@value java.lang.Byte#SIZE}-bit byte value; between {@code 0} and {@code 255}, both
     * inclusive.
     * @throws IOException if an I/O error occurs or no more bytes are available to read.
     */
    int read() throws IOException;
}
