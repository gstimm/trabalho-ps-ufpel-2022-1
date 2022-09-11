package main.instructions;

import java.util.HashSet;
import main.Instruction;
import main.Memory;
import main.Registers;
import main.TwoOperandInstruction;
import main.errors.UndefinedAddressingMode;
import main.AddressingMode;

public class Copy extends Instruction implements TwoOperandInstruction{
    private char operand1;
    private char operand2;
    private AddressingMode currentOperand1AddressingMode;
    private AddressingMode currentOperand2AddressingMode;

    public Copy(){
        super("COPY", 13, 3, 2, new HashSet<AddressingMode>());
        this.getAddressingModesSuported().add(AddressingMode.DIRECT);    
        this.getAddressingModesSuported().add(AddressingMode.INDIRECT);
        this.getAddressingModesSuported().add(AddressingMode.IMMEDIATE);
        this.operand1 = 0;
        this.operand2 = 0;
        this.currentOperand1AddressingMode = null;
        this.currentOperand2AddressingMode = null;
    }
    
    
    public void doOperation(Registers registers, Memory memory) {
        memory.setMemoryPosition(operand1, memory.getMemoryPosition(operand2));
    }


    public char getOperand1(){
        return this.operand1;
    }
    
    public void setOperand1(char value) {
        this.operand1 = value;
    }

    public AddressingMode getOperand1AddressingMode(){
        return this.currentOperand1AddressingMode;
    }

    public void setCurrentOperand1AddressingMode(char opcode) throws UndefinedAddressingMode {
        AddressingMode mode = AddressingMode.addressingModeByOpcode((char) (opcode & 0b0101111));
        if (super.getAddressingModesSuported().contains(mode) == false || mode == AddressingMode.IMMEDIATE){
            throw new UndefinedAddressingMode("The Addressing mode " + mode.toString() + " in not valid for the the first operator on instruction " + this.getMnemonic());
        }
        this.currentOperand1AddressingMode = mode;
    }


    public char getOperand2(){
        return this.operand2;
    }
    
    public void setOperand2(char value) {
        this.operand2 = value;
    }

    public AddressingMode getOperand2AddressingMode(){
        return this.currentOperand2AddressingMode;
    }

    public void setCurrentOperand2AddressingMode(char opcode) throws UndefinedAddressingMode {
        AddressingMode mode = AddressingMode.addressingModeByOpcode((char) (opcode & 0b1011111));
        if (super.getAddressingModesSuported().contains(mode) == false){
            throw new UndefinedAddressingMode("The Addressing mode " + mode.toString() + " in not valid for the second operator on instruction " + this.getMnemonic());
        }
        this.currentOperand2AddressingMode = mode;
    }
}