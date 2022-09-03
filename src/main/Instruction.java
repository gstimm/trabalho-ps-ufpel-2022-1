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
    
    public abstract void doOperation(Registers registers);

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public int getOpcode() {
        return opcode;
    }

    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    public int getInstructionSize() {
        return instructionSize;
    }

    public void setInstructionSize(int instructionSize) {
        this.instructionSize = instructionSize;
    }

    public int getNumberOfOperands() {
        return numberOfOperands;
    }

    public void setNumberOfOperands(int numberOfOperands) {
        this.numberOfOperands = numberOfOperands;
    }

    public HashSet<AddressingMode> getAddressingModesSuported() {
        return addressingModesSuported;
    }

    public void setAddressingModesSuported(HashSet<AddressingMode> addressingModesSuported) {
        this.addressingModesSuported = addressingModesSuported;
    }
    
}