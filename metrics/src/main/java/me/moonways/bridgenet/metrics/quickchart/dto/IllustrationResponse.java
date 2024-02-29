package me.moonways.bridgenet.metrics.quickchart.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class IllustrationResponse {

    @SerializedName("success")
    private final boolean isSuccess;

    @SerializedName("url")
    private final String imageURL;
}
