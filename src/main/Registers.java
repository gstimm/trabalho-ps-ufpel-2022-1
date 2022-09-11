package main;

public class Registers {
    private char PC;
    private char SP;
    private char ACC;
    private byte MOP;
    private char RI;
    private char RE;
    
    public Registers() {
        this.PC = 0;
        this.SP = 0;
        this.ACC = 0;
        this.MOP = 0;
        this.RI = 0;
        this.RE = 0;
    }

    public char getPC() {
        return this.PC;
    }
    public void setPC(char pC) {
        this.PC = pC;
    }
    public char getSP() {
        return this.SP;
    }
    public void setSP(char sP){
        this.SP = sP;
    }
    public char getACC() {
        return this.ACC;
    }
    public void setACC(char aCC) {
        this.ACC = aCC;
    }
    public byte getMOP() {
        return this.MOP;
    }
    public void setMOP(byte mOP) {
        this.MOP = mOP;
    }
    public char getRI() {
        return this.RI;
    }
    public void setRI(char rI) {
        this.RI = rI;
    }
    public char getRE() {
        return this.RE;
    }
    public void setRE(char rE) {
        this.RE = rE;
    }
    @Override
    public String toString(){
        String result = "PC  = " + (int) this.PC + "\n" +
                        "SP  = " + (int) this.SP + "\n" +
                        "ACC = " + (int) this.ACC + "\n" +
                        "MOP = " + (int) this.MOP + "\n" +
                        "RI  = " + (int) this.RI + "\n" +
                        "RE  = " + (int) this.RE + "\n";
        return result;
    }
    public void incrementACC(char value){
        this.ACC += value;
    }   
    public void incrementSP(char value){
        this.setSP((char) (this.getSP() + value));
    }
  
    public void incrementPC(char value){
        this.PC += value;
    }

    public void incrementPC(){
        this.PC += 1;
    }
}