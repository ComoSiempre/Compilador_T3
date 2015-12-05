int fact ( int n )
{
    int f;
    if ( n GT 1 )
        f ::= n * fact ( n - 1 );
    else
        f ::= 1;
    return f;
}

void main ( void )
{
    int n;
    n ::= input ( );
    if ( n GT 0 )
        output ( fact ( n ) );
}
