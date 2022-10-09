package assembler;

public class TableEntry {
    private String label;
    private Integer address;
    private Character realocation_mode;
    
    public TableEntry(String label, Integer address, Character realocation_mode) {
        this.label = label;
        this.address = address;
        this.realocation_mode = realocation_mode;
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

    public Character getRealocation_mode() {
        return realocation_mode;
    }

    public void setRealocation_mode(Character realocation_mode) {
        this.realocation_mode = realocation_mode;
    }
}
