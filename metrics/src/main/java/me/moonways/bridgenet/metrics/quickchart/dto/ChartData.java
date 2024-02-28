package me.moonways.bridgenet.metrics.quickchart.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class ChartData {

    @SerializedName("type")
    private final String type;

    @SerializedName("data")
    private final Data data;

    @SerializedName("options")
    private final Options options;

    @Getter
    @Builder
    @ToString
    public static class Data {

        @SerializedName("labels")
        private final List<String> labels;

        @SerializedName("datasets")
        private final List<Dataset> datasets;
    }
}
