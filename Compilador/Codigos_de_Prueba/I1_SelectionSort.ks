int x[10];

int minloc ( int a[], int low, int high )
{
    k ::= low;
    x ::= a[low];
    i ::= low + 1;
    while (i LT high)
    {
        if (a[i] LT x)
        {
            x ::= a[i];
            k ::= i;
        }
        i ::= i + 1;
    }
    return k;
}

void sort ( int a[], int low, int high)
{
    int i;  int k;
    i ::= low;
    while (i LT high - 1)
    {
        int t;
        k ::= minloc (a, i, high);
        t ::= a[k];
        a[k] ::= a[i];
        a[i] ::= t;
        i ::= i + 1;
    }
}

void main ( void )
{
    int i;
    i ::= 0;
    while (i LT 10)
    {
        x[i] ::= input ();
        i ::= i + 1;
    }
    sort (x, 0, 10);
    while (i LT 10)
    {
        output (x[i]);
        i ::= i + 1;
    }
}