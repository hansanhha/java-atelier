## Gradle is Build Tool

Gradle helps you build two type of Java project

**application**
- A project creating a "standalone" service which actually gets run, like a web application, a service serving an API, or a desktop application
- An application is **consumer** that it uses other libraries 

**library**
- A project creating an artifact that "isn't intended to be run standalone", but is consumed by other libraries or application to create something else
- The library is a **producer** that it generates an artifact to be consumed by others


