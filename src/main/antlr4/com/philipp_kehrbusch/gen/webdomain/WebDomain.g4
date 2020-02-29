grammar WebDomain;

fragment
StringCharacter
	:	~["\\\r\n]
	;

STRING
	:	'"' StringCharacter* '"'
	;

PATH : '/' ([0-9] | [a-z] | [A-Z] | '_' | '-' | '/' | '{' | '}' | ' ')*;

WHITESPACE : (' ' | '\t' | '\n' | '\r')+ -> skip ;

ANNOTATION: '@' NAME_FRAG ('(' ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|','|'='|STRING)* ')')? ;

fragment
NAME_FRAG : ([0-9] | [a-z] | [A-Z] | '.' | '_')+ ;

DOMAIN : 'd' 'o' 'm' 'a' 'i' 'n' ;

VIEW : 'v' 'i' 'e' 'w' ;

REST : 'r' 'e' 's' 't' ;

FROM : 'f' 'r' 'o' 'm' ;

AS : 'as' ;

NAME: NAME_FRAG ('<' NAME_FRAG (',' NAME_FRAG)? '>')?;

OPTIONAL : '?' ;

ARROW : '-' '>';

QUERY_PARAMS: 'q' 'u' 'e' 'r' 'y' 'P' 'a' 'r' 'a' 'm' 's';

artifact: (domain | view)* ;

dependencies: '(' dependency=NAME (',' dependency=NAME)* ')';

domain:
	annotations=ANNOTATION* dependencies?
	DOMAIN name=NAME '{'
		(attribute ';')*
		rest?
	'}' ;

viewFrom:
	domainName=NAME+ (AS alias=NAME+)?;

fromList:
	viewFrom (',' viewFrom)*;

viewAttribute:
	annotations=ANNOTATION*
	path=NAME+ (AS alias=NAME+)? ';';

view:
	annotations=ANNOTATION*
	VIEW name=NAME+ (FROM fromList)? '{'
		(attribute ';')*
		viewAttribute*
	'}';

rest:
	REST '{'
		methods=restMethod+
	'}';

restMethod:
	name=NAME '(' path=PATH ')' ':' httpMethod=NAME '(' (bodyType=NAME (AS bodyTypeName=NAME)?)? ')'
	ARROW returnType=NAME+ (';' | restMethodOptions);

restMethodOptions:
	'{'
		restMethodOption*
	'}';

restMethodOption:
	optionName=NAME ':' queryParams;

queryParams:
	'['
		(attribute (',' attribute)*)?
	']';

attribute: annotations=ANNOTATION* type=NAME name=NAME (optional=OPTIONAL)? ;
