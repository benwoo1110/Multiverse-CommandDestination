# Multiverse-CommandDestination

_If you are using the newer Multiverse v5, use v2 of this plugin, else use v1.2.2._

This is a small addon for people who are using [Multiverse](https://modrinth.com/plugin/multiverse-core) and want to run their own commands on teleporting or entering a [Multiverse Portal](https://modrinth.com/plugin/multiverse-portals)! Supports [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) replacements in command.

Some things you can do is to run your RandomTP or custom commands on entering a [Multiverse Portal](https://modrinth.com/plugin/multiverse-portals) that were not possible previously.

How to use:
Open the config.yml file in your Multiverse-CommandDestination folder.
Add the custom commands you want as explained below.

```yml
# +-------------------------------+
# | Multiverse-CommandDestination |
# +-------------------------------+
# Spigot: https://www.spigotmc.org/resources/multiverse-commanddestination.90232/
# Discord: https://discord.gg/Be59ehc
# Github: https://github.com/benwoo1110/Multiverse-CommandDestination.git
# Paypal: https://paypal.me/benergy10


# +------------------+
# | Command settings |
# +------------------+
# Put all the commands you want to run here.
# If you face yaml error, check formatting with http://www.yamllint.com/
commands:
  # The destination string is 'cmd:<name>'.
  # Prefix with 'console:' to run command from Console.
  # Prefix with 'op:' to run command as Operator.
  # Built in placeholders are:
  #   - %player% for player name.
  #   - %world% for world name player is in.
  # If you need additional placeholders, you can optionally get PlaceholderAPI plugin.

  # The following is an example.
  # To run this in Multiverse teleport command, you can do: '/mvtp cmd:examplename'
  # To set a portal destination, run '/mvp modify dest cmd:examplename -p <portalname>'
  # Upon teleporting to the destination, the 2 say command will be executed.
  examplename:
    - 'say I am %player% at %world%.'
    - 'console:say This is running from console.'

  # A command destination pre-made if you use BetterRTP plugin
  # Simply set your portal destination with '/mvp modify dest cmd:betterrtp -p <portalname>'
  betterrtp:
    - 'console:rtp player %player% world' # Replace world with your actual target world.

  # A command destination pre-made if you use WildernessTp plugin
  # Simply set your portal destination with '/mvp modify dest cmd:wildernesstp -p <portalname>'
  wildernesstp:
    - 'console:wild %player%'


# +----------------+
# | Other settings |
# +----------------+
# Enable parsing of placeholders provided by placeholderAPI plugin if you have it installed.
enable-papi-hook: true
```

(Note: If you are using minecraft <1.13, you will need to use an older version of Multiverse. Ask their support on it)

Contact Me:
Have a question or want to have a chat with me? the best place to ask is in my [Discord server](https://discord.gg/Be59ehc). Either myself or somebody else will be available.