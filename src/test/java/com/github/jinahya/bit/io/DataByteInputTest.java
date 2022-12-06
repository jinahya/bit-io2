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

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * A class for testing {@link DataByteInput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DataByteOutputTest
 */
class DataByteInputTest
        extends AbstractByteInputTest<DataByteInput> {

    /**
     * Creates a new instance.
     */
    DataByteInputTest() {
        super(DataByteInput.class);
    }

    @Override
    protected DataByteInput newInstance(final int size) throws IOException {
        final var tempFile = tempFile(size);
        return new DataByteInput(new DataInputStream(new FileInputStream(tempFile)));
    }
}
