grammar Pada;

// PARSER ======================================================================
unit
  : unitPackage? 
    unitImport* 
    ( unitClass
    | unitExtension
    | unitFunction
    )*
  ;

/* UNIT DECLARATION */

unitPackage
    : annotation* 'package' typeName;

unitImport
    : 'import' unitImportTarget ('as' importAlias)?;

unitImportTarget
    : typeName;

importAlias
    : identifier;

/* TYPE NAMING */
typeName
    : identifier ('.' identifier)*;

typeNameList
    : typeName (',' typeName)*;

typeArguments
    : '<' typeArgument (',' typeArgument)* '>';

typeArgument
    : typeRefOrArray | '?';

typeRefOrArray
    : typeRef ('[' ']')*;

typeRef
    : typeName typeArguments?;

typeRefList
    : typeRef (',' typeRef)*;

typeRefN
    : typeRef '?'?;

/* ANNOTATIONS */

annotation
    : '@' typeName annotationParamList?;

annotationParamList
    : '(' annotationParam (',' annotationParam )* ')';

annotationParam 
    : (identifier '=')? annotationExpr;

annotationExpr
    : '{' (annotationExpr (',' annotationExpr)*)? '}'
    | annotation      
    | expression;

/* EXTENSION FUNCTIONS */
unitFunction
    : annotation* function;

unitExtension
    : annotation* functionResultDecl typeRef '.' functionSignature;

/* TYPE DECLARATIONS */

typeDeclName
    : identifier;

unitClass
    : classDecl classBody?;

classDecl
    : annotation* modifier* 'class' typeDeclName typeArguments? classSuperList?
    ;

classSuperList
    : ':' typeRef (',' typeRef)*;

classBody
    : '{' typeMemberDecl* '}';

/* TYPE MEMBERS */

modifier
    : 'public' 
    | 'final' 
    | 'static' 
    | 'native' 
    | 'synchronized' 
    | 'transient' 
    | 'volatile';

typeMemberDecl
    : typeFunctionDecl
    | typeFieldDecl 
    | typeConstructorDecl;

typeFieldDecl
    : annotation* modifier* typeRefN identifier;

typeConstructorDecl
    : annotation* modifier* identifier typeConstructorParamList functionBody?;

typeConstructorParamList
    : '(' typeConstructorParam (',' typeConstructorParam)* ')';

typeConstructorParam
    : annotation* typeRefN identifier;

typeFunctionDecl
    : annotation* modifier* function;

/* CONSTRAINTS */

constraintThrows
    : 'throws' typeRefList;

constraintGeneric
    : 'where' identifier ':' typeRefList;

/* FUNCTIONS */
function
    : functionResultDecl functionSignature functionBody?;
             
functionSignature
    : identifier typeArguments? functionArgList functionConstraint*;

functionConstraint
    : constraintThrows
    | constraintGeneric;

functionArgList
    : '(' (functionArg (',' functionArg)*)? ')';

functionArg
    : annotation* typeRefN identifier;

functionResultDecl
    : 'void'
    | typeRefN;

functionBody
    : '{' statement* '}';

statement
    : block
    | 'if' parExpression statement ('else' statement)?
    | 'for' '(' forControl ')' statement
    | 'while' parExpression statement
    | 'do' statement 'while' parExpression
    | tryStatement
    | 'switch' parExpression switchBlock
    | 'synchronized' parExpression block
    | 'continue'
    | 'break'
    | 'return' expression?
    | 'throw' expression
    | statementExpression;


switchBlock
  : '{' switchBlockStatementGroup* switchLabel* '}';

switchBlockStatementGroup
  : switchLabel+ blockStatement*;

switchLabel
  : 'case' constantExpression ':'
  | 'case' identifier ':'
  | 'default' ':';

constantExpression
  : expression
  ;

tryStatement
  // must contain at least one resource, catch, or finally
  : 'try' '(' resources ')' block tryStatementLeave?
  | 'try' block tryStatementLeave;

tryStatementLeave
    : catchClause+ ('finally' block)?
    | 'finally' block;

catchClause
  : 'catch' '(' typeName ('|' typeName)* Identifier ')' block;

resources
  // Semicolon may be ommited for last resource
  : resource (';' resource)*;

resource
  : resourceVar? expression
  ;

resourceVar
    : ('var' | typeRefN)? variableDeclaratorId '=';

forControl
  : foreachControl
  | forInit? ';' expression? ';' forUpdate?
  ;

forInit
  : localVariableDeclaration
  | expressionList;

forUpdate
  : expressionList;

foreachControl
  : ('var' | typeRefN) Identifier ':' expression;

parExpression
    : '(' expression ')';

block
    : '{' blockStatement* '}';

blockStatement
    : localVariableDeclaration
    | statement;

localVariableDeclaration
    : ('var' | typeRefN) variableDeclarators;

variableDeclarators
  : variableDeclarator (',' variableDeclarator)*
  ;

variableDeclarator
  : variableDeclaratorId ('=' variableInitializer)?
  ;

variableDeclaratorId
  : Identifier ('[' ']')*
  ;

variableInitializer
  : arrayInitializer
  | expression
  ;

arrayInitializer
  : '{' ( variableInitializer (',' variableInitializer)* )? '}';

statementExpression
    : expression;

/* EXPRESSIONS */

identifier
    : Identifier;

expression
  : primary                                                     #exprPrimary
  | expression '.' Identifier                                   #exprMemberAccess
  | expression '.' 'this'                                       #exprOuterClassAccess
  | expression '.' 'super' '(' expressionList? ')'              #exprSuperClassCall
//  | expression '.' 'new' Identifier '(' expressionList? ')'
//  | expression '.' 'super' '.' Identifier arguments?
//  | expression '.' explicitGenericInvocation
  | expression '[' expression ']'                               #exprArrayAccess
  | expression '(' expressionList? ')'                          #exprFuncall
  | expression ('++'|'--')                                      #exprPostOp
  | ('+'|'-'|'++'|'--') expression                              #exprPreOp
  | ('~'|'!') expression                                        #exprLogicalUnary
//  | '(' typeRef ')' expression
//  | 'new' creator
  | expression ('*'|'/'|'%') expression                         #exprMul
  | expression ('+'|'-') expression                             #exprAdd
  | expression ('<<' | '>>>' | '>>') expression                 #exprShift
  | expression ('<=' | '>=' | '>' | '<' ) expression            #exprLogical
//  | expression 'instanceof' typeRef
  | expression ('==' | '!=') expression                         #exprEquality
  | expression '&' expression                                   #exprBinaryAnd
  | expression '^' expression                                   #exprXor
  | expression '|' expression                                   #exprBinaryOr
  | expression '&&' expression                                  #exprAnd
  | expression '||' expression                                  #exprOr
  | expression '?' expression ':' expression                    #exprTernary
  | expression
    ( '^='      <assoc=right>
    | '+='      <assoc=right>
    | '-='      <assoc=right>
    | '*='      <assoc=right>
    | '/='      <assoc=right>
    | '&='      <assoc=right>
    | '|='      <assoc=right>
    | '='       <assoc=right>
    | '>>='     <assoc=right>
    | '>>>='    <assoc=right>
    | '<<='     <assoc=right>
    | '%='      <assoc=right>
    )
    expression                                                  #exprAssign
  ;

primary
  : '(' expression ')'
  | 'this'
  | 'super'
  | literal
  | Identifier
//  | typeRef '.' 'class'
//  | 'void' '.' 'class'
  ;

expressionList
  : expression (',' expression)*
  ;

literal
    : BinaryLiteral
    | BooleanLiteral
    | NullLiteral
    | CharacterLiteral
    | DecimalLiteral
    | FloatingPointLiteral
    | HexLiteral
    | OctalLiteral
    | StringLiteral
    ;

// LEXER =======================================================================
KThis: 'this';
KVoid: 'void';
KSuper: 'super';
KClass: 'class';
KCase: 'case';
KSwitch: 'switch';
KContinue: 'continue';
KDo: 'do';
KThrow: 'throw';
KThrows: 'throws';
KFinally: 'finally';
KTransient: 'transient';
KDefault: 'default';
KPublic: 'public';
KSynchronized: 'synchronized';
KWhile: 'while';
KPackage: 'package';
KAs: 'as';
KTry: 'try';
KFinal: 'final';
KOverride: 'override';
KBreak: 'break';
KCatch: 'catch';
KElse: 'else';
KIf: 'if';
KImport: 'import';
KFor: 'for';
KReturn: 'return';
KWhere: 'where';
KStatic: 'static';
KVolatile: 'volatile';
KNative: 'native';
KVar: 'var';
             
ParGroupL: '(';
ParGroupR: ')';
BraGroupL: '{';
BraGroupR: '}';
SqrGroupL: '[';
SqrGroupR: ']';
Lt: '<';
Gt: '>';
Lte: '<=';
Gte: '>=';
Eq: '==';
Neq: '!=';
Dec: '--';
Inc: '++';
And: '&&';
Or: '||';
BitAnd: '&';
BitAndA: '&=';
BitOr: '|';
BitOrA: '|=';
Mul: '*';
MulA: '*=';
Div: '/';
DivA: '/=';
Mod: '%';
ModA: '%=';
Minus: '-';
MinusA: '-=';
Plus: '+';
PlusA: '+=';
ShiftL2: '<<';
ShiftL2A: '<<=';
ShiftR2: '>>';
ShiftR2A: '>>=';
ShiftR3: '>>>';
ShiftR3A: '>>>=';
At: '@';
Exclamation: '!';
Assign: '=';
Comma: ',';
Dot: '.';
Colon: ':';
Question: '?';
Xor: '^';
XorA: '^=';
SemiColon: ';';
Tilda: '~';

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

BooleanLiteral: 'false' | 'true';

NullLiteral: 'null';

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

fragment
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
Space : [ \r\t\u000C\n]+ -> skip;
Comment : '/*' .*? '*/' -> skip;
LineComment : '//' ~[\r\n]* -> skip;
