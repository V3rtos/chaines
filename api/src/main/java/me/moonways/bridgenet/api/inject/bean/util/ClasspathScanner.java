package me.moonways.bridgenet.api.inject.bean.util;

import lombok.experimental.UtilityClass;
import me.moonways.bridgenet.api.inject.bean.BeanException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@UtilityClass
public class ClasspathScanner {

    public Set<Class<?>> getAllClasses() throws InterruptedException {
        List<URL> urls = Arrays.asList(((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs());
        List<File> files = urls.stream().map(url -> new File(url.getFile())).collect(Collectors.toList());

        int chunkSize = 1000;
        List<List<File>> chunks = new ArrayList<>();
        for (int i = 0; i < files.size(); i += chunkSize) {
            chunks.add(files.subList(i, Math.min(files.size(), i + chunkSize)));
        }

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Set<Class<?>>>> futures = new ArrayList<>();

        for (List<File> chunk : chunks) {
            futures.add(executor.submit(() -> {
                Set<Class<?>> classes = new HashSet<>();
                for (File file : chunk) {
                    if (file.isDirectory()) {
                        classes.addAll(findClassesInDirectory(file, file.getAbsolutePath()));
                    }
                }
                return classes;
            }));
        }

        executor.shutdown();
        Set<Class<?>> allClasses = Collections.synchronizedSet(new HashSet<>());
        for (Future<Set<Class<?>>> future : futures) {
            try {
                allClasses.addAll(future.get());
            } catch (ExecutionException e) {
                throw new BeanException(e);
            }
        }

        return allClasses;
    }

    private Set<Class<?>> findClassesInDirectory(File directory, String rootPath) {
        Set<Class<?>> classes = new HashSet<>();
        try {
            String rootPathWithSlash = rootPath.endsWith(File.separator) ? rootPath : rootPath + File.separator;
            Files.walk(directory.toPath()).filter(Files::isRegularFile).forEach(path -> {
                String className = path.toString().replace(rootPathWithSlash, "").replace(File.separator, ".").replace(".class", "");
                try {
                    classes.add(Class.forName(className, false, Thread.currentThread().getContextClassLoader()));
                } catch (ClassNotFoundException | NoClassDefFoundError | UnsatisfiedLinkError |
                         IllegalStateException e) {
                    // ignored
                }
            });
        } catch (IOException e) {
            throw new BeanException(e);
        }
        return classes;
    }
}
