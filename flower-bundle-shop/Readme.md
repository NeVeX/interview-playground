# Flower Bundle Shop Application

Welcome to the Flower Bundle Shop Application project - this readme has some information about the project and the application.

This application aims to solve the requirements/problem which can be found in the Requirements.pdf file.

### Application Overview

This application is built using Java. It is a command line application that accepts various flower bundle orders, which are then used
to calculate the most appropriate bundle order that fulfils the request.

For example - Given that a bundle called R12 has 10 roses in the bundle and sells for $10;
then an order of
```sh
20 R12
```
would produce:
```sh
20 R12 - $24.00
   2 x 10 @ $12.00 ea
```

### Application assumptions and limitations

 - There is no database of products - instead, products are read from the resources of the application
 - Only supported currency is the Australian Dollar
 - All calculation rounding is HALF_UP (https://docs.oracle.com/javase/7/docs/api/java/math/RoundingMode.html#HALF_UP)
 - For new products to be added, the resources file will need to be updated with the new products and bundles
 - The least amount of bundles is the default order calculation

### Building the application

This application was built using:
  - Language: Java 8
  - IDE: IntelliJ 16.1
  - Build System: Gradle 2.3
  - Testing: Junit and AssertJ
  - Dependencies: Log4j, SLF4J, Jackson JSON, Apache StringUtils

Note: In the **release** directory, there is an already built application **flower-bundle-shop-1.0.0-full.jar** that can be used (without having to build the source).

To build/test the application source code, run the following in the root of the project (where gradle.build is)
```sh
$ gradle clean build
```
The above will clean any temporary directories for this application (if exists) and then run the build and tests (all tests should pass).
The artifact that is created will be posted to the **build/libs/** folder.

However, that artifact IS not directly runnable (i.e. a self contained application).
 So to create the single jar file that packages the whole application and dependencies; use the **shadowJar** command, similar to the above build command syntax:
```sh
$ gradle shadowJar
```
This will create the self-contained runnable application called **flower-bundle-shop-1.0.0-full.jar** in the **build/libs/** folder.

See the next section for running this application.

### Running the application
The application can be run by using the **java -jar** command, like so:
```sh
$ java -jar flower-bundle-shop-1.0.0-full.jar
```

When the application starts, an example of output should look like the below:
```sh
$ java -jar flower-bundle-shop-1.0.0-full.jar

Welcome to the Flower Bundle Shop Application!

Below you will be able to input orders in the form: 'X ABC' - where 'X' is the order amount and 'ABC' is the product code.
You can input multiple orders by using the enter key when each order line is completed.
Once you are done with a particular order, enter 'done' to calculate all the order bundles.

To exit the application, enter 'quit'.

[1] Enter an order:
```

### Input of the application

When the application starts, it will prompt for input like so:
```sh
[1] Enter an order:
```
The format to input orders is 'X ABC', where 'X' is a number and 'ABC' is a valid product code.

Example:
```sh
[1] Enter an order: 10 R12
```

#### Input Validation

Once you have entered an order, press enter.
The application will then validate your order and make sure it is valid - if it is not valid an error is printed on screen.

Example of input error:
```sh
[1] Enter an order: abc R12
  ** Error: Order size [abc] is not a number
[2] Enter an order: 12R12
  ** Error: Input order is not in expected format - e.g. 10 T58
[3] Enter an order:
```

If an order is valid, then no error is shown and instead, it will prompt you for your next order:

Example:
```sh
[1] Enter an order: 10 R12
[2] Enter an order: 30 L09
[3] Enter an order:
```

#### Processing Orders

To process all your input orders, enter the word **DONE** - this will use all input orders and calculate the bundles.

Example:
```sh
[1] Enter an order: 10 R12
[2] Enter an order: 30 R12
[3] Enter an order: DONE
Processing your input orders
Below are all the order bundle results:

30 R12 - $38.97
  3 x 10 @ $12.99 ea

10 R12 - $12.99
  1 x 10 @ $12.99 ea

[1] Enter an order:
```

Once an order is processed, it will show all the results and it will then ask for more input orders again.

#### Un-fulfilled Orders

Not all orders can be fulfilled with a bundle - in such cases, you will see an error for that order.

Example:
```sh
[1] Enter an order: 3 R12
[2] Enter an order: 133 R12
[3] Enter an order: 10 R12
[4] Enter an order: DONE
Processing your input orders
Below are all the order bundle results:

10 R12 - $12.99
  1 x 10 @ $12.99 ea

3 R12
  ** No bundle exists for this order **

133 R12
  ** No bundle exists for this order **

[1] Enter an order:
```

Like before, it will show all results and once done, prompt for more user input.

#### Exiting the application

To exit the application, enter **QUIT**

### Log files
The application will write all it's log files to the file in **logs\flower-bundle-shop-application.log**, where "logs\" directory will be created in the same directory as the application resides in.
If there are any problems with the application, the log files will contain more information.
