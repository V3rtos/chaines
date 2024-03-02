package me.moonways.bridgenet.metrics.settings;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class MetricsSettings {

    private final ImagesStore imagesStore;
    private final ImageDimensions dimensions;

    @Getter
    @ToString
    @EqualsAndHashCode
    @RequiredArgsConstructor
    public static final class ImagesStore {

        private final String folderName;

        private final boolean enabled;
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @RequiredArgsConstructor
    public static final class ImageDimensions {

        private final int width;
        private final int height;
    }
}
