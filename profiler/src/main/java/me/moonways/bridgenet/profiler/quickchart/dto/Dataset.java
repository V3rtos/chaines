package me.moonways.bridgenet.profiler.quickchart.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@ToString
public class Dataset {

    @SerializedName("label")
    private final String label;

    @SerializedName("data")
    private final List<Number> data;

    @SerializedName("fill")
    private final boolean hasLineForeground;
}
