package alabaster.crabbersdelight.common.item;

import alabaster.crabbersdelight.common.entity.boat.CDBoatEntity;
import alabaster.crabbersdelight.common.entity.boat.CDChestBoatEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.function.Predicate;

public class CDBoatItem extends Item {
    private static final Predicate<Entity> ENTITY_PREDICATE = EntityPredicates.EXCEPT_SPECTATOR.and(Entity::canHit);
    private final CDBoatEntity.Type type;
    private final boolean hasChest;

    public CDBoatItem(boolean hasChest, CDBoatEntity.Type type, Item.Settings settings) {
        super(settings);
        this.hasChest = hasChest;
        this.type = type;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        BlockHitResult hitResult = Item.raycast(world, player, RaycastContext.FluidHandling.ANY);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return TypedActionResult.pass(itemStack);
        }
        Vec3d vec3d = player.getRotationVec(1.0F);
        List<Entity> list = world.getOtherEntities(player,
                player.getBoundingBox().stretch(vec3d.multiply(5.0D)).expand(1.0D), ENTITY_PREDICATE);
        if (!list.isEmpty()) {
            Vec3d vec3d2 = player.getEyePos();
            for (Entity entity : list) {
                Box box = entity.getBoundingBox().expand(entity.getTargetingMargin());
                if (box.contains(vec3d2)) {
                    return TypedActionResult.pass(itemStack);
                }
            }
        }

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BoatEntity boat = this.getBoat(world, hitResult);
            if (boat instanceof CDChestBoatEntity chestBoat) {
                chestBoat.setVariant(this.type);
            } else if (boat instanceof CDBoatEntity) {
                ((CDBoatEntity) boat).setVariant(this.type);
            }
            boat.setYaw(player.getYaw());
            if (!world.isSpaceEmpty(boat, boat.getBoundingBox())) {
                return TypedActionResult.fail(itemStack);
            }
            if (!world.isClient) {
                world.spawnEntity(boat);
                world.emitGameEvent(player, GameEvent.ENTITY_PLACE, hitResult.getPos());
                if (!player.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }
            }
            player.incrementStat(Stats.USED.getOrCreateStat(this));
            return TypedActionResult.success(itemStack, world.isClient);
        }
        return TypedActionResult.pass(itemStack);
    }

    private BoatEntity getBoat(World world, BlockHitResult hitResult) {
        return this.hasChest
                ? new CDChestBoatEntity(world, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z)
                : new CDBoatEntity(world, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z);
    }
}
