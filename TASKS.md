New implemented stuff
=============
A list of all currently changed things:
- [x] the entrance in the first 5 floors is in the upper right corner
- [x] the function which causes the sad ghost to be barely visible (nearly invisible with the current tileset) was removed from com/shatteredpixel/shatteredicepixeldungeon/sprites/GhostSprite.java
- [x] added a function to switch to reverse landscape (available since android api level 9)
- [x] fixed a bug which caused the game to choose new sprites for rings, potions and scrolls every time a saved game was loaded
- [x] mage gains a MagesStaff everytime loading the game (fixed)
- [x] on the floors 1 to 5 potion of mind vision disables the heros field of view, you can only see the small region around the enemys (looks like it is fixed)
- [x] also on the floors 1 to 5: the heros field of view is also disablen in the gardens, also what you see from the garden will be only updated if you exit the room, wait for the end of the gardens effect and then reenter the garden (looks like it is fixed)

Things have to be fixed
============
- [ ] traps respawn on reentering a floor (maybe fixed)
- [ ] new sprite for ghost needed
- [ ] trap sprites for floors 1 to 5 have to be changed in traps.png
- [ ] it can be that there are doors next to the entrance
- [ ] test the app without preset openglconfig and look what is different


Reverse landscape:
============
1. change to landscape --> enables the "reverse landscape" checkbox
2. check the "reverse landscape" checkbox

Note: "reverse landscape" is currently not saved if you exit the game and is also resetted if you change to portrait
