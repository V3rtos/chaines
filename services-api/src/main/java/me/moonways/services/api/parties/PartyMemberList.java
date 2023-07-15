package me.moonways.services.api.parties;

import me.moonways.services.api.parties.participant.PartyMember;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PartyMemberList extends List<PartyMember> {

    PartyMember getMemberByName(@NotNull String name);

    PartyMember addMember(@NotNull String name);

    PartyMember removeMember(@NotNull String name);

    boolean hasMemberByName(@NotNull String name);
}
