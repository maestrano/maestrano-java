# Maestrano Javs SDK Release Notes

## maestrano-java-1.0.1

* Remove sso.creationMode option
* `com.maestrano.sso.MnoUser` `toUid` and `toEmail` has been removed and should be replaced by `getUid` and `getEmail`
* Set ConnecClient delete method to deprecated

To see all commits for this version, [click here](https://github.com/maestrano/maestrano-java/compare/maestrano-java-1.0.0...maestrano-java-1.0.1).

## maestrano-java-1.0.0

* Introduce `Maestrano.autoConfigure()` (See README.md)
* Lot of methods have been deprecated:
 * `ConnecClient.defaultClient()` and `ConnecClient.withPreset` should be replaced by the call to the `ConnecClient(Maestrano)` constructor
 * All `Maestrano.configure` methods are deprecated and should be replaced by a call to autoconfigure. (See https://maestrano.atlassian.net/wiki/display/DEV/How+to+migrate+to+the+developer+platform)
 * All `.client()` methods of the Mno Objects (MnoBill, MnoGroup, MnoRecurringBill, MnoUser) should be replaced by `.client(Maestrano)` or `.client(marketplace)`
 * `com.maestrano.saml.Response` constructor is now private. Use `com.maestrano.saml.Response.loadFromXML` or `loadFromBase64XML`
* Maestrano.authenticate methods are no longer static. Use instance methods instead. For example, Maestrano.authenticate(preset, request) should be replaced by Maestrano.get(request).authenticate(request)
* Introduction of SLF4j (http://www.slf4j.org/) for logging.


To see all commits for this version, [click here](https://github.com/maestrano/maestrano-java/compare/maestrano-java-0.9.4...maestrano-java-1.0.0).

## maestrano-java-0.9.4

To see all commits for this version, [click here](https://github.com/maestrano/maestrano-java/compare/maestrano-java-0.9.3...maestrano-java-0.9.4).


## maestrano-java-0.9.3

* Introduce ConnecService
* Connec information are being shown in the medatada
* Replaced property keys:
    * api.accountBase with api.base

To see all commits for this version, [click here](https://github.com/maestrano/maestrano-java/compare/maestrano-java-0.9.2...maestrano-java-0.9.3).


## maestrano-java-0.9.2

* Add option to choose configuration preset when checking session is valid for auto logout
* Replaced property keys:
    * app.environment with environment
    * api.accountHost with api.host
    * api.connectHost with connec.host

To see all commits for this version, [click here](https://github.com/maestrano/maestrano-java/compare/maestrano-java-0.9.1...maestrano-java-0.9.2).

## maestrano-java-0.9.1

* Fix configuration of SSL fingerprint and certificate
* Documentation update

To see all commits for this version, [click here](https://github.com/maestrano/maestrano-java/compare/maestrano-java-0.9.0...maestrano-java-0.9.1).

## maestrano-java-0.9.0

* **Code clean-up and multi-tenant support**

To see all commits for this version, [click here](https://github.com/maestrano/maestrano-java/compare/0.4.0...maestrano-java-0.9.0).
