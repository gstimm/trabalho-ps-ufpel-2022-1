package assembler;

public class TableEntry {
    private String label;
    private Integer address;
    private Character realocation_mode;
    
    public TableEntry(String label, Integer address) {
        this.label = label;
        this.address = address;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getAddress() {
        return address;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }
}
