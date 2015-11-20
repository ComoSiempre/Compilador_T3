

int aNumber1;
int aNumber2;
int anArray[123];
void aVariable;    /# technically, this is OK... #/
void anotherArray[234];		/# and, so is this... #/

/#=========================#/
/# a procedure declaration #/
/#=========================#/

void aProcedure1 (void)
{
}

void aProcedure2 ( void)
{
    ;
}

int aProcedure3 ( void )
{
    ; ;
}

/#=========================#/
/# a procedure declaration #/
/#=========================#/

int aProcedure4 ( int aNum1, void aNum2, int anArray1[], void anArray2[] )
{
    int aNum3;
    int anArray3[456];
    void aNum4;
    void anArray4[78];
    int aNum5;
    aNum3 ::= aNum1;
    aNum2 ::= aNum3 + 1;
    anArray3[0] ::= anArray1[2];
    anArray2[3] ::= anarray3[4];
    ; ;
    while (aNum3 GT aNum4)
        ;
    while (aNum3+2 GEQ 3*aNum5)
    {
        return;
    }
    if (23)
        ;
    if (aNum5)
        ;
    else
        ;
    if (5*a LEQ 8)
        aNum5 ::= 2;
    if ((aNum5 - 4) * (1/3 + 4*2))
    {
        output(1);
    }
    else
    {
        aNum4 ::= input();
        return aNum3;
    }
    for (2 + 2, 3, 0) {
        if (0) {
            return; 
        }
    }
    return 5 / 0;
}


/#==============#/
/# main routine #/
/#==============#/

void main ( void )
{
    aProcedure1 ( );
}