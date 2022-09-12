package main.instructions;

import main.Instruction;
import main.Memory;
import main.Registers;

public class Ret extends Instruction {
    public Ret(){
        super("RET", 9, 1, 0);
    }
    
    public void doOperation(Registers registers, Memory memory){
        registers.setPC(memory.getMemoryPosition(registers.getSP()));
        registers.incrementSP((short) -1);
    }
}