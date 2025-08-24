# MiniPlaceholders
[![WorkFlow](https://img.shields.io/github/actions/workflow/status/MiniPlaceholders/MiniPlaceholders/build.yml?style=flat-square)](https://github.com/MiniPlaceholders/MiniPlaceholders/actions)
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

Or check the [Developer Wiki]([https://github.com/MiniPlaceholders/MiniPlaceholders/wiki/Developer-Getting-Started](https://miniplaceholders.github.io/docs/developer-guide/Developer-Getting-Started))

### Java
```java

class Main {
    public static void registerExpansion() {
        final Expansion expansion = Expansion.builder("my-expansion")
                .filter(Player.class)
                .audiencePlaceholder("name", (audience, ctx, queue) -> {
                    final Player player = (player) audience;
                    return Tag.selfClosingInserting(player.getName());
                })
                .globalPlaceholder("tps", (ctx, queue) ->
                    Tag.selfClosingInserting(Component.text(Bukkit.getTps()[0]))
                ).build;
        
        expansion.register();
        
        Player player;
        final TagResolver playerResolver = MiniPlaceholders.getAudiencePlaceholders(player);
        player.sendMessage(miniMessage().deserialize("Player Name: <my-expansion_name>", playerResolver));
    }
}

```

### Kotlin
```kotlin
fun register() {
      val expansion = expansion("my-expansion") {
          audiencePlaceholder("name") { aud, _, _ ->
              aud.getName().asClosingTag()
          }
          globalPlaceholder("tps") { _, _ ->
              Component.text(Bukkit.getTps()[0]).asInsertingTag()
          }
      }
    
    expansion.register()
    
    val player: Player
    val playerResolver = MiniPlaceholders.getAudiencePlaceholders(player)
    player.sendMessage(miniMessage().deserialize("Player Name: <my-expansion_name>", playerResolver))
}
```
