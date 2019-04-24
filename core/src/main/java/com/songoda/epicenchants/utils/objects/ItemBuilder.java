package com.songoda.epicenchants.utils.objects;

import com.songoda.epicenchants.objects.Placeholder;
import com.songoda.epicenchants.utils.single.ConfigParser;
import com.songoda.epicenchants.utils.single.GeneralUtils;
import com.songoda.epicenchants.wrappers.EnchantmentWrapper;
import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.stream.Collectors;

import static com.songoda.epicenchants.utils.single.GeneralUtils.color;

public class ItemBuilder {

    private final ItemStack item;
    private ItemMeta meta;
    private final Set<EnchantmentWrapper> enchantmentWrappers;

    /*
     * Constructors:
     */
    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(Material material, byte data) {
        this(new ItemStack(material, 1, data));
    }

    public ItemBuilder(Material material, int amount, byte data) {
        this(new ItemStack(material, amount, data));
    }

    public ItemBuilder(ConfigurationSection section, Player player, Placeholder... placeholders) {
        this(section, placeholders);

        if (item.getType() == Material.LEGACY_SKULL_ITEM) {
            ((SkullMeta) item.getItemMeta()).setOwningPlayer(player);
        }
    }

    public ItemBuilder(ConfigurationSection section, Placeholder... placeholders) {
        this(Material.valueOf(section.getString("material")), (byte) (section.contains("data") ? section.getInt("data") : 0));

        if (section.contains("enchants")) {
            section.getStringList("enchants").stream()
                    .map(ConfigParser::parseEnchantmentWrapper)
                    .forEach(this::addEnchantWrapper);
        }

        if (section.contains("display-name")) {
            String displayName = section.getString("display-name");
            for (Placeholder placeholder : placeholders) {
                displayName = displayName.replace(placeholder.getPlaceholder(), placeholder.getToReplace().toString());
            }
            name(color(displayName));
        }

        if (section.contains("lore")) {
            List<String> lore = section.getStringList("lore");
            outer:
            for (int i = 0; i < lore.size(); i++) {
                String string = lore.get(i);

                for (Placeholder placeholder : placeholders) {
                    if (placeholder.getToReplace() instanceof ArrayList && string.contains(placeholder.getPlaceholder())) {
                        lore.remove(i);
                        lore.addAll(i, (ArrayList<String>) placeholder.getToReplace());
                        continue outer;
                    } else {
                        string = string.replace(placeholder.getPlaceholder(), placeholder.getToReplace().toString());
                    }
                }

                lore.set(i, string);
            }
            lore(lore.stream().map(GeneralUtils::color).collect(Collectors.toList()));
        }
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
        this.enchantmentWrappers = new HashSet<>();
    }

    /*
     * Meta:
     */
    public boolean hasMeta() {
        return getMeta() != null;
    }

    public ItemBuilder meta(ItemMeta meta) {
        this.meta = meta;
        return this;
    }

    /*
     * Name:
     */
    public boolean hasName() {
        return meta.hasDisplayName();
    }

    public ItemBuilder name(String name) {
        meta.setDisplayName(name);
        return this;
    }

    /*
     * Lore:
     */
    public boolean hasLore() {
        return meta.hasLore();
    }

    public ItemBuilder lore(String... lore) {
        return lore(Arrays.asList(lore));
    }

    public ItemBuilder lore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }

    public ItemBuilder addLore(List<String> lore) {
        if (!meta.hasLore()) {
            meta.setLore(lore);
            return this;
        }

        List<String> toAdd = meta.getLore();
        toAdd.addAll(lore);
        meta.setLore(toAdd);
        return this;
    }

    public ItemBuilder removeLore(String string) {
        if (meta == null || !meta.hasLore()) {
            return this;
        }

        meta.setLore(meta.getLore().stream().filter(s -> !s.startsWith(string)).collect(Collectors.toList()));
        return this;
    }

    public ItemBuilder removeLore(int index) {
        if (!meta.hasLore()) {
            return this;
        }

        List<String> lore = meta.getLore();

        if (index >= lore.size()) {
            return this;
        }

        lore.remove(index);

        meta.setLore(lore);
        return this;
    }

    public ItemBuilder addLore(String... lore) {
        return addLore(Arrays.asList(lore));
    }

    /*
     * Enchantments:
     */
    public boolean hasEnchants() {
        return meta.hasEnchants();
    }

    public boolean hasEnchant(Enchantment enchantment) {
        return meta.hasEnchant(enchantment);
    }

    public boolean hasConflictingEnchant(Enchantment enchantment) {
        return meta.hasConflictingEnchant(enchantment);
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder removeEnchant(Enchantment enchantment) {
        meta.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilder addEnchantWrapper(EnchantmentWrapper enchantmentWrapper) {
        enchantmentWrappers.add(enchantmentWrapper);
        return this;
    }

    /*
     * Skulls:
     */
    public boolean hasSkullOwner() {
        return ((SkullMeta) meta).hasOwner();
    }

    public String getSkullOwner() {
        return ((SkullMeta) meta).getOwner();
    }

    public ItemBuilder skullOwner(String owner) {
        item.setDurability((short) 3);
        ((SkullMeta) meta).setOwner(owner);
        return this;
    }

    public ItemBuilder durability(int durability) {
        item.setDurability((short) durability);
        return this;
    }

    /*
     * Flags:
     */
    public boolean hasFlag(ItemFlag flag) {
        return meta.hasItemFlag(flag);
    }

    public ItemBuilder addFlags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder removeFlags(ItemFlag... flags) {
        meta.removeItemFlags(flags);
        return this;
    }

    public ItemBuilder unbreakable() {
        return unbreakable(true);
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        meta.spigot().setUnbreakable(unbreakable);
        return this;
    }

    public NBTItem nbt() {
        return new NBTItem(build());
    }

    /*
     * Build the ItemStack.
     */
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack buildWithWrappers(int level, Player user, LivingEntity opponent) {
        item.setItemMeta(meta);
        enchantmentWrappers.forEach(enchant -> item.addUnsafeEnchantment(enchant.getEnchantment(), enchant.getAmplifier(level, user, opponent)));
        return item;
    }

    public Map<Enchantment, Integer> getEnchants() {
        return meta.getEnchants();
    }

    public Set<ItemFlag> getFlags() {
        return meta.getItemFlags();
    }

    public List<String> getLore() {
        return meta.getLore();
    }

    public ItemMeta getMeta() {
        return meta;
    }

    /*
     * NBT
     */

    public String getName() {
        return meta.getDisplayName();
    }

    /*
     * Unbreakability:
     */
    public boolean isUnbreakable() {
        return meta.spigot().isUnbreakable();
    }
}
