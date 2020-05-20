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

class Float32ArrayReader extends SequenceValueReader<float[]> {

    public Float32ArrayReader(final int lengthSize) {
        super(lengthSize);
    }

    @Override
    public float[] read(final BitInput input) throws IOException {
        final int length = readLength(input);
        final float[] value = new float[length];
        for (int i = 0; i < value.length; i++) {
            value[i] = input.readFloat32();
        }
        return value;
    }
}
