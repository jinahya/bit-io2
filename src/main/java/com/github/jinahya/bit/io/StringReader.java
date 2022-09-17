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
        extends FilterBitReader<String, byte[]> {

    /**
     * Creates a new instance for reading {@link StandardCharsets#US_ASCII} decoded strings in a compressed-manner.
     *
     * @param printableOnly a flag for printable characters only; {@code true} for printable characters; {@code false}
     *                      otherwise.
     * @return a new instance.
     * @see ByteArrayReader#ascii(int, boolean)
     */
    public static StringReader ascii(final boolean printableOnly) {
        final ByteArrayReader delegate = ByteArrayReader.ascii(Integer.SIZE - 1, printableOnly);
        return new StringReader(delegate, StandardCharsets.US_ASCII);
    }

    /**
     * Creates a new instance for reading {@link StandardCharsets#UTF_8} decoded strings in a compressed-manner.
     *
     * @return a new instance.
     * @see ByteArrayReader#utf8(int)
     */
    public static StringReader utf8() {
        final ByteArrayReader delegate = ByteArrayReader.utf8(Integer.SIZE - 1);
        return new StringReader(delegate, StandardCharsets.UTF_8);
    }

    /**
     * Creates a new instance with specified arguments.
     *
     * @param delegate a reader for reading encoded bytes.
     * @param charset  a charset for decoding a value.
     * @see ByteArrayReader#of318()
     */
    public StringReader(final ByteArrayReader delegate, final Charset charset) {
        super(delegate);
        this.charset = Objects.requireNonNull(charset, "charset is null");
    }

    @Override
    public String read(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        final byte[] bytes = getReader().read(input);
        return new String(bytes, charset);
    }

    private final Charset charset;
}
