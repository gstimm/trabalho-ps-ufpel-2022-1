TESTE   START 0
FAC INTUSE
    INTDEF  VAR1
* ESTE É UM COMENTÁRIO
    COPY    NEW   #3    * Copia o valor 3 para o endereço NEW, modo imediato
    WRITE   OLD
    READ    FAC,I   * Modo indireto
    CALL    PARA
PARA    STOP
OLD CONST   H'5A'
NEW SPACE   
VAR1    CONST 9
    END