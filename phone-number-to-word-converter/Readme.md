# Phone Number To Word Converter Application

Welcome to the Phone Number to Word project - this readme has some information about the project and the application.

This application aims to solve the requirements/problem which can be found in the Requirements.pdf file.
At a high lever, the application, when run, can parse input phone numbers (from files or command line input) and convert
those numbers to dictionary words, where possible.

### Digit to Letter Conversion Mapping
The Digit to Letter conversion is shown below - this mapping is used in the application when determing what letters
can take a digits' place in a phone number conversion.

| Digit         | Letter Combination Options |
| ------------- |:--------------------------:|
| 0             | 0                          |
| 1             | 1                          |
| 2             | a, b, c                    |
| 3             | d, e, f                    |
| 4             | g, h, i                    |
| 5             | j, k, l                    |
| 6             | m, n, o                    |
| 7             | p, q, r, s                 |
| 8             | t, u, v                    |
| 9             | w, x, y, z                 |

For example; Given the input 1225563, one of the outputs would be "1-CALL-ME".

### Building the application

This application was built using:
  - Language: Java 8
  - IDE: IntelliJ 16.1
  - Build System: Gradle 2.3

You will need the Java and Gradle minimum versions above to build the application.
Note: In the **release** directory, there is an already built application that can be used (without having to build the source).

To build (and run the tests of) the application source code, run the following in the root of the project (where gradle.build is)
```sh
$ gradle clean build
```
The above will clean any temporary directories for this application (if exists) and then run the build and tests (all tests should pass).

To create an runnable/executable application, run the below:
```sh
$ gradle jar
```
This will create the self-contained runnable application called **phone-number-to-word-converter-1.0.0.jar** in the **build/libs/** folder. See the next section for running this application.

### Running the application
Note: You must have Java 8 at a minimum installed to run this application.
This application has various modes that it can be run within.

The application can run without any argument input - in this mode the default dictionary is used and all phone numbers are prompted for.
1: Optionally, using the *-d* argument, you can override the application dictionary - all conversions will use the provided dictionary.
2: Optionally, using the *-p* argument, you can provide a file with phone numbers - all phone numbers in the file will be converted (if possible) and the result printed on screen.

##### Running the application in default mode
```sh
$ java -jar phone-number-to-word-converter-1.0.0.jar
```
##### Running the application with dictionary override
```sh
$ java -jar phone-number-to-word-converter-1.0.0.jar -d=/path/to/your.dict
```
##### Running the application with phone numbers provided
```sh
$ java -jar phone-number-to-word-converter-1.0.0.jar -p=/path/to/phone.numbers.txt
```

Note: you can provided both optional arguments at the same time if required.

### Input of the application

#### Phone Number Prompt
When running the application without providing a file of phone numbers, the application will prompt you for input of phone numbers.
Note: All non digit characters are ignored from input.

#### Phone Number File
If you provide a file of phone numbers, note that this file must contain each individual phone number on a separate line.
Note: All non digit characters are ignored in the file

#### Dictionary Override File
If you provide a dictionary file, note that each individual word must be on a separate line.
Note: All non-characters are ignored in the file


### Output of the application
#### On Screen
All output is provided on screen. An example of output is shown below:
```
$ java -jar phone-number-to-word-converter-1.0.0.jar

Welcome to the Phone Number to Word Application!

The following are the input options:
  -d=/path/to/my.dictionary  ==> Optionally provide your own dictionary file to use for conversion
  -p=/path/to/phone.numbers  ==> Optionally provide a file of phone numbers to convert to words

The following were the parsed input values:
  Dictionary File   ==> **None provided - will use application default**
  Phone Number File ==> **None provided - will prompt for phone numbers**

Enter a number (or quit with 'q'): 225563
Working...
Found [8] word combinations for [225563]:
  - CALK-ME
  - CALL-OF
  - CALL-ME
  - CALK-OF
  - BALL-OF
  - BALK-OF
  - BALL-ME
  - BALK-ME


Enter a number (or quit with 'q'): q

Thank you for using this Phone Number to Word Application - Goodbye! :-)
```

#### Log files
The application will write all it's log files to the file in **phone-number-to-word-converter.log**.
If there are any problems with the application, the log files will contain more information.

### Application assumptions and limitations

- Phone numbers (when converted to Long) will fit within it's range. Phone numbers should not generally exceed 16 digits
(http://stackoverflow.com/questions/3350500/international-phone-number-max-and-min), hence the Long data type can hold such numbers. As such,
  very large numbers are not supported by this application, but can be supported in a later release.
- The encoding of the file is not known ahead of time - the encoding use is the default of the JVM
