# REST FileSystem Adapter WAR File Deployment

This project builds a [WAR file](https://eclipse-ee4j.github.io/jakartaee-tutorial/#packaging-web-archives) that can be used to deploy the REST FileSystem Adapter to web containers like [Tomcat](https://tomcat.apache.org/) or [Jetty](https://www.eclipse.org/jetty/), or to any web container that conforms to the [Jakarta Servlet specification](https://jakarta.ee/specifications/servlet/).

| Technology Stack | Reference |
| ---------------- | --------- |
| [FAS REST FileSystem Adapter Code](https://github.com/FileAnalysisSuite/restadapter-filesystem/blob/65ee6540fc1d1c64ce4b27e294518aea7a20f547/restadapter-filesystem-core/src/main/java/io/github/fileanalysissuite/restadapters/filesystem/core/FileSystemAdapter.java#L44) | [pom.xml:38-41](https://github.com/FileAnalysisSuite/restadapter-filesystem/blob/65ee6540fc1d1c64ce4b27e294518aea7a20f547/restadapter-filesystem-war/pom.xml#L38-L41) |
| [FAS Java Adapter SDK JAX-RS Implementation](https://github.com/FileAnalysisSuite/adaptersdk-impl-jaxrs) | [pom.xml:34-37](https://github.com/FileAnalysisSuite/restadapter-filesystem/blob/65ee6540fc1d1c64ce4b27e294518aea7a20f547/restadapter-filesystem-war/pom.xml#L34-L37) |
| [Jakarta RESTful Web Services 2.1](https://jakarta.ee/specifications/restful-ws/2.1/) | [pom.xml:42-45](https://github.com/FileAnalysisSuite/restadapter-filesystem/blob/65ee6540fc1d1c64ce4b27e294518aea7a20f547/restadapter-filesystem-war/pom.xml#L42-L45) |
| [Jersey 2.x components](https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/index.html) | [pom.xml:51-65](https://github.com/FileAnalysisSuite/restadapter-filesystem/blob/65ee6540fc1d1c64ce4b27e294518aea7a20f547/restadapter-filesystem-war/pom.xml#L51-L65) |
| Web Container (e.g. Tomcat) | N/A |

[![Tomcat](https://tomcat.apache.org/res/images/tomcat.png)](https://tomcat.apache.org/)
