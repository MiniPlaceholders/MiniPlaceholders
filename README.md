# MiniPlaceholders ![WorkFlow](https://img.shields.io/github/actions/workflow/status/MiniPlaceholders/MiniPlaceholders/build.yml?style=flat-square) [![Discord](https://img.shields.io/discord/899740810956910683?color=7289da&label=Discord)](https://discord.gg/5NMMzK5mAn)
MiniMessage Component-based Placeholders for PaperMC and Velocity platforms

## Compatibility
- Paper 1.19.4+
- Velocity 3.1.2+
- Krypton

## Commands:
### Velocity
`/vminiplaceholders <parse|help> <me|player> <player-to-parse>`
### Paper
`/miniplaceholders <parse|help> <me|player> <player-to-parse>`

## Usage

Check our usage wiki [here](https://github.com/MiniPlaceholders/MiniPlaceholders/wiki/User-Getting-Started)

## API

### Java
```java

class Main {
    public static void registerExpansion() {
        Expansion expansion = Expansion.builder("my-expansion")
                .filter(Player.class)
                .audiencePlaceholder("name", (aud, ctx, queue) ->
                    Tag.selfClosingInserting(aud.getName())
                )
                .globalPlaceholder("tps", (ctx, queue) ->
                    Tag.selfClosingInserting(Component.text(Bukkit.getTps()[0]))
                ).build;
        
        expansion.register();
        
        Player player;
        player.sendMessage(miniMessage().deserialize("Player Name: <my-expansion_name>", MiniPlaceholders.getAudiencePlaceholders(player)));
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
    player.sendMessage(miniMessage().deserialize("Player Name: <my-expansion_name>", MiniPlaceholders.getAudiencePlaceholders(player)))
}
```


Check the available javadocs [here](https://javadoc.io/doc/io.github.miniplaceholders/miniplaceholders-api)

Or check the developer wiki [here](https://github.com/MiniPlaceholders/MiniPlaceholders/wiki/Developer-Getting-Started)

