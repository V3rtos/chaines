package me.moonways.service.friend;

import lombok.Getter;
import me.moonways.bridgenet.injection.Component;
import me.moonways.bridgenet.injection.PostFactoryMethod;
import me.moonways.bridgenet.injection.Inject;
import net.conveno.jdbc.ConvenoRouter;

@Component
public final class FriendsService {

    @Getter
    private FriendsDatabaseRepository repository;

    @Inject
    private ConvenoRouter convenoRouter;

    @PostFactoryMethod
    private void initRepository() {
        repository = convenoRouter.getRepository(FriendsDatabaseRepository.class);
        repository.validateTableExists();
    }
}
