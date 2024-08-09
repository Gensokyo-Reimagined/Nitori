package net.gensokyoreimagined.nitori.api.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;


public interface LithiumInventory extends ContainerEntity {

    /**
     * Getter for the inventory stack list of this inventory.
     *
     * @return inventory stack list
     */
    NonNullList<ItemStack> getInventoryLithium();

    /**
     * Setter for the inventory stack list of this inventory.
     * Used to replace the stack list with Lithium's custom stack list.
     *
     * @param inventory inventory stack list
     */
    void setInventoryLithium(NonNullList<ItemStack> inventory);

    /**
     * Generates the loot like a hopper access would do in vanilla.
     * <p>
     * If a modded inventory has custom loot generation code, it will be required to override this
     * loot generation method. Otherwise, its loot may be generated too late.
     */
    default void generateLootLithium() {
        if (this instanceof RandomizableContainerBlockEntity) {
            ((RandomizableContainerBlockEntity) this).unpackLootTable(null);
        }
        if (this instanceof ContainerEntity) {
            ((ContainerEntity) this).unpackChestVehicleLootTable(null);
        }
    }
}
