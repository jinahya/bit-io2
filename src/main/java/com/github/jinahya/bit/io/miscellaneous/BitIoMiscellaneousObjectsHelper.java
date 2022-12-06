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

import java.lang.reflect.Method;
import java.util.Arrays;

final class BitIoMiscellaneousObjectsHelper {

    @SuppressWarnings({
            "java:S1874", // isAccessible
            "java:S3011", // setAccessible
            "unchecked",
            "java:S113" // RuntimeException
    })
    static <T> T invoke(final String name, final Object... args) {
        try {
            final Class<?> clazz = Class.forName("com.github.jinahya.bio.io.BitIoObjects");
            final Method method = clazz.getDeclaredMethod(name);
            if (!method.isAccessible()) { // NOSONAR
                method.setAccessible(true); // NOSONAR
            }
            return (T) method.invoke(null, args); // NOSONAR
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException("failed to invoke " + name + " with " + Arrays.toString(args), roe); // NOSONAR
        }
    }

    private BitIoMiscellaneousObjectsHelper() {
        throw new AssertionError(BitIoMiscellaneousConstants.MESSAGE_INSTANTIATION_IS_NOT_ALLOWED);
    }
}
