* ESTE PROGRAMA LÊ UM NÚMERO E MOSTRA OS X PRIMEIROS
* TERMOS DA SEQUÊNCIA DE FIBBONACI
TESTE   START   0
        INTDEF  FIBO
        READ    NUMBER
FIBO    LOAD    NUMBER
        BRNEG   FINAL
* DECREMENTA O NÚMERO
        SUB     #1
        STORE   NUMBER
* CALCULA O AUX
        LOAD    A
        ADD     B
        STORE   AUX
* A = B
        LOAD    B
        STORE   A
* B = AUX
        LOAD    AUX
        STORE   B
        WRITE   AUX
        BR      FIBO
FINAL   STOP
A       CONST   0
B       CONST   1
AUX     SPACE
NUMBER  SPACE
        END