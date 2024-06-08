package me.moonways.bridgenet.client.api.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class TitleDto {

    private String title;
    private String subtitle;

    private int fadeIn, stay, fadeOut;
}
