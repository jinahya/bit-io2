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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * A value reader for reading string values.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see StringWriter
 */
public class StringReader
        implements BitReader<String> {

    /**
     * Creates a new instance for reading ASCII strings.
     *
     * @param maximumCharacters a maximum number of characters of a string; must be non-negative.
     * @param printableOnly     a flag for printable characters only; {@code true} for printable characters; {@code
     *                          false} otherwise.
     * @return a new instance.
     * @see StringWriter#ascii(int, boolean)
     */
    public static StringReader ascii(final int maximumCharacters, final boolean printableOnly) {
        if (maximumCharacters < 0) {
            throw new IllegalArgumentException("maximumCharacters(" + maximumCharacters + " is negative");
        }
        final int lengthSize = BitIoUtils.size(maximumCharacters);
        return new StringReader(ByteArrayReader.ascii(lengthSize, printableOnly), StandardCharsets.US_ASCII);
    }

    /**
     * Creates a new instance for writing UTF-8 strings.
     *
     * @return a new instance.
     * @see StringWriter#utf8()
     */
    public static StringReader utf8() {
        return new StringReader(ByteArrayReader.utf8(31), StandardCharsets.UTF_8);
    }

    /**
     * Creates a new instance with specified arguments.
     *
     * @param delegate a reader for reading encoded bytes.
     * @param charset  a charset for decoding values.
     */
    public StringReader(final ByteArrayReader delegate, final Charset charset) {
        super();
        this.delegate = Objects.requireNonNull(delegate, "delegate is null");
        this.charset = Objects.requireNonNull(charset, "charset is null");
    }

    @Override
    public String read(final BitInput input) throws IOException {
        return new String(delegate.read(input), charset);
    }

    private final ByteArrayReader delegate;

    private final Charset charset;
}
