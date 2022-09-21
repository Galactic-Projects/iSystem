package net.galacticprojects.common.database.migration.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.syntaxphoenix.syntaxapi.reflection.ClassCache;

import me.lauriichan.laylib.logger.ISimpleLogger;
import me.lauriichan.laylib.reflection.ClassUtil;
import me.lauriichan.laylib.reflection.JavaAccess;
import me.lauriichan.minecraft.wildcard.migration.IMigrationManager;
import me.lauriichan.minecraft.wildcard.migration.IMigrationSource;
import me.lauriichan.minecraft.wildcard.migration.Migration;
import me.lauriichan.minecraft.wildcard.migration.MigrationProcessor;
import me.lauriichan.minecraft.wildcard.migration.MigrationProvider;
import me.lauriichan.minecraft.wildcard.migration.MigrationTarget;
import me.lauriichan.minecraft.wildcard.migration.MigrationType;
import net.galacticprojects.common.util.DynamicArray;
import net.galacticprojects.common.util.Ref;
import net.galacticprojects.common.util.registry.Registry;
import net.galacticprojects.common.util.source.Resources;

public final class MigrationManager implements IMigrationManager {

    private static final Ref<MigrationManager> MIGRATION_MANAGER = Ref.of();

    public static MigrationManager manager() {
        return MIGRATION_MANAGER.get();
    }

    private final DynamicArray<MigrationTarget<?>> migrations = new DynamicArray<>();
    private final Registry<Class<?>, MigrationType<?, ?>> types = new Registry<>();

    private final ISimpleLogger logger;

    public MigrationManager(final ISimpleLogger logger, final Resources resources) {
        if(MIGRATION_MANAGER.isLocked()){
            throw new UnsupportedOperationException("Already exists");
        }
        MIGRATION_MANAGER.set(this).lock();
        this.logger = logger;
        try (BufferedReader reader = new BufferedReader(
                resources.pathIntern(MigrationProcessor.MIGRATION_RESOURCE).openReader())) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }
                final Class<?> clazz = ClassCache.getClass(line);
                if ((clazz == null) || !MigrationProvider.class.isAssignableFrom(clazz)) {
                    logger.warning("Migration class '{0}' is not a MigrationProvider!", clazz.getSimpleName());
                    continue;
                }
                final Migration migration = ClassUtil.getAnnotation(clazz, Migration.class);
                if (migration == null) {
                    logger.warning("No Migration annotation found at '{0}'!", clazz.getSimpleName());
                    continue;
                }
                final Class<?> source = migration.source();
                final Class<?> typeClazz = migration.type();
                if (source == null || typeClazz == null) {
                    logger.warning("Source or Type of migration '{0}' is not defined!", clazz.getSimpleName());
                    continue;
                }
                MigrationType<?, ?> type = types.get(typeClazz);
                if (!types.isRegistered(typeClazz)) {
                    try {
                        type = (MigrationType<?, ?>) JavaAccess.instance(typeClazz.asSubclass(MigrationType.class));
                    } catch (final Exception e) {
                        logger.warning("Failed to create instance of migration type '{0}'!", e, typeClazz.getSimpleName());
                        continue;
                    }
                    types.register(typeClazz, type);
                }
                if (!type.getSource().isAssignableFrom(source)) {
                    logger.warning("Source of migration '{0}' is not supported by migration type '{1}'!", clazz.getSimpleName(), typeClazz.getSimpleName());
                    continue;
                }
                if (!type.getMigration().isAssignableFrom(clazz)) {
                    logger.warning("MigrationProvider type of '{0}' is not supported by migration type '{1}'!", clazz.getSimpleName(), typeClazz.getSimpleName());
                    continue;
                }
                MigrationProvider provider;
                try {
                    provider = (MigrationProvider) JavaAccess.instance(clazz.asSubclass(MigrationProvider.class));
                } catch (final Exception e) {
                    logger.warning("Failed to create instance of migration '{0}'!", e, clazz.getSimpleName());
                    continue;
                }
                migrations.add(new MigrationTarget<>(migration, provider));
            }
        } catch (final IOException exp) {
            throw new IllegalStateException("Failed to load migrations!", exp);
        }
    }

    public <T extends MigrationType<?, ?>> T getType(final Class<T> type) {
        if (type == null) {
            return null;
        }
        if (!types.isRegistered(type)) {
            T migrationType;
            try {
                Constructor<?> constructor = ClassUtil.getConstructor(type, ISimpleLogger.class);
                if(constructor != null){
                    migrationType = type.cast(JavaAccess.instance(constructor, logger));
                } else {
                    migrationType = type.cast(JavaAccess.instance(type));
                }
            } catch (final Exception e) {
                logger.warning("Failed to create instance of migration type '{0}'!", e, type.getSimpleName());
                return null;
            }
            types.register(type, migrationType);
            return migrationType;
        }
        return type.cast(types.get(type));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M extends MigrationProvider> List<MigrationTarget<M>> getTargets(final Class<MigrationType<?, M>> type) {
        final ArrayList<MigrationTarget<M>> list = new ArrayList<>();
        for (int index = 0; index < migrations.length(); index++) {
            final MigrationTarget<?> target = migrations.get(index);
            if (!target.getPoint().type().equals(type)) {
                continue;
            }
            list.add((MigrationTarget<M>) target);
        }
        return list;
    }

    public static <S extends IMigrationSource, T extends MigrationType<S, ?>> void migrate(final IMigrationSource source,
        final Class<T> type) throws Exception {
        final MigrationManager manager = MIGRATION_MANAGER.get();
        final T migration = manager.getType(Objects.requireNonNull(type));
        if (migration == null) {
            throw new IllegalStateException("Can't find migration type '" + type.getSimpleName() + "'!");
        }
        if (!migration.getSource().isAssignableFrom(source.getClass())) {
            throw new IllegalArgumentException("migration source '" + source.getClass().getSimpleName()
                + "' is not compatible with migration type '" + type.getSimpleName() + "'!");
        }
        migration.migrate(manager, migration.getSource().cast(source));
    }

}