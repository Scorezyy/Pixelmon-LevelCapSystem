# Pixelmon-LevelCapSystem

**Pixelmon-LevelCapSystem** is a server-side Forge mod that adds a level cap to the Pixelmon mod.

## Features

The mod checks how many badges the player has in their badge case and sets the corresponding level cap:

- 1 Badge = Level 10
- 2 Badges = Level 20
- 3 Badges = Level 30
- 4 Badges = Level 40
- 5 Badges = Level 50
- 6 Badges = Level 60
- 7 Badges = Level 70
- 8 Badges = Level 100

This level cap applies to both catching and trading Pixelmon. When a Pixelmon reaches the level cap through leveling up, its level is stored, and once the player earns the next badge, the Pixelmon automatically levels up with the remaining experience points.

## To-Do

- Implement a configuration file that allows customization of the level caps for each badge and the associated text messages.
