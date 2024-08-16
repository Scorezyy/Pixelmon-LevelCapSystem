SUPPORT FOR MORE PROJECTS!

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/B0B011Y5SN) 

[![Discord](https://img.shields.io/discord/800383201599553597)](https://discord.gg/j3gz7sFUje) 
# Pixelmon-LevelCapSystem

**Pixelmon-LevelCapSystem** is a server-side Forge mod that adds a level cap to the Pixelmon mod.

## Features 🗂️

- The mod enforces a level cap based on the number of badges a player has in their badge case. 
his cap applies to both capturing wild Pixelmon and trading with other players.

- When a Pixelmon reaches the level cap through leveling up, its level is stored. 
After obtaining the next badge, the Pixelmon will continue to gain experience and level up automatically at the next opportunity.

- If a Pixelmon attempts to level up beyond the cap (e.g., using a Rare Candy), a message will appear in the chat indicating that the maximum level has been reached. 
The Pixelmon's experience is preserved and applied once the player earns a new badge.

- For raid battles with other players, you can catch any raid Pokémon at any time. 
For example, if it's a level 5 raid, the Pokémon you catch will be at your maximum badge level.

## Configuration
✅ All text messages and badge levels are fully customizable in the configuration file located at:
`config/levelcap.yml`

Default ConfigValues:

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

## Versions 🌌
- Minecraft: 1.16.5
- Forge: 36.2.34
- Pixelmon: 9.1.12


## TODO 📋
- Implement a permission-based system to bypass badge requirements.
- Implement a disable module to individually deactivate trading, catching, raids, and interactions.
