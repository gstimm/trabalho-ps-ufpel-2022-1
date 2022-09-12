package main;

import java.util.Set;

import main.errors.UndefinedAddressingMode;

public interface OneOperandInstruction {
    public short getOperand1();
    public void setOperand1(short value);
    public Set<AddressingMode> getOperand1AddressingModes();
    public AddressingMode getCurrentOperand1AddressingMode();
    public void setCurrentOperand1AddressingMode(short opcode) throws UndefinedAddressingMode;
}
