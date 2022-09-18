package main.gui;

public class MemoryCell {
    private int address;
    private short value;
    
    public MemoryCell(int address, short value){
        this.address = address;
        this.value = value;
    }

    public Integer getAddress() {
        return address;
    }
    public void setAddress(int address) {
        this.address = address;
    }
    public Short getValue() {
        return value;
    }
    public void setValue(Short value) {
        this.value = value;
    }
}
