package main;

public class Registers {
    private short PC;
    private short SP;
    private short ACC;
    private byte MOP;
    private short RI;
    private short RE;
    
    public Registers() {
        this.PC = 0;
        this.SP = 0;
        this.ACC = 0;
        this.MOP = 0;
        this.RI = 0;
        this.RE = 0;
    }

    public short getPC() {
        return this.PC;
    }
    public void setPC(short pC) {
        this.PC = pC;
    }
    public short getSP() {
        return this.SP;
    }
    public void setSP(short sP){
        this.SP = sP;
    }
    public short getACC() {
        return this.ACC;
    }
    public void setACC(short aCC) {
        this.ACC = aCC;
    }
    public byte getMOP() {
        return this.MOP;
    }
    public void setMOP(byte mOP) {
        this.MOP = mOP;
    }
    public short getRI() {
        return this.RI;
    }
    public void setRI(short rI) {
        this.RI = rI;
    }
    public short getRE() {
        return this.RE;
    }
    public void setRE(short rE) {
        this.RE = rE;
    }
    @Override
    public String toString(){
        String result = "PC  = " + (short) this.PC + "\n" +
                        "SP  = " + (short) this.SP + "\n" +
                        "ACC = " + (short) this.ACC + "\n" +
                        "MOP = " + (short) this.MOP + "\n" +
                        "RI  = " + (short) this.RI + "\n" +
                        "RE  = " + (short) this.RE + "\n";
        return result;
    }
    public void incrementACC(short value){
        this.ACC += value;
    }   
    public void incrementSP(short value){
        this.setSP((short) (this.getSP() + value));
    }
  
    public void incrementPC(short value){
        this.PC += value;
    }

    public void incrementPC(){
        this.PC += 1;
    }

    public void reset(){
        this.PC = 0;
        this.SP = 0;
        this.ACC = 0;
        this.MOP = 0;
        this.RI = 0;
        this.RE = 0;
    }
}