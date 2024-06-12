package me.moonways.bridgenet.client.minestom;

public class MinestomBootstrap {

    public static void main(String[] args) {
        printGreeting();
        new MinestomServer().start();
    }

    private static void printGreeting() {
        System.out.println(
                """

                         ███▄ ▄███▓ ██▓ ███▄    █▓█████   ██████ ▄▄▄█████▓ ▒█████   ███▄ ▄███▓
                        ▓██▒▀█▀ ██▒▓██▒ ██ ▀█   █▓█   ▀ ▒██    ▒ ▓  ██▒ ▓▒▒██▒  ██▒▓██▒▀█▀ ██▒
                        ▓██    ▓██░▒██▒▓██  ▀█ ██▒███   ░ ▓██▄   ▒ ▓██░ ▒░▒██░  ██▒▓██    ▓██░
                        ▒██    ▒██ ░██░▓██▒  ▐▌██▒▓█  ▄   ▒   ██▒░ ▓██▓ ░ ▒██   ██░▒██    ▒██\s
                        ▒██▒   ░██▒░██░▒██░   ▓██░▒████▒▒██████▒▒  ▒██▒ ░ ░ ████▓▒░▒██▒   ░██▒
                        ░ ▒░   ░  ░░▓  ░ ▒░   ▒ ▒░░ ▒░ ░▒ ▒▓▒ ▒ ░  ▒ ░░   ░ ▒░▒░▒░ ░ ▒░   ░  ░
                        ░  ░      ░ ▒ ░░ ░░   ░ ▒░░ ░  ░░ ░▒  ░ ░    ░      ░ ▒ ▒░ ░  ░      ░
                        ░      ░    ▒ ░   ░   ░ ░   ░   ░  ░  ░    ░      ░ ░ ░ ▒  ░      ░  \s
                               ░    ░           ░   ░  ░      ░               ░ ░         ░  \s
                                                                                             \s
                        """);
    }
}
