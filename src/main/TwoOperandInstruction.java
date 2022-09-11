package main;

import main.errors.UndefinedAddressingMode;

public interface TwoOperandInstruction extends OneOperandInstruction {
    public char getOperand2();
    public void setOperand2(char value);
    public AddressingMode getOperand2AddressingMode();
    public void setCurrentOperand2AddressingMode(char opcode) throws UndefinedAddressingMode;
}
