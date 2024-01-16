package me.moonways.bridgenet.api.modern_command.test;

import me.moonways.bridgenet.api.modern_command.*;

@Command()
@Aliases({"test", "test_command"})
@EntityLevel(EntityType.USER)
public class TestCommand {

    @Permission("primary_command_test")
    @SubcommandName("primary_command")
    @SubcommandArgument("{0} {1}")
    @SubcommandDescription("<string> <boolean>")
    private void primary_subcommand() {

    }

    @Permission("secondary_command_test")
    @SubcommandName("secondary command")
    @SubcommandArgument(arguments = "{0} {1} {2}", argumentsType = {String.class, String.class, Integer.class})
    @SubcommandDescription("<string> <string> <integer>")
    private void secondary_subcommand() {

    }
}
