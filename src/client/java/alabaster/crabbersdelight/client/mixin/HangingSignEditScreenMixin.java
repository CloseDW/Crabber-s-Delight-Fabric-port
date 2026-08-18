package alabaster.crabbersdelight.client.mixin;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.client.gui.screen.ingame.HangingSignEditScreen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(HangingSignEditScreen.class)
public abstract class HangingSignEditScreenMixin extends AbstractSignEditScreen {
    private HangingSignEditScreenMixin(SignBlockEntity blockEntity, boolean filtered, boolean bl) {
        super(blockEntity, filtered, bl);
    }

    // 编辑界面的背景贴图也是写死的：textures/gui/hanging_signs/<名字>.png（minecraft 命名空间）。
    // 带命名空间的木材名会被 Identifier 拆错，这里把参数改成 crabbersdelight:textures/gui/hanging_signs/palm.png。
    // 注意用 @ModifyArg 而不是 @Redirect(NEW)：Mixin 0.8 的 NEW 注入点不支持构造函数描述符过滤。
    @ModifyArg(method = "<init>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;<init>(Ljava/lang/String;)V"))
    private String crabbersdelight$editScreenTexture(String path) {
        if (signType.name().indexOf(Identifier.NAMESPACE_SEPARATOR) != -1) {
            Identifier identifier = new Identifier(signType.name());
            return identifier.getNamespace() + ":textures/gui/hanging_signs/" + identifier.getPath() + ".png";
        }
        return path;
    }
}
