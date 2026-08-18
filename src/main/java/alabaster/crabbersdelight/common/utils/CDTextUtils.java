package alabaster.crabbersdelight.common.utils;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class CDTextUtils {
    // 翻译键统一以 mod id 开头
    public static MutableText getTranslation(String key, Object... args) {
        return Text.translatable(CrabbersDelightFabric.MOD_ID + "." + key, args);
    }
}
