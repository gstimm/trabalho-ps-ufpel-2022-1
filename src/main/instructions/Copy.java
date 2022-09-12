package main.instructions;

import main.Instruction;
import main.Memory;
import main.TwoOperandInstruction;
import main.Registers;
import main.errors.UndefinedAddressingMode;
import main.AddressingMode;
import java.util.HashSet;
import java.util.Set;

public class Copy extends Instruction implements TwoOperandInstruction{
    private final Set<AddressingMode> operand1AddressingModes;
    private final Set<AddressingMode> operand2AddressingModes;
    private short operand1;
    private short operand2;
    private AddressingMode currentOperand1AddressingMode;
    private AddressingMode currentOperand2AddressingMode;

    public Copy(){
        super("COPY", 13, 3, 2);
        this.operand1AddressingModes = new HashSet<AddressingMode>();    
        this.operand1AddressingModes.add(AddressingMode.DIRECT);
        this.operand1AddressingModes.add(AddressingMode.INDIRECT);
        this.operand2AddressingModes = new HashSet<AddressingMode>();    
        this.operand2AddressingModes.add(AddressingMode.DIRECT);
        this.operand2AddressingModes.add(AddressingMode.INDIRECT);
        this.operand2AddressingModes.add(AddressingMode.IMMEDIATE);
        this.operand1 = 0;
        this.operand2 = 0;
        this.currentOperand1AddressingMode = null;
        this.currentOperand2AddressingMode = null;
    }
    
    
    public void doOperation(Registers registers, Memory memory) {
        memory.setMemoryPosition(operand1, memory.getMemoryPosition(operand2));
    }


    public short getOperand1(){
        return this.operand1;
    }
    
    public void setOperand1(short value) {
        this.operand1 = value;
    }

    public Set<AddressingMode> getOperand1AddressingModes(){
        return this.operand1AddressingModes;
    }

    public AddressingMode getCurrentOperand1AddressingMode(){
        return this.currentOperand1AddressingMode;
    }

    public void setCurrentOperand1AddressingMode(short opcode) throws UndefinedAddressingMode {
        AddressingMode mode = AddressingMode.addressingModeByOpcode((short) (opcode & 0b11011111));
        if (this.operand1AddressingModes.contains(mode) == false){
            throw new UndefinedAddressingMode("The Addressing mode " + mode.toString() + " in not valid for the the first operator on instruction " + this.getMnemonic());
        }
        this.currentOperand1AddressingMode = mode;
    }

    public short getOperand2(){
        return this.operand2;
    }
    
    public void setOperand2(short value) {
        this.operand2 = value;
    }
 
    public Set<AddressingMode> getOperand2AddressingModes(){
        return this.operand2AddressingModes;
    }

    public AddressingMode getCurrentOperand2AddressingMode(){
        return this.currentOperand2AddressingMode;
    }

    public void setCurrentOperand2AddressingMode(short opcode) throws UndefinedAddressingMode {
        AddressingMode mode = AddressingMode.addressingModeByOpcode((short) (opcode & 0b10111111));
        if (this.operand2AddressingModes.contains(mode) == false){
            throw new UndefinedAddressingMode("The Addressing mode " + mode.toString() + " in not valid for the second operator on instruction " + this.getMnemonic());
        }
        this.currentOperand2AddressingMode = mode;
    }
    @Override
    public String toString(){
        return super.toString() +
                "OPERAND1: \t" + this.operand1 +
                "\nADDR MODE: \t" + this.currentOperand1AddressingMode.toString() + 
                "\nOPERAND2: \t" + this.operand2 +
                "\nADDR MODE: \t" + this.currentOperand2AddressingMode.toString() + 
                "\n";
    }
}