Parser
======

The parser analyzes the raw search terms and produces the tasks to be fullfilled by the rest of the system.
For that simple variants of naturale language sentences will be processed. 
For our work we use the Stanford Natural Language Parsing Tools and convert their parsing results into our intern format.

Right now we are working on transformations similar to:
"Who likes cats?" -> "Interests: cat" 

More complex sentences will follow later.

There will be a well defined output of the parser, which we hope to define soon.	

Background Discussion: Natural language vs Request language
====================================

There were to variants available:
- NLP: Parser for search terms in plain natural language (NL)
- RLP: Parser for search terms in a special request language (RL)

In the case of NLP our work would be to use the Stanford Natural Language Parsing Tools and convert their parsing results into our intern format.

In case of RLP the idea is to use the javacc lexical scanner / parser generator to build a parser from the grammars of the request language. For now the plans are to use something similar to SQL as language.

Here is a summary of the pro/ contra arguments so far:
1. Natural language improves the usability of the tool since the user can define requests without learning a new semantic / syntax.
But on the other hand there are requirements for the supported search terms like brackets. Statements like brackets can not be expressed in natural language easily. So the preciseness of natural language is a problem.

2. In a specific request language precise logical statements like brackets, fuzzy weights ... can be defined very precisely. 
But the user has to learn the language before he is able to use the tool and creating grammars for javacc is more a challenge than using the results of the Stanford tools.

Necessary Libs in Build Path
=============
- stanford-corenlp-3.5.0.jar
- stanford-corenlp-3.5.0-models.jar

Requirements for Eclipse and Java
=============
- Stanford NLP-Core Library v3.5.0: http://nlp.stanford.edu/software/corenlp.shtml
- Java 8 (for nlp-lib)
- You need the JavaCC plugin: http://marketplace.eclipse.org/content/javacc-eclipse-plug
