grammar Pada;

// PARSER ======================================================================
compilationUnit
  : packageDecl? 
    importDecl* 
    typeDecl*
  ;

/* UNIT DECLARATION */

packageDecl
    : PACKAGE packageName;

packageName
    : identifier (Dot identifier)*;

importDecl
    : IMPORT packageName (AS importAlias)?;

importAlias
    : identifier;

/* TYPE NAMING */

typeName
    : (packageName Dot)? identifier;

typeGenericSpec
    : BracketOpen (typeName (ListDelim typeName)* )? BracketClose;

typeSpec
    : typeName typeGenericSpec?;

/* ANNOTATIONS */

annotation
    : AnnotationPrefix typeName annotationParamList?;

annotationParamList
    : ListOpen annotationParam (ListDelim annotationParam )* ListClose;

annotationParam 
    : (identifier Equal)? expr;

/* TYPE DECLARATIONS */

typeMod
    : PUBLIC | FINAL | STATIC;

typeDecl
    : classDecl classBody?;

typeDeclName
    : identifier;

classDecl
    : annotation* typeMod* CLASS typeDeclName typeGenericSpec? classSuperList?
    ;

classSuperList
    : Colon typeSpec (ListDelim typeSpec)*;

classBody
    : ScopeOpen typeMemberDecl* ScopeClose;

/* CLASS MEMBERS */

typeMemberMod
    : PUBLIC | FINAL | STATIC;

typeMemberDecl
    : annotation* typeMemberMod* 
      ( functionDecl
      | fieldDecl 
      | constructorDecl);

fieldDecl
    : typeSpec Question? identifier;

constructorDecl
    : identifier constructorParamList functionBody?;

constructorParamList
    : ListOpen constructorParam (ListDelim constructorParam)* ListClose;

constructorParam
    : annotation* typeSpec identifier;

functionDecl
    : functionResultDecl identifier functionParameterList functionBody?;

functionParameterList
    : ListOpen functionParameterDecl (ListDelim functionParameterDecl)* ListClose;

functionParameterDecl
    : annotation* typeSpec identifier;

functionResultDecl
    : typeSpec;

functionBody
    : ScopeOpen expr* ScopeClose;

/* EXPRESSIONS */
identifier
    : Identifier;

expr 
    : exprLiteral
    | identifier;

exprLiteral
    : BinaryLiteral
    | CharacterLiteral
    | DecimalLiteral
    | FloatingPointLiteral
    | HexLiteral
    | OctalLiteral
    | StringLiteral
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
FINAL : 'final';
STATIC : 'static';
SUPER : 'super';

// fragments
AnnotationPrefix : '@';
ListOpen : '(';
ListDelim : ',';
ListClose : ')';
BracketOpen : '<';
BracketClose : '>';
ScopeOpen : '{';
ScopeClose : '}';
Equal : '=';
Colon : ':';
Dot: '.';
Question: '?';
    
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
  : Letter IdentifierTail*
  ;

IdentifierTail
  : Letter
  | Digit
  ;

fragment
Letter
  : '$' // $
  | 'A'..'Z' // A-Z
  | '_' // _
  | 'a'..'z' // a-z
  ;

// terminals
SPACE : [ \r\t\u000C\n]+ -> skip;
COMMENT : '/*' .*? '*/' -> skip;
LINE_COMMENT : '//' ~[\r\n]* -> skip;
