# Payroll Application

Welcome to the Payroll Application project - this readme has some information about the project and application.

This application aims to solve the requirements/problem which can be found in Requirements.md

This application is built using:
  - Language: Java 8
  - IDE: IntelliJ 16.1
  - Build System: Gradle 1.3.1

### Building the application
To build/test the application source code, run the following in the root of the project (where gradle.build is)
```sh
$ gradle build
```
The above will run the build and tests (all tests should pass) - the artifacts that are created will be posted to the **build/libs/** folder.

However, those artifacts are not directly runnable (self contained applications) - instead to create a single jar file that hosts the whole application and dependencies; use the shadowJar command, similar to the above build command:
```sh
$ gradle shadowJar
```
This will create the self-contained runnable application called **payroll-application-1.0.0-full.jar** in the **build/libs/** folder. See the next section for running this application.

### Running the application
The application has a single argument **-f** that needs to be set to the location of the employee salary CSV file. Once you have the location of the CSV file, you can run the application like so:
```sh
$ java -jar payroll-application-1.0.0-full.jar -f C:\payroll\employee-salary-input.csv
```
Note, the file location above is the windows convention, but on other operating systems, you would use that system's directory/file structure instead.

### Input of the application
The CSV file given as application input needs to adhere to the following CSV format (as example):
```
first name,last name,annual salary,super rate (%),payment start date,payment end date
David,Rudd,60050,9,01/03/2013,31/03/2013
Ryan,Chen,120000,10,01/03/2013,31/03/2013
```
Note: the date format is dd/MM/yyyy (June 9th 2016 = 09/06/2016). Also, the CSV header must match the above otherwise the application will not parse the CSV file.

### Output of the application
##### On Screen
When the application is run, various messages will be shown on screen of the application's progress and status.
If there are any errors, it will be shown on screen too. The below shows the screen output for a successful run:
```
$ java -jar payroll-application-1.0.0-full.jar -f C:\payroll\employee-salary-input.csv

Found [5] valid and [0] invalid employee salary records in resource [C:\payroll\employee-salary-input.csv]
The supported tax years of this application are: [2013]
Calculated a total of [5] pay-stubs

Successfully wrote all calculated pay-stub information to the file: [C:\payroll\output-paystubs-employee-salary-input.csv]

Thanks for using this payroll application!
Goodbye!
```
##### Employee Pay Stubs
All calculated employee pay stubs are written to the same directory as the input file given, and the name will be prefixed with **output-paystubs-**.
For example: if the input file is C:\payroll\salaries.csv; then the output file will be C:\payroll\output-paystubs-salaries.csv.

The format of the output employee paystub file will be (as an example):
```
name,pay period,gross income,income tax,net income,super
David Rudd,01/03/2013 - 31/03/2013,5004,922,4082,450
Ryan Chen,01/03/2013 - 31/03/2013,10000,2696,7304,1000
```
##### Log files
The application will write all it's log files to the file in **logs\payroll-application.log**, where "logs\" directory will be created in the same directory as the application resides in.
If there are any problems with the application, the log files will contain more information.

### Application assumptions and limitations

 - Only the tax year 2013 is supported (more tax years can be added through the resources folder)
 - Input salary dates will not exceed the tax year bounds (i.e. multi-year tax paystubs are not supported)
 - File access (read and write) is given exclusively to this application and has permission to access them
 - Input data will be sufficiently small - not billions of salary inputs (out of memory and resource concerns)
 - A given tax year will span from July 1st - (next year) June 30th