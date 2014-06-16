# Koofr Java SDK

This is a Java SDK for easy interaction with Koofr service. See the `examples` folder for a quick start.

## Getting started

For sane defaults you should use `DefaultClientFactory` to obtain an instance of `StorageApi`. For more information read the JavaDoc.

### Mounts
Mounts are the central concept to Koofr. Each mount is a virtual filesystem root; it may be a physical device, a shared folder or something else. Each mount has a unique identifier to reference it.

A mount may contain other mounts. For example: you have a storage device *My Place* where you have a folder *Pictures*. If you share *Pictures* you will implicitly create a new mount.  So a picture stored under `My Place | /Pictures/01.jpg` will also be accessible through `Pictures | /01.jpg`.

### Files
Each file is identified by a pair of mount identifier and a path. Therefore all file operations take a mount id (to specify which filesystem root to use) and a path.

Files stored in Koofr are immutable. This means you cannot modify file after you upload it. You can however delete it and replace it with a modified version - koofr will detect this as a modification.


## Example
This is the *info* example that shows how to connect to Koofr by printing out details about currently logged in user.  


```java
package info;

import net.koofr.api.v2.StorageApi;
import net.koofr.api.v2.DefaultClientFactory;
import net.koofr.api.v2.StorageApiException;

public class Main {
    public static void main(String[] args) throws StorageApiException {
        String host = args[0];
        String username = args[1];
        String password = args[2];
        StorageApi api = DefaultClientFactory.create(host, username, password);

        System.out.println(api.getUserInfo());
    }
}
```

### Running

Examples are already configured to be ran using sbt

    sbt
    project info
    run app.koofr.net myemail@example.com mypassword

or

    sbt
    project cmdline
    run app.koofr.net myemail@example.com mypassword

Hostname is passed in without protocol and without any slashes.


## Building

To build jars use

    sbt package


## Maven

Prebuilt jars are available for maven at [oss.sonatype.org](https://oss.sonatype.org) under id `net.koofr.sdk-java`