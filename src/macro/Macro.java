package macro;

import java.util.ArrayList;
import java.util.Stack;

import assembler.errors.WrongNumberOfOperands;

public class Macro {
    private ArrayList<Parameter> formal_paramenters;
    private String macroName;
    private String bodyMacro;
    private Stack<Parameter> actual_paramenters;

    public Macro(String macroName, ArrayList<String> formal_paramenters) {
        this.macroName = macroName;
        this.bodyMacro = "";
        this.formal_paramenters = parseParameters(formal_paramenters);
        this.actual_paramenters = new Stack<Parameter>();
    }

    public String expand(){
        String expandedMacro = bodyMacro;
        for(int i = 0; i < formal_paramenters.size(); i++){
            Parameter parameter = formal_paramenters.get(i);
            String par_level = "#("+parameter.getLevel()+","+parameter.getIndex()+")";
            expandedMacro = expandedMacro.replace(par_level, actual_paramenters.get(i).getName());
        }
        return expandedMacro;
    }
    
    
    public void setActualParamenter(ArrayList<String> parameters) throws WrongNumberOfOperands {
        if (parameters.size() != formal_paramenters.size()) {
            throw new WrongNumberOfOperands("ERROR while setting the paramenters, expected: "+ formal_paramenters.size() + ", got: " + parameters.size());
        }
        ArrayList<Parameter>parameters_parsed = parseParameters(parameters);
        for (int i = 0; i < parameters_parsed.size(); i++){
            actual_paramenters.push(parameters_parsed.get(i));
        }
    }

    public void appendToBody(String string, int levelCounter) {
        // Procurar se na linha existe uma ocorrencia de algum dos parâmetros formais,
        // Se existir substituir por o valor correspondente #(d,i)

        for (Parameter parameter : formal_paramenters) {
            if (string.contains(parameter.getName())) {
                String regex = "\\b"+ parameter.getName() +"\\b";
                string = string.replaceAll(regex, "#(" + parameter.getLevel() + "," + parameter.getIndex() +")");
            }
        }

        this.bodyMacro += string;
    }

    private ArrayList<Parameter> parseParameters(ArrayList<String> entry) {
        ArrayList<Parameter> result = new ArrayList<>();
        int level_counter = 1;
        int parameter_index = 1;
        for (String string : entry) {
            String formatted = string.replaceAll(",", "");
            Parameter aux = new Parameter(formatted, level_counter, parameter_index++);
            result.add(aux);
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
