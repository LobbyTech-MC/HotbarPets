package io.github.thebusybiscuit.hotbarpets;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HotbarPet extends SlimefunItem {

	private static final long MESSAGE_DELAY = 10_000;

	private static final Map<UUID, Long> messageDelay = new HashMap<>();

	private final ItemStack food;

	public HotbarPet(Category category, SlimefunItemStack item, ItemStack food, ItemStack[] recipe) {
		super(category, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
		this.food = food;
	}

	public ItemStack getFavouriteFood() {
		return this.food;
	}

	/**
	 * Tries to consume the pets favourite food, if this was successful it will return true. Otherwise false.
	 * If it fails to consume food it will send a message to the player, this is on a delay as to not spam the player.
	 * The default delay is {@link #MESSAGE_DELAY}.
	 *
	 * @param player  The {@link Player} who owns this pet
	 * @return If the food consumption was successful
	 */
	public boolean tryToConsumeFood(Player player) {
		if (!player.getInventory().containsAtLeast(getFavouriteFood(), 1)) {
			if (messageDelay.getOrDefault(player.getUniqueId(), 0L) <= System.currentTimeMillis()) {
				player.sendMessage(ChatColors.color("&9Your " + getItemName()
						+ " &9would have helped you if you did not neglect it by not feeding it :("));
				messageDelay.put(player.getUniqueId(), System.currentTimeMillis() + MESSAGE_DELAY);
			}
			return false;
		}

		player.getInventory().removeItem(getFavouriteFood());
		return true;
	}
}