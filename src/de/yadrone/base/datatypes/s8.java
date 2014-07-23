package de.yadrone.base.datatypes;

public class s8 extends c_int {
    public s8(String name) {
        signed = true;
        length = 8;
        this.name = name;
    }
    public s8(String name,int minValue,int maxValue) {
        signed = true;
        length = 8;
        this.name = name;
        this.minValue=new Integer(minValue);
        this.maxValue=new Integer(maxValue);
    }
}