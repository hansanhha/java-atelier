## Test Task

if we run the test with gradle, it always used `test` task which is based on the `Test` task class

## Configure the Test Task

by the default, configured test task in build script by `gradle init` command is as follows: 

```kotlin
tasks.named<Test>("test") {
    useJUnitPlatform()
}
```

if you use other test dependency besides JUnit, then you can configure:
```kotlin
tasks.withType<Test>().configureEach {
//    useTestNG()
//    useJUnitPlatform()
}
```

In addition to, it can also exclude or include specific package that runs the test

```kotlin
tasks.withType<Test>().configureEach {
//    useTestNG()
    useJUnitPlatform()
    include("com/example/application/unit")
    exclude("com/example/application/integration")
}
```

## CleanTest task

Gradle has incremental build feature, so if you don't change production code or test code, 

then gradle use cached test result  when you run test after first run test

`cleanTest` task clean only about test result, so you can use it to rerun test task without change the code

`./gradlew :playground:theme-park:cleanTest :playground:theme-park:test --console=verbose`
