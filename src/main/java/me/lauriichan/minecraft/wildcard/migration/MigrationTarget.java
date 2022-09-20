package me.lauriichan.minecraft.wildcard.migration;

public final class MigrationTarget<M extends MigrationProvider> {

    private final Migration point;
    private final M migration;

    public MigrationTarget(final Migration point, final M migration) {
        this.migration = migration;
        this.point = point;
    }

    public Migration getPoint() {
        return point;
    }

    public M getMigration() {
        return migration;
    }
}
