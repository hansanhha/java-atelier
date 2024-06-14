## Java plugin

It's gradle core plugin that gave you the follow useful features:
- compiling code
- processing resources
- producing a jar file
- running tests

The java plugin initialises project as a java project 

It also adds several dependency configuration, which we can declare dependencies against to control the generation of the classpath

### Compile classes - compileJava

The java plugin compile classes through a task called `compileJava`

This task use whatever java version you're using to run gradle to compile .java file into .class files

These .class files get output into the build directory

`src/main/java/....java` -> --> compileJava --> `build/classes/java/main/....class` 

### Manage Resources - processResources

The java plugin's `processResources` task let us manage resources 

When you execute the processResources task, it scans specific directories within your project, and copies their contents into the build directory 

`src/main/java/resources/hello.txt` --> processResources --> `build/resources/main/hello.txt`

The reason it's called "processResources" is that it can do additional processing along the way 

### Define Dependencies

The java plugin provides dependency configurations, which determine dependencies are added to the classpath  

`implementation`, `compileOnly`, `runtimeOnly`, `testImplementation`, `testCompileOnly`, `testRuntimeOnly`

### Package - jar

The java plugin provides a `jar` task that packages the compiled classes and resources into a single jar file in the build directory

The name of jar file is `projectName-projectVersion.jar` by default, using the project version in the build script

jar --> `build/libs/projectName-projectVersion.jar`

### Run Tests - test

To run easily your test, the java plugin provides a `test` task that compiles your test code, process any resources, and then run the tests

It also produces a pretty test report in the build directory

The report tells us what's passed and what's failed

## Source Set

The java plugin also adds a source set to your project

**Source set** is a structure of directories that gradle uses to compile and package your code

`src/main/java` is for java source files and packages

`src/main/java/resources` is for java resources

`src/test/java` is for test java source files and packages

`src/test/resources` is for test java resources

**Build directory** contains all the gradle build outputs(generated classes, processed resources, test reports, jar files)

and importantly doesn't get committed into version control

`build/classes/java/main` is for compiled classes

`build/resources/main` is for processed resources

`build/classes/java/test` is for compiled test classes

`build/resources/test` is for processed test resources