# bit-io2

[![GitHub Action](https://github.com/jinahya/bit-io2/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/jinahya/bit-io2/actions?workflow=Java+CI)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jinahya_bit-io2&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=jinahya_bit-io2)
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
OutputStream stream = outputStream();
BitOutput output = BitOutputFactory.from(stream);
output.writeBoolean(true);           // 1 bit   1
output.writeInt(true, 3, 1);         // 3 bits  4
output.writeLong(false, 37, 0L);     // 37 bits 41        
long padded = output.align(1);
assert padded == 7L;
assert (padded + 41) % Byte.SIZE == 0;

InputStream stream = inputStream();
BitInput input = BitInputFactory.from(stream);
boolean v1 = input.readBoolean();    // 1 bit   1
int v2 = input.readInt(true, 3);     // 3 bits  4
assert v2 == 1;
long v3 = input.readLong(false, 37); // 37 bits 41
assert v3 == 0L;        
long discarded = input.align(1);
assert discarded == 7L;
assert (discarded + 41) % Byte.SIZE == 0;
```

See [Specifications](https://github.com/jinahya/bit-io2/wiki/Specifications) and [Recipes](https://github.com/jinahya/bit-io2/wiki/Recipes) for more information.
