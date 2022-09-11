package main;

import main.errors.UndefinedAddressingMode;

public interface OneOperandInstruction {
    public char getOperand1();
    public void setOperand1(char value);
    public AddressingMode getOperand1AddressingMode();
    public void setCurrentOperand1AddressingMode(char opcode) throws UndefinedAddressingMode;
}
