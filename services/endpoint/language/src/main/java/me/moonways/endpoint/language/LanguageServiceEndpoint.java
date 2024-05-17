package me.moonways.endpoint.language;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.event.EventService;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.assembly.ResourcesAssembly;
import me.moonways.bridgenet.assembly.ini.IniConfig;
import me.moonways.bridgenet.assembly.ini.type.IniProperty;
import me.moonways.bridgenet.model.language.*;
import me.moonways.bridgenet.model.players.PlayersServiceModel;
import me.moonways.bridgenet.rsi.endpoint.persistance.EndpointRemoteContext;
import me.moonways.bridgenet.rsi.endpoint.persistance.EndpointRemoteObject;
import net.kyori.adventure.text.Component;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Log4j2
public final class LanguageServiceEndpoint extends EndpointRemoteObject implements LanguageServiceModel {
    private static final long serialVersionUID = -7274752404023780386L;

    private static final Language DEFAULT_LANG = LanguageTypes.ENGLISH;
    private static final String LANGUAGES_CONFIGS_FORMAT = "lang/%s.ini";

    private final Map<UUID, RegisteredLanguage> registeredLanguagesMap
            = new TreeMap<>();

    private final PlayerLanguagesRepository playerLanguagesRepository
            = new PlayerLanguagesRepository();

    private final Cache<UUID, Language> playersLanguagesCache =
            CacheBuilder.newBuilder()
                    .expireAfterAccess(5, TimeUnit.HOURS)
                    .build();

    @Inject
    private EventService eventService;
    @Inject
    private ResourcesAssembly assembly;
    @Inject
    private PlayersServiceModel playersServiceModel;

    public LanguageServiceEndpoint() throws RemoteException {
        super();
    }

    @Override
    protected void construct(EndpointRemoteContext context) {
        context.inject(playerLanguagesRepository);
        doRegisterDefaultTypes();
    }

    private void doRegisterDefaultTypes() {
        for (Language language : LanguageTypes.TYPES) {

            String resourceName = String.format(LANGUAGES_CONFIGS_FORMAT, language.getName());
            IniConfig iniConfig = assembly.readIniConfig(resourceName);

            RegisteredLanguage registeredLanguage =
                    RegisteredLanguage.builder()
                            .language(language)
                            .messagesConfig(iniConfig)
                            .build();

            registeredLanguagesMap.put(language.getId(), registeredLanguage);
            log.info("Registered language type §6{} §rfrom resource §6/{}", language.getName(), resourceName);
        }
    }

    private Optional<RegisteredLanguage> findLanguage(Language language) {
        return Optional.ofNullable(registeredLanguagesMap.get(language.getId()));
    }

    private Optional<String> findTranslatedMessage(Language language, Message message) {
        RegisteredLanguage registeredLanguage = findLanguage(language)
                .orElseGet(() -> findLanguage(DEFAULT_LANG).get());

        IniConfig messagesConfig = registeredLanguage.getMessagesConfig();
        String messageKey = message.getKey();

        String[] split = messageKey.split("\\.");
        return Optional.of(
                messagesConfig.readProperty(split[0], split[1])
                        .map(IniProperty::getAsString)
                        .orElseGet(() -> findTranslatedMessage(language, MessageTypes.UNKNOWN_MESSAGE_TYPE)
                                .get()
                                .replace(Message.PLACEHOLDER_MSG_KEY, messageKey)));
    }

    @Override
    public Optional<Language> getLang(UUID id) {
        return Optional.ofNullable(registeredLanguagesMap.get(id))
                .map(RegisteredLanguage::getLanguage);
    }

    @Override
    public Optional<Language> getLang(String name) throws RemoteException {
        return findLanguage(Language.fromName(name))
                .map(RegisteredLanguage::getLanguage);
    }

    @Override
    public Optional<Language> getLang(Locale locale) throws RemoteException {
        return findLanguage(Language.fromLocale(locale))
                .map(RegisteredLanguage::getLanguage);
    }

    @Override
    public Component message(Language language, String key) throws RemoteException {
        return Component.text(messageText(language, key));
    }

    @Override
    public Component message(Language language, Message message) throws RemoteException {
        return Component.text(messageText(language, message));
    }

    @Override
    public String messageText(Language language, String key) throws RemoteException {
        return findTranslatedMessage(language, Message.keyed(key)).orElse(null);
    }

    @Override
    public String messageText(Language language, Message message) throws RemoteException {
        return findTranslatedMessage(language, message).orElse(null);
    }

    @Override
    public Language getDefault() throws RemoteException {
        return DEFAULT_LANG;
    }

    @Override
    public Language getPlayerLang(UUID playerId) throws RemoteException {
        playersLanguagesCache.cleanUp();

        Language cached = playersLanguagesCache.getIfPresent(playerId);
        if (cached == null) {
            cached = playerLanguagesRepository.get(playerId)
                    .flatMap(entityLanguage -> getLang(entityLanguage.getLangId()))
                    .orElse(DEFAULT_LANG);

            playersLanguagesCache.put(playerId, cached);
        }

        return cached;
    }

    @Override
    public Language getPlayerLang(String playerName) throws RemoteException {
        return getPlayerLang(playersServiceModel.findPlayerId(playerName));
    }

    @Override
    public Optional<PlayerLanguageUpdateEvent> setPlayerLang(UUID playerId, Language language) throws RemoteException {
        Language previous = getPlayerLang(playerId);

        if (previous.equals(language)) {
            return Optional.empty();
        }

        playerLanguagesRepository.update(new EntityLanguage(playerId, language.getId()));
        playersLanguagesCache.put(playerId, language);

        PlayerLanguageUpdateEvent event =
                PlayerLanguageUpdateEvent.builder()
                        .playerId(playerId)
                        .language(language)
                        .build();

        eventService.fireEvent(event);
        return Optional.of(event);
    }

    @Override
    public Optional<PlayerLanguageUpdateEvent> setPlayerLang(String playerName, Language language) throws RemoteException {
        return setPlayerLang(playersServiceModel.findPlayerId(playerName), language);
    }
}
