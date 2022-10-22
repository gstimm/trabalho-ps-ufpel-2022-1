package macro;

import java.util.ArrayList;

import assembler.errors.WrongNumberOfOperands;

public class Macro {
    private ArrayList<String> formal_paramenters;
    private ArrayList<String> actual_paramenters;
    private String macroName;
    private String bodyMacro;

    public Macro(String macroName, ArrayList<String> formal_paramenters) {
        this.macroName = macroName;
        this.bodyMacro = "";
        this.formal_paramenters = removeComma(formal_paramenters);
        this.actual_paramenters = new ArrayList<String>();
    }

    public String expand(){
        String expandedMacro = bodyMacro;
        for(int i = 0; i < formal_paramenters.size(); i++){
            expandedMacro = expandedMacro.replaceAll(formal_paramenters.get(i), actual_paramenters.get(i));
        }
        return expandedMacro;
    }
    
    public void setActualParamenter(ArrayList<String> parameters) throws WrongNumberOfOperands {
        parameters = removeComma(parameters);
        if (parameters.size() != formal_paramenters.size()) {
            throw new WrongNumberOfOperands("ERROR while setting the paramenters, expected: "+ formal_paramenters.size() + ", got: " + parameters.size());
        }
        this.actual_paramenters = parameters;
    }

    public void appendToBody(String string) {
        this.bodyMacro += string;
    }

    private ArrayList<String> removeComma(ArrayList<String> entry) {
        ArrayList<String> result = new ArrayList<String>();
        for (String string : entry) {
            result.add(string.replaceAll(",", ""));
        }
        return result;
    }

    // getters and setters
    public String getMacroName() {
        return macroName;
    }

    public void setMacroName(String macroName) {
        this.macroName = macroName;
    }

    public String getBodyMacro() {
        return bodyMacro;
    }

    public void setBodyMacro(String bodyMacro) {
        this.bodyMacro = bodyMacro;
    }

}
