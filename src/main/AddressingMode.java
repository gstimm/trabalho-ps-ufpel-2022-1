package main;

import main.errors.UndefinedAddressingMode;

public enum AddressingMode {
    DIRECT("DIRECT"), INDIRECT("INDIRECT"), IMMEDIATE("IMMEDIATE");
    private final String mode;

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
    public static AddressingMode addressingModeByOpcode(short opcode){
        if ((opcode & 0b0110_0000) != 0){
            return INDIRECT;
        }
        if ((opcode & 0b1000_0000) != 0){
            return IMMEDIATE;
        }
        return DIRECT;
    }
    public static int opcodeByAddressingMode(int opcode, AddressingMode operand1, AddressingMode operand2) {
        if (operand1 != null) {
            switch (operand1) {
                case INDIRECT:
                    opcode = opcode | 0b0100_0000;
                    break;
                case IMMEDIATE:
                    opcode = opcode | 0b1000_0000;
                default:
                    break;
            }
        }

        if (operand2 != null) {
            switch (operand2) {
                case INDIRECT:
                    opcode = opcode | 0b0010_0000;
                    break;
                case IMMEDIATE:
                    opcode = opcode | 0b1000_0000;
                default:
                    break;
            }
        }
        return opcode;
    }
}
