grammar Pada;

// PARSER ======================================================================

compilationUnit
  : packageDecl? importDecl* typeDecl* EOF
  ;

packageDecl
    : PACKAGE packageName
    ;

packageName
    : Identifier (DOT Identifier)*
    ;

importDecl
    : IMPORT packageName (AS importAlias)?
    ;

importAlias
    : Identifier
    ;

typeDecl
    :   classDecl
    ;

classDecl
    : typeMod*
    ;

typeMod
    :   PUBLIC
    |   ABSTRACT
    |   FINAL
    |   STATIC
    ;

// LEXER =======================================================================

// keywords
AS : 'as';
IMPORT : 'import';
PACKAGE : 'package';
CLASS : 'class';
INTERFACE : 'interface';
PUBLIC : 'public';
PRIVATE : 'private';
ABSTRACT : 'abstract';
FINAL : 'final';
STATIC : 'static';


HexLiteral
  // underscores may be freely inserted after first hex digit and before last
  : '0' ('x'|'X')
    HexDigits
    IntegerTypeSuffix?
  ;

DecimalLiteral
  // Only a single zero digit may begin with a zero
  // Underscores may be freely inserted after first digit and before last
  : ( '0' | '1'..'9' ('_'* Digit)* ) IntegerTypeSuffix?
  ;

OctalLiteral
  // Underscores may be freely inserted before the last digit.
  // Don't know why underscores here are different from others -
  // Maybe the leading 0 is considered a digit as well as a marker
  // indicating that the following is a base 8 number
  : '0' ('_'* '0'..'7')+ IntegerTypeSuffix?
  ;

BinaryLiteral
  // underscores may be freely inserted after first digit and before last
  : '0' ('b'|'B')
    BinaryDigit ('_'* BinaryDigit)*
    IntegerTypeSuffix?
  ;

fragment
BinaryDigit : ('0'|'1') ;

fragment
HexDigits : HexDigit ('_'* HexDigit)* ;

fragment
HexDigit : (Digit|'a'..'f'|'A'..'F') ;

fragment
Digits : Digit ('_'* Digit)* ;

fragment
Digit : '0'..'9' ;

fragment
IntegerTypeSuffix : ('l'|'L') ;

FloatingPointLiteral
  : Digits '.' Digits? Exponent? FloatTypeSuffix?
  | '.' Digits Exponent? FloatTypeSuffix?
  | Digits Exponent FloatTypeSuffix?
  | Digits FloatTypeSuffix

    // Hex float literal
  | ('0x' | '0X')
    HexDigits? ('.' HexDigits?)?
    ( 'p' | 'P' ) ( '+' | '-' )? Digits // note decimal exponent
    FloatTypeSuffix?
  ;

fragment
Exponent : ('e'|'E') ('+'|'-')? Digits;

fragment
FloatTypeSuffix : ('f'|'F'|'d'|'D');

CharacterLiteral
  : '\'' ( EscapeSequence | ~('\''|'\\') ) '\''
  ;

StringLiteral
  : '"' ( EscapeSequence | ~('\\'|'"') )* '"'
  ;

fragment
EscapeSequence
  : '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
  | UnicodeEscape
  | OctalEscape
  ;

fragment
OctalEscape
  : '\\' ('0'..'3') ('0'..'7') ('0'..'7')
  | '\\' ('0'..'7') ('0'..'7')
  | '\\' ('0'..'7')
  ;

fragment
UnicodeEscape
  : '\\' 'u' HexDigit HexDigit HexDigit HexDigit
  ;

Identifier
  : Letter (Letter|JavaIDDigit)*
  ;

fragment
Letter
  : '$' // $
  | 'A'..'Z' // A-Z
  | '_' // _
  | 'a'..'z' // a-z
  ;

fragment
JavaIDDigit
    : '0' .. '9'
    ;

// terminals
NL: '\r\n' | '\r' | '\n';
DOT: '.';
SPACE : [ \r\t\u000C\n]+ -> channel(HIDDEN);
COMMENT : '/*' .*? '*/' -> channel(HIDDEN);
LINE_COMMENT : '//' ~[\r\n]* -> channel(HIDDEN);
