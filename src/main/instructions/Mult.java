package main.instructions;

import java.util.HashSet;

import main.AddressingMode;
import main.Instruction;
import main.Registers;

public class Mult extends Instruction {
    public Mult(){
        super("MULT", 14, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.DIRECT);
        modes.add(AddressingMode.INDIRECT);
        modes.add(AddressingMode.IMMEDIATE);
        this.setAddressingModesSuported(modes);
    }
    @Override
    public void doOperation(Registers registers) {
        return;
    }
    
}