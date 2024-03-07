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
  - [ ] Collections.entity_by_type 
  - [ ] Collections.entity_filtering 
  - [ ] Chunk serialization
  - [ ] Cache iterate outwards
  - [ ] Block moving block shapes
  - [ ] Shapes blockstate cache
  - [ ] Lithium gen
  - [ ] Ai sensor secondary POI
  - [ ] World tick scheduler
- [x] Smarter statistics-ticking
- [ ] Async Pathfinding
- [ ] Multithreaded Tracker

**NOTE: This mod may or may not alter the default behaviors of some mob AI.**
