package rip.tilly.bedwars.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class ItemUtil {

	private ItemUtil() {
		throw new RuntimeException("Cannot instantiate a utility class.");
	}

	public static ItemStack createItem(Material material, String name) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack createItem(Material material, String name, int amount) {
		ItemStack item = new ItemStack(material, amount);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack createItem(Material material, String name, int amount, short damage) {
		ItemStack item = new ItemStack(material, amount, damage);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack createUnbreakableItem(Material material, String name, int amount, short damage) {
		ItemStack item = new ItemStack(material, amount, damage);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		item.setItemMeta(meta);

		meta.spigot().setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack createNoFlagsItem(Material material, String name, int amount, short damage) {
		ItemStack item = new ItemStack(material, amount, damage);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		item.setItemMeta(meta);

		meta.spigot().setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack createPlayerHead(Material material, String name, String playerHead, int amount, short damage) {
		ItemStack item = new ItemStack(material, amount, damage);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		item.setItemMeta(meta);

		meta.spigot().setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
		item.setItemMeta(meta);

		if (item.getType() == Material.SKULL_ITEM) {
			SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
			skullMeta.setOwner(playerHead);
			item.setItemMeta(skullMeta);

			return item;
		}

		return item;
	}

	public static ItemStack setUnbreakable(ItemStack item) {
		ItemMeta meta = item.getItemMeta();

		meta.spigot().setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack renameItem(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack reloreItem(ItemStack item, String... lores) {
		return reloreItem(ReloreType.OVERWRITE, item, lores);
	}

	public static ItemStack reEnchantItem(ItemStack itemStack, Enchantment enchantment, int level, boolean b) {
		ItemMeta meta = itemStack.getItemMeta();

		meta.addEnchant(enchantment, level, b);

		itemStack.setItemMeta(meta);
		return itemStack;
	}

	public static ItemStack reloreItem(ReloreType type, ItemStack item, String... lores) {
		ItemMeta meta = item.getItemMeta();

		List<String> lore = meta.getLore();
		if (lore == null) {
			lore = new LinkedList<>();
		}

		switch (type) {
			case APPEND:
				lore.addAll(Arrays.asList(lores));
				meta.setLore(lore);
				break;
			case PREPEND:
				List<String> nLore = new LinkedList<>(Arrays.asList(lores));
				nLore.addAll(lore);
				meta.setLore(nLore);
				break;
			case OVERWRITE:
				meta.setLore(Arrays.asList(lores));
				break;
		}

		item.setItemMeta(meta);
		return item;
	}

	public enum ReloreType {
		OVERWRITE, PREPEND, APPEND
	}
}
