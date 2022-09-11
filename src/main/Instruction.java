package main;

import java.util.HashSet;

public abstract class Instruction {
    private final String mnemonic;
    private final int opcode;
    private final int instructionSize;
    private final int numberOfOperands;
    private final HashSet<AddressingMode> addressingModesSuported;

    public Instruction(String mnemonic, int opcode, int instructionSize, int numberOfOperands, HashSet<AddressingMode> addressingModesSuported){
        this.mnemonic = mnemonic;
        this.opcode = opcode;
        this.instructionSize = instructionSize;
        this.numberOfOperands = numberOfOperands;
        this.addressingModesSuported = addressingModesSuported;
    }
    
    public abstract void doOperation(Registers registers, Memory memory);

    public String toString(){
        return "MNEMONIC:\t" + mnemonic + 
               "\nOPCODE:\t\t" + opcode + 
               "\nSIZE:\t\t" + instructionSize + 
               "\nOPERANDS:\t" + numberOfOperands + "\n";
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public int getOpcode() {
        return opcode;
    }

    public int getInstructionSize() {
        return instructionSize;
    }

    public int getNumberOfOperands() {
        return numberOfOperands;
    }

    public HashSet<AddressingMode> getAddressingModesSuported() {
        return addressingModesSuported;
    }
}