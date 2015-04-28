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
  * [Metadata Endpoint](#metadata-endpoint)
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

- - -

## Getting Setup
Before integrating with us you will need an App ID and API Key. Maestrano Cloud Integration being still in closed beta you will need to contact us beforehand to gain production access.

For testing purpose we provide an API Sandbox where you can freely obtain an App ID and API Key. The sandbox is great to test single sign-on and API integration (e.g: billing API).

To get started just go to: http://api-sandbox.maestrano.io

A **java demo application** is also available: https://github.com/maestrano/demoapp-java

## Getting Started

### Installation

To install maestrano-java using Maven, add this dependency to your project's POM:
```
<dependency>
  <groupId>com.maestrano</groupId>
  <artifactId>maestrano-java</artifactId>
  <version>0.6.0</version>
</dependency>
```

### Configuration
#### Via config file

You can configure maestrano by dropping a "maestrano.properties" file in the classpath which looks like this:
```ini
# ===> App Configuration
#
# => environment
# The environment to connect to. If set to 'production' then all Single Sign-On (SSO) and API requests will be made to maestrano.com. If set to 'test' then requests will be made to api-sandbox.maestrano.io. 
# The api-sandbox allows you to easily test integration scenarios.
app.environment=test

# => host
# This is your application host (e.g: my-app.com) which is ultimately used to redirect users to the right SAML url during SSO handshake.
app.host=http\://localhost:8080

# ===> Api Configuration
#
# => id and key
# Your application App ID and API key which you can retrieve on http://maestrano.com via your cloud partner dashboard. 
# For testing you can retrieve/generate an api.id and api.key from the API Sandbox directly on http://api-sandbox.maestrano.io
api.id=prod_or_sandbox_app_id
api.key=prod_or_sandbox_api_key


# ===> SSO Configuration
#
# => enabled
# Enable/Disable single sign-on. When troubleshooting authentication issues you might want to disable SSO temporarily
sso.enabled=true

# => sloEnabled
# Enable/Disable single logout. When troubleshooting authentication issues you might want to disable SLO temporarily. 
# If set to false then MnoSession#isValid - which should be used in a controller action filter to check user session - always return true
sso.sloEnabled=true

# => idm
# By default we consider that the domain managing user identification is the same as your application host (see above config.app.host parameter). 
# If you have a dedicated domain managing user identification and therefore responsible for the single sign-on handshake (e.g: https://idp.my-app.com) then you can specify it below
sso.idm=https\://idp.myapp.com

# => initPath
# This is your application path to the SAML endpoint that allows users to initialize SSO authentication. 
# Upon reaching this endpoint users your application will automatically create a SAML request and redirect the user to Maestrano. Maestrano will then authenticate and authorize the user. 
# Upon authorization the user gets redirected to your application consumer endpoint (see below) for initial setup and/or login.
sso.initPath=/maestrano/auth/saml/init

# => consumePath
#This is your application path to the SAML endpoint that allows users to finalize SSO authentication. 
# During the 'consume' action your application sets users (and associated group) up and/or log them in.
sso.consumePath=/maestrano/auth/saml/consume

# => creationMode
# !IMPORTANT
# On Maestrano users can take several "instances" of your service. You can consider
# each "instance" as 1) a billing entity and 2) a collaboration group (this is
# equivalent to a 'customer account' in a commercial world). When users login to
# your application via single sign-on they actually login via a specific group which
# is then supposed to determine which data they have access to inside your application.
# 
# E.g: John and Jack are part of group 1. They should see the same data when they login to
# your application (employee info, analytics, sales etc..). John is also part of group 2 
# but not Jack. Therefore only John should be able to see the data belonging to group 2.
# 
# In most application this is done via collaboration/sharing/permission groups which is
# why a group is required to be created when a new user logs in via a new group (and 
# also for billing purpose - you charge a group, not a user directly). 
# 
# - mode: 'real'
# In an ideal world a user should be able to belong to several groups in your application.
# In this case you would set the 'sso.creation_mode' to 'real' which means that the uid
# and email we pass to you are the actual user email and maestrano universal id.
# 
# - mode: 'virtual'
# Now let's say that due to technical constraints your application cannot authorize a user
# to belong to several groups. Well next time John logs in via a different group there will
# be a problem: the user already exists (based on uid or email) and cannot be assigned 
# to a second group. To fix this you can set the 'sso.creation_mode' to 'virtual'. In this
# mode users get assigned a truly unique uid and email across groups. So next time John logs
# in a whole new user account can be created for him without any validation problem. In this
# mode the email we assign to him looks like "usr-sdf54.cld-45aa2@mail.maestrano.com". But don't
# worry we take care of forwarding any email you would send to this address
sso.creationMode="virtual"
      
# ===> Account Webhooks
# Single sign on has been setup into your app and Maestrano users are now able
# to use your service. Great! Wait what happens when a business (group) decides to 
# stop using your service? Also what happens when a user gets removed from a business?
# Well the endpoints below are for Maestrano to be able to notify you of such
# events.
#
# Even if the routes look restful we issue only issue DELETE requests for the moment
# to notify you of any service cancellation (group deletion) or any user being
# removed from a group.
webhook.account.groupsPath = /maestrano/account/groups/\:id
webhook.account.groupUsersPath = /maestrano/account/groups/\:group_id/users/\:id

# ===> Connec!™ Webhooks
# == Notification Path
# This is the path of your application where notifications (created/updated entities) will be POSTed to.
# You should have a controller matching this path handling the update of your internal entities
# based on the Connec!™ entities you receive
webhook.connec.notificationsPath = /maestrano/connec/notifications

# == Subscriptions
# This is the list of entities (organizations,people,invoices etc.) for which you want to be
# notified upon creation/update in Connec!™
webhook.connec.subscriptions.accounts = true
webhook.connec.subscriptions.company = true
webhook.connec.subscriptions.events = false
webhook.connec.subscriptions.event_orders = false
webhook.connec.subscriptions.invoices = true
webhook.connec.subscriptions.items = true
webhook.connec.subscriptions.journals = false
webhook.connec.subscriptions.organizations = true
webhook.connec.subscriptions.payments = false
webhook.connec.subscriptions.pay_items = false
webhook.connec.subscriptions.pay_schedules = false
webhook.connec.subscriptions.pay_stubs = false
webhook.connec.subscriptions.pay_runs = false
webhook.connec.subscriptions.people = true
webhook.connec.subscriptions.projects = false
webhook.connec.subscriptions.tax_codes = true
webhook.connec.subscriptions.tax_rates = false
webhook.connec.subscriptions.time_activities = false
webhook.connec.subscriptions.time_sheets = false
webhook.connec.subscriptions.venues = false
webhook.connec.subscriptions.work_locations = false
      
```

#### At runtime

You can configure maestrano with the Properties class using the same config parameters as described above:

```java
Properties props = new Properties();
props.setProperty("app.environment", "production");
Maestrano.configure(props);
```

### Metadata Endpoint
Your configuration initializer is now all setup and shiny. Great! But need to know about it. Of course
we could propose a long and boring form on maestrano.com for you to fill all these details (especially the webhooks) but we thought it would be more convenient to fetch that automatically.

For that we expect you to create a metadata endpoint that we can fetch regularly (or when you press 'refresh metadata' in your maestrano cloud partner dashboard). By default we assume that it will be located at
YOUR_WEBSITE/maestrano/metadata(.json)

Of course if you prefer a different url you can always change that endpoint in your maestrano cloud partner dashboard.

What would the controller action look like? First let's talk about authentication. You don't want that endpoint to be visible to anyone. Maestrano always uses http basic authentication to contact your service remotely. The login/password used for this authentication are your actual api.id and api.key.

So here is an example of page to adapt depending on the framework you're using:

```jsp
<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.maestrano.Maestrano" %>

<%
  java.io.PrintWriter writer = response.getWriter();

  if (Maestrano.authenticate(request)) {
    writer.write(Maestrano.toMetadata());
  } else {
    writer.write("Failed");
  }
  
  writer.flush();
%>
```

## Single Sign-On Setup
In order to get setup with single sign-on you will need a user model and a group model. It will also require you to write a controller for the init phase and consume phase of the single sign-on handshake.

You might wonder why we need a 'group' on top of a user. Well Maestrano works with businesses and as such expects your service to be able to manage groups of users. A group represents 1) a billing entity 2) a collaboration group. During the first single sign-on handshake both a user and a group should be created. Additional users logging in via the same group should then be added to this existing group (see controller setup below)

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
  AuthRequest authReq = new AuthRequest(request);
  String ssoUrl = authReq.getRedirectUrl();
  
  response.sendRedirect(ssoUrl);
%>
```

Based on your application requirements the consume action might look like this:
```jsp
<%@ page import="com.maestrano.saml.Response,com.maestrano.sso.*" %>
<%
  Response authResp = new Response();
  authResp.loadXmlFromBase64(request.getParameter("SAMLResponse"));
  
  if (authResp.isValid()) {
    
    // Build maestrano user and group objects
    MnoUser mnoUser = new MnoUser(authResp);
    MnoGroup mnoGroup = new MnoGroup(authResp);
    
    // Build/Map local entities
    MyGroup localGroup = MyGroup.FindOrCreateForMaestrano(mnoGroup);
    MyUser localUser = MyUser.FindOrCreateForMaestrano(mnoUser);
    
    // Add localUser to the localGroup if not already part
    // of it
    if (!localGroup.hasMember(localUser)){
      localGroup.addMember(localUser);
    }
    
    // Set Maestrano session (for Single Logout)
    MnoSession mnoSession = new MnoSession(request.getSession(),mnoUser);
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
MnoSession mnoSession = new MnoSession(request.getSession());

if (!mnoSession.isValid()) {
  response.sendRedirect(Maestrano.ssoService().getInitUrl());
}
```

The above piece of code makes at most one request every 3 minutes (standard session duration) to the Maestrano website to check whether the user is still logged in Maestrano. Therefore it should not impact your application from a performance point of view.

If you start seing session check requests on every page load it means something is going wrong at the http session level. In this case feel free to send us an email and we'll have a look with you.

### Redirecting on logout
When Maestrano users sign out of your application you can redirect them to the Maestrano logout page. You can get the url of this page by calling:

```java
Maestrano.ssoService().getLogoutUrl()
```

### Redirecting on error
If any error happens during the SSO handshake, you can redirect users to the following URL:

```csharp
Maestrano.ssoService().getUnauthorizedUrl()
```

## Account Webhooks
Single sign on has been setup into your app and Maestrano users are now able to use your service. Great! Wait what happens when a business (group) decides to stop using your service? Also what happens when a user gets removed from a business? Well the controllers describes in this section are for Maestrano to be able to notify you of such events.

### Groups Controller (service cancellation)
Sad as it is a business might decide to stop using your service at some point. On Maestrano billing entities are represented by groups (used for collaboration & billing). So when a business decides to stop using your service we will issue a DELETE request to the webhook.account.groups_path endpoint (typically /maestrano/account/groups/:id).

Maestrano only uses this controller for service cancellation so there is no need to implement any other type of action - ie: GET, PUT/PATCH or POST. The use of other http verbs might come in the future to improve the communication between Maestrano and your service but as of now it is not required.

The controller example below reimplements the authenticate_maestrano! method seen in the [metadata section](#metadata) for completeness. Utimately you should move this method to a helper if you can.

The example below needs to be adapted depending on your application:

```java
if (Maestrano.authenticate(request)) {
  MyGroupModel someGroup = MyGroupModel.findByMnoId(restfulIdFromUrl);
  someGroup.disableAccess();
}
```

### Group Users Controller (business member removal)
A business might decide at some point to revoke access to your services for one of its member. In such case we will issue a DELETE request to the webhook.account.group_users_path endpoint (typically /maestrano/account/groups/:group_id/users/:id).

Maestrano only uses this controller for user membership cancellation so there is no need to implement any other type of action - ie: GET, PUT/PATCH or POST. The use of other http verbs might come in the future to improve the communication between Maestrano and your service but as of now it is not required.

The controller example below reimplements the authenticate_maestrano! method seen in the [metadata section](#metadata) for completeness. Utimately you should move this method to a helper if you can.

The example below needs to be adapted depending on your application:

```java
if (Maestrano.authenticate(request)) {
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

List all bills you have created and iterate through the list
```java
List<MnoBill> bills = MnoBill.all();
```

Access a single bill by id
```java
MnoBill bill = MnoBill.retrieve("bill-f1d2s54");
```

Create a new bill
```csharp
Map<String, Object> attrsMap = new HashMap<String, Object>();
attrsMap.put("groupId", "cld-3");
attrsMap.put("priceCents", 2000);
attrsMap.put("description", "Product purchase");

MnoBill bill = MnoBill.create(attrsMap);
```

Cancel a bill
```csharp
MnoBill bill = MnoBill.retrieve("bill-f1d2s54");
bill.cancel();
```

#### Recurring Bill
A recurring bill charges a given customer at a regular interval without you having to do anything.

```csharp
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
List<MnoRecurringBill> bills = MnoRecurringBill.all();
```

Access a single recurring bill by id
```java
MnoRecurringBill bill = MnoRecurringBill.retrieve("rbill-f1d2s54");
```

Create a new recurring bill
```csharp
Map<String, Object> attrsMap = new HashMap<String, Object>();
attrsMap.put("groupId", "cld-3");
attrsMap.put("priceCents", 2000);
attrsMap.put("description", "Product purchase");
attrsMap.put("period", "Month");
attrsMap.put("startDate", new Date());

MnoRecurringBill bill = MnoRecurringBill.create(attrsMap);
```

Cancel a recurring bill
```csharp
MnoRecurringBill bill = MnoRecurringBill.retrieve("rbill-f1d2s54");
bill.cancel();
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

// Fetch all organizations
Map<String, Object> organizations = ConnecClient.all("organizations", groupId);
System.out.println("Fetched organizations: " + organizations);
// Fetched organizations: {organizations=[{name=Doe Corp Inc., id=8afd71e0-8394-0132-a4d2-2623376cdffe, group_id=cld-3, type=organizations}, ... }

// Retrieve first organization
List<Map<String, Object>> organizationsHashes = (List<Map<String, Object>>) organizations.get("organizations");
String firstOrganizationId = (String) organizationsHashes.get(0).get("id");
Map<String, Object> organization = (Map<String, Object>) ConnecClient.retrieve("organizations", groupId, firstOrganizationId).get("organizations");
System.out.println("Retrieved first organization: " + organization);
// Retrieved first organization: {name=Doe Corp Inc., id=8afd71e0-8394-0132-a4d2-2623376cdffe, group_id=cld-3, type=organizations}

// Create a new organization
Map<String, Object> newOrganization = new HashMap<String, Object>();
newOrganization.put("name", "New Organization");
organization = (Map<String, Object>) ConnecClient.create("organizations", groupId, newOrganization).get("organizations");
System.out.println("Created new organization: " + organization);
// Created new organization: {name=New Organization, id=347e0fa0-cfaf-0132-4f1a-42f46dd33bd3, group_id=cld-3, type=organizations}

// Update an organization
organization.put("industry", "Hardware");
String organizationId = (String) organization.get("id");
Map<String, Object> updatedOrganization = (Map<String, Object>) ConnecClient.update("organizations", groupId, organizationId, organization).get("organizations");
System.out.println("Updated organization: " + updatedOrganization);
// Updated organization: {name=New Organization, id=347e0fa0-cfaf-0132-4f1a-42f46dd33bd3, group_id=cld-3, industry=Hardware, type=organizations}
```


### Webhook Notifications
If you have configured the Maestrano API to receive update notifications (see 'subscriptions' configuration at the top) from Connec!™ then you can expect to receive regular POST requests on the `webhook.connec.notificationsPath` you have configured.

Notifications are JSON messages containing the list of entities that have recently changed in other systems. You will only receive notifications for entities you have subscribed to.

Example of notification message:
```ruby
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

## Support
This README is still in the process of being written and improved. As such it might not cover some of the questions you might have.

So if you have any question or need help integrating with us just let us know at support@maestrano.com

## License

MIT License. Copyright 2014 Maestrano Pty Ltd. https://maestrano.com

You are not granted rights or licenses to the trademarks of Maestrano.

