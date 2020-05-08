import java.math.BigInteger;

/** IEEE.java is a support class for FloatingPoint.java to deal with the conversion
 *  of java Double to an IEEE 754 format binary String, for both 32 bit and 64 bit numbers.
 */
public class IEEE {

    private final double MAX32 = (2 - Math.pow(2, -23)) * Math.pow(2, 127);
    private final double MIN32 = Math.pow(2, -126);
    private final double MAX64 = (1 + (1 - Math.pow(2, -52))) * Math.pow(2, 1023);
    private final double MIN64 = Math.pow(2, -1022);

    private final String POSINF32 = "01111111100000000000000000000000";
    private final String NEGINF32 = "11111111100000000000000000000000";   
    private final String POSINF64 = "0111111111110000000000000000000000000000000000000000000000000000";
    private final String NEGINF64 = "1111111111110000000000000000000000000000000000000000000000000000";

    private final String POSZERO32 = "00000000000000000000000000000000";
    private final String NEGZERO32 = "10000000000000000000000000000000";
    private final String POSZERO64 = "0000000000000000000000000000000000000000000000000000000000000000";
    private final String NEGZERO64 = "1000000000000000000000000000000000000000000000000000000000000000";

    private boolean tooBig = false, tooSmall = false;
    private int prec;
    private double ibmDouble;
    private int exp = 0;

    /**
     *  Constructor for the IEEE class.
     *  @param ibmDouble the double to be converted to and IEEE number.
     *  @param prec the precision to be converted to.
     */
    public IEEE(double ibmDouble, int prec){
        this.ibmDouble = ibmDouble;
        this.prec = prec;
    }

    /** Function to check if the double representation returned by the IBM class is within
     *  the range of values representable by an IEEE 32 or 64 bit number.
     *  @param inDouble the output from the IBM class to be converted to IEEE format.
     *  @return checkRange the double that has been range checked.
     */
    public double checkrange(double inDouble){

        boolean negative = false;
        
        if (inDouble < 0){
            inDouble *= -1;
            negative = true;
        }
        
        if (prec == 32){
            if (inDouble > MAX32){
                inDouble = MAX32;
                tooBig = true;
            } else if (inDouble < MIN32) {
                inDouble = MIN32;
                tooSmall = true;
            } else {
                if(negative){
                    inDouble *= -1;
                }
                return inDouble;
            }
        } else {
            if (inDouble > MAX64){
                inDouble = MAX64;
                tooBig = true;
            } else if (inDouble < MIN64) {
                inDouble = MIN64;
                tooSmall = true;
            } else {
                if(negative){
                    inDouble *= -1;
                }
                return inDouble;
            }
        }
        return inDouble;
    }

    /** Method to convert hex strings into binary numbers.
     *  @param hexIn the hex number to be converted.
     *  @return String the binary representation of the hex number in String format.
     */
    public String hexToBin(String hexIn){

        return new BigInteger(hexIn, 16).toString(2);

    }
    /** Converts the double output from the IBM class into an IEEE format binary String.
     *  @return String the newly generated IEEE format String.
     */
    public String IEEEconvert() {

        ibmDouble = checkrange(ibmDouble);

        if (prec == 64){

            // This section deals with overflow/underflow resulting from conversion
            //between ibm/IEEE 32/64 using IEEE default values for +/- infinity and zero.
            if(tooBig){
                if (ibmDouble < 0){
                    return NEGINF64;
                }else{
                    return POSINF64;
                }
            }else if(tooSmall){
                if (ibmDouble < 0){
                    return NEGZERO64;
                }else{
                    return POSZERO64;
                }
            }
            
            long longIEEE = Double.doubleToRawLongBits(ibmDouble);
            
            String binIEEE = Long.toBinaryString(longIEEE);

            while (binIEEE.length() < 64){
                binIEEE= "0" + binIEEE;;
            }
            
            return binIEEE;

        } else {

            // This section deals with overflow/underflow resulting from conversion
            // between ibm/IEEE 32/64 using IEEE default values for +/- infinity and zero.
            if(tooBig){
                if (ibmDouble < 0){
                    return NEGINF32;
                }else{
                    return POSINF32;
                }
            }else if(tooSmall){
                if (ibmDouble < 0){
                    return NEGZERO32;
                }else{
                    return POSZERO32;
                }
            }
            
            float floatIEEE = (float)ibmDouble;

            int intIEEE = Float.floatToIntBits(floatIEEE);

            String binIEEE = Integer.toBinaryString(intIEEE);

            while (binIEEE.length() < 32){
                binIEEE= "0" + binIEEE;
            }

            return binIEEE;
            
        }
    }
}
