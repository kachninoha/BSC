This application is homework demo project for Bank System Company, s.r.o.
It keeps a record of payments. Each payment includes a currency and an amount.

- after clone, it can be built by maven (mvn clean package), test will be runned by maven and jar package prepared

- application requires to have input file on UTF-8 format only
- if currencly is not regular number, in czech locale it has to be inputed with "," not with "." (for example USD 10,2 is correct input)
it depends on setted locale.

* if are two arguments provided, fist has to be full path to input file and second is number of miliseconds delay between printing outputs
* if only one argument provided, it has to be number of miliseconds delay between printing outputs
* if no arguments provided, used default print delay 60 seconds and no input file

- example commandline runs :
```
java -jar PaymentTracker-0.0.1-SNAPSHOT.jar

java -jar PaymentTracker-0.0.1-SNAPSHOT.jar 10000

java -jar PaymentTracker-0.0.1-SNAPSHOT.jar C:\Projekty\PaymentTracker\badfile.txt 10000
```

## Important TOTO list:
- test if works -Dfile.encoding commandline param works for file inputs which changes default encoding
- cover code by more test
- fix existing commented tests. currently tests used input file doesnt work probably because of problem with encoding probably
- add releasing of project for example by maven release plugin
- add support of other locales for example setu as command line param (example -Duser.country=CA -Duser.language=fr)
- implement bonus tasks :)

