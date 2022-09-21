package me.lauriichan.minecraft.wildcard.migration;

import java.util.List;

public abstract class MigrationType<T extends IMigrationSource, M extends MigrationProvider> {

    private final Class<T> source;
    private final Class<M> migration;

    public MigrationType(final Class<T> source, final Class<M> migration) {
        this.source = source;
        this.migration = migration;
    }

    public final Class<T> getSource() {
        return source;
    }

    public final Class<M> getMigration() {
        return migration;
    }

    @SuppressWarnings("unchecked")
    protected final List<MigrationTarget<M>> getTargets(final IMigrationManager manager) {
        return manager.getTargets((Class<MigrationType<?, M>>) getClass());
    }

    public abstract void migrate(IMigrationManager manager, T source) throws Exception;
}
