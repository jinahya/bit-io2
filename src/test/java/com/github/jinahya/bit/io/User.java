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

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.RandomStringGenerator;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@Slf4j
public class User {

    public static User newRandomInstance() {
        final String name = new RandomStringGenerator.Builder().build().generate(
                ThreadLocalRandom.current().nextInt(128)
        );
        final int age = ThreadLocalRandom.current().nextInt(128);
        return new User(name, age);
    }

    @NotBlank
    private final String name;

    @Max(127)
    @PositiveOrZero
    private final int age;
}
