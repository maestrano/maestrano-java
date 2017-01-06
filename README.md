[ ![Codeship Status for maestrano/maestrano-java](https://codeship.com/projects/b206abd0-b0f4-0133-7057-3674ea8aa855/status?branch=master)](https://codeship.com/projects/132856)

<p align="center">
<img src="https://raw.github.com/maestrano/maestrano-java/master/maestrano.png" alt="Maestrano Logo">
<br/>
<br/>
</p>

Maestrano Cloud Integration is currently in closed beta. Want to know more? Send us an email to <contact@maestrano.com>.

- - -

1.  [Getting Setup](#getting-setup)
2. [Getting Started](#getting-started)
  * [Installation](#installation)
  * [Configuration](#configuration)
3. [Single Sign-On Setup](#single-sign-on-setup)
  * [User Setup](#user-setup)
  * [Group Setup](#group-setup)
  * [Controller Setup](#controller-setup)
  * [Other Controllers](#other-controllers)
  * [Redirecting on logout](#redirecting-on-logout)
  * [Redirecting on error](#redirecting-on-error)
4. [Account Webhooks](#account-webhooks)
  * [Groups Controller](#groups-controller-service-cancellation)
  * [Group Users Controller](#group-users-controller-business-member-removal)
5. [API](#api)
  * [Bill](#bill)
  * [Recurring Bill](#recurring-bill)
6. [Connec!™ Data Sharing](#connec-data-sharing)
  * [Making Requests](#making-requests)
  * [Webhook Notifications](#webhook-notifications)
7. [Migrating from 0.*](#migrating-from-previous-version)
  * [Migrating Maestrano methods calls](#migrating-maestrano-methods-calls)
  * [Migrating Connec!™ API calls](#migrating-connec-api-calls)
  * [Migrating Connec!™ Data Sharing API calls](#migrating-connec-data-sharing-api-calls)
8. [Logging](#logging)

## Getting Setup

Before integrating with us you will need an to create your app on the developer platform and link it to a marketplace. Maestrano Cloud Integration being still in closed beta you will need to contact us beforehand to gain production access.

We provide a Sandbox environment where you can freely launch your app to test your integration. The sandbox is great to test single sign-on and API integration (e.g: Connec! API). This Sandbox is available on the developer platform on your app technical page.

To get started just go to: https://developer.maestrano.com. You will find the developer platform documentation here: Documentation.

A **java demo application** is also available: https://github.com/maestrano/demoapp-java

Do not hesitate to go to our Service Desk (https://maestrano.atlassian.net/servicedesk/customer/portal/2) if you have any question.


## Getting Started

### Installation

To install maestrano-java using Maven, add this dependency to your project's POM:
```
<dependency>
  <groupId>com.maestrano</groupId>
  <artifactId>maestrano-java</artifactId>
  <version>1.0.0</version>
</dependency>
```

Or download the Jars directly from Maven: http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22maestrano-java%22
You will require the following dependencies:
* Google-gson
* Apache Commons IO
* Servlet-api (such as from Apache Tomcat)

### Configuration

The [developer platform](https://developer.maestrano.com) is the easiest way to configure Maestrano. The only actions needed from your part is to create your application and environments on the developer platform and to create a config file. The framework will then contact the developer platform and retrieve the marketplaces configuration for your app environment.

A `dev-platform.properties` config file is loaded using:
```java
Maestrano.autoConfigure("/path/to/dev-platform.propertie");
```
The properties file may look like this:
```ini
# ===> Developer Platform Configuration
# This is the host and base path that should be used by your environment to retrieve its marketplaces configuration.
# The api-sandbox allows you to easily test integration scenarios.
dev-platform.host=https://developer.maestrano.com
dev-platform.api_path=/api/config/v1
# => Environment credentials
# These are your environment credentials, you can get them by connecting on the developer platform, then go on your app, they will be display under the technical view on each environment.
environment.name=<your environment nid>
environment.apiKey="<your environment key>
environment.apiSecret=<your environment secret>
```

You can also use environment variables as follow to configure your app environment:
```
export MNO_DEVPL_HOST=<developer platform host>
export MNO_DEVPL_API_PATH=<developer platform host>
export MNO_DEVPL_ENV_NAME=<your environment nid>
export MNO_DEVPL_ENV_KEY=<your environment key>
export MNO_DEVPL_ENV_SECRET=<your environment secret>
```
and directly call
```java
Maestrano:autoConfigure();
```
You may also call autoConfigure using a Properties instance.
```java
Properties properties = new Properties();
properties.setProperty("dev-platform.host", "https://developer.maestrano.com");
properties.setProperty("dev-platform.api_path", "/api/config/v1");
properties.setProperty("environment.name", "<your environment nid>");
properties.setProperty("environment.apiKey", "<your environment key>");
properties.setProperty("environment.apiSecret", "<your environment secret>");
Maestrano.autoConfigure(properties);
```

## Single Sign-On Setup

It will require you to write a controller for the init phase and consume phase of the single sign-on handshake. You will receive 3 informations when logging in a user: the user, his group and the merketplace he's coming from.

You might wonder why we need a 'group' on top of a user. Well Maestrano works with businesses and as such expects your service to be able to manage groups of users. A group represents 1) a billing entity 2) a collaboration group. During the first single sign-on handshake both a user and a group should be created. Additional users logging in via the same group should then be added to this existing group (see controller setup below)

For more information, please consult [Multi-Marketplace Ingration](https://maestrano.atlassian.net/wiki/display/DEV/Multi-Marketplace+Integration).

### User Setup
Let's assume that your user model is called 'User'. The best way to get started with SSO is to define a class method on this model called 'findOrCreateForMaestrano' accepting a Maestrano.Sso.User and aiming at either finding an existing maestrano user in your database or creating a new one. Your user model should also have a 'Provider' property and a 'Uid' property used to identify the source of the user - Maestrano, LinkedIn, AngelList etc..

### Group Setup
The group setup is similar to the user one. The mapping is a little easier though. Your model should also have the 'Provider' property and a 'Uid' properties. Also your group model could have a AddMember method and also a hasMember method (see controller below)

### Controller Setup
You will need two controller action init and consume. The init action will initiate the single sign-on request and redirect the user to Maestrano. The consume action will receive the single sign-on response, process it and match/create the user and the group.

The init action is all handled via Maestrano methods and should look like this:

```jsp
<%@ page import="com.maestrano.saml.AuthRequest" %>
<%
  //You should have a different url per marketplace
  // For example /mno-enterprise/saml/init?marketplace={marketplace_key}
  //See https://maestrano.atlassian.net/wiki/display/DEV/Multi-Marketplace+Integration for more information
  String marketplace = readMarketplaceFromParameter();
  AuthRequest authReq = new AuthRequest(Maestrano.get(marketplace), request);
  String ssoUrl = authReq.getRedirectUrl();
  
  response.sendRedirect(ssoUrl);
%>
```

Based on your application requirements the consume action might look like this:
```jsp
<%@ page import="com.maestrano.saml.Response,com.maestrano.sso.*" %>
<%
    String marketplace = readMarketplaceFromParameter();
    Maestrano maestrano = Maestrano.get(marketplace);
    Response authResp = new Response(maestrano);

   authResp.loadXmlFromBase64(request.getParameter("SAMLResponse"));
  
  if (authResp.isValid()) {

    // Build/Map local entities
    MyGroup localGroup = MyGroup.findOrCreateForMaestrano(mnoGroup);
    MyUser localUser = MyUser.findOrCreateForMaestrano(mnoUser);
    
    // Add localUser to the localGroup if not already part
    // of it
    if (!localGroup.hasMember(localUser)){
      localGroup.addMember(localUser);
    }
    
    // Set Maestrano session (for Single Logout)
    MnoSession mnoSession = new MnoSession(marketplace, request.getSession(),mnoUser);
    mnoSession.save();
    
    // Redirect to you application home page
    response.sendRedirect("/");
    
  } else {
    java.io.PrintWriter writer = response.getWriter();
    writer.write("Failed");
    writer.flush();
  }
%>
```

Note that for the consume action you should disable CSRF authenticity if your framework is using it by default. If CSRF authenticity is enabled then your app will complain on the fact that it is receiving a form without CSRF token.

### Other Controllers
If you want your users to benefit from single logout then you should define the following filter in a module and include it in all your controllers except the one handling single sign-on authentication.


```java
MnoSession mnoSession = new MnoSession(marketplace, request.getSession());
if (!mnoSession.isValid()) {
  response.sendRedirect(Maestrano.get(marketplace).ssoService().getInitUrl());
}
```

The above piece of code makes at most one request every 3 minutes (standard session duration) to the Maestrano website to check whether the user is still logged in Maestrano. Therefore it should not impact your application from a performance point of view.

If you start seing session check requests on every page load it means something is going wrong at the http session level. In this case feel free to raise an issue on our support platform: https://maestrano.atlassian.net/servicedesk/customer/portal/2

### Redirecting on logout
When Maestrano users sign out of your application you can redirect them to the Maestrano logout page. You can get the url of this page by calling:

```java
  //Retrieve current user uid
  String userUid = getUserUid();
  Maestrano.get(marketplace).ssoService().getLogoutUrl(userUid);
```
or if you have the `MnoSession`
```java
MnoSession mnoSession = new MnoSession(marketplace, request.getSession());
mnoSession.getLogoutUrl();
```


### Redirecting on error
If any error happens during the SSO handshake, you can redirect users to the following URL:

```java
Maestrano.get(marketplace).ssoService().getUnauthorizedUrl()
```

## Account Webhooks
Single sign on has been setup into your app and Maestrano users are now able to use your service. Great! Wait what happens when a business (group) decides to stop using your service? Also what happens when a user gets removed from a business? Well the controllers describes in this section are for Maestrano to be able to notify you of such events.

### Groups Controller (service cancellation)
Sad as it is a business might decide to stop using your service at some point. On Maestrano billing entities are represented by groups (used for collaboration & billing). So when a business decides to stop using your service we will issue a DELETE request to the webhook.account.groups_path endpoint (typically /maestrano/account/groups/:id).

Maestrano only uses this controller for service cancellation so there is no need to implement any other type of action - ie: GET, PUT/PATCH or POST. The use of other http verbs might come in the future to improve the communication between Maestrano and your service but as of now it is not required.

The example below needs to be adapted depending on your application:

```java
if (Maestrano.get(marketplace).authenticate(request)) {
  MyGroupModel someGroup = MyGroupModel.findByMnoId(restfulIdFromUrl);
  someGroup.disableAccess();
}
```

### Group Users Controller (business member removal)
A business might decide at some point to revoke access to your services for one of its member. In such case we will issue a DELETE request to the webhook.account.group_users_path endpoint (typically /maestrano/account/groups/:group_id/users/:id).

Maestrano only uses this controller for user membership cancellation so there is no need to implement any other type of action - ie: GET, PUT/PATCH or POST. The use of other http verbs might come in the future to improve the communication between Maestrano and your service but as of now it is not required.


The example below needs to be adapted depending on your application:

```java
if (Maestrano.get(marketplace).authenticate(request)) {
  MyGroupModel someGroup = MyGroupModel.findByMnoId(restfulGroupIdFromUrl);
  someGroup.removeUserById(restfulIdFromUrl);
}
```

## API
The maestrano package also provides bindings to its REST API allowing you to access, create, update or delete various entities under your account (e.g: billing).

### Payment API
 
#### Bill
A bill represents a single charge on a given group.

```java
com.maestrano.account.MnoBill
```

##### Attributes

<table>
<tr>
<th>Field</th>
<th>Mode</th>
<th>Type</th>
<th>Required</th>
<th>Default</th>
<th>Description</th>
<tr>

<tr>
<td><b>id</b></td>
<td>readonly</td>
<td>String</td>
<td>-</td>
<td>-</td>
<td>The id of the bill</td>
<tr>

<tr>
<td><b>groupId</b></td>
<td>read/write</td>
<td>String</td>
<td><b>Yes</b></td>
<td>-</td>
<td>The id of the group you are charging</td>
<tr>

<tr>
<td><b>priceCents</b></td>
<td>read/write</td>
<td>Integer</td>
<td><b>Yes</b></td>
<td>-</td>
<td>The amount in cents to charge to the customer</td>
<tr>

<tr>
<td><b>description</b></td>
<td>read/write</td>
<td>String</td>
<td><b>Yes</b></td>
<td>-</td>
<td>A description of the product billed as it should appear on customer invoice</td>
<tr>

<tr>
<td><b>createdAt</b></td>
<td>readonly</td>
<td>Date</td>
<td>-</td>
<td>-</td>
<td>When the the bill was created</td>
<tr>
  
<tr>
<td><b>updatedAt</b></td>
<td>readonly</td>
<td>Time</td>
<td>-</td>
<td>-</td>
<td>When the bill was last updated</td>
<tr>

<tr>
<td><b>status</b></td>
<td>readonly</td>
<td>String</td>
<td>-</td>
<td>-</td>
<td>Status of the bill. Either 'submitted', 'invoiced' or 'cancelled'.</td>
<tr>

<tr>
<td><b>currency</b></td>
<td>read/write</td>
<td>String</td>
<td>-</td>
<td>AUD</td>
<td>The currency of the amount charged in <a href="http://en.wikipedia.org/wiki/ISO_4217#Active_codes">ISO 4217 format</a> (3 letter code)</td>
<tr>

<tr>
<td><b>units</b></td>
<td>read/write</td>
<td>Float</td>
<td>-</td>
<td>1.0</td>
<td>How many units are billed for the amount charged</td>
<tr>

<tr>
<td><b>periodStartedAt</b></td>
<td>read/write</td>
<td>Date</td>
<td>-</td>
<td>-</td>
<td>If the bill relates to a specific period then specifies when the period started. Both period_started_at and period_ended_at need to be filled in order to appear on customer invoice.</td>
<tr>

<tr>
<td><b>periodEndedAt</b></td>
<td>read/write</td>
<td>Date</td>
<td>-</td>
<td>-</td>
<td>If the bill relates to a specific period then specifies when the period ended. Both period_started_at and period_ended_at need to be filled in order to appear on customer invoice.</td>
<tr>

</table>

##### Actions

List all bills you have created and iterate through the list, you will need to precise the marketplace.

```java
List<MnoBill> bills = MnoBill.client(marketplace).all();
```

Access a single bill by id
```java
MnoBill bill = MnoBill.client(marketplace).retrieve("bill-f1d2s54");
```

Create a new bill
```java
Map<String, Object> attrsMap = new HashMap<String, Object>();
attrsMap.put("groupId", "cld-3");
attrsMap.put("priceCents", 2000);
attrsMap.put("description", "Product purchase");

MnoBill bill = MnoBill.client(marketplace).create(attrsMap);
```

Cancel a bill
```java
MnoBillClient client = MnoBill.client(marketplace);
MnoBill bill = client.retrieve("bill-f1d2s54");
client.cancel(bill);
```

#### Recurring Bill
A recurring bill charges a given customer at a regular interval without you having to do anything.

```java
com.maestrano.account.MnoRecurringBill
```

##### Attributes

<table>
<tr>
<th>Field</th>
<th>Mode</th>
<th>Type</th>
<th>Required</th>
<th>Default</th>
<th>Description</th>
<tr>

<tr>
<td><b>id</b></td>
<td>readonly</td>
<td>String</td>
<td>-</td>
<td>-</td>
<td>The id of the recurring bill</td>
<tr>

<tr>
<td><b>groupId</b></td>
<td>read/write</td>
<td>String</td>
<td><b>Yes</b></td>
<td>-</td>
<td>The id of the group you are charging</td>
<tr>

<tr>
<td><b>priceCents</b></td>
<td>read/write</td>
<td>Integer</td>
<td><b>Yes</b></td>
<td>-</td>
<td>The amount in cents to charge to the customer</td>
<tr>

<tr>
<td><b>description</b></td>
<td>read/write</td>
<td>String</td>
<td><b>Yes</b></td>
<td>-</td>
<td>A description of the product billed as it should appear on customer invoice</td>
<tr>

<tr>
<td><b>period</b></td>
<td>read/write</td>
<td>String</td>
<td>-</td>
<td>Month</td>
<td>The unit of measure for the billing cycle. Must be one of the following: 'Day', 'Week', 'SemiMonth', 'Month', 'Year'</td>
<tr>

<tr>
<td><b>frequency</b></td>
<td>read/write</td>
<td>Integer</td>
<td>-</td>
<td>1</td>
<td>The number of billing periods that make up one billing cycle. The combination of billing frequency and billing period must be less than or equal to one year. If the billing period is SemiMonth, the billing frequency must be 1.</td>
<tr>

<tr>
<td><b>cycles</b></td>
<td>read/write</td>
<td>Integer</td>
<td>-</td>
<td>nil</td>
<td>The number of cycles this bill should be active for. In other words it's the number of times this recurring bill should charge the customer.</td>
<tr>

<tr>
<td><b>startDate</b></td>
<td>read/write</td>
<td>Date</td>
<td>-</td>
<td>Now</td>
<td>The date when this recurring bill should start billing the customer</td>
<tr>

<tr>
<td><b>createdAt</b></td>
<td>readonly</td>
<td>DateTime</td>
<td>-</td>
<td>-</td>
<td>When the the bill was created</td>
<tr>
  
<tr>
<td><b>updatedAt</b></td>
<td>readonly</td>
<td>Time</td>
<td>-</td>
<td>-</td>
<td>When the recurring bill was last updated</td>
<tr>

<tr>
<td><b>currency</b></td>
<td>read/write</td>
<td>String</td>
<td>-</td>
<td>AUD</td>
<td>The currency of the amount charged in <a href="http://en.wikipedia.org/wiki/ISO_4217#Active_codes">ISO 4217 format</a> (3 letter code)</td>
<tr>

<tr>
<td><b>status</b></td>
<td>readonly</td>
<td>String</td>
<td>-</td>
<td>-</td>
<td>Status of the recurring bill. Either 'submitted', 'active', 'expired' or 'cancelled'.</td>
<tr>
  
<tr>
<td><b>initialCents</b></td>
<td>read/write</td>
<td>Integer</td>
<td><b>-</b></td>
<td>0</td>
<td>Initial non-recurring payment amount - in cents - due immediately upon creating the recurring bill</td>
<tr>

</table>

##### Actions

List all recurring bills you have created and iterate through the list
```java
List<MnoRecurringBill> bills = MnoRecurringBill.client(marketplace).all();
```

Access a single recurring bill by id
```java
MnoRecurringBill bill = MnoRecurringBill.client(marketplace).retrieve("rbill-f1d2s54");
```

Create a new recurring bill
```java
Map<String, Object> attrsMap = new HashMap<String, Object>();
attrsMap.put("groupId", "cld-3");
attrsMap.put("priceCents", 2000);
attrsMap.put("description", "Product purchase");
attrsMap.put("period", "Month");
attrsMap.put("startDate", new Date());

MnoRecurringBill bill = MnoRecurringBill.client(marketplace).create(attrsMap);
```

Cancel a recurring bill
```java
MnoRecurringBillClient client = MnoRecurringBill.client(marketplace);
MnoRecurringBill bill = client.retrieve("rbill-f1d2s54");
client.cancel(bill);
```

## Connec!™ Data Sharing
Maestrano offers the capability to share actual business data between applications via its data sharing platform Connec!™.

The platform exposes a set of RESTful JSON APIs allowing your application to receive data generated by other applications and update data in other applications as well!

Connec!™ also offers the ability to create webhooks on your side to get automatically notified of changes happening in other systems. 

Connec!™ enables seamless data sharing between the Maestrano applications as well as popular apps such as QuickBooks, Xero, MYOB.... One connector - tens of integrations!

### Making Requests

Connec!™ REST API documentation can be found here: http://maestrano.github.io/connec

The Maestrano API provides a built-in client - for connecting to Connec!™. Things like connection and authentication are automatically managed by the Connec!™ client.


```java
String groupId = "cld-3";
// Retrieve default Connect client
ConnecClient connecClient = new ConnecClient(marketplace);

// Fetch all organizations
Map<String, Object> organizations = connecClient.all("organizations", groupId);
System.out.println("Fetched organizations: " + organizations);
// Fetched organizations: {organizations=[{name=Doe Corp Inc., id=8afd71e0-8394-0132-a4d2-2623376cdffe, group_id=cld-3, type=organizations}, ... }

// Retrieve first organization
List<Map<String, Object>> organizationsHashes = (List<Map<String, Object>>) organizations.get("organizations");
String firstOrganizationId = (String) organizationsHashes.get(0).get("id");
Map<String, Object> organization = (Map<String, Object>) connecClient.retrieve("organizations", groupId, firstOrganizationId).get("organizations");
System.out.println("Retrieved first organization: " + organization);
// Retrieved first organization: {name=Doe Corp Inc., id=8afd71e0-8394-0132-a4d2-2623376cdffe, group_id=cld-3, type=organizations}

// Create a new organization
Map<String, Object> newOrganization = new HashMap<String, Object>();
newOrganization.put("name", "New Organization");
organization = (Map<String, Object>) connecClient.create("organizations", groupId, newOrganization).get("organizations");
System.out.println("Created new organization: " + organization);
// Created new organization: {name=New Organization, id=347e0fa0-cfaf-0132-4f1a-42f46dd33bd3, group_id=cld-3, type=organizations}

// Update an organization
organization.put("industry", "Hardware");
String organizationId = (String) organization.get("id");
Map<String, Object> updatedOrganization = (Map<String, Object>) connecClient.update("organizations", groupId, organizationId, organization).get("organizations");
System.out.println("Updated organization: " + updatedOrganization);
// Updated organization: {name=New Organization, id=347e0fa0-cfaf-0132-4f1a-42f46dd33bd3, group_id=cld-3, industry=Hardware, type=organizations}
```


### Webhook Notifications
If you have configured the Maestrano API to receive update notifications (see 'subscriptions' configuration at the top) from Connec!™ then you can expect to receive regular POST requests on the `webhook.connec.notificationsPath` you have configured.

Notifications are JSON messages containing the list of entities that have recently changed in other systems. You will only receive notifications for entities you have subscribed to.

Example of notification message:
```javascript
{
  "organizations": [
    { "id": "e32303c1-5102-0132-661e-600308937d74", name: "DoeCorp Inc.", ... }
  ],
  "people": [
    { "id": "a34303d1-4142-0152-362e-610408337d74", first_name: "John", last_name: "Doe", ... }
  ]
}
```

Entities sent via notifications follow the same data structure as the one described in our REST API documentation (available at http://maestrano.github.io/connec)


## Migrating from previous version

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
Maetrano defaultInstance = Maestrano.configure();
Maetrano presetInstance = Maestrano.configure("myPreset", myPresetProperties);

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

For API calls, you need now to retrieve an instance of a client (MnoBillClient, MnoUserClient etc..) to make the calls.
Before 0.9.0:
```java
List<MnoBill> bills = MnoBill.all();
MnoBill bill = MnoBill.retrieve("rbill-f1d2s54");
```
After 0.9.0:
```java
List<MnoBill> bills = MnoBill.client().all();
MnoBill bill = MnoBill.client().retrieve("rbill-f1d2s54");
// or:
MnoBillClient client = MnoBill.client();
List<MnoBill> bills = client.all();
MnoBill bill = client.retrieve("rbill-f1d2s54");
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

## Logging

Maestrano Java SDK uses the Simple Logging Facade for Java ([SLF4J](http://www.slf4j.org/)), allowing to plug in the desired logging framework at deployment time.

For example, if you use [Log4j](http://logging.apache.org/log4j/2.x/), all you need to do is add the slf4j-log4j12 dependency in your pom file.

```
<dependency>
	<groupId>org.slf4j</groupId>
	<artifactId>slf4j-log4j12</artifactId>
	<version>1.7.21</version>
</dependency>
```


## Support
This README is still in the process of being written and improved. As such it might not cover some of the questions you might have.

So if you have any question or need help integrating with us please contact us on our Support Desk: https://maestrano.atlassian.net/servicedesk/customer/portal/2

## License

MIT License. Copyright 2017 Maestrano Pty Ltd. https://maestrano.com

You are not granted rights or licenses to the trademarks of Maestrano.

