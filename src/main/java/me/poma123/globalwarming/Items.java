package me.poma123.globalwarming;

import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Items {
    public static final SlimefunItemStack THERMOMETER = new SlimefunItemStack("THERMOMETER", "24fa511f2628d56a8c8691ac5df3e3f82716384514a5ea5bae3eda86f48ad6e1", "&eThermometer", "", "&eRight Click &7to switch between", "&7temperature types");
    public static final SlimefunItemStack AIR_QUALITY_METER = new SlimefunItemStack("AIR_QUALITY_METER", "24fa511f2628d56a8c8691ac5df3e3f82716384514a5ea5bae3eda86f48ad6e1", "&bAir Quality Meter");
    public static final SlimefunItemStack EMPTY_CANISTER = new SlimefunItemStack("EMPTY_CANISTER", Material.GLASS_BOTTLE, "&7Empty Canister");
    public static final SlimefunItemStack AIR_COMPRESSOR = new SlimefunItemStack("AIR_COMPRESSOR", Material.DISPENSER, "&bAir Compressor", "", "&aCompresses CO2 into a canister");
    public static final SlimefunItemStack CO2_CANISTER;

    static {
        ItemStack item = new ItemStack(Material.POTION);
        ItemMeta meta = item.getItemMeta();
        ((PotionMeta) meta).setColor(Color.fromRGB(61, 61, 61));
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(meta);

        CO2_CANISTER = new SlimefunItemStack("CO2_CANISTER", item, "&7CO2 Canister", "", "&8&oCompressed CO2");
    }
}