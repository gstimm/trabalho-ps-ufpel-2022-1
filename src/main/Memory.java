package main;

public class Memory {
    private final int memorySize;
    private final short[] memory;

    Memory(int numberWords) {
        memorySize = numberWords;
        memory = new short[memorySize];
    }

    public short getMemoryPosition(int address) throws IndexOutOfBoundsException {
        if(address < 0 || address >= memorySize){
            throw new IndexOutOfBoundsException("Address out of bounds");
        } else {
            return memory[address];
        }
    }

    public void setMemoryPosition(int address, short value) throws IndexOutOfBoundsException {
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
            System.out.println("Memory[" + i + "] = " + memory[i]);
        }
    }
    public void printMemoryInRange(int startIndex, int finishIndex) throws IndexOutOfBoundsException{
        if (startIndex < 0 || finishIndex > memorySize){
            throw new IndexOutOfBoundsException();
        }
        int start = startIndex < finishIndex ? startIndex : finishIndex;
        int end = startIndex > finishIndex ? startIndex : finishIndex;
        for(int i = start; i < end; i++) {
            System.out.println("Memory[" + i + "] = " + memory[i]);
        }
    }
    
}