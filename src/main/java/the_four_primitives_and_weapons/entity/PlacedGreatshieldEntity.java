package the_four_primitives_and_weapons.entity;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;
import the_four_primitives_and_weapons.item.GreatshieldItem;

/** 設置位置に固定した大盾。保存するItemStackが耐久・名前・エンチャントを保持する。 */
public class PlacedGreatshieldEntity extends Entity {
    private static final EntityDataAccessor<ItemStack> SHIELD =
            SynchedEntityData.defineId(PlacedGreatshieldEntity.class, EntityDataSerializers.ITEM_STACK);

    public PlacedGreatshieldEntity(EntityType<? extends PlacedGreatshieldEntity> type, Level level) {
        super(type, level);
        setNoGravity(true);
    }

    @Override protected void defineSynchedData() { entityData.define(SHIELD, ItemStack.EMPTY); }
    public ItemStack getShield() { return entityData.get(SHIELD); }
    public void setShield(ItemStack stack) { entityData.set(SHIELD, stack); }

    public void updateShieldBounds() {
        boolean sideways = getDirection().getAxis() == Direction.Axis.X;
        double halfX = sideways ? 0.0625 : 0.5;
        double halfZ = sideways ? 0.5 : 0.0625;
        setBoundingBox(new AABB(getX() - halfX, getY(), getZ() - halfZ,
                getX() + halfX, getY() + 1.875, getZ() + halfZ));
    }

    @Override public void tick() {
        super.tick();
        updateShieldBounds();
        if (level().isClientSide) return;
        if (!GreatshieldItem.isGreatshield(getShield())) { discard(); return; }
    }

    @Override public boolean isPickable() { return !isRemoved(); }
    @Override public boolean canBeCollidedWith() { return !isRemoved(); }
    @Override public boolean isPushable() { return false; }
    @Override public ItemStack getPickResult() { return getShield().copy(); }

    @Override public InteractionResult interact(Player player, InteractionHand hand) {
        if (!player.isShiftKeyDown() || player.isSpectator()) return InteractionResult.PASS;
        if (!level().mayInteract(player, blockPosition()) || !player.mayBuild()) return InteractionResult.FAIL;
        if (!level().isClientSide && !isRemoved()) {
            ItemStack stored = getShield().copy();
            setShield(ItemStack.EMPTY);
            if (!player.getInventory().add(stored)) player.drop(stored, false);
            discard();
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override public boolean hurt(DamageSource source, float amount) {
        if (level().isClientSide || isRemoved() || isInvulnerableTo(source)) return false;
        if (!source.is(DamageTypeTags.IS_EXPLOSION)
                && source.getEntity() instanceof Player player && player.getAbilities().instabuild) {
            discard();
            return true;
        }
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) { dropShield(); return true; }
        absorbDamage(amount);
        return true;
    }

    public void absorbDamage(float amount) {
        if (level().isClientSide || isRemoved() || getShield().isEmpty()) return;
        ItemStack stack = getShield().copy();
        int cost = Math.max(1, (int) Math.ceil(amount));
        if (stack.hurt(cost, random, null)) {
            setShield(ItemStack.EMPTY);
            playSound(SoundEvents.SHIELD_BREAK, 1.0f, 1.0f);
            discard();
        } else {
            setShield(stack);
            playSound(SoundEvents.SHIELD_BLOCK, 1.0f, 0.8f);
        }
    }

    private void dropShield() {
        ItemStack stack = getShield().copy();
        setShield(ItemStack.EMPTY);
        if (!stack.isEmpty()) spawnAtLocation(stack);
        discard();
    }

    @Override protected void addAdditionalSaveData(CompoundTag tag) { tag.put("Shield", getShield().save(new CompoundTag())); }
    @Override protected void readAdditionalSaveData(CompoundTag tag) { setShield(ItemStack.of(tag.getCompound("Shield"))); }
    @Override public Packet<ClientGamePacketListener> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
}
