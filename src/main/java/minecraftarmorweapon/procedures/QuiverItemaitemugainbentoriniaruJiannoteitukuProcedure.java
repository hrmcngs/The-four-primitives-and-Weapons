package minecraftarmorweapon.procedures;

import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;

import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;

import java.util.function.Supplier;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Map;

public class QuiverItemaitemugainbentoriniaruJiannoteitukuProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		double playerSlot = 0;
		double quiverSlot = 0;
		double boggedouterquiverSlot = 0;
		double playerSlot1 = 0;
		if (!(entity.getPersistentData().getBoolean("minecraft_armor_weapon:QuiverItemsyorisunna") == true)) {
			if ((entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains(new ItemStack(MinecraftArmorWeaponModItems.ENDER_QUIVER_ITEM.get())) : false)
					|| (entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains(new ItemStack(MinecraftArmorWeaponModItems.ENDER_PEARL_NETHERITE_QUIVER_ITEM.get())) : false)) {
				quiverSlot = 0;
				for (int index0 = 0; index0 < 9; index0++) {
					if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack)).getItem() == Items.ARROW && !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index1 = 0; index1 < 36; index1++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									final int _slotid = (int) playerSlot;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									_setstack.setCount(1);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable _modHandler)
											_modHandler.setStackInSlot(_slotid, _setstack);
									});
								}
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount((int) (((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount() - 1));
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot + 1;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack)).getItem() == Items.SPECTRAL_ARROW && !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index2 = 0; index2 < 36; index2++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									final int _slotid = (int) playerSlot;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									_setstack.setCount(1);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable _modHandler)
											_modHandler.setStackInSlot(_slotid, _setstack);
									});
								}
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount((int) (((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount() - 1));
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot + 1;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack)).getItem() == Items.TIPPED_ARROW && !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index3 = 0; index3 < 36; index3++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									final int _slotid = (int) playerSlot;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									_setstack.setCount(1);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable _modHandler)
											_modHandler.setStackInSlot(_slotid, _setstack);
									});
								}
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount((int) (((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount() - 1));
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot + 1;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack)).getItem() == Items.FIREWORK_ROCKET && !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index4 = 0; index4 < 36; index4++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									final int _slotid = (int) playerSlot;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									_setstack.setCount(1);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable _modHandler)
											_modHandler.setStackInSlot(_slotid, _setstack);
									});
								}
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount((int) (((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount() - 1));
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot + 1;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack)).getItem() == Items.SNOWBALL && !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index5 = 0; index5 < 36; index5++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									final int _slotid = (int) playerSlot;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									_setstack.setCount(1);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable _modHandler)
											_modHandler.setStackInSlot(_slotid, _setstack);
									});
								}
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount((int) (((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount() - 1));
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot + 1;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack)).getItem() == Items.ENDER_PEARL && !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index6 = 0; index6 < 36; index6++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									final int _slotid = (int) playerSlot;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									_setstack.setCount(1);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable _modHandler)
											_modHandler.setStackInSlot(_slotid, _setstack);
									});
								}
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount((int) (((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount() - 1));
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot + 1;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))
							.getItem() == (entity instanceof ServerPlayer _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).getItem()
							&& !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
								public ItemStack getItemStack(int sltid, ItemStack _isc) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index7 = 0; index7 < 36; index7++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount(((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount());
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))
							.getItem() == (entity instanceof ServerPlayer _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(1)).getItem() : ItemStack.EMPTY).getItem()
							&& !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
								public ItemStack getItemStack(int sltid, ItemStack _isc) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index8 = 0; index8 < 36; index8++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount(((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount());
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))
							.getItem() == (entity instanceof ServerPlayer _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(2)).getItem() : ItemStack.EMPTY).getItem()
							&& !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
								public ItemStack getItemStack(int sltid, ItemStack _isc) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index9 = 0; index9 < 36; index9++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount(((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount());
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))
							.getItem() == (entity instanceof ServerPlayer _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(3)).getItem() : ItemStack.EMPTY).getItem()
							&& !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
								public ItemStack getItemStack(int sltid, ItemStack _isc) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index10 = 0; index10 < 36; index10++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount(((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount());
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))
							.getItem() == (entity instanceof ServerPlayer _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(4)).getItem() : ItemStack.EMPTY).getItem()
							&& !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
								public ItemStack getItemStack(int sltid, ItemStack _isc) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index11 = 0; index11 < 36; index11++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount(((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount());
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))
							.getItem() == (entity instanceof ServerPlayer _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(5)).getItem() : ItemStack.EMPTY).getItem()
							&& !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
								public ItemStack getItemStack(int sltid, ItemStack _isc) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index12 = 0; index12 < 36; index12++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount(((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount());
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))
							.getItem() == (entity instanceof ServerPlayer _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(6)).getItem() : ItemStack.EMPTY).getItem()
							&& !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
								public ItemStack getItemStack(int sltid, ItemStack _isc) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index13 = 0; index13 < 36; index13++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount(((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount());
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))
							.getItem() == (entity instanceof ServerPlayer _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(7)).getItem() : ItemStack.EMPTY).getItem()
							&& !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
								public ItemStack getItemStack(int sltid, ItemStack _isc) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index14 = 0; index14 < 36; index14++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount(((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount());
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot;
						}
					}
					quiverSlot = quiverSlot + 1;
				}
			} else if ((entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains(new ItemStack(MinecraftArmorWeaponModItems.QUIVER_ITEM.get())) : false)
					|| (entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains(new ItemStack(MinecraftArmorWeaponModItems.BOGGED_OUTER_QUIVER.get())) : false)
					|| (entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains(new ItemStack(MinecraftArmorWeaponModItems.NETHERITE_QUIVER_ITEM.get())) : false)) {
				quiverSlot = 0;
				for (int index15 = 0; index15 < 9; index15++) {
					if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack)).getItem() == Items.ARROW && !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index16 = 0; index16 < 36; index16++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									final int _slotid = (int) playerSlot;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									_setstack.setCount(1);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable _modHandler)
											_modHandler.setStackInSlot(_slotid, _setstack);
									});
								}
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount((int) (((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount() - 1));
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot + 1;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack)).getItem() == Items.SPECTRAL_ARROW && !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index17 = 0; index17 < 36; index17++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									final int _slotid = (int) playerSlot;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									_setstack.setCount(1);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable _modHandler)
											_modHandler.setStackInSlot(_slotid, _setstack);
									});
								}
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount((int) (((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount() - 1));
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot + 1;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack)).getItem() == Items.TIPPED_ARROW && !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index18 = 0; index18 < 36; index18++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									final int _slotid = (int) playerSlot;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									_setstack.setCount(1);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable _modHandler)
											_modHandler.setStackInSlot(_slotid, _setstack);
									});
								}
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount((int) (((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount() - 1));
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot + 1;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack)).getItem() == Items.FIREWORK_ROCKET && !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index19 = 0; index19 < 36; index19++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									final int _slotid = (int) playerSlot;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									_setstack.setCount(1);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable _modHandler)
											_modHandler.setStackInSlot(_slotid, _setstack);
									});
								}
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount((int) (((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount() - 1));
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot + 1;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack)).getItem() == Items.SNOWBALL && !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index20 = 0; index20 < 36; index20++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									final int _slotid = (int) playerSlot;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									_setstack.setCount(1);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable _modHandler)
											_modHandler.setStackInSlot(_slotid, _setstack);
									});
								}
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount((int) (((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount() - 1));
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot + 1;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))
							.getItem() == (entity instanceof ServerPlayer _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).getItem()
							&& !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
								public ItemStack getItemStack(int sltid, ItemStack _isc) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index21 = 0; index21 < 36; index21++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount(((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount());
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))
							.getItem() == (entity instanceof ServerPlayer _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(1)).getItem() : ItemStack.EMPTY).getItem()
							&& !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
								public ItemStack getItemStack(int sltid, ItemStack _isc) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index22 = 0; index22 < 36; index22++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount(((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount());
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))
							.getItem() == (entity instanceof ServerPlayer _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(2)).getItem() : ItemStack.EMPTY).getItem()
							&& !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
								public ItemStack getItemStack(int sltid, ItemStack _isc) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index23 = 0; index23 < 36; index23++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount(((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount());
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))
							.getItem() == (entity instanceof ServerPlayer _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(3)).getItem() : ItemStack.EMPTY).getItem()
							&& !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
								public ItemStack getItemStack(int sltid, ItemStack _isc) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index24 = 0; index24 < 36; index24++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount(((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount());
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))
							.getItem() == (entity instanceof ServerPlayer _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(4)).getItem() : ItemStack.EMPTY).getItem()
							&& !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
								public ItemStack getItemStack(int sltid, ItemStack _isc) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index25 = 0; index25 < 36; index25++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount(((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount());
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))
							.getItem() == (entity instanceof ServerPlayer _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(5)).getItem() : ItemStack.EMPTY).getItem()
							&& !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
								public ItemStack getItemStack(int sltid, ItemStack _isc) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index26 = 0; index26 < 36; index26++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount(((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount());
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))
							.getItem() == (entity instanceof ServerPlayer _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(6)).getItem() : ItemStack.EMPTY).getItem()
							&& !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
								public ItemStack getItemStack(int sltid, ItemStack _isc) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index27 = 0; index27 < 36; index27++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount(((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount());
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot;
						}
					} else if ((new Object() {
						public ItemStack getItemStack(int sltid, ItemStack _isc) {
							AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
							_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
							return _retval.get();
						}
					}.getItemStack((int) quiverSlot, itemstack))
							.getItem() == (entity instanceof ServerPlayer _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(7)).getItem() : ItemStack.EMPTY).getItem()
							&& !(entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains((new Object() {
								public ItemStack getItemStack(int sltid, ItemStack _isc) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) quiverSlot, itemstack))) : false)) {
						playerSlot = 0;
						for (int index28 = 0; index28 < 36; index28++) {
							if (((new Object() {
								public ItemStack getItemStack(int sltid, Entity entity) {
									AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
									entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
									return _retval.get();
								}
							}.getItemStack((int) playerSlot, entity))).getCount() == 0) {
								{
									ItemStack _isc = itemstack;
									final ItemStack _setstack = (new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack));
									final int _sltid = (int) quiverSlot;
									_setstack.setCount(((new Object() {
										public ItemStack getItemStack(int sltid, ItemStack _isc) {
											AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
											_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
												_retval.set(capability.getStackInSlot(sltid).copy());
											});
											return _retval.get();
										}
									}.getItemStack((int) quiverSlot, itemstack))).getCount());
									_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
										if (capability instanceof IItemHandlerModifiable) {
											((IItemHandlerModifiable) capability).setStackInSlot(_sltid, _setstack);
										}
									});
								}
								break;
							}
							playerSlot = playerSlot;
						}
					}
					quiverSlot = quiverSlot + 1;
				}
			}
		}
	}
}
