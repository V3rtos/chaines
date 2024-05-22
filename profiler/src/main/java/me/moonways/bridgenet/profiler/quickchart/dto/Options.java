package me.moonways.bridgenet.profiler.quickchart.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Options {

    @SerializedName("title")
    private final Title title;

    @Getter
    @Builder
    @ToString
    public static class Title {

        @SerializedName("title")
        private final boolean isDisplay;

        @SerializedName("text")
        private final String text;
    }
}
