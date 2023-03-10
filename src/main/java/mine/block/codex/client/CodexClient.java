package mine.block.codex.client;

import com.google.gson.JsonObject;
import mine.block.codex.CodexUtils;
import mine.block.codex.config.CodexConfig;
import mine.block.codex.math.*;
import mine.block.codex.search.SearchManager;
import mine.block.codex.ui.QuicksearchScreen;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.lang.JLang;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static mine.block.codex.math.MathRegistry.registerFunction;

@Environment(EnvType.CLIENT)
public class CodexClient implements ClientModInitializer {
    private static final KeyBinding OPEN_QUICKSEARCH_KEY = new KeyBinding("open_quicksearch", GLFW.GLFW_KEY_N, "quicksearch");
    public static Consumer<Item> OPEN_RECIPE_SUPPLIER;
    public static CodexConfig CONFIG = CodexConfig.createAndLoad();
    public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create("codex:lang");
    private static HashMap<String, JsonObject> LANGUAGE_CACHE_COMPARABLE = new HashMap<>();

    @Override
    public void onInitializeClient() {

        KeyBindingHelper.registerKeyBinding(OPEN_QUICKSEARCH_KEY);

        ClientTickEvents.START_WORLD_TICK.register(world -> {
            if(OPEN_QUICKSEARCH_KEY.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new QuicksearchScreen());
            }
        });

        RRPCallback.AFTER_VANILLA.register(resources -> {
            SearchManager.refresh();
            var translations = CodexUtils.getCodexLanguageFiles();
            if(translations != LANGUAGE_CACHE_COMPARABLE) {
                LANGUAGE_CACHE_COMPARABLE = translations;
                translations.forEach((lang, entries) -> {
                    JLang langFile = new JLang();
                    entries.asMap().forEach((key, val) -> langFile.entry(key, val.getAsString()));
                    RESOURCE_PACK.addLang(new Identifier("codex", lang), langFile);
                });
            }

            resources.add(RESOURCE_PACK);
        });

        registerFunction("stacksof", new ToStackFunction());
        registerFunction("itemsof", new FromStackFunction());
        registerFunction("shulkersof", new StackToShulkerFunction());
        registerFunction("stacksin", new ShulkerToStackFunction());
        registerFunction("secsin", new SecToTickFunction());
        registerFunction("ticksin", new TickToSecFunction());


        MathRegistry.CUSTOM_CONSTANTS.put("STACK", 64);
        MathRegistry.CUSTOM_CONSTANTS.put("SHULKER", 1920);
        MathRegistry.CUSTOM_CONSTANTS.put("DAY", 72 * 24000);
        MathRegistry.CUSTOM_CONSTANTS.put("HOUR", (72 * 24000) / 24);
        MathRegistry.CUSTOM_CONSTANTS.put("MIN", (72 * 24000) / 24 / 60);
        MathRegistry.CUSTOM_CONSTANTS.put("SEC", (72 * 24000) / 24 / 60 / 60);
        MathRegistry.CUSTOM_CONSTANTS.put("MS", (72 * 24000) / 24 / 60 / 60 / 1000);
        MathRegistry.CUSTOM_CONSTANTS.put("MC_DAY", 24000);
        MathRegistry.CUSTOM_CONSTANTS.put("MC_HOUR", 24000 / 24);
        MathRegistry.CUSTOM_CONSTANTS.put("MC_MIN", 24000 / 24 / 60);
        MathRegistry.CUSTOM_CONSTANTS.put("MC_SEC", 24000 / 24 / 60 / 60);
        MathRegistry.CUSTOM_CONSTANTS.put("MC_MS", 24000 / 24 / 60 / 1000);

        MathRegistry.CUSTOM_CONSTANTS.putAll(Map.of());
    }
}
