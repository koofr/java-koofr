# Koofr Java SDK

This is a Java SDK for easy interaction with Koofr service. See the `examples` folder for a quick start.

## A Quick Start

```java
import net.koofr.api.auth.Authenticator;
import net.koofr.api.auth.basic.HttpBasicAuthenticator;
import net.koofr.api.http.Client;
import net.koofr.api.rest.v2.Api;

public class Main {

  public static void main(String args[]) {

    Client c = new BasicClient();
    Authenticator a = new HttpBasicAuthenticator("username", "password");
    Api api = new Api("https://app.koofr.net/api/v2", a, c);

    Self self = api.self().get();
    
  }

}
```

Obviously, you need an HTTP Client and an Authenticator to use the Api. 

The SDK provides an abstraction for both. You probably won't need to implement your
own authenticator since the ones supplied in net.kofr.api.auth subpackages cover all
possible authentication options you have with Koofr: OAuth2, HTTP Basic and our own
authentication token scheme.

You might want to implement your own HTTP client on the other hand: we provide a simple one
based on the HttpURLConnection class from the Java runtime, but you might want to use an
Apache HttpComponents based one or whatever the preferred option on your platform is.
You're welcome, you just need to implement the Client, Request and Response interfaces.


## Examples

There are two examples demonstrating use of a Koofr client. Run them via SBT. 

    sbt
    project info
    run app.koofr.net myemail@example.com mypassword

or

    sbt
    project cmdline
    run app.koofr.net myemail@example.com mypassword

Hostname is passed in without protocol and without any slashes.

You can also use OAuth2-based authentication in the examples, by replacing the username and
password with token URL, client ID, client secret and grant code. However, you need to get
the grant code before running the examples: perhaps with curl or with
[OAuth Playground](https://developers.google.com/oauthplayground/)


## Building

To build jars use

    sbt package


## Maven

Prebuilt jars are available for maven at [JCenter](https://bintray.com/koofr/maven/java-koofr/view), group ID `net.koofr`, artefact ID `java-koofr`

