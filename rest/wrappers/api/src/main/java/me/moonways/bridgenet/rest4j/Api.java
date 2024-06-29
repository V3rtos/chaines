package me.moonways.bridgenet.rest4j;

public interface Api {

    String VERSION = "/v1";

    String FRIENDS_SERVICE = "/friends";
    String PARTIES_SERVICE = "/parties";
    String GUILDS_SERVICE = "/guilds";
    String LANGUAGE_SERVICE = "/language";
    String PERMISSIONS_SERVICE = "/permissions";
    String REPORTS_SERVICE = "/reports";
    String PLAYERS_SERVICE = "/players";
    String SERVERS_SERVICE = "/servers";

    static String anyServicePath(String service, String... paths) {
        return String.format("%s%s/%s", VERSION, service, String.join("/", paths));
    }
    
    static String friendsPath(String... paths) {
        return anyServicePath(FRIENDS_SERVICE, paths);
    }

    static String partiesPath(String... paths) {
        return anyServicePath(PARTIES_SERVICE, paths);
    }
    
    static String guildsPath(String... paths) {
        return anyServicePath(GUILDS_SERVICE, paths);
    }

    static String languagePath(String... paths) {
        return anyServicePath(LANGUAGE_SERVICE, paths);
    }

    static String permissionsPath(String... paths) {
        return anyServicePath(PERMISSIONS_SERVICE, paths);
    }

    static String reportsPath(String... paths) {
        return anyServicePath(REPORTS_SERVICE, paths);
    }
    
    static String playersPath(String... paths) {
        return anyServicePath(PLAYERS_SERVICE, paths);
    }

    static String serversPath(String... paths) {
        return anyServicePath(SERVERS_SERVICE, paths);
    }
}
