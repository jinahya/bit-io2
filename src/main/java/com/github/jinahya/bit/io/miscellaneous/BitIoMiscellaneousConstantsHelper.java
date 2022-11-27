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

import com.github.jinahya.bit.io.BitIoConstants;

import java.lang.reflect.Field;

final class BitIoMiscellaneousConstantsHelper {

    @SuppressWarnings({
            "java:S112", // RuntimeException
            "java:S1874", // isAccessible
            "java:S3011" // setAccessible
    })
    static String getString(final String name) {
        try {
            final Field field = BitIoConstants.class.getDeclaredField(name);
            if (!field.isAccessible()) { // NOSONAR
                field.setAccessible(true); // NOSONAR
            }
            return (String) field.get(null);
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException("failed to get string for '" + name + "'", roe); // NOSONAR
        }
    }

    private BitIoMiscellaneousConstantsHelper() {
        throw new AssertionError(BitIoMiscellaneousConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
