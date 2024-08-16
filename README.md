# Pixelmon-LevelCapSystem

**Pixelmon-LevelCapSystem** is a server-side Forge mod that adds a level cap to the Pixelmon mod. ♥️

## Features

The mod enforces a level cap based on the number of badges a player has in their badge case. 
This cap applies to both capturing wild/Raids Pixelmon and trading with other players.

Default Config Values:

```
- 0 Badges = Level 10
- 1 Badges = Level 20
- 2 Badges = Level 30
- 3 Badges = Level 40
- 4 Badges = Level 50
- 5 Badges = Level 60
- 6 Badges = Level 70
- 7 Badges = Level 80
- 8 Badges = Level 100
```

When a Pixelmon reaches the level cap through leveling up, its level is stored. 
After obtaining the next badge, the Pixelmon will continue to gain experience and level up automatically at the next opportunity.

If a Pixelmon attempts to level up beyond the cap (e.g., using a Rare Candy), a message will appear in the chat indicating that the maximum level has been reached. 
The Pixelmon's experience is preserved and applied once the player earns a new badge.

## Configuration
✅ All text messages and badge levels are fully customizable in the configuration file located at:
``config/levelcap.yml``

## TODO
⚠️Implement a permission-based system to bypass badge requirements.
