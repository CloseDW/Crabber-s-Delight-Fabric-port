package alabaster.crabbersdelight.common.entity.boat;

import alabaster.crabbersdelight.common.registry.CDModEntities;
import alabaster.crabbersdelight.common.registry.CDModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class CDChestBoatEntity extends ChestBoatEntity {
    private static final TrackedData<Integer> DATA_ID_TYPE =
            DataTracker.registerData(BoatEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public CDChestBoatEntity(EntityType<? extends ChestBoatEntity> entityType, World world) {
        super(entityType, world);
    }

    public CDChestBoatEntity(World world, double x, double y, double z) {
        this(CDModEntities.MOD_CHEST_BOAT, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    public Item asItem() {
        switch (getModVariant()) {
            case PALM -> {
                return CDModItems.PALM_CHEST_BOAT;
            }
        }
        return super.asItem();
    }

    public void setVariant(CDBoatEntity.Type variant) {
        this.dataTracker.set(DATA_ID_TYPE, variant.ordinal());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(DATA_ID_TYPE, CDBoatEntity.Type.PALM.ordinal());
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
            this.setVariant(CDBoatEntity.Type.byName(nbt.getString("Type")));
        }
    }

    public CDBoatEntity.Type getModVariant() {
        return CDBoatEntity.Type.byId(this.dataTracker.get(DATA_ID_TYPE));
    }
}
