package me.moonways.bridgenet.profiler;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class ProfilerSettings {

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
