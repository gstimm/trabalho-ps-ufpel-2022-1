package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import main.errors.StackOverflow;

public class VirtualMachine {
    private final int defultMemorySize = 2048;
    private final char defaultStackSize = 100;
    private final char defaultStackStartIndex = 2;
    private Memory memory;
    private CPU cpu;

    VirtualMachine(CPU cpu, Memory memory){
        this.memory = memory;
        this.cpu = cpu;
    }
    VirtualMachine(){
        cpu = new CPU(defaultStackSize, defaultStackStartIndex);
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
    public void initMachine() throws StackOverflow {
        memory.setMemoryPosition(defaultStackStartIndex, defaultStackSize);
        cpu.getRegisters().setSP((char) (defaultStackStartIndex + 1));
        cpu.getRegisters().setPC((char) (defaultStackStartIndex + defaultStackSize - 1));
    }
    public void readFile(String fileName) throws IndexOutOfBoundsException, FileNotFoundException{
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        int next_index = cpu.getRegisters().getPC();
        while (scanner.hasNextLine()){
            String linha = scanner.nextLine();
            String tokens[] = linha.split(" ");
            if (tokens[0].equals("X")) {
                next_index++;
                continue;
            }
            for (int c = 0; c < tokens.length; c++){
                memory.setMemoryPosition(next_index++, (char) Integer.parseInt(tokens[c]));
            }
        }
        scanner.close();
    }
}