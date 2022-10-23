package macro;

public class Parameter {
    private String name;
    private int level;
    private int index;
    
    public Parameter(String name, int level, int index) {
        this.name = name;
        this.level = level;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
