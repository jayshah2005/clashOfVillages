# Inital Setup

First the server runs. Next, you start the client.

The first step is to assign a player to the client by either loading a player or creating a new player. 

To do this, the server sends a bool to signify whether there are pre-saved players. It also sends a list of player names that are saved.
Based on this information, the client decides whether it wants to load a player (if so, which one) or create a new one.

Once the client selects the name of the player they want to lead/create, it is sent to the server and the player is loaded/created.
After this, the client recieves the player object and loads it into their GUI.