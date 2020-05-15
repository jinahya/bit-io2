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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * A class for testing {@link ValueAdapter} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
class ValueAdapterTest {

    /**
     * Tests {@link ValueAdapter#nullable(ValueAdapter)} method.
     */
    @Test
    void testNullable() {
        final ValueAdapter<User> instance = ValueAdapter.nullable(new UserAdapter());
        assertNotNull(instance);
    }

    /**
     * Tests {@link ValueAdapter#compose(ValueReader, ValueWriter)} method.
     */
    @Test
    void testCompose() {
        final ValueAdapter<User> instance = ValueAdapter.compose(new UserReader(), new UserWriter());
        assertNotNull(instance);
    }
}
