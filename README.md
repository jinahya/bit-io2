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

## How it works?

Two interfaces are defined for reading/writing non-octet aligned values and two more interfaces are defined for reading/writing octets from/to various sources/targets.

```
BitInput <- (Adapter) <- ByteInput <- (Adapter) <- DataInput
                                                   InputStream
                                                   ...
```

```
BitOutput -> (Adapter) -> ByteOutput -> (Adapter) -> DataOutput
                                                     OutputStream
                                                     ...
```

See [Specifications](https://github.com/jinahya/bit-io2/wiki/Specifications) and [Recipes](https://github.com/jinahya/bit-io2/wiki/Recipes) for more information.
