package main.instructions;

import java.util.HashSet;

import main.AddressingMode;
import main.Instruction;
import main.Registers;

public class Read extends Instruction {
    public Read(){
        super("READ", 12, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.DIRECT);    
        modes.add(AddressingMode.INDIRECT);
        this.setAddressingModesSuported(modes);
    }

    @Override
    public void doOperation(Registers registers) {
        return;
    }
}