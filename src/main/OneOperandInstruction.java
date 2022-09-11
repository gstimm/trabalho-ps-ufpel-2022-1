package main;

public interface OneOperandInstruction {
    public char getOperand1();
    public void setOperand1(char value);
    public AddressingMode getOperand1AddressingMode(char opcode);
}
