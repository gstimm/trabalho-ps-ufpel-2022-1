package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import main.errors.StackOverflow;
import main.errors.UndefinedAddressingMode;
import main.errors.UnknownInstrucion;

public class VirtualMachine {
    public final int defaultMemorySize = 2048;
    public final short defaultStackSize = 100;
    public final short defaultStackStartIndex = 2;
    private final Memory memory;
    private final CPU cpu;

    VirtualMachine(CPU cpu, Memory memory){
        this.memory = memory;
        this.cpu = cpu;
    }
    VirtualMachine(){
        cpu = new CPU();
        memory = new Memory(defaultMemorySize);
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
            throw new StackOverflow("Stack Overflowed, the value " + cpu.getRegisters().getSP()  + " is bigger than the maximum size of the stack: " + (int) defaultStackSize);
        }
        else if (cpu.getRegisters().getSP() < defaultStackStartIndex){
            throw new StackOverflow("Stack Underflowed, the value " + cpu.getRegisters().getSP()  + " is lower than the minimun index of the stack: " + (int) defaultStackStartIndex);
        }
    }
    public void initMachine(){
        memory.setMemoryPosition(defaultStackStartIndex, defaultStackSize);
        cpu.getRegisters().setSP(defaultStackStartIndex);
        cpu.getRegisters().setPC((short) (defaultStackStartIndex + defaultStackSize));
    }
    public void readFile(String fileName) throws IndexOutOfBoundsException, FileNotFoundException{
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        short next_index = cpu.getRegisters().getPC();
        short start_position_of_code = cpu.getRegisters().getPC();
        while (scanner.hasNextLine()){
            String linha = scanner.nextLine();
            String linha_separada[] = linha.split("\\|");
            if (linha_separada.length == 0) return; // Arquivo com erro
            String codigo_de_maquina[] = linha_separada[1].strip().split("\\s+");
            String modo_realocacao[] = linha_separada[0].strip().split("\\s+");
            if (modo_realocacao.length != codigo_de_maquina.length) return; // Arquivo com erro
            for (int c = 0; c < codigo_de_maquina.length; c++){
                if (Short.parseShort(modo_realocacao[c]) == 1){
                    memory.setMemoryPosition(next_index, (short) (Short.parseShort(codigo_de_maquina[c]) + start_position_of_code));
                }
                else {
                    memory.setMemoryPosition(next_index, Short.parseShort(codigo_de_maquina[c]));
                }
                next_index++;
            }
        }
        scanner.close();
    }
    public void printStack(){
        memory.printMemoryInRange(defaultStackStartIndex + 1, defaultStackSize + defaultStackStartIndex + 1);
    }
}