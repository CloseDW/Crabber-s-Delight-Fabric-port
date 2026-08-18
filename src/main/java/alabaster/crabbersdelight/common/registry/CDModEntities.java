package alabaster.crabbersdelight.common.registry;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import alabaster.crabbersdelight.common.entity.boat.CDBoatEntity;
import alabaster.crabbersdelight.common.entity.boat.CDChestBoatEntity;
import alabaster.crabbersdelight.common.entity.crab.CrabEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.World;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CDModEntities {
    public static final EntityType<CrabEntity> CRAB = Registry.register(
            Registries.ENTITY_TYPE, CrabbersDelightFabric.id("crab"),
            EntityType.Builder.create(CrabEntity::new, SpawnGroup.CREATURE)
                    .setDimensions(0.7f, 0.7f)
                    .build("crabbersdelight:crab"));

    public static final EntityType<CDBoatEntity> MOD_BOAT = Registry.register(
            Registries.ENTITY_TYPE, CrabbersDelightFabric.id("mod_boat"),
            EntityType.Builder.create((EntityType<CDBoatEntity> type, World world) -> new CDBoatEntity(type, world), SpawnGroup.MISC)
                    .setDimensions(1.375f, 0.5625f).build("palm_boat"));
    public static final EntityType<CDChestBoatEntity> MOD_CHEST_BOAT = Registry.register(
            Registries.ENTITY_TYPE, CrabbersDelightFabric.id("mod_chest_boat"),
            EntityType.Builder.create((EntityType<CDChestBoatEntity> type, World world) -> new CDChestBoatEntity(type, world), SpawnGroup.MISC)
                    .setDimensions(1.375f, 0.5625f).build("palm_chest_boat"));

    public static void register() {
    }
}
