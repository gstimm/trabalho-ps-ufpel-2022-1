package main.instructions;

import main.Instruction;
import main.Registers;
import main.AddressingMode;
import java.util.HashSet;

public class Divide extends Instruction {
    public Divide(){
        super("DIVIDE", 10, 2, 1);
        HashSet<AddressingMode> modes = new HashSet<AddressingMode>();
        modes.add(AddressingMode.IMMEDIATE);
        modes.add(AddressingMode.DIRECT);
        modes.add(AddressingMode.INDIRECT);
        this.setAddressingModesSuported(modes);
    }

    @Override
    public void doOperation(Registers registers) {
        return;
    }
}