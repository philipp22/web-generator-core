grammar WebDomain;

fragment
StringCharacter
	:	~["\\\r\n]
	;

STRING
	:	'"' StringCharacter* '"'
	;

ANNOTATION: '@' NAME_FRAG ('(' ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|','|'='|STRING)* ')')? ;

fragment
NAME_FRAG : ([a-z] | [A-Z] | '_')+ ([0-9] | [a-z] | [A-Z] | '_' | '<' | '>')* ;

WHITESPACE : (' ' | '\t' | '\n' | '\r')+ -> skip ;

DOMAIN : 'd' 'o' 'm' 'a' 'i' 'n' ;

REST : 'r' 'e' 's' 't' ;

NAME: NAME_FRAG ;

OPTIONAL : '?' ;

artifact: domain* ;

domain:
	annotations=ANNOTATION*
	DOMAIN name=NAME '{'
		attributes=attribute*
		rest?
	'}' ;

rest:
	REST '{'
		methods=restMethod+
	'}';

restMethod:
	httpMethod=NAME+ ':' returnType=NAME+ ';' ;

attribute: annotations=ANNOTATION* type=NAME name=NAME (optional=OPTIONAL)? ';' ;
