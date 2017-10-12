<h1>Version list Maven plugin</h1><br>

Maven plugin for retrieving the version list of an artifact

[![][Build Status img]][Build Status]
[![][Coverage Status img]][Coverage Status]
[![][Dependency Status img]][Dependency Status]
[![][license img]][license]
[![][Maven Central img]][Maven Central]
[![][Javadocs img]][Javadocs]

*Maven 3.3.&ast; artifact*
```
<plugin>
     <groupId>com.github.lucapino</groupId>
     <artifactId>versions-list-maven-plugin</artifactId>
     <version>1.0.1</version>
</plugin>
```

*Maven 3.5.&ast; artifact*
```
<plugin>
     <groupId>com.github.lucapino</groupId>
     <artifactId>versions-list-maven-plugin</artifactId>
     <version>2.0.0</version>
</plugin>
```

Available goals:
---
* **list** - create a project property containing the versions list of an artifact

Example plugin definition:
==========================
   ```
   <plugin>
        <groupId>com.github.lucapino</groupId>
        <artifactId>versions-list-maven-plugin</artifactId>
        <version>1.0.1</version>
        <configuration>
            <groupId>commons-collections</groupId>
            <artifactId>commons-collections</artifactId>
            <startingVersion>1.0</startingVersion>
            <versionListPropertyName>versionList</versionListPropertyName>
        </configuration>
    </plugin>
    
Example _list_ goal configuration:
-------------------------------------
```
    <configuration>
        <groupId>commons-collections</groupId>
        <artifactId>commons-collections</artifactId>
        <startingVersion>1.0</startingVersion>
        <versionListPropertyName>versionList</versionListPropertyName>
    </configuration>


[Build Status]:https://travis-ci.org/lucapino/versions-list-maven-plugin
[Build Status img]:https://travis-ci.org/lucapino/versions-list-maven-plugin.svg?branch=master

[Coverage Status]:https://codecov.io/gh/lucapino/versions-list-maven-plugin
[Coverage Status img]:https://codecov.io/gh/lucapino/versions-list-maven-plugin/branch/master/graph/badge.svg

[Dependency Status]:https://www.versioneye.com/user/projects/59df2c5915f0d723d921f2cc
[Dependency Status img]:https://www.versioneye.com/user/projects/59df2c5915f0d723d921f2cc/badge.svg?style=flat

[license]:LICENSE
[license img]:https://img.shields.io/badge/license-Apache%202-blue.svg

[Maven Central]:https://maven-badges.herokuapp.com/maven-central/com.github.lucapino/versions-list-maven-plugin
[Maven Central img]:https://maven-badges.herokuapp.com/maven-central/com.github.lucapino/versions-list-maven-plugin/badge.svg

[Javadocs]:http://www.javadoc.io/doc/com.github.lucapino/versions-list-maven-plugin
[Javadocs img]:http://javadoc.io/badge/com.github.lucapino/versions-list-maven-plugin.svg
