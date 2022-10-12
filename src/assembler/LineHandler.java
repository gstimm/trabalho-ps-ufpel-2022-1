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
            this.isComentary = false;

            String line = scanner.nextLine();
            if (line.length() > Assembler.MAX_LINE_SIZE){
                throw new LineTooLong("Line is bigger than the defined maximum size. Line size: " + line.length() + ", maximum allowed line size: " + Assembler.MAX_LINE_SIZE + "!!!");
            }
            String tokens[] = line.split("\\s+");

            if (tokens.length == 0) {
                throw new FailToReadTokens("Not possible to read the line tokens");
            }

            if (tokens.length > 0) {
                if (tokens[0].equals("*")){
                    this.isComentary = true;
                    return;
                }
                this.label = tokens[0];
            }
            if (tokens.length > 1) {
                if (tokens[1].equals("*")) return;
                this.mnemonic = tokens[1];
            }
            if (tokens.length > 2) {
                if (tokens[2].equals("*")) return;
                this.operand1 = tokens[2];
            }
            if (tokens.length > 3) {
                if (tokens[3].equals("*")) return;
                this.operand2 = tokens[3];
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

    public int getNumberOfOperandsRead(){
        int result = 0;
        if (operand1.isBlank() == false) result++;
        if (operand2.isBlank() == false) result++;
        return result;
    }

    @Override
    public String toString(){
        return  this.label + "\t" +
                this.mnemonic + "\t" +
                this.operand1 + "\t" + 
                this.operand2;

    }

}

