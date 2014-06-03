Available goals:
================
* **list** - create a project property containing the versions list of an artifact

Example plugin definition:
==========================
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
    <configuration>
        <groupId>commons-collections</groupId>
        <artifactId>commons-collections</artifactId>
        <startingVersion>1.0</startingVersion>
        <versionListPropertyName>versionList</versionListPropertyName>
    </configuration>
