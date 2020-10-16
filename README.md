# GlobalWarming

This Slimefun addon aims to add climate change mechanics to the game.
A worth-trying, 99% configurable plugin for Minecraft Servers.

You can download GlobalWarming right here: [Development Builds](https://thebusybiscuit.github.io/builds/poma123/GlobalWarming/master/)

<p align="center">
  <a href="https://thebusybiscuit.github.io/builds/poma123/GlobalWarming/master/">
    <img src="https://thebusybiscuit.github.io/builds/poma123/GlobalWarming/master/badge.svg" alt="Build Server"/>
  </a>
</p>

## Mechanics
### Environmental mechanics:

- Forest fires (happens in loaded chunks, fire blocks will appear on random highest blocks in high temperature conditions)
- Ice melting (happens in loaded chunks, ice will melt randomly in high temperature conditions)
- Player slowness (happens to players if the temp. is high or low enough)
- Player burning (happens to players if the temp. is extremely high)

### Pollution mechanics:
The pollution can change in a per-world basis. There are two types of pollution mechanics:

##### 1. Pollution production
- When animals breed
- When a polluted Slimefun machine completes their process.
- When a polluted Slimefun item was used in a Slimefun machine.

##### 2. Pollution absorbtion
- When a tree grows
- When an absorbent Slimefun machine completes its process. (default: Air Compressor)

### News system:
- Every time the pollution changes in a world, a new "Breaking News" message will appear to all the players in that world, with a nice randomly chosen news report from the real world.

## Items and machines
- Thermometer (Indicates the current temperature)
- Air Quality Meter (Indicates the current temperature rise)
- Air Compressor (Compresses CO2 into an Empty Canister, and absorbs pollution)
- Empty Canister
- CO2 Canister (Contains compressed CO2)
- Cinnabarite (GEOResource, needed for the Mercury)
- Mercury (Resource, needed for the Air Compressor craft)
- Filter (needed for the Air Compressor craft)

![image](https://user-images.githubusercontent.com/25465545/96293130-90bcfa80-0fea-11eb-9f16-d57105148973.png)
## API
All well-documented API classes can be found under the [`me.poma123.globalwarming.api`](https://github.com/poma123/GlobalWarming/tree/master/src/main/java/me/poma123/globalwarming/api) package.
