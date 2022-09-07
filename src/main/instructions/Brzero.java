package main.instructions;

import java.util.HashSet;

import main.Instruction;
import main.Memory;
import main.Registers;
import main.AddressingMode;

public class Brzero extends Instruction {
    public Brzero(){
        super("BRZERO", 4, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.DIRECT);    
        modes.add(AddressingMode.INDIRECT);
        this.setAddressingModesSuported(modes);
    }

    public static void doOperation(Registers registers, Memory memory, int addressOperand) {
        if (registers.getACC() == 0){
            registers.setPC(memory.getMemoryPosition(addressOperand));
        }
    }
}