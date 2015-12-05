//Esto suma 3 valores de un arreglo.
int multiplicar(int valor1, int valor2){
	int multiplicacion;
	multiplicacion = valor1*valor2;
	return multiplicacion;
}

int sumar(int array[]) {
    int _n;
    int suma;
    _n = 0;   
    suma = 0;
    while (_n < 4) {
    	array[0] = array[0] + array[1];
    	array[1] = array[0] + array[1];
        multiplicar(array[0], array[1]);
        array[1] = multiplicar + array[1];
        _n = _n + 1;
    }
    return suma;
}

void main(void) {
    int array[3];
    array[0] = 1;
    array[1] = 2;
    sumaMultiplicacion(array);
}