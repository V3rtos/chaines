package me.moonways.bridgenet.dependencyinjection.scanner;

import me.moonways.bridgenet.dependencyinjection.Depend;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Depend
public final class ResourceClasspathScanner {

    public ResourceClasspathScannerResponse find(String packageName) {
        System.out.println("[ResourceClasspathScanner] Scanning package: " + packageName);
        String path = packageName.replaceAll("[.]", "/");

        InputStream localInputStream = getClass().getResourceAsStream(path);

        if (localInputStream == null)
            localInputStream = getClass().getResourceAsStream("/" + path);

        if (localInputStream == null)
            return new ResourceClasspathScannerResponse(packageName, new HashSet<>());

        InputStreamReader inputStreamReader = new InputStreamReader(localInputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        Stream<String> linesStream = reader.lines();
        Set<Class<?>> classesSet = new HashSet<>();

        linesStream.forEach(resourceLine -> {

            if (!resourceLine.contains(".")) {
                classesSet.addAll(find(packageName + "." + resourceLine).getClasses());
            }
            else if (resourceLine.endsWith(".class")) {
                classesSet.add(getClass(resourceLine, packageName));
            }
        });

        return new ResourceClasspathScannerResponse(packageName, classesSet);
    }

    private Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
