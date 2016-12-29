## Description

Command is interface for all commands

Shell uses Lexer and Parser to parse commands

Lexer divides line to words and service symbols and then substitutes 
variables

Parser makes list of commands from list of tokens, it uses JCommander 
for parsing arguments

PipelineCommand is constructed from list of commands and executes all 
commands with corresponding input/output streams
