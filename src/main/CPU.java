package main;

import java.util.ArrayList;

import main.instructions.*;
import main.errors.*;

public class CPU {
    private ArrayList<Instruction> instructions;
    private Registers registers;
    private int index_current_instruction;
    private char opcode_read;
    private boolean halt_state;

    public CPU(char stackSize, char stackStart){
        registers = new Registers(stackSize, stackStart);
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
        halt_state = false;
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
        this.opcode_read = memory.getMemoryPosition(registers.getPC());
        registers.incrementPC();
        char opcode = (char)(opcode_read & 0b1111);
        identifyInstructionByOpcode(opcode);
         
        if (instructions.get(index_current_instruction) instanceof OneOperandInstruction){
            ((OneOperandInstruction) instructions.get(index_current_instruction)).setOperand1(registers.getPC());
            registers.incrementPC();
            return;
        }
        if (instructions.get(index_current_instruction) instanceof TwoOperandInstruction){
            ((TwoOperandInstruction) instructions.get(index_current_instruction)).setOperand1(registers.getPC());
            registers.incrementPC();
            ((TwoOperandInstruction) instructions.get(index_current_instruction)).setOperand2(registers.getPC());
            registers.incrementPC();
            return;
        }
    }
    
    private void decode(Memory memory){
        Instruction instruction = instructions.get(index_current_instruction);
        
        if (instruction instanceof OneOperandInstruction){
            switch (AddressingMode.addressingModeOperand1(opcode_read)){
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
            return;
        }
        if (instructions.get(index_current_instruction) instanceof TwoOperandInstruction){
            switch (AddressingMode.addressingModeOperand1((char) (opcode_read & 0b111111))){
                case DIRECT:
                    ((TwoOperandInstruction) instruction).setOperand1(memory.getMemoryPosition(
                        ((TwoOperandInstruction) instruction).getOperand1()
                    ));
                    break;
                case INDIRECT:
                    ((TwoOperandInstruction) instruction).setOperand1(memory.getMemoryPosition(
                        memory.getMemoryPosition(((TwoOperandInstruction) instruction).getOperand1()
                    )));
                    break;
                case IMMEDIATE:
                    break;
                    
            }
            switch (AddressingMode.addressingModeOperand2(opcode_read)){
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
            return;
        }
    }
    
    private void execute(Memory memory) throws StackOverflow {
        if (instructions.get(index_current_instruction) instanceof Stop) {
            halt_state = true;
        }
        instructions.get(index_current_instruction).doOperation(registers, memory);
    }

    public void cycle(Memory memory) throws UnknownInstrucion, StackOverflow {
        this.fetch(memory);
        this.decode(memory);
        this.execute(memory);
    }

    private void identifyInstructionByOpcode(char opcode) throws UnknownInstrucion {
        for (int c = 0; c < instructions.size(); c++){
            if (instructions.get(c).getOpcode() == opcode){
                this.index_current_instruction = c;
                return;
            }
        }
        throw new UnknownInstrucion("The instruction with the opcode " + (int) opcode + " is not in the CPU instruction set!!");
    }

    public boolean getHaltState(){
        return this.halt_state;
    }
}
