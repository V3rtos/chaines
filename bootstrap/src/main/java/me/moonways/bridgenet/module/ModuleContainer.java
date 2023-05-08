package me.moonways.bridgenet.module;

import sun.misc.Unsafe;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleContainer {

    private static final Unsafe UNSAFE;

    static {
        try {
            Field unsafeInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeInstanceField.setAccessible(true);

            UNSAFE = ((Unsafe) unsafeInstanceField.get(null));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private final File moduleFolder;
    private final Map<String, ModuleData> registered = new HashMap<>();

    public ModuleContainer(File folder) {
        this.moduleFolder = new File(folder + File.separator + "modules");

        moduleFolder.mkdirs();
    }

    public Module getModuleById(String id) {
        if (!registered.containsKey(id))
            return null;

        return registered.get(id).getModule();
    }

    public ModuleData[] getRegisteredData() {
        Collection<ModuleData> mapResult = registered.values();
        ModuleData[] moduleData = new ModuleData[mapResult.size()];

        Iterator<ModuleData> iterator = mapResult.iterator();

        int index = 0;

        while (iterator.hasNext()) {
            moduleData[index] = iterator.next();
            index++;
        }

        return moduleData;
    }

    public Module[] getModules() {
        Collection<ModuleData> mapResult = registered.values();
        Module[] modules = new Module[mapResult.size()];

        Iterator<ModuleData> iterator = mapResult.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            modules[index] = iterator.next().getModule();
            index++;
        }

        return modules;
    }

    public Module[] getModules(String nameOrVersion) {
        Collection<ModuleData> mapResult = registered.values();
        Module[] modules = new Module[mapResult.size()];

        Iterator<ModuleData> iterator = mapResult.iterator();

        int index = 0;

        while (iterator.hasNext()) {
            ModuleData data = iterator.next();
            if (nameOrVersion.equals(data.getName()) || nameOrVersion.equals(data.getVersion())) {
                modules[index] = data.getModule();
                index++;
            }
        }

        return modules;
    }

    public void register(String className) throws InstantiationException {
        register(Objects.requireNonNull(getClass(className)));
    }

    public void loadModules() {
        scanModules().forEach(moduleClass -> {
            try {
                register(moduleClass);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void register(Class<?> moduleClass) throws InstantiationException {
        ModuleIdentifier annotation = moduleClass.getDeclaredAnnotation(ModuleIdentifier.class);

        if (!Module.class.isAssignableFrom(moduleClass) || annotation == null)
            return;

        String id = annotation.id(), name = annotation.name(), version = annotation.version();

        Module module = (Module) UNSAFE.allocateInstance(moduleClass);
        registered.put(id, new ModuleData(module, id, name, version));

        module.enable();
    }

    private List<Class<?>> scanModules() {
        List<Class<?>> moduleClassList = new ArrayList<>();

        Arrays.stream(Objects.requireNonNull(moduleFolder.listFiles((File dir, String name) ->
                name.endsWith(".jar")))).forEach(file ->
        {
            try (JarFile jarFile = new JarFile(file)) {
                Enumeration<JarEntry> enumeration = jarFile.entries();

                URL[] urls = {new URL("jar:file:" + file.getAbsolutePath() + "!/")};

                URLClassLoader classLoader = URLClassLoader.newInstance(urls, Module.class.getClassLoader());

                while (enumeration.hasMoreElements()) {
                    JarEntry entry = enumeration.nextElement();
                    if (entry.isDirectory() || !entry.getName().endsWith(".class"))
                        continue;

                    String className = entry.getName().substring(0, entry.getName().length() - ".class".length()).replace("/", ".");
                    Class<?> moduleClass = classLoader.loadClass(className);

                    moduleClassList.add(moduleClass);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        return moduleClassList;
    }

    private Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}