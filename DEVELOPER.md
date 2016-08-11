# Developers notes

## GPG Key
Ask project owners

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