package main;

import java.util.ArrayList;

import main.instructions.*;
import main.errors.*;

public class CPU {
    private final ArrayList<Instruction> instructions;
    private final Registers registers;
    private int index_current_instruction = -1;

    public CPU() {
        registers = new Registers();
        instructions = new ArrayList<Instruction>();
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

    public CPU(ArrayList<Instruction> instructions, Registers registers){
        this.registers = registers;
        this.instructions = instructions;
    }

    public Registers getRegisters(){
        return registers;
    }

    public ArrayList<Instruction> getInstructions(){
        return instructions;
    }

    private void fetch(Memory memory) throws UnknownInstrucion {
        registers.setRI(memory.getMemoryPosition(registers.getPC()));
        registers.incrementPC();
        
        short opcode = (short)(registers.getRI() & 0b1111_1111_0001_1111);
        identifyInstructionByOpcode(opcode);
         
        if (instructions.get(index_current_instruction) instanceof OneOperandInstruction){
            ((OneOperandInstruction) instructions.get(index_current_instruction)).setOperand1(registers.getPC());
            registers.incrementPC();
        }
        if (instructions.get(index_current_instruction) instanceof TwoOperandInstruction){
            ((TwoOperandInstruction) instructions.get(index_current_instruction)).setOperand2(registers.getPC());
            registers.incrementPC();
        }
    }
    
    private void decode(Memory memory) throws UndefinedAddressingMode {
        Instruction instruction = instructions.get(index_current_instruction);
        
        if (instruction instanceof OneOperandInstruction){
            ((OneOperandInstruction) instruction).setCurrentOperand1AddressingMode(registers.getRI());
            switch (((OneOperandInstruction) instruction).getCurrentOperand1AddressingMode()){
                case DIRECT:
                    ((OneOperandInstruction) instruction).setOperand1(memory.getMemoryPosition(
                        ((OneOperandInstruction) instruction).getOperand1()
                    ));
                    break;
                case INDIRECT:
                    ((OneOperandInstruction) instruction).setOperand1(memory.getMemoryPosition(
                        memory.getMemoryPosition(((OneOperandInstruction) instruction).getOperand1()
                    )));
                    break;
                case IMMEDIATE:
                    break;
                    
            }
        }
        if (instructions.get(index_current_instruction) instanceof TwoOperandInstruction){
            ((TwoOperandInstruction) instruction).setCurrentOperand2AddressingMode(registers.getRI());
            switch (((TwoOperandInstruction) instruction).getCurrentOperand2AddressingMode()){
                case DIRECT:
                    ((TwoOperandInstruction) instruction).setOperand2(memory.getMemoryPosition(
                        ((TwoOperandInstruction) instruction).getOperand2()
                    ));
                    break;
                case INDIRECT:
                    ((TwoOperandInstruction) instruction).setOperand2(memory.getMemoryPosition(
                        memory.getMemoryPosition(((TwoOperandInstruction) instruction).getOperand2()
                    )));
                    break;
                case IMMEDIATE:
                    break; 
            }
        }
    }

    private void execute(Memory memory){
        System.out.println(instructions.get(index_current_instruction).toString());
        Instruction instruction = instructions.get(index_current_instruction);
        if (instruction instanceof ExecuteOperation){
            ((ExecuteOperation) instruction).doOperation(registers, memory);
        }
    }

    public void cycle(Memory memory) throws UnknownInstrucion, UndefinedAddressingMode{
        this.fetch(memory);
        this.decode(memory);
        this.execute(memory);
    }

    private void identifyInstructionByOpcode(short opcode) throws UnknownInstrucion {
        for (int c = 0; c < instructions.size(); c++){
            if (instructions.get(c).getOpcode() == opcode){
                this.index_current_instruction = c;
                return;
            }
        }
        throw new UnknownInstrucion("The instruction with the opcode " + opcode + " is not in the CPU instruction set!!");
    }

    public int getCurrentInstructionIndex(){
        return index_current_instruction;
    }
}
