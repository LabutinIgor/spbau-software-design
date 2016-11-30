# Description

MessengerGUIMain class is view for chatting, it implements MessagesReceiver interface

MessengerGUIMain constructs panel with text dialogs and menu with items "start as server", "connect" and "set name" 
and their handlers

MessengerGUIMain creates MessangerClient or MessangerServer when corresponding menu clicked

MessangerClient and MessangerServer uses Connection that provides common interface of receiving messages from input stream,
and sending messages to output stream

