package me.jarva.origins_power_expansion.registry;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.apace100.origins.Origins;
import io.github.apace100.origins.OriginsClient;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.registry.KeyBindings;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeybindRegistry {
    public static KeyMapping useTernaryActivePowerKeybind;
    public static KeyMapping useQuaternaryActivePowerKeybind;
    public static KeyMapping useQuinaryActivePowerKeybind;
    public static KeyMapping useSenaryActivePowerKeybind;
    public static KeyMapping useSeptenaryActivePowerKeybind;
    public static KeyMapping useOctonaryActivePowerKeybind;
    public static KeyMapping useNonaryActivePowerKeybind;
    public static KeyMapping useDenaryActivePowerKeybind;

    public static void register() {
        if (Platform.isModLoaded("extrakeybinds")) return;
        useTernaryActivePowerKeybind = registerKeybind("key.origins.ternary_active", "ternary");
        useQuaternaryActivePowerKeybind = registerKeybind("key.origins.quaternary_active", "quaternary");
        useQuinaryActivePowerKeybind = registerKeybind("key.origins.quinary_active", "quinary");
        useSenaryActivePowerKeybind = registerKeybind("key.origins.senary_active", "senary");
        useSeptenaryActivePowerKeybind = registerKeybind("key.origins.septenary_active", "septenary");
        useOctonaryActivePowerKeybind = registerKeybind("key.origins.octonary_active", "octonary");
        useNonaryActivePowerKeybind = registerKeybind("key.origins.nonary_active", "nonary");
        useDenaryActivePowerKeybind = registerKeybind("key.origins.denary_active", "denary");
    }

    private static KeyMapping registerKeybind(String primaryKeyId, String secondaryKeyId) {
        KeyMapping keyMapping = new KeyMapping(primaryKeyId, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category." + Origins.MODID);
        OriginsClient.registerPowerKeybinding(primaryKeyId, keyMapping);
        OriginsClient.registerPowerKeybinding(secondaryKeyId, keyMapping);
        KeyBindings.registerKeyBinding(keyMapping);
        return keyMapping;
    }
}
