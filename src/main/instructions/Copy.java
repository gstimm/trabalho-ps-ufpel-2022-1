package main.instructions;

import java.util.HashSet;
import main.Instruction;
import main.Registers;
import main.AddressingMode;

public class Copy extends Instruction {
    public Copy(){
        super("COPY", 13, 3, 1);
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