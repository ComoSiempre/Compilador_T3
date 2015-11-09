package scanner;

import java_cup.runtime.*;
import parser.*;
import Tables.*;

%%
%class Lexer
%ignorecase
%public
%unicode
%line
%column
%cup

%{
	
%}


LETRA = [a-zA-Z]
DIGITO = [0-9]
NUMERO = {DIGITO} | [1-9]{DIGITO}*
ELSE = ([eE])([lL])([sS])([eE])
IF = ([iI])([fF])
INT = ([iI])([nN])([tT])
VOID = ([vV])([oO])([iI])([dD])
RETURN = ([rR])([eE])([tT])([uU])([rR])([nN])
WHILE = ([wW])([hH])([iI])([lL])([eE])
FOR = ([fF])([oO])([rR]) 

SALTO=\n|\r|\r\n /*saltos de linea, que no nos interesa*/
InputCharacter = [^\r\n] /*cualquier cosa excepto /r/n*/
Commentario = {multiComment} | {lineComment}
multiComment   = "/#"[^#]~"#/" | "/#" "#"+ "/"  /* "/#"~"#/" */
lineComment     = "%" {InputCharacter}* {SALTO}?
ESPACIOS     = {SALTO} | [ \t\f] /*tabulaciones o saltos de linea (whitespace)*/

%%

{SALTO} {/*Ignorar*/}
{ESPACIOS} {/*Ignorar*/}
{Commentario} {/*ignorar*/}
{IF} {return new Symbol(sym.IF,yyline,yycolumn,yytext());}
{INT} {return new Symbol(sym.INT,yyline,yycolumn,yytext());}
{ELSE} {return new Symbol(sym.ELSE,yyline,yycolumn,yytext());}
{RETURN} {return new Symbol(sym.RETURN,yyline,yycolumn,yytext());}
{VOID} {return new Symbol(sym.VOID,yyline,yycolumn,yytext());}
{WHILE} {return new Symbol(sym.WHILE,yyline,yycolumn,yytext());}
{FOR} {return new Symbol(sym.FOR,yyline,yycolumn,yytext());}

"+" {return new Symbol(sym.SUMA,yyline,yycolumn,yytext());}
"-" {return new Symbol(sym.RESTA,yyline,yycolumn,yytext());}
"*" {return new Symbol(sym.MULTIPLICACION,yyline,yycolumn,yytext());}
"/" {return new Symbol(sym.DIVISION,yyline,yycolumn,yytext());}
"^" {return new Symbol(sym.EXPO_1,yyline,yycolumn,yytext());}
"**" {return new Symbol(sym.EXPO_2,yyline,yycolumn,yytext());}
"::=" {return new Symbol(sym.ASSIGN,yyline,yycolumn,yytext());}
"(" {return new Symbol(sym.PARENT_IZQ,yyline,yycolumn,yytext());}
")" {return new Symbol(sym.PARENT_DER,yyline,yycolumn,yytext());}
"[" {return new Symbol(sym.CORCH_IZQ,yyline,yycolumn,yytext());}
"]" {return new Symbol(sym.CORCH_DER,yyline,yycolumn,yytext());}
"{" {return new Symbol(sym.LLAVE_IZQ,yyline,yycolumn,yytext());}
"}" {return new Symbol(sym.LLAVE_DER,yyline,yycolumn,yytext());}
"LT" {return new Symbol(sym.MENOR,yyline,yycolumn,yytext());}
"LEQ" {return new Symbol(sym.MENIGUAL,yyline,yycolumn,yytext());}
"GT" {return new Symbol(sym.MAYOR,yyline,yycolumn,yytext());}
"GEQ" {return new Symbol(sym.MAYIGUAL,yyline,yycolumn,yytext());}
"EQ" {return new Symbol(sym.IGUALIGUAL,yyline,yycolumn,yytext());}
"NEQ" {return new Symbol(sym.DISTINTO,yyline,yycolumn,yytext());}
";" {return new Symbol(sym.PUNTOCOMA,yyline,yycolumn,yytext());}
"," {return new Symbol(sym.COMA,yyline,yycolumn,yytext());}
/*"++" {return new Symbol(sym.PLUSPLUS,yyline,yycolumn,yytext());}
"--" {return new Symbol(sym.MINUSMINUS,yyline,yycolumn,yytext());}*/

([a-z]){LETRA}*_?{LETRA}*{DIGITO}* {return new Symbol(sym.ID,yyline,yycolumn,yytext());}
{NUMERO} {
	return new Symbol(sym.NUM,yyline,yycolumn,yytext());
}

. {	System.err.println("Error Lexico: linea "+(yyline)+" columna "+(yycolumn)+ " token: "+yytext()); 
	
}

