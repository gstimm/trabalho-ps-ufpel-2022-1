package main.instructions;

import main.Instruction;
import main.Memory;
import main.OneOperandInstruction;
import main.Registers;
import main.errors.UndefinedAddressingMode;
import main.AddressingMode;
import java.util.HashSet;

public class Add extends Instruction implements OneOperandInstruction {
    private char operand1;
    private AddressingMode currentOperand1AddressingMode;

    public Add(){
        super("ADD", 2, 2, 1, new HashSet<AddressingMode>());
        this.getAddressingModesSuported().add(AddressingMode.DIRECT);    
        this.getAddressingModesSuported().add(AddressingMode.INDIRECT);
        this.getAddressingModesSuported().add(AddressingMode.IMMEDIATE);
        this.operand1 = 0;
        this.currentOperand1AddressingMode = null;
    }
    
    public void doOperation(Registers registers, Memory memory) {
        registers.incrementACC(memory.getMemoryPosition(operand1));
    }
    
    public char getOperand1(){
        return this.operand1;
    }
    
    public void setOperand1(char value){
        this.operand1 = value;
    }

    public AddressingMode getOperand1AddressingMode(){
        return this.currentOperand1AddressingMode;
    }

    public void setCurrentOperand1AddressingMode(char opcode) throws UndefinedAddressingMode {
        AddressingMode mode = AddressingMode.addressingModeByOpcode(opcode);
        if (super.getAddressingModesSuported().contains(mode) == false){
            throw new UndefinedAddressingMode("The Addressing mode " + mode.toString() + " in not valid for the instruction " + this.getMnemonic());
        }
        this.currentOperand1AddressingMode = mode;
    }
}
