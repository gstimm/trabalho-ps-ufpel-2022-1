TESTE   START
FAC INTUSE
    INTDEF  VAR1
* ESTE É UM COMENTÁRIO
    COPY    NEW   #3    * Copia o valor 3 para o endereço NEW, modo imediato
    WRITE   OLD
    READ    FAC,I   * Modo indireto
    STOP
OLD CONST   5
NEW SPACE   
VAR1    CONST 9
    END