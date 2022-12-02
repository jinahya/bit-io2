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

final class BitIoMiscellaneousConstants {

    static final String MESSAGE_INPUT_IS_NULL = BitIoMiscellaneousConstantsHelper.getString("MESSAGE_INPUT_IS_NULL");

    static final String MESSAGE_OUTPUT_IS_NULL = BitIoMiscellaneousConstantsHelper.getString("MESSAGE_OUTPUT_IS_NULL");

    static final String MESSAGE_INSTANTIATION_IS_NOT_ALLOWED
            = BitIoMiscellaneousConstantsHelper.getString("MESSAGE_INSTANTIATION_IS_NOT_ALLOWED");

    static final int SIZE_NIBBLE = Byte.SIZE >> 1;

    private BitIoMiscellaneousConstants() {
        throw new AssertionError(MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
