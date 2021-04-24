# ByteChat 
A multi-threaded java chat program using sockets.
I made this project to further dive deeper into internet architecture and practice working with and building my own communication software.

I have also implemented my first attempt at public key cryptography. This part of the project was very finicky, but I was successfully able to create private and public keys for two users who wish to communicate through the text chat. 

# Unmaintained
This repository is unmaintained. Although I am planning on revisiting this type of project again.

## Step 1
    -Compile each java file `javac *.java`
  
## Step 2
    -Run server `java ByteChatServer`

## Step 3
    -Run clients `java ByteChatClient`

## Using the client
    -Clients must have a uniques identifier

    -Type in the command prompt and press enter to send a message

## Leaving a conversation
    -Client types "END" and server responds by telling the lobby that the user has disconnected
    
    -the client closes threads and terminates
