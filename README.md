## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## Rodando o montador
Execute o arquivo .jar de  acordo com as instruções em *Rodando a interface gráfica* e no menu `file` existe a opção `assemble file` <br>
Se o programa estiver totalmente contido dentro do mesmo arquivo e não necessita do ligador, você pode rodar o programa montado utilizando a opção `load file` e selecionando o arquivo `.OBJ` <br>
Saída: na mesma pasta do arquivo de entrada, e com o mesmo nome serão gerados os seguintes arquivos <br>
1 - nome.GLO    -> tabela de símbolos definidos dentro do módulo que serão utilizados por outros módulos <br>
2 - nome.LST    -> arquivo de listagem, contém a linha do arquivo fonte, o endereço, o código lido e o código objeto gerado, para melhor visualização renomeie como .csv e abra com um editor de planilhas qualquer <br>
3 - nome.OBJ    -> arquivo objeto montado <br>
4 - nome.USE    -> tabela com as localizações de onde os símbolos definidos em módulos externos está sendo usada <br>

## Rodando a interface gráfica
<b>Requisitos:</b> <br>
Possuir o SDK do JavaFX no computador, faça download pelo site: https://gluonhq.com/products/javafx/ <br>
Extraia o arquivo `.zip` <br>
Execute o programa com o seguinte comando:<br>
`java -jar --module-path 'caminho_para_sdk_javafx/lib/' --add-modules javafx.controls,javafx.fxml .\trabalho-ps-ufpel-2022-1.jar` <br>

Caso contenha espaços no caminho do SDK do JavaFX, coloque entre aspas

Exemplo:<br>

`java -jar --module-path 'C:/Program Files/JavaFX/javafx-sdk-19/lib/' --add-modules javafx.controls,javafx.fxml .\trabalho-ps-ufpel-2022-1.jar` <br>

Neste exemplo o SDK do JavaFX está localizado na pasta `C:/Program Files/JavaFX/`