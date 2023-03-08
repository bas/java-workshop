# LaunchDarkly Workshop

## Create a new environment in LaunchDarkly

Follow the documentation to [create an environment](https://docs.launchdarkly.com/home/organize/environments#creating-environments) in the  LaunchDArkly project. Use your email name as the name for the environment.

## Install and import the SDK

Open the file `pom.xml` and add the following dependency:

```xml
<dependency>
  <groupId>com.launchdarkly</groupId>
  <artifactId>launchdarkly-java-server-sdk</artifactId>
  <version>6.0.0</version>
</dependency>
```

Get the Server-side SDK key for your environment, rename `launchdarkly.properties.samples` to `launchdarkly.properties` and add the key. You can use CMD+K (Mac) or CTRL+K (PC) to open the quick search bar to copy the [Server-side SDK key](https://docs.launchdarkly.com/sdk/concepts/client-side-server-side#keys).

Next, import the LaunchDarkly client in `DemoServer.java`:

```java
import com.launchdarkly.sdk.server.*;
```

In the `main()` method fetch the property for the `SERVER_SIDE_SDK` and initialize the client:

```java
String sdkKey = launchDarklyProperties.getProperty("SERVER_SIDE_SDK");
LDClient client = new LDClient(sdkKey);
ctxHandler.setAttribute("ldClient", client);
```

Also add the following method to the class below the `main()` method to subscribe to flag change events:

```java
private static void logWheneverAnyFlagChanges(LDClient client) {
  client.getFlagTracker().addFlagChangeListener(event -> {
     logger.info("Flag has changed: " + event.getKey());
  });
}
```

Call the method below the code to initialize the client:

```java
 logWheneverAnyFlagChanges(client);
```

Test the SDK initialization by running:

```java
maven package
java -jar target/bookstore-v2-1.0.0-SNAPSHOT.jar
 ```

You should see a couple of log statements from the `com.launchdarkly.sdk.server.LDClient.DataSource`.

## Add your first feature flag.

In the `BookServlet.java` class add the following import statements:

```java
import com.launchdarkly.sdk.*;
import com.launchdarkly.sdk.server.*;
```

Next, in the `doGet(HttpServletRequest req, HttpServletResponse resp)` method in `BookServlet.java` in the `try...catch` statemen add the following to get the client and create a context object:

```java
LDClient client = (LDClient) getServletContext().getAttribute("ldClient");

LDContext context = LDContext.builder("context-key-123abc")
  .name("Sandy")
  .build();
```

Get the flag variation for the `show-banner` feature flag and pass it to the template:

```java
boolean showBanner = client.boolVariation("show-banner", context, false);
ctx.setVariable("showBanner", showBanner);
```

Finally in the template `book.html` right above the table class add the following to add the banner feature:

```html
<!-- Campaign banner -->
<span th:if="${showBanner}">
  <div class="banner">
    <img class="media-object" th:src="'/static/images/books.png'" alt="Books icon" />
    <span>Sign up for our news letter and get 10% off on checkout!</span>
  </div>
</span>
```

Restart the server:

```java
maven package
java -jar target/bookstore-v2-1.0.0-SNAPSHOT.jar
 ```

Toggle the `show-banner` flag and watch the console for any flag changes.

Refresh the page and you should now see a banner appearing at the top of the page.

Congratulations for implementing your first flag. :tada:

## Add banner configuration flag

In the `BookServlet.java` class add the following to get the `configure-banner` flag variation and pass it to the template:

```java
String configureBanner = client.stringVariation("configure-banner", context, "Get 3 books for the price of 2");
ctx.setVariable("configureBanner", configureBanner);
```

Replace the banner text:

```html
<span>Sign up for our news letter and get 10% off on checkout!</span>
```

With the value form the flag variation:

```html
<span>[[${configureBanner}]]</span>
```

Restart the server again and test the configuration by toggling the `configure-banner` flag and refreshing the page.

## Add the book ratings

Get the book ratings flag variattion and poass the value to the template:

```java
boolean showRatings = client.boolVariation("show-ratings", context, false);
tx.setVariable("showRatings", showRatings);
```

Add the ratings to the template `booka.html`:

```html
<span th:if="${showRatings}">
  <div>
    <span>Rating: </span>
    <span th:each="i : ${#numbers.sequence(1, book.rating)}">
      <img src="/static/images/star.png" style="width: 12px; height: 12px;" />
    </span>
  </div>
</span>
```

Restart the server again and test the configuration by toggling the `show-ratings` flag and refreshing the page.

## Targeting

Add a few name value pairs to the [context object](https://docs.launchdarkly.com/sdk/features/user-context-config#java), for example:

```java
LDContext context = LDContext.builder("context-key-123abc")
  .name("Sandy")
  .set("isPremium", true)
  .set("categories", LDValue.buildArray().add("Fantasy").add("Programming").add("Travel").build())
  .build();
```

Restart the server. In LaunchDarkly you can open the [Live events page](https://docs.launchdarkly.com/home/flags/live-events?q=watch%20events) to follow the events coming in from our application when flags are evaluated.

To test targeting you can for example add a rule to the `configure-banner` and return the free shipping variation when `isPremium` is `true`.

## Getting evaluation details

The following code example shows you how you can [get evaluation details](https://docs.launchdarkly.com/sdk/features/evaluation-reasons#java):

```java
EvaluationDetail<Boolean> detail = client.boolVariationDetail("show-banner", context, false);
EvaluationReason reason = detail.getReason();
logger.info(reason.toString());
```

## Resources

- [LaunchDarkly docs](https://docs.launchdarkly.com/home?q=watch%20events)
- [Guides](https://docs.launchdarkly.com/guides)
- [Java SDK reference](https://docs.launchdarkly.com/sdk/server-side/java/?q=java+sdk)

