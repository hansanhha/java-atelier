[5 core requirements](#5-core-requirements-of-build-tool)
[Additional requirements (developer workflow features)](#additional-requirements-developer-workflow-features)

## 5 core requirements of build tool

### Compile classes

**.java file**
1. Writing .java source file
2. Java compiler compiles source files into .class class file

**class file**
- Bytecode that is ready to run on the jvm

**classpath**
- A list of files passed to jvm(source file, properties ...)
- If application use external libraries such spring boot then compiler needs to know where they're located during compilation
- This is where classpath helps

### Resources

It's additional files that an application relies on(not java source file)

Source code may refer this, so need to get packaged up with the code

Images, HTML, text files or anything else accessed by the application

### Dependencies

Any libraries referenced from your application code becomes a dependency of the application

It's needed to compile and run the application

But java doesn't resolve dependencies itself, and it relies on the classpath being correctly generated,

referencing any libraries used in the code

The build tool should let us specify dependencies required for compilation, which it should use to generate the compile classpath

### Package

The build tool should package the compiled classes and resources into a single artifact

**jar file**
- single artifact in the java
- which is a zip file containing all the class files and resources, extra metadata

### Run Tests

The tests themselves need to be compiled, and may need a different classpath to that of the main application

Test classes shouldn't end up in the published artifact

The build tool should also be able to run those and show us the results

## Additional requirements (developer workflow features)

### Run application

If you run through build tool, and it can generate the runtime classpath automatically

### Java version

The version of java used to run gradle should be decoupled from the version of java used to compile or run in application

To change java version just change a single line of code

### Separate unit & integration tests

as the title says

### Publish

If an artifact(library) is stored where maven repository, whether public or private, then it can be referenced by some other project  