# bit-io2

![Java CI with Maven](https://github.com/jinahya/bit-io2/workflows/Java%20CI%20with%20Maven/badge.svg)
![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/bit-io2)
[![javadoc](https://javadoc.io/badge2/com.github.jinahya/bit-io2/javadoc.svg)](https://javadoc.io/doc/com.github.jinahya/bit-io2)

A Java 8+ flavored version of [bit-io](https://github.com/jinahya/bit-io).

## How to use?

Add this module as a dependency. Check the [central](https://search.maven.org/search?q=g:com.github.jinahya%20a:bit-io2) for the current version.

```xml
<dependency>
  <groupId>com.github.jinahya</groupId>
  <artifactId>bit-io2</artifactId>
</dependency>
```

```java
OutputStream stream = open();
BitOutput output = BitOutput.of(stream);
output.writeBoolean(true);       // 1 bit   1
output.writeUnsignedInt(3, 1);   // 3 bits  4
output writeLong(false, 37, 0L); // 37 bits 41        
int padded = output.align();
assert padded == 7;

InputStream stream = open();
BitInput input = BitInput.of(stream);
boolean v1 = input.readBoolean();    // 1 bit   1
int v2 = input.readUnsignedInt(3);   // 3 bits  4
long v3 = input.readLong(false, 37); // 37 bits 41        
int discarded = input.align();
assert discarded == 7;
```

See [Specifications](https://github.com/jinahya/bit-io2/wiki/Specifications) and [Recipes](https://github.com/jinahya/bit-io2/wiki/Recipes) for more information.
