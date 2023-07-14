package me.moonways.bridgenet.service.party;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@RequiredArgsConstructor
public class PartyMemberList extends ArrayList<PartyMember> {

    private final Party party;

    private void validateNull(String playerName) {
        if (playerName == null) {
            throw new NullPointerException("player name");
        }
    }

    @Nullable
    public PartyMember getMemberByName(@NotNull String name) {
        validateNull(name);
        return stream()
                .filter(partyMember -> partyMember.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @NotNull
    public PartyMember addMember(@NotNull String name) {
        validateNull(name);
        PartyMember partyMember = new PartyMember(name, party);

        super.add(partyMember);
        return partyMember;
    }

    @Nullable
    public PartyMember removeMember(@NotNull String name) {
        validateNull(name);
        PartyMember memberByName = getMemberByName(name);

        if (memberByName == null) {
            return null;
        }

        super.remove(memberByName);
        return memberByName;
    }

    public boolean hasMemberByName(@NotNull String name) {
        validateNull(name);
        return getMemberByName(name) == null;
    }
}
