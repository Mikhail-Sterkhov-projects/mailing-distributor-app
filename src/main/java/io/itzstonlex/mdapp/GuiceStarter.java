package io.itzstonlex.mdapp;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public final class GuiceStarter {

    private final Stage stage;
    private final List<Class<? extends Module>> modulesClasses;
    private final List<Module> modulesImpls;

    public Injector start() {
        try {
            List<Module> modulesToInstances = this.modulesToInstances();
            return Guice.createInjector(Optional.ofNullable(this.stage).orElse(Stage.PRODUCTION), modulesToInstances);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }

    private List<Module> modulesToInstances() throws ReflectiveOperationException {
        ArrayList<Module> instances = new ArrayList<>();
        HashSet<Class<? extends Module>> distinctModulesClasses = new HashSet<>(this.modulesClasses);

        for (Class<? extends Module> moduleClass : distinctModulesClasses) {
            instances.add(moduleClass.getConstructor().newInstance());
        }

        instances.addAll(Optional.ofNullable(this.modulesImpls).orElse(Collections.emptyList()));
        return instances;
    }

    public static GuiceStarterBuilder builder() {
        return new GuiceStarterBuilder();
    }

    private GuiceStarter(Stage stage, List<Class<? extends Module>> modulesClasses, List<Module> modulesImpls) {
        this.stage = stage;
        this.modulesClasses = modulesClasses;
        this.modulesImpls = modulesImpls;
    }

    public static class GuiceStarterBuilder {
        private Stage stage;
        private List<Class<? extends Module>> modulesClasses;
        private List<Module> modulesImpls;

        GuiceStarterBuilder() {
        }

        public GuiceStarterBuilder stage(Stage stage) {
            this.stage = stage;
            return this;
        }

        public GuiceStarterBuilder modulesClasses(List<Class<? extends Module>> modulesClasses) {
            this.modulesClasses = modulesClasses;
            return this;
        }

        public GuiceStarterBuilder modulesImpls(List<Module> modulesImpls) {
            this.modulesImpls = modulesImpls;
            return this;
        }

        public GuiceStarter build() {
            return new GuiceStarter(this.stage, this.modulesClasses, this.modulesImpls);
        }

        public String toString() {
            String var10000 = String.valueOf(this.stage);
            return "GuiceStarter.GuiceStarterBuilder(stage=" + var10000 + ", modulesClasses=" + this.modulesClasses + ", modulesImpls=" + this.modulesImpls + ")";
        }
    }
}
