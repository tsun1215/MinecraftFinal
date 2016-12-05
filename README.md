# Betrayal Minecraft Mod
Minecraft final project: Proof-of-concept room generator that could serve as a base for a game. This idea was based off of the board game "Betrayal at a House on a Hill".

Authors: Terence Sun, Neil Ryan

## Building
```bash
gradle build
gradle eclipse
```

## Compiling jar
```bash
gradle jar
cp build/libs/minecraft-final.jar <YOUR_SERVER>/plugins/BetrayalPlugin.jar
```

## Bugs
- Java GC deletes the RoomManager when there are no users left in the world. On reconnect, this causes problems because then the user cannot open doors again. This is solvable by somehow saving these data points to a database or some sort of file.
- Generated object should not be breakable by players.


## Enhancements
- Rooms can be even more random in shape. It is theoretically possible to create a room that exists in multiple chunks if the RoomManager is modified. The set of walls just needs to be updated accordingly.
- Random furniture create could be done better through either setpieces or pseudorandom placement of objects throughout a room. Each `Materializable` object has a `.contains(Location loc)` function which can allow a populator to place furniture anywhere in the room and it can be ensure to not overlap or block other objects (see `Door#contains`).
- Multiple floors can be added by extending the start room to have a 2nd floor or basement level.