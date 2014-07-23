package de.yadrone.base.datatypes;

/**
 * 8bit unsigned int
 *
 * @author Claas Anders "CaScAdE" Rathje
 */
public class u8 extends c_int {
	   public u8() {
	        signed = false;
	        length = 8;
	        this.name = "";
	    }

	    public u8(String name) {
	        signed = false;
	        length = 8;
	        this.name = name;
	    }

	    public u8(String name,int maxValue) {
	        signed = false;
	        length = 8;
	        this.name = name;
	        this.maxValue=new Integer(maxValue);
	    }
}
