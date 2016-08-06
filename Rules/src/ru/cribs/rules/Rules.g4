/**
 * Define a grammar called Hello
 */
grammar Rules;

// Parser
rules : simpleRule* ;

simpleRule : left=elementsLeft '->' right=elementsRight ';';

stringLiteral : StringLiteral;

id : ID;

simpleElement: stringLiteral | id ;

elementsLeft : elementLeft+;

elementsRight : elementRight+;

elementLeft : (identifier=ID ':')? element=simpleElementLeft addition=('+'|'*'|'?')?;

simpleElementLeft : element1=simpleElement | '(' element2=elementsLeft ')';

elementRight : element=simpleElement block=Block? ;

// Lexer
CharacterLiteral :  '\'' (PrintableChar | CharEscapeSeq) '\'';
StringLiteral    :  '"' StringElement* '"'
                 |  '"""' MultiLineChars '"""';
IntegerLiteral   :  (DecimalNumeral | HexNumeral) ('L' | 'l')?;
ID  : (Letter | Digit)+ ;
Block : '{' (Block|Anything|.)*? '}';

Comment          :  ('/*' .*?  '*/' |  '//' .*? '\n'?) -> skip;
WS  : [ \t\r\n]+ -> skip ;
Anything : . ;

// fragments
fragment UnicodeEscape    :	'\\' 'u' 'u'? HexDigit HexDigit HexDigit HexDigit ;
fragment WhiteSpace       :  '\u0020' | '\u0009' | '\u000D' | '\u000A';

fragment StringElement    :  '\u0020'| '\u0021'|'\u0023' .. '\u007F'  // (PrintableChar  Except '"')
                          |  CharEscapeSeq;
fragment MultiLineChars   :  ('"'? '"'? .*?)* '"'*;

fragment HexDigit         :  '0' .. '9'  |  'A' .. 'Z'  |  'a' .. 'z' ;
fragment FloatType        :  'F' | 'f' | 'D' | 'd';
fragment Upper            :  'A'  ..  'Z' | '$' | '_';  // and Unicode category Lu
fragment Lower            :  'a' .. 'z'; // and Unicode category Ll
fragment Letter           :  Upper | Lower; // and Unicode categories Lo, Lt, Nl
fragment ExponentPart     :  ('E' | 'e') ('+' | '-')? Digit+;
fragment PrintableChar    : '\u0020' .. '\u007F' ;
fragment CharEscapeSeq    : '\\' ('b' | 't' | 'n' | 'f' | 'r' | '"' | '\'' | '\\');
fragment DecimalNumeral   :  '0' | NonZeroDigit Digit*;
fragment HexNumeral       :  '0' 'x' HexDigit HexDigit+;
fragment Digit            :  '0' | NonZeroDigit;
fragment NonZeroDigit     :  '1' .. '9';