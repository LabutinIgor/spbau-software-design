## Description

Command is interface for all commands

Shell uses Lexer and Parser to parse commands

Lexer divides line to words and service symbols and then substitutes 
variables

Parser makes list of commands from list of tokens, it uses JCommander 
for parsing arguments

PipelineCommand is constructed from list of commands and executes all 
commands with corresponding input/output streams

##Diagram
![](diagram.png)

##JCommander
It's easy to use this framework for parsing parameters,
I just needed to add an annotation for each parameter and slightly 
change construction of commands 

## REVIEW

Из того, что понравилось:
* Очень коротки ясный код, легко разобраться
* Хороший общий интерфейс для команд и для автопарсинга аргументов. Чтобы
добавить новые команды вообще не нужно было запариваться о парсинге (только
собственно добавить имена в список команд в парсере), а аргументы и все
остальное за тебя распарсят.
* Разделенные стадии лексинга и парсинга
* Очень круто реализован пайплайн. Если я (и не только я) для каждого `|`
создавал по своему отдельному пайпу и как-то криво соединял стримы, то
Игорь сделал отдельный класс `PipilineCommand` и просто внутри него всегда
запускает команды, соединяя стримы. Очень круто.

Из того, что не очень понравилось:
* Команды возвращают `void`, хотя логичнее `int`, чтобы давать exit code.
И вообще, командам разрешено кидать только `IOException`, а иногда хочется
ещё что-нибудь кинуть (т.е. я считаю, что метод `Command::run` должен
`throws Exception` и `return int`)
* Нет фабрики команд. Хотелось бы иметь фарбрику команд, в которую можно
просто регистрировать команды. Из-за отсутствия таковой появляется некрасивый
`switch` в `Parser::parseCommand` и вообще необходимость изменять парсер при
добавлении команд (правда, зато не надо накручивать reflection'a).
* Скорее бага, чем дизайн, но все же -- `Shell` обрабатывает не все ошибки
 (вылетает с ненулевым exit code'ом на `Ctrl^D`)
* В `Environment` было бы круто ище добавить какой-то "inner state". А то сейчас
юзер как бы может изменить любую переменную нашего окружения (это не совсем так,
см. комметарий в `Shell::main`, но это скорее хак), а хочется иметь какие-то свои, внутренние
переменные.
* Мне кажется, почти все классы здесь должны быть помечаны `final` (кстати,
это так же касается локальных переменных). Не уверен, архитектура это или
code style, но добавлю)