package main;

public class Registers {
    private char PC;
    private char SP;
    private char ACC;
    private byte MOP;
    private char RI;
    private char RE;
    
    
    public Registers() {
        PC = 0;
        SP = 0;
        ACC = 0;
        MOP = 0;
        RI = 0;
        RE = 0;
    }

    public char getPC() {
        return PC;
    }
    public void setPC(char pC) {
        PC = pC;
    }
    public char getSP() {
        return SP;
    }
    public void setSP(char sP) {
        SP = sP;
    }
    public char getACC() {
        return ACC;
    }
    public void setACC(char aCC) {
        ACC = aCC;
    }
    public byte getMOP() {
        return MOP;
    }
    public void setMOP(byte mOP) {
        MOP = mOP;
    }
    public char getRI() {
        return RI;
    }
    public void setRI(char rI) {
        RI = rI;
    }
    public char getRE() {
        return RE;
    }
    public void setRE(char rE) {
        RE = rE;
    }

    @Override
    public String toString(){
        String result = "PC  = " + (int) PC + "\n" +
                        "SP  = " + (int) SP + "\n" +
                        "ACC = " + (int) ACC + "\n" +
                        "MOP = " + (int) MOP + "\n" +
                        "RI  = " + (int) RI + "\n" +
                        "RE  = " + (int) RE + "\n";
        return result;
    }
}