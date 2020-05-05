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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.List;

import static com.github.jinahya.bit.io.User.newRandomInstance;
import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserListAdapterTest {

    @MethodSource({"com.github.jinahya.bit.io.ByteIoTestParameters#ByteIoTestParameters"})
    @ParameterizedTest
    void test(@ConvertWith(ByteOutput2BitOutputConverter.class) final BitOutput output,
              @ConvertWith(ByteInput2BitInputConverter.class) final BitInput input)
            throws IOException {
        final List<User> expected
                = range(0, current().nextInt(1, 128)).mapToObj(i -> newRandomInstance()).collect(toList());
        output.writeValue(new UserListAdapter(7), expected);
        final long padded = output.align();
        final List<User> actual = input.readValue(new UserListAdapter(7));
        final long discarded = input.align();
        assertEquals(expected, actual);
        assertEquals(padded, discarded);
    }
}
