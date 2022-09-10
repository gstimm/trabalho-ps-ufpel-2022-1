package main;

import main.errors.UndefinedAddressingMode;

public enum AddressingMode {
    DIRECT("DIRECT"), INDIRECT("INDIRECT"), IMMEDIATE("IMMEDIATE");
    private String mode;

    AddressingMode(String mode){
        this.mode = mode;
    }

    @Override
    public String toString() {
        return mode;
    }
    public boolean equals(AddressingMode mode){
        return this.mode.equals(mode.toString());
    }

    public static AddressingMode stringToAddressingMode(String mode) throws UndefinedAddressingMode {
        if (mode.equals(DIRECT.toString())) return DIRECT;
        if (mode.equals(INDIRECT.toString())) return INDIRECT;
        if (mode.equals(IMMEDIATE.toString())) return IMMEDIATE;
        throw new UndefinedAddressingMode("The string does not correspond to any Addressing Mode");
    }
    public static AddressingMode addressingModeOperand1(char opcode){
        if ((opcode & 0b10000) != 0){
            return INDIRECT;
        }
        if ((opcode & 0b1000000) != 0){
            return IMMEDIATE;
        }
        return DIRECT;
    }
    public static AddressingMode addressingModeOperand2(char opcode){
        if ((opcode & 0b100000) != 0){
            return INDIRECT;
        }
        if ((opcode & 0b1000000) != 0){
            return IMMEDIATE;
        }
        return DIRECT;
    }

}
