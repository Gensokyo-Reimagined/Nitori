# Nitori
A performance mod that converts patches into mixins using the Ignite Framework for Paper/Spigot.

## Optimizations
This plugin provides the following optimizations:
- [x] Iterate entity trackers faster by using Int2ObjectLinkedOpenHashMap
- [ ] Reduce constants allocations
- [x] Entity Micro Optimizations
- [ ] Lithium mixins
  - [x] Fast util
  - [x] HashedReferenceList
  - [x] CompactSineLUT
  - [x] Fast retrieval
  - [x] Cached hashcode
  - [x] Store gamerules in fastutil hashmap
  - [ ] Precompute shape arrays
  - [ ] Collections.attributes
  - [x] Collections.entity_by_type 
  - [x] Collections.entity_filtering 
  - [ ] Chunk serialization
  - [x] Cache iterate outwards
  - [ ] Block moving block shapes
  - [x] Shapes blockstate cache
  - [x] Lithium gen
  - [x] Ai sensor secondary POI
  - [ ] World tick scheduler
  - [ ] TileEntity Tracking
- [x] Smarter statistics-ticking
- [ ] Async Pathfinding
- [x] Multithreaded Tracker

**NOTE: This mod may or may not alter the default behaviors of some mob AI.**

---

<p align="center">
  <img src="https://github.com/Gensokyo-Reimagined/Nitori/assets/67013996/7443c502-ca8e-4a30-8a3c-5bae28565e5d" width="75%">
</p>
