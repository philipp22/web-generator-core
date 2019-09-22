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

NAME: NAME_FRAG ;

OPTIONAL : '?' ;

artifact: domain* ;

domain:
	annotations=ANNOTATION*
	DOMAIN name=NAME '{'
		constants=attribute*
	'}' ;

attribute: annotations=ANNOTATION* type=NAME name=NAME (optional=OPTIONAL)? ';' ;
