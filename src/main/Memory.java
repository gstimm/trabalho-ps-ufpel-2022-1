package main;

public class Memory {
    private int memorySize = 2048;
    private char[] memory;

    Memory(int numberWords) {
        memorySize = numberWords;
        memory = new char[memorySize];
    }

    public char getMemoryPosition(int address) throws IndexOutOfBoundsException {
        if(address < 0 || address >= memorySize){
            throw new IndexOutOfBoundsException("Address out of bounds");
        } else {
            return memory[address];
        }
    }

    public void setMemoryPosition(int address, char value) throws IndexOutOfBoundsException {
        if(address < 0 || address >= memorySize){
            throw new IndexOutOfBoundsException("Address out of bounds");
        } else {
            memory[address] = value;
        }
    }

    public int getMemorySize() {
        return memorySize;
    }

    public void clearMemory() {
        for(int i = 0; i < memorySize; i++) {
            memory[i] = 0;
        }
    }

    public void printMemory() {
        for(int i = 0; i < memorySize; i++) {
            System.out.println("Memory[" + i + "] = " + (int)memory[i]);
        }
    }
    public void printMemoryInRange(int startIndex, int finishIndex) throws IndexOutOfBoundsException{
        if (startIndex < 0 || finishIndex > memorySize){
            throw new IndexOutOfBoundsException();
        }
        int start = startIndex < finishIndex ? startIndex : finishIndex;
        int end = startIndex > finishIndex ? startIndex : finishIndex;
        for(int i = start; i < end; i++) {
            System.out.println("Memory[" + i + "] = " + (int)memory[i]);
        }
    }
    
}