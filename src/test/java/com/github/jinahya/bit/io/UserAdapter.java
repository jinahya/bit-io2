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

import static java.nio.charset.StandardCharsets.UTF_8;

class UserAdapter
        implements ValueAdapter<User> {

    static final ValueAdapter<String> NAME_ADAPTER
            = ValueAdapter.nullable(new StringAdapter(new ByteArrayAdapter(16, 8), UTF_8));

    @Override
    public void write(final BitOutput output, final User value) throws IOException {
        NAME_ADAPTER.write(output, value.name);
        output.writeUnsignedInt(7, value.age);
    }

    @Override
    public User read(final BitInput input) throws IOException {
        final User value = new User();
        value.name = NAME_ADAPTER.read(input);
        value.age = input.readUnsignedInt(7);
        return value;
    }
}
