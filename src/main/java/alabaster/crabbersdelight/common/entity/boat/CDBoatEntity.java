package alabaster.crabbersdelight.common.entity.boat;

import alabaster.crabbersdelight.common.registry.CDModBlocks;
import alabaster.crabbersdelight.common.registry.CDModEntities;
import alabaster.crabbersdelight.common.registry.CDModItems;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class CDBoatEntity extends BoatEntity {
    private static final TrackedData<Integer> DATA_ID_TYPE =
            DataTracker.registerData(BoatEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public CDBoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
    }

    public CDBoatEntity(World world, double x, double y, double z) {
        this(CDModEntities.MOD_BOAT, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    public Item asItem() {
        return switch (getModVariant()) {
            case PALM -> CDModItems.PALM_BOAT;
        };
    }

    public void setVariant(Type variant) {
        this.dataTracker.set(DATA_ID_TYPE, variant.ordinal());
    }

    public Type getModVariant() {
        return Type.byId(this.dataTracker.get(DATA_ID_TYPE));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(DATA_ID_TYPE, Type.PALM.ordinal());
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Type", this.getModVariant().asString());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Type", 8)) {
            this.setVariant(Type.byName(nbt.getString("Type")));
        }
    }

    public enum Type {
        PALM(CDModBlocks.PALM_PLANKS, "palm");

        private final String name;
        private final Block planks;
        private static final Type[] BY_ID = values();

        Type(Block planks, String name) {
            this.name = name;
            this.planks = planks;
        }

        public String asString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }

        public Block getPlanks() {
            return this.planks;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static Type byId(int id) {
            return BY_ID[id % BY_ID.length];
        }

        public static Type byName(String name) {
            for (Type type : values()) {
                if (type.name.equals(name)) return type;
            }
            return PALM;
        }
    }
}
