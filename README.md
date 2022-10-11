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
Utilize o comando `java -jar .\trabalho-ps-ufpel-2022-1.jar caminho_do_arquivo_que_sera_montado.asm`
Saída: na mesma pasta do arquivo de entrada, e com o mesmo nome serão gerados os seguintes arquivos
1 - nome.GLO    -> tabela de símbolos definidos dentro do módulo que serão utilizados por outros módulos
2 - nome.LST    -> arquivo de listagem, contém a linha do arquivo fonte, o endereço, o código lido e o código objeto gerado, para melhor visualização renomeie como .csv e abra com um editor de planilhas qualquer
3 - nome.OBJ    -> arquivo objeto montado
4 - nome.USE    -> tabela com as localizações de onde os símbolos definidos em módulos externos está sendo usada
