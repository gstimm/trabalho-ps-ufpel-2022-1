package main;

import java.util.Set;
import java.util.HashSet;
import main.instructions.*;

public class CPU {
    private Set<Instruction> instructions;
    private Registers registers;

    public CPU(){
        registers = new Registers();
        instructions = new HashSet<Instruction>();
        instructions.add(new Add());
        instructions.add(new Br());
        instructions.add(new Brneg());
        instructions.add(new Brpos());
        instructions.add(new Brzero());
        instructions.add(new Call());
        instructions.add(new Copy());
        instructions.add(new Divide());
        instructions.add(new Load());
        instructions.add(new Mult());
        instructions.add(new Read());
        instructions.add(new Ret());
        instructions.add(new Stop());
        instructions.add(new Store());
        instructions.add(new Sub());
        instructions.add(new Write());
    }

    public CPU(Set<Instruction> instructions, Registers registers){
        this.registers = registers;
        this.instructions = instructions;
    }

    public Registers getRegisters(){
        return registers;
    }

    public Set<Instruction> getInstructions(){
        return instructions;
    }

}
