package de.yadrone.base.datatypes;

public class s8 extends c_int {
	
    public s8(String name) {
    	this(name, 0);
    }
    
    public s8(long value) {
    	this(null, value);
    }
    
    public s8(String name, long value) {
        signed = true;
        length = 8;
        this.name = name;
        this.value = value;
    }
    
    public s8(String name,int minValue,int maxValue) {
        signed = true;
        length = 8;
        this.name = name;
        this.minValue=new Integer(minValue);
        this.maxValue=new Integer(maxValue);
    }
}
