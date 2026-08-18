package alabaster.crabbersdelight.common.entity.boat;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class CDBoatModelLayers {
    public static final EntityModelLayer PALM_BOAT_LAYER = new EntityModelLayer(
            new Identifier(CrabbersDelightFabric.MOD_ID, "boat/palm"), "main");
    public static final EntityModelLayer PALM_CHEST_BOAT_LAYER = new EntityModelLayer(
            new Identifier(CrabbersDelightFabric.MOD_ID, "chest_boat/palm"), "main");
}
