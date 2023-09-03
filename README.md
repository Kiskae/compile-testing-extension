# compile-testing-extension <!-- [ ![Download](https://api.bintray.com/packages/kiskae/maven/compile-testing-extension/images/download.svg) ](https://bintray.com/kiskae/maven/compile-testing-extension/_latestVersion) -->

A JUnit5 Extension implementation of [`google/compile-testing`](https://github.com/google/compile-testing)'s JUnit4 
[`CompilationRule`](https://github.com/google/compile-testing/blob/master/src/main/java/com/google/testing/compile/CompilationRule.java) rule.

## Usage

This library is available on `mavenCentral`:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    // JUnit API and Engine
    testImplementation(platform("org.junit:junit-bom:5.3.2"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntime("org.junit.jupiter:junit-jupiter-engine")
    
    // CompilationExtension
    testImplementation("io.github.kiskae:compile-testing-extension:1.0.2")
}
```

This extension uses JUnit5's native `ParameterResolver` to make the `java.lang.model` utility types available
for any of the usual injection targets. Check out [JUnit5 / User Guide / Dependency Injection for Constructors and Methods ](https://junit.org/junit5/docs/current/user-guide/#writing-tests-dependency-injection)
for more information.

```java
@ExtendWith(CompilationExtension.class)
class JavaModelTest {
    @Test
    public void testTypes(Types types) {
        NullType nt = types.getNullType();
        assertNotNull(nt);
    }
    
    @Test
    public void testElements(Elements elements) {
        TypeElement element = elements.getTypeElement(String.class.getCanonicalName());
        assertNotNull(element);
    }
}
```

## License

```
Copyright 2018 David van Leusen

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
    
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```