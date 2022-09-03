package main.instructions;

import main.Instruction;
import main.Registers;
import java.util.HashSet;
import main.AddressingMode;

public class Load extends Instruction {
    public Load(){
        super("LOAD", 3, 2, 1);
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