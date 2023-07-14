package me.moonways.service.friend;

import lombok.Getter;
import me.moonways.bridgenet.service.inject.Component;
import me.moonways.bridgenet.service.inject.InitMethod;
import me.moonways.bridgenet.service.inject.Inject;
import net.conveno.jdbc.ConvenoRouter;

@Component
public final class FriendsService {

    @Getter
    private FriendsDatabaseRepository repository;

    @Inject
    private ConvenoRouter convenoRouter;

    @InitMethod
    private void initRepository() {
        repository = convenoRouter.getRepository(FriendsDatabaseRepository.class);
        repository.validateTableExists();
    }
}
