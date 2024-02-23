package me.moonways.bridgenet.assembly;

/**
 * Данный класс отвечает за хранения наименований
 * ресурсов, хранящихся в модуле 'assembly' в константах
 * для более быстрого и удобного доступа к ним.
 */
public final class ResourcesTypes {

    // general resources into 'etc' directory.
    public static final String BOOTSTRAP_XML = "bootstrap.xml";
    public static final String MTP_SETTINGS_JSON = "mtp_settings.json";
    public static final String REST_SERVER_XML = "rest_server.xml";
    public static final String RSI_CONFIG_XML = "rsiconfig.xml";

    // class-loader resources with InputStream.
    public static final String DECORATORS_XML = "decorators.xml";
    public static final String RMI_POLICY = "rmi.policy";

}
