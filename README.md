# Crabber's Delight（Fabric Port）

> Unofficial Fabric port. The original mod **Crabber's Delight** was developed by [AlabasterLeking](https://github.com/AlabasterLeking/Crabbers-Delight).
> This repository ports the original gameplay and balance to **Fabric 1.20.1**.

## Introduction

Crabber's Delight is an ocean-themed expansion for Farmer's Delight: it adds crabs, shrimp, clams and other seafood ingredients,
as well as items such as coconuts and palm wood, along with a complete set of cooking recipes (crafting, cooking pot, cutting).

## Dependencies

| Mod | Type | Description |
| --- | --- | --- |
| [Farmer's Delight Refabricated](https://modrinth.com/mod/farmers-delight-refabricated) | Required | Prerequisite of the original mod |
| [Forge Config API Port](https://modrinth.com/mod/forge-config-api-port) | Required | Provides Forge-style configuration API |

## Implemented Features

- Crabs: natural spawning, variants, bucketable, crab claw tools
- Crab Trap: bait / bait bucket, loot tables, aquatic biome bonuses
- Full palm wood set: log/planks/stairs/slabs/fence/door/trapdoor/sign/boat, etc.
- Coconut helmet, pearl necklace
- All ingredients and Farmer's Delight recipes
- Seashells and underwater seashell world generation
- Independent creative tab

## Differences from the Original (Forge)

- Item ID fix: the Forge-side `palm_trapdoor_bottom` is `palm_trapdoor` on the Fabric side
- Equipment effect refresh strategy: refreshes every second, lasting 16 seconds each time, reducing per-tick performance cost
- Uses the Fabric ecosystem to implement registration, world generation and Mixin compatibility layer

## Building

Requires **Java 21+** (the Gradle wrapper downloads the rest automatically):

```bash
./gradlew build
```

The output is in `build/libs/`.

## License

[MIT](LICENSE). The original code is copyrighted by AlabasterLeking (2023); the Fabric port is copyrighted by CloseDW (2026).