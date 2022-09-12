package main.instructions;

import main.Instruction;
import main.Memory;
import main.Registers;

public class Stop extends Instruction {
    public Stop(){
        super("STOP", 11, 1, 0);
	}

    @Override
    public void doOperation(Registers registers, Memory memory) {}
}