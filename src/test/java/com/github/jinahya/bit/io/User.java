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

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.RandomStringGenerator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.util.concurrent.ThreadLocalRandom.current;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@Slf4j
class User {

    static class Reader
            implements BitReader<User> {

        @Override
        public User read(final BitInput input) throws IOException {
            final String name = reader.read(input);
            final int age = input.readInt(true, 7);
            return new User(name, age);
        }

        final BitReader<String> reader = new StringReader(ByteArrayReader.utf8(9), StandardCharsets.UTF_8);
    }

    static class Writer
            implements BitWriter<User> {

        @Override
        public void write(final BitOutput output, final User value) throws IOException {
            writer.write(output, value.name);
            output.writeInt(true, 7, value.age);
        }

        final BitWriter<String> writer = new StringWriter(ByteArrayWriter.utf8(9), StandardCharsets.UTF_8);
    }

    static User newRandomInstance() {
        final String name = new RandomStringGenerator.Builder().build().generate(current().nextInt(128));
        final int age = current().nextInt(128);
        return new User(name, age);
    }

    final String name;

    final int age;
}
