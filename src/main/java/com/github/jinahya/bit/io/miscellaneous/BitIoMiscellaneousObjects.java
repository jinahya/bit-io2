package com.github.jinahya.bit.io.miscellaneous;

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

import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.BitOutput;
import com.github.jinahya.bit.io.BitReader;
import com.github.jinahya.bit.io.BitWriter;
import com.github.jinahya.bit.io.ByteInput;
import com.github.jinahya.bit.io.ByteOutput;

final class BitIoMiscellaneousObjects {

    static <T extends BitInput> T requireNonNullInput(final T input) {
        return BitIoMiscellaneousObjectsHelper.invoke("requireNonNullInput", input);
    }

    static <T extends BitOutput> T requireNonNullOutput(final T output) {
        return BitIoMiscellaneousObjectsHelper.invoke("requireNonNullOutput", output);
    }

    static <T extends ByteInput> T requireNonNullInput(final T input) {
        return BitIoMiscellaneousObjectsHelper.invoke("requireNonNullInput", input);
    }

    static <T extends ByteOutput> T requireNonNullOutput(final T output) {
        return BitIoMiscellaneousObjectsHelper.invoke("requireNonNullOutput", output);
    }

    static <T extends BitReader<?>> T requireNonNullReader(final T reader) {
        return BitIoMiscellaneousObjectsHelper.invoke("requireNonNullReader", reader);
    }

    static <T extends BitWriter<?>> T requireNonNullWriter(final T writer) {
        return BitIoMiscellaneousObjectsHelper.invoke("requireNonNullWriter", writer);
    }

    private BitIoMiscellaneousObjects() {
        throw new AssertionError(BitIoMiscellaneousConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
