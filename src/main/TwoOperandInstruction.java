package main;

public interface TwoOperandInstruction extends OneOperandInstruction {
    public char getOperand2();
    public void setOperand2(char value);
    public AddressingMode getOperand2AddressingMode(char opcode);
}
