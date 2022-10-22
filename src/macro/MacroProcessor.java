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
    }

    public void process(String filename) throws FileNotFoundException, Exception {
        File output = new File(filename.substring(0, filename.length() - 4) + "_EXPANDED.ASM");
        output.createNewFile();
        FileWriter output_file = new FileWriter(output);
        
        Scanner scanner = new Scanner(new File(filename));
        LineHandlerMacro lineHandler = new LineHandlerMacro();
        while (scanner.hasNextLine()) {
            lineHandler.readLine(scanner);

            // Verificar o mnemônico
            String mnemonic = lineHandler.getMnemonic();
            String label = lineHandler.getLabel();
            ArrayList<String> tokens = lineHandler.getOperands();
            
            // Verifica se é definição de macro
            if (mnemonic.equals("MACRO")){
                if (currentState == State.COPY){
                    currentState = State.DEFINITION;
                    levelCounter = 1;
                    if (macrosDefinition.containsKey(label)) {
                        macrosDefinition.remove(label);
                    }
                    lineHandler.readLine(scanner);
                    String macro_name = lineHandler.getMnemonic();
                    tokens = lineHandler.getOperands();
                    Macro macro = new Macro(macro_name, tokens);
                    this.current_defining_macro = macro;
                    macrosDefinition.put(macro_name, macro);
                    continue;   
                }
            } else if (mnemonic.equals("MEND")) {
                levelCounter--;
                if (levelCounter == 0){
                    currentState = State.COPY;
                }
                continue;
            } else if (macrosDefinition.containsKey(mnemonic)) {
                Macro macro = macrosDefinition.get(mnemonic);
                macro.setActualParamenter(tokens);
                String expanded = macro.expand();
                output_file.write(expanded);
                continue;
            }
            
            if (this.currentState == State.DEFINITION) {
                current_defining_macro.appendToBody(lineHandler.toString() + "\n");
            }
            else {
                output_file.write(lineHandler.toString() + "\n");
            }
        }
        output_file.close();
    }

    // public void expandMacro(String macroName, )

}
