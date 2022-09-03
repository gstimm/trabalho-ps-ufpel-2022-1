package main.instructions;
import java.util.HashSet;

import main.AddressingMode;
import main.Instruction;
import main.Registers;

public class Stop extends Instruction {
    public Stop(){
        super("STOP", 11, 1, 0);
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