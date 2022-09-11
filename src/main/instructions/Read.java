package main.instructions;

import java.util.HashSet;
import java.util.Scanner;

import main.Memory;
import main.OneOperandInstruction;
import main.Registers;
import main.AddressingMode;
import main.Instruction;

public class Read extends Instruction implements OneOperandInstruction {
    private char operand1;

    public Read(){
        super("READ", 12, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.DIRECT);    
        modes.add(AddressingMode.INDIRECT);
        this.setAddressingModesSuported(modes);
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
    
    public void setOperand1(char value){
        this.operand1 = value;
    }  
    
    public AddressingMode getOperand1AddressingMode(char opcode) {
        return AddressingMode.addressingModeByOpcode(opcode);
    }
}