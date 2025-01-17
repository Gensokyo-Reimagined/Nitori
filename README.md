# Nitori
A performance mod that converts patches into mixins using the Ignite Framework for Paper/Spigot.

> [!CAUTION]
> Nitori is more or less depraced to use [Leaf](https://github.com/Winds-Studio/Leaf) in it's replacement for performance orianted features.

## Optimizations:
This plugin provides the following optimizations:
- Faster Entity tracker by utilizing **Multiple Cores** this will allow larger servers to have way more entities
- Async NBT data saving which improves where paper doesn't on world saving
- Many of Lithium's Optimization patches which includes:
  - Faster Math
  - Faster Entity retrieval
  - Reduced Memory allocations
  - Improved inlined logics
  - Improved collections
  - Pre-Computed Shape arrays
  - Improved Block Entity tickings
  - Lithium Chunk Gen
  - Mob Ai Improvements (soon)
  - Fast BlockPos
  - Faster entity related inmterractions (Hand swing, Sprinting particles etc.)
  - ...and much more
- Includes some of the patches from Very Many Players (VMP)'s
  - Improved player tracking logic
  - Improved TypeFilterableList
  - If entity velocity is zero it won't send it as packets
  - Improved Player lookups
  - Faster VarInts
- Some patches from Potatoptimize
  - Way faster math
  - Faster rotation logic
  - Many Inlined logics

## Optimizations To-Do:
- Async Mob Pathfinding
- Multithreading starlight using ScaleableLux
- Easier config to toggle optimizations on and off
- Improving EntityTickList further

---

<p align="center">
  <img src="https://github.com/Gensokyo-Reimagined/Nitori/assets/67013996/7443c502-ca8e-4a30-8a3c-5bae28565e5d" width="75%">
</p>
