grammar CribsReader;
startRule  : simpleRule* ;

simpleRule : ID | STH;

ID : [a-zA-Z0-9]+ ;

WS : [ \t\r\n]+ -> skip ;

STH: . ;