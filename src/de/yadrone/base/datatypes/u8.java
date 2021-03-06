package de.yadrone.base.datatypes;

/**
 * 8bit unsigned int
 * 
 * @author Claas Anders "CaScAdE" Rathje
 */
public class u8 extends c_int {
	
	public u8() {
		this(0, null, 0);
	}
	
	public u8(String name) {
		this(0, name, 0);
	}
	
	public u8(long value) {
		this(value, null, 0);
	}

	public u8(long value, String name) {
		this(value, name, 0);
	}
	
    public u8(String name,int maxValue) {
        signed = false;
        length = 8;
        this.name = name;
        this.minValue = 0;
        this.maxValue=new Integer(maxValue);
    }

	public u8(long value, String name, int maxValue) {
		super();
		this.signed = false;
		this.length = 8;
		this.value = value;
		if(name == null) {
			this.name = "";
		} else {
			this.name = name;
		}
		this.minValue = 0;
		this.maxValue = new Integer(maxValue);
	}

	public u8(int[] rxdBuffer, int pRxData, String name) {
		this();
		loadFromInt(rxdBuffer, pRxData);
		this.name = name;
	}
}
