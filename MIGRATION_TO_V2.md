# Migration Guide to v2

The change to the v2.0 version introduced some breaking changes.

## Configuration

Maestrano 2.0 only accept configuration retrieved from the Developer platform. See [Readme](README.md) for more details.

## Maestrano

- Direct static method from Maestrano have been removed.
- Use `Maestrano.get(marketplaceName)` instead to retrieve a `Preset` instance, from which you can call all of the previous methods.
- Before you were retrieving a `Maestrano` instance, you will now retrieve a `Preset` instance.
- In order to be consistent accross the library, all Maestrano Object and Services are now expecting to be called with a preset given as an argument.
- `ApiService` has been renamed to `Api`, `SsoService` to `Sso`, `ConnecService` to `Connec`, `AppService` to `App` and `WebhookService` to `Webhook`
- `MnoSession` has been renamed `Session`

For example:
```java
Maestrano maestrano = Maestrano.get(marketplace);
SsoService ssoService = maestrano.ssoService();
```
should be replaced by

```java
Preset preset = Maestrano.get(marketplace);
Sso sso = preset.getSso();
```



## Accounts

`MnoUser`, `MnoGroup`, `MnoBill` and `MnoRecurringBill` have been renamed `User`, `Group`, `Bill` and `RecurringBill`

Direct static method from `User`, `Group`, `Bill` and `RecurringBill` have been removed.

All the call should be done following this model:

```java
MnoBill.all();
```
should be replaced by

```java
Preset preset = Maestrano.get(marketplaceName);
Bill.client(preset).all();
// or 
preset.billClient().all()
```