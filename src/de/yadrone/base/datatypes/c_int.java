package de.yadrone.base.datatypes;

import java.util.LinkedList;

/**
 * Base datatype class. c_int objects are used as parameter in MK commands.
 * This class is useful to give properties and attributes to MKCommand parameters and add functions,
 * such as mapping the parameters to byte arrays.
 * @author Ello Oliveira and Diogo Branco
 */
public class c_int {
	protected boolean signed;
	protected String name; //The name the c_int has when used as a parameter
	protected int length;
	protected long value;
	protected Integer minValue;
	protected Integer maxValue;
	protected LinkedList<c_int> allAttribs = null;
	
	public c_int() {
		this.length = 0;
		this.value = 0;
	}
    
    /**
     * @return The values of all attributes in a int[]
     */
    public int[] getAsInt() {
        if (allAttribs == null) {
            int[] ret = new int[length / 8];
            for (int i = 0; i < ret.length; i++) {
                ret[ret.length - 1 - i] = (char) (0xff & (value >> ((ret.length - i - 1) * 8)));
            }
            return ret;
        } else {
            int[] ret = new int[0];
            for (c_int o : allAttribs) {
                ret = concatArray(ret, o.getAsInt());
            }
            return ret;
        }
    }
    
    /**
     * Concat two integer arrays
     * @author Claas Anders "CaScAdE" Rathje
     * @param a one array
     * @param b another array
     * @return ab
     */
    public static int[] concatArray(int[] a, int[] b) {
        if (a.length == 0) {
            return b;
        }
        if (b.length == 0) {
            return a;
        }
        int[] ret = new int[a.length + b.length];
        for (int i = 0; i < a.length; i++) {
            ret[i] = a[i];
        }
        for (int i = a.length; i < a.length + b.length; i++) {
            ret[i] = b[i - a.length];
        }
        return ret;
    }
    
    public void loadFromInt(int RxdBuffer[], int pRxData) {
        if (allAttribs != null && allAttribs.size() > 0) {
            int offset = 0;
            for (c_int c : allAttribs) {
                c.loadFromInt(RxdBuffer, pRxData + offset);
                offset += c.getLength() / 8;
            }
        } else {
            long v = 0;
            if (pRxData + (getLength() / 8) <= RxdBuffer.length) {
                for (int i = (length / 8) - 1; i >= 0; i--) {
                    v <<= 8;
                    v |= RxdBuffer[i + pRxData];
                }
                //TODO: signed!!!!
                if (signed) {
                    long signmask = 1 << (getLength() - 1);
                    long signbit = ((v & signmask) != 0) ? 1 : 0;
                    v &= ~signmask;
//                    long newsignmask = signbit << 63;
//                    v = v | newsignmask;
                    if (signbit == 1) {
                        v = v + getMin();
                    }
                }
                value = v;
            }
        }

    }
    
    public int getMin() {
        if (!signed) {
            return 0;
        } else {
            if (minValue != null) {
                return minValue.intValue();
            }
            return (int) -(Math.pow(2, length - 1));
        }
    }
    
    public int getLength() {
        if (allAttribs == null) {
            return length;
        } else {
            int len = 0;
            for (c_int c : allAttribs) {
                len += c.getLength();
            }
            return len;
        }
    }
    
    public long getValue() {
    	return value;
    }
    
    /**
     * Sets the value, checking if the value set is between min and max. 
     * @param value The value to be set
     * @throws Exception If the value is out of the range
     */
    public void setAndCheckValue(long value) throws Exception {
    	if(minValue != null && maxValue != null){
    		if(inRange(value))
    			this.value = value;
    		else throw new Exception("Out of range");
    	}
    	else{
    		this.value = value;
    	}
    }
    
    private boolean inRange(long value){
    	if(minValue == null || maxValue == null)
    		return true;
    	return (value >= minValue && value<=maxValue);
    }

}
