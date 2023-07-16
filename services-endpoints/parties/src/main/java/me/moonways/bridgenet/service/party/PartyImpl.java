package me.moonways.bridgenet.service.party;

import lombok.*;
import me.moonways.service.api.parties.PartyMemberList;
import me.moonways.service.api.parties.party.Party;
import me.moonways.service.api.parties.participant.PartyOwner;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class PartyImpl implements Party {

    @Getter
    private final PartyMemberListImpl membersList = new PartyMemberListImpl(this);

    @Getter
    private PartyOwner owner;

    private final long createdTimeMillis;

    private void validateNull(PartyOwner owner) {
        if (owner == null) {
            throw new NullPointerException("party owner");
        }
    }

    @Override
    public void setOwner(@NotNull PartyOwner newOwner) {
        validateNull(owner);

        PartyOwner previousOwner = this.owner;
        this.owner = newOwner;

        if (previousOwner == null) {
            return;
        }

        membersList.add(previousOwner);
        membersList.remove(newOwner);
    }

    @Override
    public long getTimeOfCreated(@NotNull TimeUnit unit) {
        return unit.convert(createdTimeMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public int getTotalMembersCount() {
        return membersList.size();
    }

    @Override
    public PartyMemberList getPartyMemberList() {
        return membersList;
    }
}
