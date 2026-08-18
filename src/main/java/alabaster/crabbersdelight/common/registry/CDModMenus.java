package alabaster.crabbersdelight.common.registry;

import alabaster.crabbersdelight.CrabbersDelightFabric;
import alabaster.crabbersdelight.common.block.container.CrabTrapMenu;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public final class CDModMenus {
    public static final ScreenHandlerType<CrabTrapMenu> CRAB_TRAP_MENU =
            ScreenHandlerRegistry.registerExtended(CrabbersDelightFabric.id("crab_trap_menu"), CrabTrapMenu::new);

    public static void register() {
    }
}
