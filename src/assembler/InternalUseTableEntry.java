package assembler;

import java.util.ArrayList;

public class InternalUseTableEntry {
    private String label;
    private ArrayList<Integer> occurences;
    
    public InternalUseTableEntry(String label, ArrayList<Integer> occurences) {
        this.label = label;
        this.occurences = occurences;
    }

    public InternalUseTableEntry(String label) {
        this(label, new ArrayList<Integer>());
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public ArrayList<Integer> getOccurences() {
        return occurences;
    }
    public void setOccurences(ArrayList<Integer> occurences) {
        this.occurences = occurences;
    }
    public void addOccurence(Integer occurence){
        this.occurences.add(occurence);
    }
}
