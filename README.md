##File Segregator ##
create a directory structure
HOME
DEV
TEST

When the file appears in the HOME directory, depending on the extension, it will move to the folder according to the following rules
a file with the extension .jar, whose creation time is even, we move to the DEV folder
a file with the extension .jar, whose creation time is odd, we move to the TEST folder
file with the .xml extension, we move to the DEV folder
Additionally, the newly created /home/count.txt file should contain the number of transferred files (all and broken down into
directories), the file should store the current number of processed files at any time during program operation.

## Instalation ##

* JDK 1.8
* Apache Maven 3.x

## Build and Run ##
```
mvn clean package:
mvn exec:java
```
