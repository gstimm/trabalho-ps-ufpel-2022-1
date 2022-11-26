package macro;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class MacroProcessor {
    private State currentState;
    private int levelCounter;
    private Hashtable<String,Macro> macrosDefinition;
    private Macro current_defining_macro;

    public MacroProcessor(){
        this.currentState = State.COPY;
        this.levelCounter = 0;
        this.macrosDefinition = new Hashtable<String,Macro>();
        current_defining_macro = null;
    }

    public void process(String filename) throws FileNotFoundException, Exception {
        File output = new File(filename.substring(0, filename.length() - 4) + ".MXF");
        output.createNewFile();
        FileWriter output_file = new FileWriter(output);
        
        Scanner scanner = new Scanner(new File(filename));
        LineHandlerMacro lineHandler = new LineHandlerMacro();
        while (scanner.hasNextLine()) {
            lineHandler.readLine(scanner);

            if (lineHandler.isComentary() == false) {
                String line_to_write = processLine(lineHandler);
                output_file.write(line_to_write);
            }
        }
        output_file.close();
    }

    private String expandMacro(String macro_name, ArrayList<String> parameters) throws Exception {
        String write_output = "";
        LineHandlerMacro lineHandlerMacro = new LineHandlerMacro();
        Macro macro = macrosDefinition.get(macro_name);
        macro.setActualParamenter(parameters);
        String expanded = macro.expand();
        String lines[] = expanded.split("\\n");
        for (String line : lines) {
            lineHandlerMacro.readLine(line);
            write_output += processLine(lineHandlerMacro);
        }
        return write_output;
    }

    private String processLine(LineHandlerMacro lineHandlerMacro) throws Exception {
        // Verificar o mnemônico
        String output = "";
        String mnemonic = lineHandlerMacro.getMnemonic();
        String label = lineHandlerMacro.getLabel();
        ArrayList<String> tokens = lineHandlerMacro.getOperands();
        
        // Verifica se é definição de macro
        if (mnemonic.equals("MACRO")) {
            levelCounter++;
            if (currentState == State.COPY) {
                currentState = State.DEFINITION;
                if (macrosDefinition.containsKey(label)) {
                    macrosDefinition.remove(label);
                }
                current_defining_macro = null;
                return output;
            }
        } else if (mnemonic.equals("MEND")) {
            levelCounter--;
            if (levelCounter == 0){
                currentState = State.COPY;
                current_defining_macro = null;
                return output;
            }
        } else if (macrosDefinition.containsKey(mnemonic)) {
            this.currentState = State.COPY;
            return expandMacro(mnemonic, tokens);
        }
        
        if (this.currentState == State.DEFINITION) {
            if (current_defining_macro == null) {
                String macro_name = lineHandlerMacro.getMnemonic();
                tokens = lineHandlerMacro.getOperands();
                Macro macro = new Macro(macro_name, tokens);
                this.current_defining_macro = macro;
                macrosDefinition.put(macro_name, macro);
            }
            else {
                current_defining_macro.appendToBody(lineHandlerMacro.toString() + "\n", levelCounter);
            }
        }
        else {
            output = lineHandlerMacro.toString() + "\n";
        }

        return output;
    }
}
