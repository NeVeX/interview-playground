# RedBubble - Camera Website Generator Application

### Welcome!

This project aims to solve the problem presented in the `Requirements.html` file.

At a high level, this project represents an application that is able to invoke an API, retrieve camera EXIF data from the API,
use this information to create a simple web site with camera pictures and navigation from all the cameras <-> models <-> makes.

### Assumptions Made

While building this application, certain assumptions were made:
* Camera images can be injected into generated html using <img> with `src` attributes using the full http url - i.e. pictures are not downloaded and referenced locally
* The API (XML) contract will be the same for all API urls passed in
* The output directory given will be clean (have no other files/directories in it) - if the application outputs a file/directory that already exists
on the machine, then it will not overwrite and instead throw an error
* The API (XML) will not return enormous data volumes (no need to page)
* The Model page does not need to be paged - all images will fit reasonably in the one page (extension on above point)
* For thumbnail pictures, the smallest picture size should be used
* Invalid data from the API (missing make and model information) will be discarded - application will log a message but continue other processing
* There will be sufficient space on the machine to save all files
* The HTML does not need to be optimized for various devices

### Application Stack

The application uses `Java 8`, `HTML` and `JS`; and a combination of various libraries, which are listed below:

* Log4j + SL4J - Logging
* Apache Commons - Helpful utilities
* Thymeleaf - Template engine to render html
* Simple XML - To help parse XML documents
* OKHttp - To help consume Http API's
* Commons CLI - To help with parsing user input
* Junit - For testing
* AssertJ - For asserting test cases within Junit

The build system used is `Gradle 2.3`

### Application Release

While you can checkout this repo and build the source code to create the application, there is already a pre-existing application
artifact `redbubble-generate-camera-website-1.0.0-full.jar` created in the `/releases` directory.

See the below section to know how to run the application.

### Building The Application

If you wish to build the application from source, then check out this repo first.

**Note, you will need `Java 8` and at least `Gradle 2.3` installed to be able to build this application.**

Navigate to the location where this repo lives on your computer.

Then at the root level of this project, issue a build command:

```gradle
$ gradle clean build
```

This will build the application, run all the tests, and save the artifact to `builds/libs`.

### Building A Runnable Application

The above section deals with building the application, which only contains the source code of this project - it does not contain the dependencies.

To create an all in one application, we use the third party library `shadowJar`.

So, to create a runnable application; do the following:

```gradle
$ gradle clean shadowJar
```

This will then create the `full` application and save it in the `builds/libs` folder, called `redbubble-generate-camera-website-1.0.0-full.jar`.

### Running The Application

At this point, we should have an application file that we wish to run, e.g. `redbubble-generate-camera-website-1.0.0-full.jar`

There are required input arguments that must be provided to the application.

#### Input Arguments

|Input Argument|Description|
|:-----:|:---------:|
|`a`|The `A`PI Url to use to get camera EXIF data from|
|`o`|The `O`utput directory location to save all generated web pages|

Note, if the output directory does not exist, it will be created.

However, if there is any output file that also exists in the target directory, the application will not override the file.

#### Example (Success Run)

An example of running the application successfully is shown below:

```sh
$ java -jar redbubble-generate-camera-website-1.0.0-full.jar -o=/user/website -a=http://take-home-test.herokuapp.com/api/v1/works.xml

usage: java -jar redbubble-generate-camera-website-*.jar -a <arg> -o <arg>

See below on how to use this RedBubble Camera Website Generator
-a <arg>The camera works API to use for exif data
-o <arg>The full directory location to output all generated HTML files

Valid Arguments received from input:
 -- Api Camera Url:   http://take-home-test.herokuapp.com/api/v1/works.xml
 -- Output Directory: /user/website

Attempting to get all Cameras from the API...
Received [12] cameras from the API

Success! Generated all html web pages to: /user/website
```

#### Example (Failure Run)

If you run the application and there is a problem with input, the API, or anything, then you should see an error message on screen like so:

Note, in this example, the API argument was not provided

```sh
$ java -jar redbubble-generate-camera-website-1.0.0-full.jar -o=/user/website

usage: java -jar redbubble-generate-camera-website-*.jar -a <arg> -o <arg>

See below on how to use this RedBubble Camera Website Generator
-a <arg>The camera works API to use for exif data
-o <arg>The full directory location to output all generated HTML files

****ERROR: A problem occurred during execution - Missing required arguments: [a]
For more information, look in the log file within the /logs directory.
```

As you can see above, it detected missing arguments and failed the application.

#### Application Logging

There is a log file (and folder) created in the corresponding application directory called `logs/redbubble-camera-website-generator.log`.

This log file can be consulted to get more information about the application events. Also if there are any errors, the log file
will contain more details about the error.

### Viewing The Website

After the application is run, navigate to the folder that you provided the application with (the `o` option).

In that directory, you will see:

* `*.html` files - all the html files that correspond to
 * `index.html` - the home page
 * `make` pages - the html pages for camera makes
 * `model` pages - the html pages for camera models
* `scripts` directory - this houses the javascript files to use in various html pages
* `styles` directory - this houses all the styles to apply

The entry point into the website is through the `index.html` - using this will allow you to navigate all parts of the website

#### Example Website

In the `/releases` directory of this repo, there is a `example-website-output` folder. Inside this folder is an example output
using the given requirements API.