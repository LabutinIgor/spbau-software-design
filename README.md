# spbau-software-design

Command is base abstract class for all commands

Shell uses Lexer and Parser to parse commands

Lexer divides line to words and service symbols and then substitutes variables

Parser makes list of commands from list of tokens

PipelineCommand is constructed from list of commands and executes all commands with corresponding input/output streams

![](diagram.png)
