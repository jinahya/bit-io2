package com.github.jinahya.bit.io;

import org.apache.commons.text.RandomStringGenerator;

import java.util.Objects;

import static java.util.concurrent.ThreadLocalRandom.current;

class User {

    static User newRandomInstance() {
        final User instance = new User();
        if (current().nextBoolean()) {
            instance.name = new RandomStringGenerator.Builder().build().generate(current().nextInt(128));
        }
        instance.age = current().nextInt(128);
        return instance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age &&
               Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        return super.toString() + "{"
               + "name=" + name
               + ",age=" + age
               + "}";
    }

    String name;

    int age;
}
