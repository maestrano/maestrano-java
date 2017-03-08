# Migration Guide to v0.9.0

Before the 0.9.0 version, the methods were static and directly made on the classes. Starting from 0.9.0, you need to get instances of Maestrano configuration or MnoObject connection client to do the calls.

### Migrating Maestrano methods calls

Before 0.9.0:
```java
Maestrano.configure();
Maestrano.configure("myPreset", myPresetProperties);
Maestrano.toMetadata();
Maestrano.toMetadata("myPreset");
//...
Maestrano.ssoService().getLogoutUrl()
Maestrano.ssoService().getLogoutUrl("myPreset")
```
After 0.9.0:
```java
Maestrano defaultInstance = Maestrano.configure();
Maestrano presetInstance = Maestrano.configure("myPreset", myPresetProperties);

defaultInstance.toMetadata();
presetInstance.toMetadata();
// or:
Maestrano.getDefault().toMetadata();
Maestrano.get("myPreset").toMetadata();
//...
Maestrano.getDefault().ssoService().getLogoutUrl();
Maestrano.get("myPreset").ssoService().getLogoutUrl();
```
### Migrating Connec!™ API calls

For API calls, you need now to retrieve an instance of a client (BillClient, MnoUserClient etc..) to make the calls.
Before 0.9.0:
```java
List<Bill> bills = Bill.all();
Bill bill = Bill.retrieve("rbill-f1d2s54");
```
After 0.9.0:
```java
List<Bill> bills = Bill.client().all();
Bill bill = Bill.client().retrieve("rbill-f1d2s54");
// or:
BillClient client = Bill.client();
List<Bill> bills = client.all();
Bill bill = client.retrieve("rbill-f1d2s54");
```

### Migrating Connec!™ Data Sharing API calls

Before you could directly make the static call on ConnecClient. Now you need to use the constructor
Before 0.9.0:
```java
Map<String, Object> organizations = ConnecClient.all("organizations", groupId);
organization = (Map<String, Object>) ConnecClient.create("organizations", groupId, newOrganization).get("organizations");
```
After 0.9.0:
```java
ConnecClient connecClient = new ConnecClient(marketplace);
Map<String, Object> organizations = connecClient.all("organizations", groupId);
organization = (Map<String, Object>) connecClient.create("organizations", groupId, newOrganization).get("organizations");
```