package main;

import java.util.ArrayList;

public class Memory {
    private final int memorySize;
    private final ArrayList<Short> memory;

    Memory(int numberWords) {
        memorySize = numberWords;
        memory = new ArrayList<Short>(memorySize);
        clearMemory();
    }

    public short getMemoryPosition(int address) throws IndexOutOfBoundsException {
        if(address < 0 || address >= memorySize){
            throw new IndexOutOfBoundsException("Address out of bounds");
        } else {
            return memory.get(address);
        }
    }

    public void setMemoryPosition(int address, short value) throws IndexOutOfBoundsException {
        if(address < 0 || address >= memorySize){
            throw new IndexOutOfBoundsException("Address out of bounds");
        } else {
            memory.set(address, value);
        }
    }

    public int getMemorySize() {
        return memorySize;
    }

    public void clearMemory() {
        if (memory.size() < memorySize){
            for (int c = memory.size(); c < memorySize - memory.size(); c++){
                memory.add(c, (short) 0);
            }
        }
        else {
            memory.clear();
        }
    }

    public void printMemory() {
        for(int i = 0; i < memorySize; i++) {
            System.out.println("Memory[" + i + "] = " + memory.get(i));
        }
    }
    public void printMemoryInRange(int startIndex, int finishIndex) throws IndexOutOfBoundsException{
        if (startIndex < 0 || finishIndex > memorySize){
            throw new IndexOutOfBoundsException();
        }
        int start = startIndex < finishIndex ? startIndex : finishIndex;
        int end = startIndex > finishIndex ? startIndex : finishIndex;
        for(int i = start; i < end; i++) {
            System.out.println("Memory[" + i + "] = " + memory.get(i));
        }
    }
    public ArrayList<Short> getMemory(){
        return this.memory;
    }
    
}