Parser
======

The parser analyzes the raw search terms and produces the tasks to be fullfilled by the rest of the system.

For now there are to variants in planning:
- NLP: Parser for search terms in plain natural language (NL)
- RLP: Parser for search terms in a special request language (RL)

NOTE: We are still discussing which variants we are going to implement. So above statement about NLP/RLP is quite unstable. It might be that one of both will be dropped in the future.

Nethertheless there will be a well defined output of the parser, which we hope to define soon.	

In the case of NLP our work would be to use the Stanford Natural Language Parsing Tools and convert their parsing results into our intern format.

In case of RLP the idea is to use the javacc lexical scanner / parser generator to build a parser from the grammars of the request language. For now the plans are to use something similar to SQL as language.

Natural language vs Request language
====================================

Right now there is a discussion going on in our team if natural language or a special request language should be the input of the parser component.

Here is a summary of the pro/ contra arguments so far:
1. Natural language improves the usability of the tool since the user can define requests without learning a new semantic / syntax.
But on the other hand there are requirements for the supported search terms like brackets. Statements like brackets can not be expressed in natural language easily. So the preciseness of natural language is a problem.
2. In a specific request language precise logical statements like brackets, fuzzy weights ... can be defined very precisely. 
But the user has to learn the language before he is able to use the tool and creating grammars for javacc is more a challenge than using the results of the Stanford tools.

Using eclipse
=============

You need the JavaCC plugin: http://marketplace.eclipse.org/content/javacc-eclipse-plug