package assembler;

import java.util.Scanner;

import assembler.errors.FailToReadTokens;
import assembler.errors.LineTooLong;

public class LineHandler {
    private String label;
    private String mnemonic;
    private String operand1;
    private String operand2;
    private boolean isComentary;
    
    public LineHandler(){
        resetVaules();
    }

    public void readLine(Scanner scanner) throws LineTooLong, FailToReadTokens {
        if (scanner.hasNextLine()){
            this.resetVaules();

            String line = scanner.nextLine();
            if (line.length() > Assembler.MAX_LINE_SIZE){
                throw new LineTooLong("Line is bigger than the defined maximum size. Line size: " + line.length() + ", maximum allowed line size: " + Assembler.MAX_LINE_SIZE + "!!!");
            }
            String tokens[] = line.split("\\s+");

            if (tokens.length == 0) {
                throw new FailToReadTokens("Not possible to read the line tokens");
            }

            if (tokens.length > 0) {
                switch (tokens[0]) {
                    case "*":
                        this.isComentary = true;
                        return;
                    default:
                        this.label = tokens[0];
                        break;
                }
            }
            if (tokens.length > 1) {
                switch (tokens[1]) {
                    case "*":
                        return;
                    default:
                        this.mnemonic = tokens[1];
                        break;
                }
            }
            if (tokens.length > 2) {
                switch (tokens[2]) {
                    case "*":
                        return;
                    default:
                        this.operand1 = tokens[2];
                        break;
                }
            }
            if (tokens.length > 3) {
                switch (tokens[3]) {
                    case "*":
                        return;
                    default:
                        this.operand2 = tokens[3];
                        break;
                }
            }
        }
    }

    private void resetVaules() {
        this.label = "";
        this.mnemonic = "";
        this.operand1 = "";
        this.operand2 = "";
        this.isComentary = true;
    }

    public String getLabel() {
        return label;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public String getOperand1() {
        return operand1;
    }

    public String getOperand2() {
        return operand2;
    }

    public boolean isComentary() {
        return isComentary;
    }

}

