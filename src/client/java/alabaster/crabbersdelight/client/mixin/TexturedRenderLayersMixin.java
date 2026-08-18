package alabaster.crabbersdelight.client.mixin;

import net.minecraft.block.WoodType;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TexturedRenderLayers.class)
public class TexturedRenderLayersMixin {
    @Shadow
    @Final
    public static Identifier SIGNS_ATLAS_TEXTURE;

    // 原版把告示牌贴图 id 拼成 entity/signs/<名字>（minecraft 命名空间），
    // 遇到带命名空间的木材名（crabbersdelight:palm）就会解析错。
    // 在方法开头直接替换成 crabbersdelight:entity/signs/palm，图集查找才能命中。
    @Inject(method = "createSignTextureId", at = @At("HEAD"), cancellable = true)
    private static void crabbersdelight$signTextureId(WoodType type, CallbackInfoReturnable<SpriteIdentifier> cir) {
        if (type.name().indexOf(Identifier.NAMESPACE_SEPARATOR) != -1) {
            Identifier identifier = new Identifier(type.name());
            cir.setReturnValue(new SpriteIdentifier(SIGNS_ATLAS_TEXTURE,
                    new Identifier(identifier.getNamespace(), "entity/signs/" + identifier.getPath())));
        }
    }

    @Inject(method = "createHangingSignTextureId", at = @At("HEAD"), cancellable = true)
    private static void crabbersdelight$hangingSignTextureId(WoodType type, CallbackInfoReturnable<SpriteIdentifier> cir) {
        if (type.name().indexOf(Identifier.NAMESPACE_SEPARATOR) != -1) {
            Identifier identifier = new Identifier(type.name());
            cir.setReturnValue(new SpriteIdentifier(SIGNS_ATLAS_TEXTURE,
                    new Identifier(identifier.getNamespace(), "entity/signs/hanging/" + identifier.getPath())));
        }
    }
}
