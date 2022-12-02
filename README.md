## Rodando a interface gráfica
<b>Requisitos:</b> <br>
Possuir o SDK do JavaFX no computador, faça download pelo site: https://gluonhq.com/products/javafx/ <br>
Extraia o arquivo `.zip` <br>
Execute o programa com o seguinte comando:<br>
`java -jar --module-path 'caminho_para_sdk_javafx/lib/' --add-modules javafx.controls,javafx.fxml .\trabalho-ps-ufpel-2022-1.jar` <br>

Caso contenha espaços no caminho do SDK do JavaFX, coloque entre aspas

Exemplo:<br>

`java -jar --module-path 'C:/Program Files/JavaFX/javafx-sdk-19/lib/' --add-modules javafx.controls,javafx.fxml .\trabalho-ps-ufpel-2022-1.jar` <br>

Neste exemplo o SDK do JavaFX está localizado na pasta `C:/Program Files/JavaFX/` <br>

## OBS.: <br>
O arquivo `.jar` foi criado para a versão 18 do JDK, caso sua versão seja anterior, atualize ou crie outro arquivo `.jar` para sua versão <br>
Você pode verificar sua verão utilizando o comando `java -version` <br>

## Compilando
<b>É requisito para compilação ter a biblioteca do JavaFX</b> <br>
Para compilar, clone o projeto <br>
```
git clone https://github.com/gstimm/trabalho-ps-ufpel-2022-1
```
Entre na pasta do projeto <br>
```
cd trabalho-ps-ufpel-2022-1
cd src
```
Compile o projeto com o seguinte comando
```
javac main/Main.java -d ../bin --module-path "caminho_para_sdk_javafx\lib" --add-modules javafx.controls,javafx.fxml
```

Exemplo:
```
javac main/Main.java -d ../bin --module-path "C:\Program Files (x86)\JavaFX\javafx-sdk-19\lib" --add-modules javafx.controls,javafx.fxml
```

Copie o arquivo `main/gui/GUI.fxml` com a descrição da interface gráfica para junto dos arquivos compilados
```
cd ..
cp .\src\main\gui\GUI.fxml .\bin\main\gui\
```
Entre na pasta `bin`
```
cd bin
```

Execute a máquina virtual com o comando

```
java --module-path "caminho_para_sdk_javafx\lib" --add-modules javafx.controls,javafx.fxml main.Main
```

Exemplo
```
java --module-path "C:\Program Files (x86)\JavaFX\javafx-sdk-19\lib" --add-modules javafx.controls,javafx.fxml main.Main
```

## Saída dos módulos
O montador, ligador, carregador e processador de macros podem ser acessados pela interface gráfica no menu `File`<br>

### Processador de macros
Recebe como entrada um arquivo `.asm` e vai expandir as macros e remover os comentários do arquivo, salvando o resultado em um arquivo de mesmo nome com a extensão `.MXF (Macro eXpanded File)`

### Montador
O montador recebe como entrada o arquivo com as macros já expandidas, se o arquivo contiver macros não expandidas isso acarretará em um erro na execução do montador. <br>
O montador retorna três arquivos:
>.OBJ -> arquivo objeto que contém o mapa de realocação e o código objeto em formato texto <br>
>.TABLE -> arquivo com informações das tabelas de usos interno, definições globais e também informações de tamanho do módulo e início do módulo <br>
>.LST -> arquivo com informações de qual linha do arquivo de entrada gerou qual código objeto e o endereço <br>

### Ligador
O ligador recebe como entrada os arquivos `.OBJ` dos módulos que serão ligados, vai procurar para cada arquivo selecionado, o arquivo `.TABLE` correspondente na mesma pasta e retorna um arquivo `.HPX` com a ligação dos módulos, com o nome do primeiro módulo escolhido para ser ligado

### Carregador
O carregador recebe um arquivo objeto e coloca na memória da máquina realocando os endereços de acordo com o mapa de realocação do arquivo

## Demo
![Alt text](./assets/Virtual_Machine.png?raw=true "Virtual Machine")

![Alt text](./assets/Executing_code.png?raw=true "Virtual Machine Executing Fibbonaci")