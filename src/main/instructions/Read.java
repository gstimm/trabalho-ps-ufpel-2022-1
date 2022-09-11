package main.instructions;

import java.util.HashSet;
import java.util.Scanner;

import main.Memory;
import main.OneOperandInstruction;
import main.Registers;
import main.errors.UndefinedAddressingMode;
import main.AddressingMode;
import main.Instruction;

public class Read extends Instruction implements OneOperandInstruction {
    private char operand1;
    private AddressingMode currentOperand1AddressingMode;

    public Read(){
        super("READ", 12, 2, 1, new HashSet<AddressingMode>());
        this.getAddressingModesSuported().add(AddressingMode.DIRECT);    
        this.getAddressingModesSuported().add(AddressingMode.INDIRECT);
        this.operand1 = 0;
        this.currentOperand1AddressingMode = null;
    }

    public void doOperation(Registers registers, Memory memory) {
        Scanner scanner = new Scanner(System.in);
        short value = scanner.nextShort();
        memory.setMemoryPosition(operand1, (char) value);
        scanner.close();
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
        AddressingMode mode = AddressingMode.addressingModeByOpcode(opcode);
        if (super.getAddressingModesSuported().contains(mode) == false){
            throw new UndefinedAddressingMode("The Addressing mode " + mode.toString() + " in not valid for the instruction " + this.getMnemonic());
        }
        this.currentOperand1AddressingMode = mode;
    }
}