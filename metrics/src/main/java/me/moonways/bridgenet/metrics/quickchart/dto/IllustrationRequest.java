package me.moonways.bridgenet.metrics.quickchart.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class IllustrationRequest {

    @SerializedName("backgroundColor")
    private final String backgroundColor;

    @SerializedName("format")
    private final String imageFormat;

    @SerializedName("width")
    private final int imageWidth;
    @SerializedName("height")
    private final int imageHeight;

    @SerializedName("chart")
    private final ChartData chart;
}
