# Developers notes

## GPG Key
Ask project owners

## Release
### Increment version
Manually update version in Maestrano.java

### Perform release dry run
```
mvn release:prepare -DdryRun=true
```

### Perform release
```
mvn -Pgpg release:clean release:prepare release:perform
```