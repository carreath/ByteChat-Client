# ByteChat 
A multi-threaded java chat program using sockets

## Known issues to be fixed
Hardcoded IPs to be fixed -> Working on dedicated servers and dynamic IP lookup

## Step 1
    -Compile each java file `javac *.java`
  
## Step 2
    -Run server `java ByteChatServer`

## Step 3
    -Run clients `java ByteChatClient`

## Using the client
    -Clients must have a uniques identifier

    -Join a room with the command
    	"!CONNECT <id>" where <id> is (0, 1, or 2)

    -Leave a room with the command
    	"!DISCONNECT"

    -Close ByteChat from anywhere
    	"!CLOSE"

    -Type in the command prompt and press enter to send a message

## Leaving a conversation
    -Client types "END" and server responds by telling the lobby that the user has disconnected
    
    -the client closes threads and terminates
