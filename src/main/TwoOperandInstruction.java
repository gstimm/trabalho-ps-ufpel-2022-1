package main;

import java.util.Set;

import main.errors.UndefinedAddressingMode;

public interface TwoOperandInstruction extends OneOperandInstruction {
    public short getOperand2();
    public void setOperand2(short value);
    public Set<AddressingMode> getOperand2AddressingModes();
    public AddressingMode getCurrentOperand2AddressingMode();
    public void setCurrentOperand2AddressingMode(short opcode) throws UndefinedAddressingMode;
}
