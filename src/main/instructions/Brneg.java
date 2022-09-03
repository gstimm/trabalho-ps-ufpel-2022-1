package main.instructions;

import java.util.HashSet;
import main.Instruction;
import main.Registers;
import main.AddressingMode;

public class Brneg extends Instruction {
    public Brneg(){
        super("BRNEG", 5, 2, 1);
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
