# MiniPlaceholders
[![GitHub WorkFlow](https://img.shields.io/github/actions/workflow/status/MiniPlaceholders/MiniPlaceholders/build.yml?logo=GitHub&style=flat-square)](https://github.com/MiniPlaceholders/MiniPlaceholders/actions)
[![Jenkins Build](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.codemc.io%2Fjob%2FMiniPlaceholders%2Fjob%2FMiniPlaceholders%2F&style=flat-square&logo=jenkins)](https://ci.codemc.io/job/MiniPlaceholders/job/MiniPlaceholders/)
![Latest Version](https://img.shields.io/github/v/release/MiniPlaceholders/MiniPlaceholders?style=flat-square)
[![Discord](https://img.shields.io/discord/899740810956910683?color=7289da&logo=Discord&label=Discord&style=flat-square)](https://discord.gg/5NMMzK5mAn)
![Modrinth Downloads](https://img.shields.io/modrinth/dt/HQyibRsN?logo=Modrinth&style=flat-square)
![GitHub Downloads](https://img.shields.io/github/downloads/MiniPlaceholders/MiniPlaceholders/total?logo=GitHub&style=flat-square)



MiniMessage Component-based Placeholders API for Minecraft Platforms.

## Compatibility
- Paper 1.21+
- Velocity 3.4+
- Fabric 1.21.6+
- Sponge API 12+

## User Usage

Check our user usage wiki [here](https://miniplaceholders.github.io/docs/user-guide/User-Getting-Started)

## API

Check the available [Javadocs](https://javadoc.io/doc/io.github.miniplaceholders/miniplaceholders-api)

Or check the [Developer Wiki](https://miniplaceholders.github.io/docs/developer-guide/Developer-Getting-Started)

### Java
```java

class Main {
    public static void registerExpansion() {
        final Expansion expansion = Expansion.builder("my-expansion")
                .audiencePlaceholder(Player.class, "name", (player, ctx, queue) -> {
                    return Tag.selfClosingInserting(player.getName());
                })
                .globalPlaceholder("tps", (ctx, queue) ->
                    Tag.selfClosingInserting(Component.text(Bukkit.getTps()[0]))
                ).build;
        
        expansion.register();
        
        Player player;
        final TagResolver playerResolver = MiniPlaceholders.audiencePlaceholders();
        player.sendMessage(miniMessage().deserialize("Player Name: <my-expansion_name>", player, playerResolver));
    }
}

```

### Kotlin
```kotlin
fun register() {
      val expansion = expansion("my-expansion") {
          audience<Player>("name") { player, _, _ ->
              aud.getName().asClosingTag()
          }
          global("tps") { _, _ ->
              Component.text(Bukkit.getTps()[0]).asInsertingTag()
          }
      }
    
    expansion.register()
    
    val player: Player
    val playerResolver = MiniPlaceholders.audiencePlaceholders()
    player.sendMessage(miniMessage().deserialize("Player Name: <my-expansion_name>", player, playerResolver))
}
```
