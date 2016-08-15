# Developers notes

## GPG Key
Ask project owners to get the private GPG key and import it using command
```
gpg --import 64C54E61.asc
```

Configure the maven settings in `~/.m2/settings.xml`
```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>ossrh</id>
      <username>maestrano</username>
      <password>ask_owner</password>
    </server>
  </servers>

  <profiles>
    <profile>
      <id>ossrh</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <gpg.executable>gpg</gpg.executable>
        <gpg.keyname>Maestrano</gpg.keyname>
        <gpg.passphrase>ask_owner</gpg.passphrase>
      </properties>
    </profile>
  </profiles>
</settings>
```

## Release
### Increment version
Increment version in pom.xml

### Perform release dry run
```
mvn release:prepare -DdryRun=true
```

### Perform release
```
mvn -Pgpg release:clean release:prepare release:perform
```
