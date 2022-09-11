package main;

import java.util.HashSet;

public abstract class Instruction {
    private String mnemonic;
    private int opcode;
    private int instructionSize;
    private int numberOfOperands;
    private HashSet<AddressingMode> addressingModesSuported;

    public Instruction(String mnemonic, int opcode, int instructionSize, int numberOfOperands){
        this.mnemonic = mnemonic;
        this.opcode = opcode;
        this.instructionSize = instructionSize;
        this.numberOfOperands = numberOfOperands;
        addressingModesSuported = null;
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

    public void setAddressingModesSuported(HashSet<AddressingMode> addressingModesSuported) {
        this.addressingModesSuported = addressingModesSuported;
    }
    
}