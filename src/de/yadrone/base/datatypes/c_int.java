package de.yadrone.base.datatypes;

import java.util.LinkedList;

/**
 * Base datatype class. c_int objects are used as parameter in MK commands.
 * This class is useful to give properties and attributes to MKCommand parameters and add functions,
 * such as mapping the parameters to byte arrays.
 * @author Ello Oliveira
 */
public class c_int {
	public boolean signed;
	public String name; //The name the c_int has when used as a parameter
    public int length = 0;
    public long value = 0;
    public Integer minValue;
    public Integer maxValue;
    public LinkedList<c_int> allAttribs = null;
    
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

}
