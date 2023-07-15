package me.moonways.bridgenet.service.party;

import lombok.RequiredArgsConstructor;
import me.moonways.services.api.parties.PartyMemberList;
import me.moonways.services.api.parties.participant.PartyMember;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@RequiredArgsConstructor
public class PartyMemberListImpl extends ArrayList<PartyMember> implements PartyMemberList {

    private final PartyImpl partyImpl;

    private void validateNull(String playerName) {
        if (playerName == null) {
            throw new NullPointerException("player name");
        }
    }

    @Nullable
    @Override
    public PartyMember getMemberByName(@NotNull String name) {
        validateNull(name);
        return stream()
                .filter(partyMember -> partyMember.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @NotNull
    @Override
    public PartyMember addMember(@NotNull String name) {
        validateNull(name);
        PartyMember partyMember = new PartyMember(name, partyImpl);

        super.add(partyMember);
        return partyMember;
    }

    @Nullable
    @Override
    public PartyMember removeMember(@NotNull String name) {
        validateNull(name);
        PartyMember memberByName = getMemberByName(name);

        if (memberByName == null) {
            return null;
        }

        super.remove(memberByName);
        return memberByName;
    }

    @Override
    public boolean hasMemberByName(@NotNull String name) {
        validateNull(name);
        return getMemberByName(name) == null;
    }
}
