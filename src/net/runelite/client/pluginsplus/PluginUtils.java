package net.runelite.client.pluginsplus;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.runeliteplus.Utils;
import com.runeliteplus.core.attach.RLCallbacks;
import net.runelite.client.RuneLite;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginInstantiationException;
import net.runelite.client.plugins.config.ConfigPlugin;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class PluginUtils {

    private static PluginUtils instance;

    public static PluginUtils getInstance() {
        if (instance == null)
            instance = new PluginUtils();
        return instance;
    }

    public HashMap<Class<?>, Plugin> loadedDeveloperPlugins = new HashMap<>();
    public ConfigPlugin configPlugin = null;

    public void enableDevBoolean() throws NoSuchFieldException, IllegalAccessException {
        Field field = RLCallbacks.getInstance().getPluginManager().getClass().getDeclaredField("developerMode");
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(RLCallbacks.getInstance().getPluginManager(), true);
    }

    public void enableDevTools() throws PluginInstantiationException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, IOException {
        System.out.println("Enabling developer plugins...");

        List<Plugin> scannedPlugins = new ArrayList<>();
        ClassPath classPath = ClassPath.from(PluginUtils.class.getClassLoader());
        ImmutableSet<ClassPath.ClassInfo> classes = classPath.getTopLevelClassesRecursive("net.runelite.client.plugins");

        for (ClassPath.ClassInfo plugInfo : classes) {
            Class<?> plug = plugInfo.load();

            if (plug.getSuperclass() != Plugin.class)
                continue;

            if (plug.getName().toLowerCase().contains("configplugin")) {
                Class<ConfigPlugin> conf = (Class<ConfigPlugin>)plug;
                configPlugin = conf.newInstance();
            }

            PluginDescriptor desc = plug.getAnnotation(PluginDescriptor.class);

            if (desc == null || !desc.developerPlugin())
                continue;

            System.out.println("Found a developer plugin: " + plug.getName());

            PluginDescriptor newDesc = new PluginDescriptor() {

                @Override
                public Class<? extends Annotation> annotationType() {
                    return desc.annotationType();
                }

                @Override
                public String name() {
                    return desc.name().concat("_unlocked");
                }

                @Override
                public String description() {
                    return desc.description();
                }

                @Override
                public String[] tags() {
                    return desc.tags();
                }

                @Override
                public boolean enabledByDefault() {
                    return true;
                }

                @Override
                public boolean hidden() {
                    return false;
                }

                @Override
                public boolean developerPlugin() {
                    return false;
                }

                @Override
                public boolean loadWhenOutdated() {
                    return false;
                }
            };


            Method method = Class.class.getDeclaredMethod("annotationData", null);
            method.setAccessible(true);
            //Since AnnotationData is a private class we cannot create a direct reference to it. We will have to
            //manage with just Object
            Object annotationData = method.invoke(plug, null);

            //System.out.println(">Found annotation data: " + annotationData.hashCode());

            Field field = annotationData.getClass().getDeclaredField("annotations");
            field.setAccessible(true);
            Map<Class<? extends Annotation>, Annotation> annotations = (Map<Class<? extends Annotation>, Annotation>) field.get(annotationData);

            //System.out.println(">Found annotation list: " + annotations.hashCode());

            Object old_data = annotations.remove(PluginDescriptor.class);
            annotations.put(PluginDescriptor.class, newDesc);


            Class<Plugin> pluginC = (Class<Plugin>) plug;
            Plugin pluginClass = pluginC.newInstance();


            PluginDependency[] pluginDependencies = pluginC.getAnnotationsByType(PluginDependency.class);
            List<Plugin> deps = new ArrayList<>();
            for (PluginDependency pluginDependency : pluginDependencies) {
                Optional<Plugin> dependency = scannedPlugins.stream().filter(p -> p.getClass() == pluginDependency.value()).findFirst();
                if (!dependency.isPresent()) {
                    throw new PluginInstantiationException("Unmet dependency for " + pluginC.getSimpleName() + ": " + pluginDependency.value().getSimpleName());
                }
                deps.add(dependency.get());
            }

            Module pluginModule = (Binder binder) ->
            {
                binder.bind(pluginC).toInstance(pluginClass);
                binder.install(pluginClass);
                for (Plugin p : deps) {
                    Module p2 = (Binder binder2) ->
                    {
                        binder2.bind((Class<Plugin>) p.getClass()).toInstance(p);
                        binder2.install(p);
                    };
                    binder.install(p2);
                }
            };

            Injector pluginInjector = RuneLite.getInjector().createChildInjector(pluginModule);
            pluginInjector.injectMembers(pluginClass);
            Utils.getField(pluginClass.getClass(), "injector").set(pluginClass, pluginInjector);

            loadedDeveloperPlugins.put(pluginC, pluginClass);
            RLCallbacks.getInstance().getPluginManager().startPlugin(pluginClass);
        }
    }
}
