package me.moonways.bridgenet.test.data;

import me.moonways.bridgenet.connector.description.DeviceDescription;
import me.moonways.bridgenet.connector.description.UserDescription;
import me.moonways.bridgenet.model.service.economy.currency.Currency;
import me.moonways.bridgenet.model.service.gui.GuiDescription;
import me.moonways.bridgenet.model.service.gui.GuiSlot;
import me.moonways.bridgenet.model.service.gui.GuiType;
import me.moonways.bridgenet.model.service.gui.click.ClickType;
import me.moonways.bridgenet.model.service.gui.item.entries.material.Material;
import me.moonways.bridgenet.model.service.gui.item.types.Materials;
import me.moonways.bridgenet.model.service.language.Language;
import me.moonways.bridgenet.model.service.language.Message;
import me.moonways.bridgenet.model.service.permissions.permission.Permission;

import java.time.Duration;
import java.util.UUID;

public final class TestConst {

    public static final class Exceptions {
        // Printing stacktrace elements only with packages of Bridgenet if this value is TRUE.
        public static final boolean FILTER_BY_PACKAGE = Boolean.TRUE;
    }

    public static final class Auth {
        public static final String ACTUAL_PASSWORD = "123qweasdzxc";
        public static final String NEW_PASSWORD = "cxzdsaewq321";
    }

    public static final class Game {
        public static final String NAME = "Developing Game";
        public static final String MAP = "IDE with opened Bridgenet project";

        public static final String SERVER_NAME = "development-arena-1";
        public static final String SERVER_HOST = "127.0.0.1";
        public static final int SERVER_PORT = 12345;

        public static final DeviceDescription SERVER_DESC =
                DeviceDescription.builder()
                        .name(SERVER_NAME)
                        .host(SERVER_HOST)
                        .port(SERVER_PORT)
                        .build();
    }

    public static final class Player {
        public static final UUID PROXY_ID = UUID.randomUUID();
        public static final UUID ID = UUID.randomUUID();
        public static final String NICKNAME = "itzstonlex";

        public static final UserDescription DESC =
                UserDescription.builder()
                        .name(NICKNAME)
                        .uniqueId(ID)
                        .proxyId(PROXY_ID)
                        .build();
    }

    public static final class Server {
        public static final String NAME = "testing-local-1";
        public static final String HOST = "127.0.0.1";
        public static final int PORT = 25565;

        public static final DeviceDescription DESC =
                DeviceDescription.builder()
                        .name(NAME)
                        .host(HOST)
                        .port(PORT)
                        .build();
    }

    public static final class Economy {
        public static final Currency CURRENCY = Currency.COINS;
        public static final int BANK_CHARGE_AMOUNT = 2000;
        public static final int BANK_PAY_AMOUNT = 100;
        public static final int BANK_TRANSFER_AMOUNT = 990;
        public static final int DEPOSIT_INVEST_AMOUNT = 62_000;
        public static final int DEPOSIT_WITHDRAW_AMOUNT = 20_000;
    }

    public static final class Connector {
        public static final DeviceDescription DEVICE_DESC =
                DeviceDescription.builder()
                        .name("testing-connector")
                        .host(Server.HOST)
                        .port(Server.PORT + 1)
                        .build();
    }

    public static final class Friend {
        public static final UUID FRIEND_ID = UUID.randomUUID();
        public static final String FRIEND_NICKNAME = "bridgenet_user";
    }

    public static final class Report {
        public static final String REPORTER = "itzstonlex";
        public static final String INTRUDER = "AnyPlayer";
        public static final String COMMENT = "Flying, KillAura and etc..";
        public static final String SOURCE = "MoonWays";
    }

    public static final class Permissions {
        public static final String PERMISSION_NAME = "bridgenet.test";
        public static final Permission PERMISSION = Permission.named(PERMISSION_NAME);
        public static final Permission TEMP_PERMISSION = Permission.temp(PERMISSION_NAME, Duration.ofSeconds(3));
    }

    public static final class Languages {
        public static final Language ANY_UNKNOWN_LANG = Language.fromName("any_unknown");
        public static final Message ANY_UNKNOWN_MESSAGE = Message.keyed("any.unknown");
    }

    public static final class Items {
        public static final GuiSlot SLOT = GuiSlot.first();
        public static final Material MATERIAL = Materials.DIAMOND_BLOCK;
        public static final String NAME = "Tested item stack";
        public static final ClickType CLICK_TYPE = ClickType.LEFT_MOUSE;
    }

    public static final class Inventory {
        public static final GuiType TYPE = GuiType.CHEST;
        public static final String TITLE = "Testing inventory";
        public static final int SIZE = GuiDescription.toSize(4, TYPE);
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

    public static final class Interceptor {
        public static final String HELLO_MESSAGE = "Hello world!";
    }
}
