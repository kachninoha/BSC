This application is homework demo project for Bank System Company, s.r.o.
It keeps a record of payments. Each payment includes a currency and an amount.

- after clone, it can be built by maven (mvn clean package), test will be runned and jar package prepared

- application requires to be input file on UTF-8 format ONLY.iy 
- if currencly is not regular number, in czech locale it has to be inputed with "," not with "." (for example USD 10,2 is correct input)
it depends on setted locale.

- where if there are two arguments provided, fist has to be full path to input file and second is number of miliseconds delay between printing outputs
if only one argument provided, it has to be number of miliseconds delay between printing outputs
if no arguments provided, used default print delay 60 seconds and no input file

- example commandline runs :
```
java -jar PaymentTracker-0.0.1-SNAPSHOT.jar

java -jar PaymentTracker-0.0.1-SNAPSHOT.jar 10000

java -jar PaymentTracker-0.0.1-SNAPSHOT.jar C:\Projekty\PaymentTracker\badfile.txt 10000
```

## Important TOTO list:
- test with -Dfile.encoding commandline param whether changing encoding to another works for file inputs
- add more tests-
- fix existing commented tests. currently test of file doesnt work probably because of problem with encoding probably
- add releasing of project for example by maven release plugin
- implement bonus tasks :)

USD 0,5 encoding, mezera utf znak
