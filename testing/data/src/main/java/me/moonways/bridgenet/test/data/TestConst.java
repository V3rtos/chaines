package me.moonways.bridgenet.test.data;

import me.moonways.bridgenet.model.permissions.permission.Permission;

import java.time.Duration;
import java.util.UUID;

public final class TestConst {

    public static final class Game {
        public static final String NAME = "Developing Game";
        public static final String MAP = "IDE with opened Bridgenet project";
    }

    public static final class Player {
        public static final UUID ID = UUID.randomUUID();
        public static final String NICKNAME = "itzstonlex";
    }

    public static final class Friend {
        public static final UUID FRIEND_ID = UUID.randomUUID();
        public static final String FRIEND_NICKNAME = "bridgenet_user";
    }

    public static final class Report {
        public static final String REPORTER = "itzstonlex";
        public static final String INTRUDER = "any_player";
        public static final String COMMENT = "Flying, KillAura and etc..";
        public static final String SOURCE = "MoonWays";
    }

    public static final class Permissions {
        public static final String PERMISSION_NAME = "bridgenet.test";

        public static final Permission PERMISSION = Permission.named(PERMISSION_NAME);
        public static final Permission TEMP_PERMISSION = Permission.temp(PERMISSION_NAME, Duration.ofSeconds(3));
    }

    public static final class Entity {
        public static final String FIRST_NAME = "Mikhail";
        public static final String LAST_NAME = "Sterkhov";
        public static final String STATUS_NAME = "Middle Java Developer";
        public static final int AGE = 20;
    }

    public static final class SqlQuery {

        public static final String PLAYERS_CREATE_ENCODED_TABLE_NATIVE = "CREATE TABLE IF NOT EXISTS Players ( ID BIGINT AUTO_INCREMENT NOT NULL UNIQUE , NAME VARCHAR(32) UNIQUE NOT NULL  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci, AGE INT NOT NULL DEFAULT 1 , PRIMARY KEY ( ID, NAME ) ) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
        public static final String PLAYERS_CREATE_TABLE_NATIVE = "CREATE TABLE IF NOT EXISTS Players ( ID BIGINT AUTO_INCREMENT NOT NULL UNIQUE , NAME VARCHAR(32) UNIQUE NOT NULL, AGE INT NOT NULL DEFAULT 1 , PRIMARY KEY ( ID, NAME ) )";
        public static final String PLAYERS_DELETE_TABLE_NATIVE = "DROP TABLE Players";
        public static final String PLAYERS_INSERT_DISTINCT_ROW_NATIVE = "INSERT INTO Players ( NAME, AGE ) VALUES ( 'moonways_user', 20 ) ON DUPLICATE KEY UPDATE NAME = 'moonways_user'";
        public static final String PLAYERS_INSERT_ROW_NATIVE = "INSERT INTO Players ( NAME, AGE ) VALUES ( 'moonways_user', 20 )";
        public static final String PLAYERS_DELETE_ROW_NATIVE = "DELETE FROM Players WHERE NAME = 'moonways_user'";
        public static final String PLAYERS_SELECT_JOINED_ROW_NATIVE = "SELECT AVG(AGE) AS AVG_AGES, MIN(ID), ID FROM Players  OUTER JOIN PlayersAges ON ID = PlayersAges.ID WHERE NAME LIKE 'moonways_user' OR AGE = 5 GROUP BY ID ORDER BY AGE DESC LIMIT 2";
        public static final String PLAYERS_SELECT_ROW_NATIVE = "SELECT AVG(AGE) AS AVG_AGES, MIN(ID), ID FROM Players WHERE NAME LIKE 'moonways_user' OR AGE = 5 GROUP BY ID ORDER BY AGE DESC LIMIT 2";
        public static final String PLAYERS_ALTER_COLUMN_NATIVE = "";

        public static final String PLAYERS_TABLE = "Players";
        public static final String PLAYERS_AGES_TABLE = "PlayersAges";
        public static final String PLAYER_ID_COLUMN = "ID";
        public static final String PLAYER_NAME_COLUMN = "NAME";
        public static final String PLAYER_AGE_COLUMN = "AGE";
    }
}
