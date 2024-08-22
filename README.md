Support me for more nice Projects :) <3

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/B0B011Y5SN) 

[![Discord](https://img.shields.io/discord/800383201599553597)](https://discord.gg/j3gz7sFUje) ![Downloads](https://img.shields.io/github/downloads/Scorezyy/Pixelmon-LevelCapSystem/total.svg)
![Forge](https://img.shields.io/badge/Forge-1.16.5--36.2.34-brightgreen.svg?colorB=26303d&logo=Conda-Forge) ![JDK](https://img.shields.io/badge/JDK-17-brightgreen.svg?colorB=469C00&logo=java) 
![JDK](https://img.shields.io/badge/JDK-8-brightgreen.svg?colorB=469C00&logo=java)
![Pixelmon](https://img.shields.io/badge/Pixelmon-9.1.12-brightgreen.svg?colorB=880808&logo=java)

# Pixelmon-LevelCapSystem

**Pixelmon-LevelCapSystem** is a Server-Client Forge mod that adds a level cap to the Pixelmon mod.

## Features 🗂️

- The mod enforces a level cap based on the number of badges a player has in their badge case. 
his cap applies to both capturing wild Pixelmon and trading with other players.

- When a Pixelmon reaches the level cap through leveling up, its level is stored. 
After obtaining the next badge, the Pixelmon will continue to gain experience and level up automatically at the next opportunity.

- If a Pixelmon attempts to level up beyond the cap (e.g., using a Rare Candy), a message will appear in the chat indicating that the maximum level has been reached. 
The Pixelmon's experience is preserved and applied once the player earns a new badge.

- For Pokémon received from NPC trades, the Pokémon's level will automatically adjust to match your maximum badge level.
For example, if your highest badge allows you to control level 20 Pokémon, the traded Pokémon will be adjusted to level 20.

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
```

## Versions 🌌
- Minecraft: 1.16.5
- Forge: 36.2.34
- Pixelmon: 9.1.12


## TODO 📋
- Implement a disable module to individually deactivate trading, catching, raids, and interactions.
