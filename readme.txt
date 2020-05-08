FloatingPoint.java

________BACKGROUND_____________________________________________________________

Computers use floating point representations (sign, mantissa, and exponent) to 
store real numbers. The format of this representation has been mostly standardised
to the IEEE format- there was a popular precursor that was popular in the 50's
and 6-'s: The IBM format.

FloatingPoint is a program to convert 32 or 64 binary files from IBM to IEEE
format and vice-versa, for the purpose of updating old database files.

Since the IBM format can store numbers which are non-representable as IEEE numbers,
the program simplifies them as per IEEE standards:

Numbers which are too large (too far from 0, positive or negative) are returned
as positive of negative infinity.

Numbers which are closer to 0 than IEEE can represent are returned as positive or
negative zero.

_______HOW TO EXECUTE__________________________________________________________

The program consists of three java files:
FloatingPoint.java
IBM.java
IEEE.java

Navigate to the directory containing the three .java files, and copy any files
you would like to convert into the same location. Use javac to compile the program:

$ javac *.java

Use java to run to execute FloatingPoint which contains the main() method

$ java FloatingPoint

The program will prompt you to specify an input file and the  precision, then an
output filename and the desired precision. The program will then write the
values from the binary input file converted to IEEE binary Strings.

________TESTING_________________________________________________________________

This repo contains 2 .bin test files..

testfile32.bin contains the following values:

-118.625 / C276A000
70.3125 / 42465000
998.15625 / 433E6280

Which should result in the following output

11000010011101101010000000000000
01000010010001100101000000000000
01000011001111100110001010000000

testfile64.bin contains the following values:
123.456 / 427B74BC70000000
6.66 / 416A8F5C00000000 
765.4321 / 432FD6E9E2000000

Which should result in the following output:
0100001001111011011101001011110001110000000000000000000000000000
0100000101101010100011110101110000000000000000000000000000000000
0100001100101111110101101110100111100010000000000000000000000000

________AUTHORS_________________________________________________________________

Mickey Treadwell
Benjamin McCarthy
Josh Signal
Josef Bode


