package me.moonways.bridgenet.service.party;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Party {

    @Getter
    private final PartyMemberList membersList = new PartyMemberList(this);

    @Getter
    private PartyOwner owner;

    private final long createdTimeMillis;

    private void validateNull(PartyOwner owner) {
        if (owner == null) {
            throw new NullPointerException("party owner");
        }
    }

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

    public long getTimeOfCreated(@NotNull TimeUnit unit) {
        return unit.convert(createdTimeMillis, TimeUnit.MILLISECONDS);
    }

    public int getTotalMembersCount() {
        return membersList.size();
    }
}
