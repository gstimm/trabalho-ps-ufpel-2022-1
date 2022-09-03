package main.instructions;

import main.Instruction;
import main.Registers;
import main.AddressingMode;
import java.util.HashSet;


public class Store extends Instruction {
    public Store() {
        super("STORE", 7, 2, 1);
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
