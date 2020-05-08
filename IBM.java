import java.lang.Math;

/** IBM.java is a support class for FloatingPoint.java to deal with the conversion of
 *  a binary string (in IBM System/360-format) to java Double format.
 */

public class IBM{

    public String ibmBin;

    /** Constructor for the IBM class.
     *  @param ibmBin the binary number in String format to be converted to Double. 
     */
    public IBM(String ibmBin){
        this.ibmBin = ibmBin;
    }

    /** Method to convert hex Strings to double.
     *  @param hexString the string to be converted to double.
     *  @return double the double format of the given hex String.
     */
    public double hexToDouble(String hexString){

        hexString += "p0";
        hexString = "0x" + hexString;

        double hexDouble = Double.valueOf(hexString);

        return hexDouble;
        }

    /** Method to convert binary Strings to hex.
     *  @param binString the binary String to be converted to hex format.
     */ 
    public String binToHex(String binString){

        long binDec = Long.parseLong(binString, 2);
        String hexString = Long.toHexString(binDec);

        return hexString;
    }

    /** Method to convert the object's ibmBin String into java Double format.
     *  @return double the double representation of the given IBM number.
     */
    public double IBMconvert(){

        String sign, exponentBin, mantissaBin;

        sign = ibmBin.substring(0,1);

        //Isolate exponent
        exponentBin = ibmBin.substring(1,8);
        
        //Isolate mantissa
        mantissaBin = ibmBin.substring(8);

        //Convert mantissa binary String to hexadecimal String
        String mantissaHex = "0." + binToHex(mantissaBin);

        //Convert mantissa hex String to decimal double
        double mantissaDouble = hexToDouble(mantissaHex);

        //Convert exponent binary String to decimal int
        int exponentInt = Integer.parseInt(exponentBin, 2);

        //Formula to convert to unsigned decimal double
        double output = mantissaDouble * Math.pow(16, (exponentInt - 64));
        
        if (sign.equals("1")){
            output *= -1;
        }

        return output;
    }
}
