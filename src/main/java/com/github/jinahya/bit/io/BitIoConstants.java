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

/**
 * Defines constants for bit-io.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;\
 */
final class BitIoConstants {

    static final String MESSAGE_INSTANTIATION_IS_NOT_ALLOWED = "instantiation is not allowed";

    static final String MESSAGE_UNSUPPORTED_ALREADY_NULLABLE = "unsupported; already nullable";

    /**
     * Creates a new instance.
     */
    private BitIoConstants() {
        throw new AssertionError("initialization is not allowed");
    }
}
