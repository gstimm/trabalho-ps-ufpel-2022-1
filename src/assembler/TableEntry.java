package assembler;

public class TableEntry {
    private String label;
    private Integer address;
    private Boolean isGlobal;

    public TableEntry(String label, Integer address, Boolean isGlobal) {
        this.label = label;
        this.address = address;
        this.isGlobal = isGlobal;
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

    public Boolean getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(Boolean isGlobal) {
        this.isGlobal = isGlobal;
    }
}
