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

class Double64ArrayWriter extends SequenceValueWriter<double[]> {

    public Double64ArrayWriter(final int lengthSize) {
        super(lengthSize);
    }

    @Override
    public void write(final BitOutput output, final double[] value) throws IOException {
        final int length = writeLength(output, value.length);
        for (int i = 0; i < length; i++) {
            output.writeDouble64(value[i]);
        }
    }
}
