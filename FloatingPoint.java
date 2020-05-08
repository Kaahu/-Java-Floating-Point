import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.math.BigInteger; 

/**
 * FloatingPoint.java is the application class of a conversion program which
 * takes IBM/360 format floating point numbers (double or single precision)
 * and converts them to IEEE Standard 754-1985, outputting the resulting
 * values to a user-specified file.
 *
 * @author Ben McCarthy, Mickey Treadwell, Josef Bode, Josh Signal
 */

public class FloatingPoint{

    // char array for converting byte values to hex characters
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * Main method: prompts the user to specify a filename, followed by a precision, then
     * prompts  the user to specify a second filename for an output file, also followed
     * by a precision. 
     * Reads the bytes from the input file and convert them to hex Strings, which are then 
     * used to initialise IBM objects.
     *
     * @param args optional arguments sent from the command line (none specified).
     */
    public static void main(String[] args){

        System.out.println("Specify an input file followed by a precision (32 or 64)");

        Scanner sc = new Scanner(System.in);
        String infile = "", outfile = "";
        String inprecision = "-1", outprecision = "-1";

        String inputLine = sc.nextLine();

        while (inputLine.split(" ").length != 2){
            System.out.println("Please enter 2 (and only 2) arguments");
            inputLine = sc.nextLine();
        }

        Scanner scLine = new Scanner(inputLine);

        infile = scLine.next();
        int inPrecInt = Integer.parseInt(scLine.next());

        // Verify that a valid value has been specified for precision.
        while (!(inPrecInt == 32) && !(inPrecInt == 64)){
            System.out.println("Invalid precision, please input either 32 or 64");
            inprecision = sc.nextLine();
        }
        
        scLine.close();
        
        System.out.println("Specify an output file followed by a precision (32 or 64)");

        String outputLine = sc.nextLine();

        // Prompt if the user inputs too many/few arguments
        while (outputLine.split(" ").length != 2){
            System.out.println("Please enter 2 (and only 2) arguments");
            outputLine = sc.nextLine();
        }
        
        Scanner scLine2 = new Scanner(outputLine);
        
        outfile = scLine2.next();

        int outprecInt = Integer.parseInt(scLine2.next());

        // Verify that a valid value has been specified for precision.
        while (!(outprecInt == 32) && !(outprecInt == 64)){
            System.out.println("Invalid precision, please input either 32 or 64");
            outprecInt = Integer.parseInt(scLine2.next());
        }
        
        scLine2.close();

        // Input filename cannot be the same as output filename
        while (outfile.equals(infile)){
            
            System.out.println("Output file cannot be the same as input file");
            
            System.out.println("Specify an output file followed by a precision (32 or 64)");

            outputLine = sc.nextLine();

            scLine2 = new Scanner(outputLine);
            
            outfile = scLine2.next();

            outprecInt = Integer.parseInt(scLine2.next());

            scLine2.close();
        }

        sc.close();

        try {
            // Read from binary input file.
            InputStream inputStream = new FileInputStream(infile);

            // The number of bytes per value is 4 for 32 bit input, 8 for 64 bit.
            int bytesPerVal = inPrecInt/8;

            // Populate byte array with representation of hex value.
            byte[] numByte = new byte[bytesPerVal];

            StringBuilder values = new StringBuilder();
            
            while(inputStream.read(numByte) != -1){
                
                // Convert byte array to String representation of hex value.
                String hexString = bytesToHex(numByte);
                
                System.out.println(hexString);
                
                // Convert hex String to binary String.
                String binString = hexToBin(hexString, inPrecInt);

                System.out.println(binString);
                System.out.println("Length: " + binString.length());
                
                // Declare and instantiate IBM object.
                IBM ibm = new IBM(binString);

                double num = ibm.IBMconvert();
                
                System.out.println("\n\nDouble from IBMconvert: " + num);

                // Declare and instantiate IEEE object.
                IEEE ieee = new IEEE(num, outprecInt);
                
                String value = ieee.IEEEconvert();

                values.append(value);
                
            }

            String fullVals = values.toString();

            fullVals = binToHex(fullVals);

            byte[] finalData = hexStringToByteArray(fullVals);
            
            try (FileOutputStream outStream = new FileOutputStream(outfile)) {
                
                outStream.write(finalData);
                
            } catch (Exception e) {
                System.out.println("Invalid output file");
                e.printStackTrace();
            }
                
            
        } catch (IOException e) {
            System.out.println("something wrong");
            e.printStackTrace();
        }
    }


    /**
     * Converts a String hex representation of a hex value to a String binary
     * representation of a hex value using the BigInteger class.
     *
     * @param inHex the hex String to be converted
     * @return String the converted binary String hex value
     */
    public static String hexToBin(String inHex, int prec) {
        
        String hexBin = new BigInteger(inHex, 16).toString(2);

        if (prec == 32){
            while (hexBin.length() < 32){
                hexBin = "0" + hexBin;
                
            }
        }else if (prec == 64){
            while (hexBin.length() < 64){
                hexBin = "0" + hexBin;
                
            }
        }
        return hexBin;
    }

    /**
     * Converts a byte array to a String representation of a hex value.
     *
     * @param inBytes the byte array containing the values to be converted.
     * @return String the resulting hex String.
     */
    public static String bytesToHex(byte[] inBytes){

        /* We're going to add chars to this char array then convert it to a string
           at the end. The char array is twice the length of the bite array, because
           each byte we parse will contain two hex values.
        */
        char[] hexChars = new char[inBytes.length *2];

        // Iterate through the byte array 
        for (int i = 0; i < inBytes.length; i++){

            /* This part's a bit complicated. When you promote a byte to an int,
               because int is a signed value, it changes the value from
               00 00 00 xx to FF FF FF xx. By using a bitwise & with 0xFF, it
               sets the F's back to 0, but keeps the last two bytes ie. it
               converts it back to unsigned.
            */
            
            int byteValue = inBytes[i] & 0xFF;

            // This line gets the character corresponding to the FIRST hex value
            
            hexChars[i*2] = HEX_ARRAY[byteValue/16];

            // This line gets the character corresponding to the SECOND hex value
            
            hexChars[i*2+1] = HEX_ARRAY[byteValue%16];
        }
        return new String(hexChars);
    }


    /**
     * Converts a String binary representation of a hex value to a String hex
     * representation of a hex value using the BigInteger class.
     *
     * @param inHex the hex String to be converted
     * @return String the converted hex String hex value
     */
    public static String binToHex(String binStr){

        String binHex = new BigInteger(binStr, 2).toString(16);
        return binHex;
    }

    
    /**
     *Support method to convert a String of hex values to a byte array.
     *
     * @param hexString the hex String to be converted.
     * @return byte[] the resulting byte array.
     */
    public static byte[] hexStringToByteArray(String hexString){

        int len = hexString.length();
        byte[] data = new byte[len/2];

        for (int i = 0; i < len; i+=2) {
            data[i/2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }
        
}
