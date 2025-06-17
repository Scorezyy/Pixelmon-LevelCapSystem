Support me for more nice Projects :) <3

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/B0B011Y5SN) 

[![Discord](https://img.shields.io/discord/800383201599553597)](https://discord.gg/RnDtKgq3pK) ![Downloads](https://img.shields.io/github/downloads/Scorezyy/Pixelmon-LevelCapSystem/total.svg)
![Forge](https://img.shields.io/badge/Forge-1.16.5--36.2.34-brightgreen.svg?colorB=26303d&logo=Conda-Forge) ![JDK](https://img.shields.io/badge/JDK-17-brightgreen.svg?colorB=469C00&logo=java) 
![JDK](https://img.shields.io/badge/JDK-8-brightgreen.svg?colorB=469C00&logo=java)
![Pixelmon](https://img.shields.io/badge/Pixelmon-9.1.12-brightgreen.svg?colorB=880808&logo=java)

# Pixelmon-LevelCapSystem

**Pixelmon-LevelCapSystem** is a Server-Client Forge mod that adds a level cap to the Pixelmon mod.

## Features 🗂️

🔒 **Capture**:
- The mod enforces a level cap based on the number of badges a player has in their badge case. 
his cap applies to both capturing wild Pixelmon.

🎮 **Leveling**:
- When a Pixelmon reaches the level cap through leveling up, its level is stored. 
After obtaining the next badge, the Pixelmon will continue to gain experience and level up automatically at the next opportunity.

📈 **Level-Items**:
- If a Pixelmon attempts to level up beyond the cap (e.g., using a Rare Candy), a message will appear in the chat indicating that the maximum level has been reached. 
The Pixelmon's experience is preserved and applied once the player earns a new badge.

🌌 **NPC Trades**:
- For Pokémon received from NPC trades, the Pokémon's level will automatically adjust to match your maximum badge level.
For example, if your highest badge allows you to control level 20 Pokémon, the traded Pokémon will be adjusted to level 20.

⚔️ **Raids**:
- Additionally, when you catch a Raid Pokémon, its level will automatically adjust to match your current badge level.

👨‍👨‍👦 **Player Trades**:
- You want to trade with other players, you must have the appropriate badge level to receive the Pokémon.

🆙 **Level-capped Spawns**  
- Pokémon will now spawn at levels up to the cap determined by your badge level. This dynamic scaling keeps encounters rewarding —all based on your progress!  

👥 **Multiplayer Spawn Priority**  
- When two players are within a 7-chunk radius, spawns are prioritized for the player with the lowest badge level.

🔑 **Permission-Based Badge Case**
- You can now replace the Badge Case with your own permission nodes when using a permissions plugin like LuckPerms. Check `badgelevel.yml` for more details.

🔍 **Duplicate Badge Check**
- Automatically blocks duplicate badges from being added to your Badge Case. You can tweak this feature in settings.yml.

🚫 **Exclude Pokémon**
- You can now exclude specific Pokémon from the level cap. Configure your exclusions in excludelevelcap.yml.

🐞 **Debug Messages**  
- New debug log output can be enabled to help track all events in real time.

## Configuration
✅ All texts, settings, messages, and excluded Pokémon can now be fully customized in the configuration files at:
`config/levelcapsystem/:`
 `settings.yml
 messages.yml
 excludelevelcap.yml
 badgelevel.yml`

## Versions 🌌
- Minecraft: 1.16.5 & 1.20.2
- Forge: 36.2.34 / 48.1.0
- Pixelmon: 9.1.12 - 9.1.13 / 9.2.10 (TESTED)


## TODO 📋
- ✅ Implement a disable module to individually deactivate trading, catching, raids, and interactions.
- ❌ Version Support: 1.20.2 / 1.21.1 - comming
