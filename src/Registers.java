public class Registers {
    // Program Counter Register
    private String PC;

    // Instrução, End. Mem., Buffer, Aculumador, Dados, Pilha,
    // Endereçamento Indireto, Instruction Pointer, State Register
    private String RI, REM, RBM, ACC, DX, SP, SI, IP, SR;


    // INCREMENT PROGRAM COUNTER
    public void incPC() {
        int pcInt;
        pcInt = Conversion.stringBinaryToInt(PC);
        if (++pcInt > 2047) {
            System.out.printf("PC overflow");
            pcInt = 0;
        }
        this.PC = Conversion.intToStringBinary(pcInt);
    }

    // GETTERS & SETTERS

    public int getPC() { return Conversion.stringBinaryToInt(this.PC); }
    public void setPC(String val) { this.PC = val; }

    public int getRI() { return Conversion.stringBinaryToInt(this.RI); }
    public void setRI(String val) { this.RI = val; }

    public int getREM() { return Conversion.stringBinaryToInt(this.REM); }
    public void setREM(String val) { this.REM = val; }

    public int getRBM() { return Conversion.stringBinaryToInt(this.RBM); }
    public void setRBM(String val) { this.RBM = val; }

    public int getACC() { return Conversion.stringBinaryToInt(this.ACC); }
    public void setACC(String val) { this.ACC = val; }

    public int getDX() { return Conversion.stringBinaryToInt(this.DX); }
    public void setDX(String val) { this.DX = val; }

    public int getSP() { return Conversion.stringBinaryToInt(this.SP); }
    public void setSP(String val) { this.SP = val; }

    public int getSI() { return Conversion.stringBinaryToInt(this.SI); }
    public void setSI(String val) { this.SI = val; }

    public int getIP() { return Conversion.stringBinaryToInt(this.IP); }
    public void setIP(String val) { this.IP = val; }

    public int getSR() { return Conversion.stringBinaryToInt(this.SR); }
    public void setSR(String val) { this.SR = val; }


    // ************ other methods ????

    public void resetRegisters() {
        PC = RI = REM = RBM = ACC = DX = SP = SI = IP = SR = "0";
    }
}