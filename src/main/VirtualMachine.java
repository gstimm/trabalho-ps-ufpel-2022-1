package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import main.errors.StackOverflow;
import main.errors.UndefinedAddressingMode;
import main.errors.UnknownInstrucion;

public class VirtualMachine {
    private final int defultMemorySize = 2048;
    private final char defaultStackSize = 100;
    private final char defaultStackStartIndex = 2;
    private final Memory memory;
    private final CPU cpu;

    VirtualMachine(CPU cpu, Memory memory){
        this.memory = memory;
        this.cpu = cpu;
    }
    VirtualMachine(){
        cpu = new CPU();
        memory = new Memory(defultMemorySize);
    }
    
    public boolean isStackFull() {
        return cpu.getRegisters().getSP() == (defaultStackSize + defaultStackStartIndex);
    }
    public Memory getMemory(){
        return memory;
    }
    public CPU getCPU(){
        return cpu;
    }
    public void cycle() throws UnknownInstrucion, StackOverflow, UndefinedAddressingMode {
        cpu.cycle(memory);
        
        if(cpu.getRegisters().getSP() > defaultStackSize){
            throw new StackOverflow("Stack Overflowed, the value " + (int) cpu.getRegisters().getSP()  + " is bigger than the maximum size of the stack: " + (int) defaultStackSize);
        }
        else if (cpu.getRegisters().getSP() < defaultStackStartIndex){
            throw new StackOverflow("Stack Underflowed, the value " + (int) cpu.getRegisters().getSP()  + " is lower than the minimun index of the stack: " + (int) defaultStackStartIndex);
        }
    }
    public void initMachine(){
        memory.setMemoryPosition(defaultStackStartIndex, defaultStackSize);
        cpu.getRegisters().setSP((char) (defaultStackStartIndex));
        cpu.getRegisters().setPC((char) (defaultStackStartIndex + defaultStackSize - 1));
    }
    public void readFile(String fileName) throws IndexOutOfBoundsException, FileNotFoundException{
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        int next_index = cpu.getRegisters().getPC();
        while (scanner.hasNextLine()){
            String linha = scanner.nextLine();
            String tokens[] = linha.split("\s+");
            if (tokens[0].startsWith("X")) {
                next_index++;
                continue;
            }
            for (int c = 0; c < tokens.length; c++){
                memory.setMemoryPosition(next_index++, (char) Integer.parseInt(tokens[c]));
            }
        }
        scanner.close();
    }
    public void printStack(){
        memory.printMemoryInRange(defaultStackStartIndex + 1, defaultStackSize);
    }
}