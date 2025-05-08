Support me for more nice Projects :) <3

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/B0B011Y5SN) 

[![Discord](https://img.shields.io/discord/800383201599553597)](https://discord.gg/j3gz7sFUje) ![Downloads](https://img.shields.io/github/downloads/Scorezyy/Pixelmon-LevelCapSystem/total.svg)
![Forge](https://img.shields.io/badge/Forge-1.16.5--36.2.34-brightgreen.svg?colorB=26303d&logo=Conda-Forge) ![JDK](https://img.shields.io/badge/JDK-17-brightgreen.svg?colorB=469C00&logo=java) 
![JDK](https://img.shields.io/badge/JDK-8-brightgreen.svg?colorB=469C00&logo=java)
![Pixelmon](https://img.shields.io/badge/Pixelmon-9.1.12-brightgreen.svg?colorB=880808&logo=java)

# Pixelmon-LevelCapSystem

**Pixelmon-LevelCapSystem** is a Server-Client Forge mod that adds a level cap to the Pixelmon mod.

## Features 🗂️

🔒 Capture:
- The mod enforces a level cap based on the number of badges a player has in their badge case. 
his cap applies to both capturing wild Pixelmon.

🎮 Leveling:
- When a Pixelmon reaches the level cap through leveling up, its level is stored. 
After obtaining the next badge, the Pixelmon will continue to gain experience and level up automatically at the next opportunity.

📈 Level-Items:
- If a Pixelmon attempts to level up beyond the cap (e.g., using a Rare Candy), a message will appear in the chat indicating that the maximum level has been reached. 
The Pixelmon's experience is preserved and applied once the player earns a new badge.

🌌 NPC Trades:
- For Pokémon received from NPC trades, the Pokémon's level will automatically adjust to match your maximum badge level.
For example, if your highest badge allows you to control level 20 Pokémon, the traded Pokémon will be adjusted to level 20.

⚔️ Raids:
- Additionally, when you catch a Raid Pokémon, its level will automatically adjust to match your current badge level.

👨‍👨‍👦 Player Trades:
- You want to trade with other players, you must have the appropriate badge level to receive the Pokémon.

🆙 **Level-capped Spawns**  
- Pokémon will now spawn at levels up to the cap determined by your badge level. This dynamic scaling keeps encounters rewarding —all based on your progress!  


  _You can disable this behavior via configuration._

👥 **Multiplayer Spawn Priority**  
- When two players are within a 7-chunk radius, spawns are prioritized for the player with the lowest badge level.

🐞 **Debug Messages**  
- New debug log output can be enabled to help track the caped poke spawns changes in real time.


## Configuration
✅ All text messages and badge levels are fully customizable in the configuration file located at:
`config/levelcap.yml`

Default Config::

```
badge_levels:
  badge.level.0: '10'
  badge.level.1: '20'
  badge.level.2: '30'
  badge.level.3: '40'
  badge.level.4: '50'
  badge.level.5: '60'
  badge.level.6: '70'
  badge.level.7: '80'
  badge.level.8: '100'

messages:
  interact_blocked: '&cThe Pokémon is too high level for you!'
  capture_blocked: '&cThis &ePokémon &cis too strong for you to catch!'
  level_blocked: '&cThis level is too high for your Pokémon!'
  max_level_reached: '&cYour &ePokémon &chas already reached the maximum level'
  trade_blocked: '&cYou cannot trade your Pokémon because it exceeds the allowed level!'
  npcTrade_access_message: "&aThe level of the traded Pokémon has been adjusted to match your badge level!"
  config_reloaded: '&aConfig has been successfully reloaded!'

capped_poke_spawn: true    # enable level-capped spawns (set to false to disable)

debug_messages: true       # show debug messages in console/log 

```

## Versions 🌌
- Minecraft: 1.16.5
- Forge: 36.2.34
- Pixelmon: 9.1.12- 9.1.13 (TESTED)


## TODO 📋
- Implement a disable module to individually deactivate trading, catching, raids, and interactions.
