package the_four_primitives_and_weapons.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModCustomEntities;

/**
 * 地面に突き刺さった武器 ( 戦場の建築用 )。
 * 武器アイテムを持って地面の上面をスニーク右クリックで設置、 右クリック / 攻撃で回収できる。
 */
public class StabbedWeaponEntity extends Entity {
    /** 上端と下端を結ぶ軸からの判定半径。長い武器でも太くしない。 */
    private static final double HIT_RADIUS = 0.05;

    @Override
    public float getPickRadius() { return 0.0F; }

	/** 0より大きい間は回収不能な次元移動演出。サーバー側だけで寿命を数える。 */
	private int ritualLifetime;
    private final java.util.List<NinjatoTetherSegmentEntity> tetherSegments = new java.util.ArrayList<>();

    /** 切断しても刀は返さず、地面に残す。紐・鎖の再取り付けは再クラフトする。 */
    public boolean cutTether(Player attacker, boolean skill, float damage) {
        if (level().isClientSide || isRemoved() || !vaultTethered
                || !the_four_primitives_and_weapons.util.NinjatoTetherCutRule.canCut(isChainTether(), skill, damage)) return false;
        vaultTethered = false;
        entityData.set(DATA_TETHER_OWNER, java.util.Optional.empty());
        ItemStack sheath = getItem().copy();
        sheath.getOrCreateTag().remove(the_four_primitives_and_weapons.util.NinjatoVault.TETHERED);
        sheath.getOrCreateTag().remove(the_four_primitives_and_weapons.util.NinjatoVault.MATERIAL);
        setItem(sheath);
        Player owner = level().getServer().getPlayerList().getPlayer(vaultOwner);
        if (owner != null) {
            for (int i = 0; i < owner.getInventory().getContainerSize(); i++) {
                if (isMatchingCord(owner.getInventory().getItem(i))) owner.getInventory().setItem(i, ItemStack.EMPTY);
            }
            owner.getInventory().setChanged();
            owner.containerMenu.broadcastChanges();
        }
        clearTetherSegments();
        level().playSound(null, blockPosition(), isChainTether() ? SoundEvents.CHAIN_BREAK : SoundEvents.WOOL_BREAK,
            SoundSource.PLAYERS, 1.0F, 1.0F);
        return true;
    }

    private boolean isMatchingCord(ItemStack stack) {
        return the_four_primitives_and_weapons.util.NinjatoVault.isRecallItem(stack) && stack.hasTag()
            && stack.getTag().hasUUID("PlantedWeapon") && getUUID().equals(stack.getTag().getUUID("PlantedWeapon"));
    }

    private void clearTetherSegments() {
        for (NinjatoTetherSegmentEntity segment : tetherSegments) segment.discard();
        tetherSegments.clear();
    }

    private void updateTetherSegments() {
        Player owner = vaultOwner == null ? null : level().getPlayerByUUID(vaultOwner);
        if (!vaultTethered || isRemoved() || owner == null || !owner.isAlive()) {
            clearTetherSegments(); return;
        }
        boolean main = isMatchingCord(owner.getMainHandItem());
        if (!main && !isMatchingCord(owner.getOffhandItem())) { clearTetherSegments(); return; }
        int arm = owner.getMainArm() == net.minecraft.world.entity.HumanoidArm.RIGHT ? 1 : -1;
        if (!main) arm = -arm;
        double yaw = Math.toRadians(owner.yBodyRot);
        net.minecraft.world.phys.Vec3 hand = owner.getEyePosition().add(
            -Math.cos(yaw) * arm * 0.35 - Math.sin(yaw) * 0.4,
            (owner.isCrouching() ? -0.1875 : 0) - 0.55,
            -Math.sin(yaw) * arm * 0.35 + Math.cos(yaw) * 0.4);
        net.minecraft.world.phys.Vec3 anchor = getTetherAnchor();
        net.minecraft.world.phys.Vec3 line = hand.subtract(anchor);
        int count = Math.max(1, Math.min(48, (int)Math.ceil(line.length() / 0.3)));
        while (tetherSegments.size() > count) tetherSegments.remove(tetherSegments.size() - 1).discard();
        while (tetherSegments.size() < count) {
            NinjatoTetherSegmentEntity segment = new NinjatoTetherSegmentEntity(
                the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModCustomEntities.NINJATO_TETHER_SEGMENT.get(), level());
            segment.attach(this);
            net.minecraft.world.phys.Vec3 point = anchor.add(line.scale((tetherSegments.size() + 0.5) / count));
            segment.setPos(point.x, point.y - 0.2, point.z);
            if (!level().addFreshEntity(segment)) break;
            tetherSegments.add(segment);
        }
        for (int i = 0; i < tetherSegments.size(); i++) {
            net.minecraft.world.phys.Vec3 point = anchor.add(line.scale((i + 0.5) / count));
            tetherSegments.get(i).setPos(point.x, point.y - 0.2, point.z);
        }
        the_four_primitives_and_weapons.util.NinjatoChainPower.tick(this, owner, anchor, hand);
    }

    private java.util.UUID vaultOwner;
    private boolean vaultTethered;

    public void setVaultOwner(java.util.UUID owner, boolean tethered) {
        vaultOwner = owner;
        vaultTethered = tethered;
        entityData.set(DATA_TETHER_CHAIN, the_four_primitives_and_weapons.util.NinjatoVault.isChain(getItem()));
        entityData.set(DATA_TETHER_OWNER, tethered ? java.util.Optional.of(owner) : java.util.Optional.empty());
    }

    /** 描画用の持ち主。UUIDを同期し、追跡開始・再ログイン後も接続できるようにする。 */
    public java.util.Optional<java.util.UUID> getTetherOwner() {
        return entityData.get(DATA_TETHER_OWNER);
    }

    /** 紐の実体を鞘に置き換える。実体を先に消して二重回収を防ぐ。 */
    public boolean recallVault(Player player) {
        if (level().isClientSide || isRemoved() || vaultOwner == null || !vaultOwner.equals(player.getUUID()))
            return false;
        if (!vaultTethered || getItem().isEmpty()) return false;
        int cordSlot = -1;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack cord = player.getInventory().getItem(i);
            if (the_four_primitives_and_weapons.util.NinjatoVault.isRecallItem(cord)
                    && cord.hasTag() && cord.getTag().hasUUID("PlantedWeapon")
                    && getUUID().equals(cord.getTag().getUUID("PlantedWeapon"))) {
                if (cordSlot < 0) cordSlot = i;
            }
        }
        if (cordSlot < 0 && player.getInventory().getFreeSlot() < 0) return false;
        ItemStack result = getItem().copy();
        if (cordSlot >= 0 && !isChainTether()) {
            ItemStack cord = player.getInventory().getItem(cordSlot);
            if (cord.hasTag() && cord.getTag().contains(the_four_primitives_and_weapons.util.NinjatoVault.CORD_COLOR, 99))
                result.getOrCreateTag().putInt(the_four_primitives_and_weapons.util.NinjatoVault.CORD_COLOR,
                    the_four_primitives_and_weapons.util.NinjatoVault.cordColor(cord));
        }
        // 回収パケットと遅れて届く回避パケットが同時に動かないようにする。
        player.getPersistentData().putLong("NinjatoRecallDodgeUntil", player.level().getGameTime() + 10);
        discard();
        if (cordSlot >= 0) {
            // クリエイティブ等で複製された古い紐も無効化する。
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack cord = player.getInventory().getItem(i);
                if (the_four_primitives_and_weapons.util.NinjatoVault.isRecallItem(cord)
                        && cord.hasTag() && cord.getTag().hasUUID("PlantedWeapon")
                        && getUUID().equals(cord.getTag().getUUID("PlantedWeapon")))
                    player.getInventory().setItem(i, ItemStack.EMPTY);
            }
            player.getInventory().setItem(cordSlot, result);
        } else {
            player.getInventory().add(result);
        }
        player.getInventory().setChanged();
        player.containerMenu.broadcastChanges();
        return true;
    }

    private static final EntityDataAccessor<Boolean> DATA_TETHER_CHAIN =
        SynchedEntityData.defineId(StabbedWeaponEntity.class, EntityDataSerializers.BOOLEAN);

    public boolean isChainTether() { return entityData.get(DATA_TETHER_CHAIN); }

    private static final EntityDataAccessor<java.util.Optional<java.util.UUID>> DATA_TETHER_OWNER =
        SynchedEntityData.defineId(StabbedWeaponEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<ItemStack> DATA_ITEM =
			SynchedEntityData.defineId(StabbedWeaponEntity.class, EntityDataSerializers.ITEM_STACK);
	private static final EntityDataAccessor<Float> DATA_YAW =
			SynchedEntityData.defineId(StabbedWeaponEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_TILT =
			SynchedEntityData.defineId(StabbedWeaponEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_RADIUS =
			SynchedEntityData.defineId(StabbedWeaponEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_ROLL =
			SynchedEntityData.defineId(StabbedWeaponEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_SCALE =
			SynchedEntityData.defineId(StabbedWeaponEntity.class, EntityDataSerializers.FLOAT);

	public StabbedWeaponEntity(EntityType<? extends StabbedWeaponEntity> type, Level level) {
		super(type, level);
	}

	public StabbedWeaponEntity(Level level) {
		this(TheFourPrimitivesAndWeaponsModCustomEntities.STABBED_WEAPON.get(), level);
	}

	public StabbedWeaponEntity(PlayMessages.SpawnEntity packet, Level level) {
		this(TheFourPrimitivesAndWeaponsModCustomEntities.STABBED_WEAPON.get(), level);
	}

	@Override
	protected void defineSynchedData() {
        this.entityData.define(DATA_TETHER_CHAIN, false);
        this.entityData.define(DATA_TETHER_OWNER, java.util.Optional.empty());
		this.entityData.define(DATA_ITEM, ItemStack.EMPTY);
		this.entityData.define(DATA_YAW, 0f);
		this.entityData.define(DATA_TILT, 12f);
		this.entityData.define(DATA_RADIUS, 1.0f); // 当たり判定/編集球の半径 ( 武器ごとに変更可 )
		this.entityData.define(DATA_ROLL, 0f);      // ロール ( 軸まわりの回転 )
		this.entityData.define(DATA_SCALE, 1.0f);   // 表示スケール ( 武器ごとに変更可 )
	}

	public void setItem(ItemStack stack) {
		ItemStack one = stack.copy();
		one.setCount(1);
		this.entityData.set(DATA_ITEM, one);
        setBoundingBox(makeBoundingBox());
	}

	public ItemStack getItem() {
		return this.entityData.get(DATA_ITEM);
	}

	public void setStabYaw(float yaw) {
		this.entityData.set(DATA_YAW, yaw);
		this.setBoundingBox(makeBoundingBox());
	}

	public float getStabYaw() {
		return this.entityData.get(DATA_YAW);
	}

	public void setTilt(float tilt) {
		this.entityData.set(DATA_TILT, tilt);
		this.setBoundingBox(makeBoundingBox());
	}

	public float getTilt() {
		return this.entityData.get(DATA_TILT);
	}

	public void setRadius(float r) {
		this.entityData.set(DATA_RADIUS, Math.max(0.3f, Math.min(3.0f, r)));
		this.setBoundingBox(makeBoundingBox()); // 半径変更を当たり判定に即反映
	}

	public float getRadius() {
		return this.entityData.get(DATA_RADIUS);
	}

	public void setRoll(float roll) {
		this.entityData.set(DATA_ROLL, roll);
        setBoundingBox(makeBoundingBox());
	}

	/** 表示スケール ( 0.2〜3.0 )。 見た目のみ。 当たり判定は半径 ( setRadius ) で別管理。 */
	public void setScale(float sc) {
		this.entityData.set(DATA_SCALE, Math.max(0.2f, Math.min(3.0f, sc)));
        setBoundingBox(makeBoundingBox());
	}

	public float getScale() {
		return this.entityData.get(DATA_SCALE);
	}

	public float getRoll() {
		return this.entityData.get(DATA_ROLL);
	}

	public void setRitualLifetime(int ticks) {
		this.ritualLifetime = Math.max(0, ticks);
	}

	@Override
	public boolean isPickable() {
		return !this.isRemoved(); // 右クリック / 攻撃でターゲット可能
	}

	@Override
	public boolean isPushable() {
		return false;
	}

    /** クリック判定だけでなく、足場として移動・着地の衝突判定にも参加する。 */
    @Override
    public boolean canBeCollidedWith() {
        return !isRemoved();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        // 向き・長さの同期後にクライアント側の足場も同じ形へ更新する。
        if (key.equals(DATA_YAW) || key.equals(DATA_TILT) || key.equals(DATA_RADIUS)
                || key.equals(DATA_SCALE) || key.equals(DATA_ROLL) || key.equals(DATA_ITEM)) {
            setBoundingBox(makeBoundingBox());
        }
    }

	@Override
	public boolean isNoGravity() {
		return true; // 地面に刺さったまま静止
	}

	/**
	 * 当たり判定を「武器の軸 ( 向き・傾き )」 に沿わせる。 刃先 ( 下＋前 ) と 柄 ( 上＋後ろ ) の
	 * 2 端点を結ぶ線分を内包する AABB にするので、 表示した武器に沿った細長い判定になる
	 * ( 詳細な衝突判定は collisionPieces で細分化する )。
	 */
	@Override
	protected net.minecraft.world.phys.AABB makeBoundingBox() {
		if (this.entityData == null) {
			return new net.minecraft.world.phys.AABB(
					getX() - 0.5, getY() - 0.5, getZ() - 0.5, getX() + 0.5, getY() + 0.5, getZ() + 0.5);
		}
        net.minecraft.world.phys.Vec3[] ends = weaponSegment();
        return new net.minecraft.world.phys.AABB(ends[0], ends[1]).inflate(HIT_RADIUS);
	}

    /** 傾いた武器を囲む大きな箱ではなく、軸に沿った細い小箱を足場にする。 */
    public java.util.List<net.minecraft.world.phys.shapes.VoxelShape> collisionPieces() {
        return the_four_primitives_and_weapons.util.StabbedWeaponGeometry.collisionPieces(weaponSegment(), HIT_RADIUS);
    }

	@Override
	public void tick() {
		super.tick();
        if (!level().isClientSide) updateTetherSegments();
        if (!level().isClientSide) the_four_primitives_and_weapons.util.NinjatoChainPower.tickBlade(this,
                vaultOwner == null ? null : level().getPlayerByUUID(vaultOwner));
        if (!level().isClientSide && vaultTethered && vaultOwner != null && !isRemoved()) {
            net.minecraft.server.level.ServerPlayer owner = level().getServer().getPlayerList().getPlayer(vaultOwner);
            if (owner != null && owner.isAlive()
                    && (owner.level() != level() || distanceToSqr(owner) >= 100.0)) recallVault(owner);
        }
		// 静止エンティティなので毎tickの速度・AABB再計算は不要。向き/半径変更時にだけ更新する。
		if (!this.level().isClientSide && ritualLifetime > 0 && --ritualLifetime == 0) this.discard();
	}

	/** 右クリックで回収 ( 視線が実際に武器 ( 棒 ) に当たっている時だけ )。 */
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (ritualLifetime > 0) return InteractionResult.PASS;
		if (!lookHitsWeapon(player, 6.0)) return InteractionResult.PASS; // 武器に当たっていない
        if (!this.level().isClientSide) {
            if (vaultOwner != null && vaultTethered) recallVault(player);
            else retrieveTo(player);
        }
		return InteractionResult.sidedSuccess(this.level().isClientSide);
	}

	/** 攻撃でも回収 ( 視線が実際に武器に当たっている時だけ )。 */
	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (ritualLifetime > 0 || vaultOwner != null) return false;
		if (this.level().isClientSide || this.isRemoved()) return false;
		if (source.getEntity() instanceof Player player) {
			if (!lookHitsWeapon(player, 7.0)) return false; // 武器に当たっていない → 無視
			retrieveTo(player);
			return true;
		}
		return false;
	}

	private boolean lookHitsWeapon(Player player, double maxDist) {
		return clipWeapon(player.getEyePosition(1.0f), player.getViewVector(1.0f), maxDist) >= 0;
	}

	// ─────────────────────────────────────────────────────────────
	// OBB ( カプセル ) 精密ヒット判定: 武器の線分 ( 柄→刃先 ) に視線レイが近いか
	// ─────────────────────────────────────────────────────────────

    /** 鞘口の少し下、鞘本体の表面。描画と切断判定で同じ接続位置を使う。 */
    public net.minecraft.world.phys.Vec3 getTetherAnchor() {
        // ninzyatousayatuki の鞘表面 (7.9, 9, 7.6) に手持ちモデルの
        // translation / rotation / scale を適用した位置。設置レンダラーの沈み込みも加える。
        org.joml.Vector3f offset = new org.joml.Vector3f(-0.0375F, -0.0625F, 0.15F)
            .mul(getScale()).add(0, the_four_primitives_and_weapons.util.StabbedWeaponGeometry.RENDER_OFFSET, 0);
        offset.rotate(new org.joml.Quaternionf()
            .rotationY((float)Math.toRadians(getStabYaw()))
            .rotateX((float)Math.toRadians(getTilt()))
            .rotateZ((float)Math.toRadians(180 + getRoll())));
        return position().add(offset.x, offset.y, offset.z);
    }

	/** 武器の軸線分 [ 柄, 刃先 ] を 向き・傾き から求める。 */
	public net.minecraft.world.phys.Vec3[] weaponSegment() {
        net.minecraft.world.phys.Vec3[] model = the_four_primitives_and_weapons.util.StabbedWeaponGeometry.localAxis(getItem());
        if (model != null) {
            org.joml.Quaternionf rotation = new org.joml.Quaternionf()
                .rotationY((float)Math.toRadians(getStabYaw()))
                .rotateX((float)Math.toRadians(getTilt()))
                .rotateZ((float)Math.toRadians(180 + getRoll()));
            net.minecraft.world.phys.Vec3[] result = new net.minecraft.world.phys.Vec3[2];
            for (int i = 0; i < 2; i++) {
                org.joml.Vector3f point = model[i].toVector3f().mul(getScale())
                    .add(0, the_four_primitives_and_weapons.util.StabbedWeaponGeometry.RENDER_OFFSET, 0).rotate(rotation);
                result[i] = position().add(point.x, point.y, point.z);
            }
            return result;
        }
		double r = getRadius();
		double len = 1.0 * r;
		double tr = Math.toRadians(getTilt());
		double yr = Math.toRadians(getStabYaw());
		double horiz = Math.sin(tr), vert = Math.cos(tr);
		double fx = -Math.sin(yr) * horiz, fz = Math.cos(yr) * horiz;
		net.minecraft.world.phys.Vec3 tip = new net.minecraft.world.phys.Vec3(
				getX() + fx * len, getY() - vert * len, getZ() + fz * len);
		net.minecraft.world.phys.Vec3 hnd = new net.minecraft.world.phys.Vec3(
				getX() - fx * len * 0.55, getY() + vert * len * 0.55, getZ() - fz * len * 0.55);
		return new net.minecraft.world.phys.Vec3[]{hnd, tip};
	}

	/**
	 * 視線レイ ( origin, dir(正規化), maxDist ) が武器の棒 ( 太さ TH ) に当たれば、
	 * レイ上の距離を返す。 当たらなければ -1。
	 */
	public double clipWeapon(net.minecraft.world.phys.Vec3 origin, net.minecraft.world.phys.Vec3 dir, double maxDist) {
		net.minecraft.world.phys.Vec3[] seg = weaponSegment();
		net.minecraft.world.phys.Vec3 q1 = origin.add(dir.x * maxDist, dir.y * maxDist, dir.z * maxDist);
		double th = HIT_RADIUS; // 上端・下端をつなぐ細い線と共通の太さ
		double[] res = closestSegSeg(origin, q1, seg[0], seg[1]);
		if (res[0] <= th * th) return res[1] * maxDist;
		return -1;
	}

	/** 2 線分の最近距離² と、 1本目上の位置 s(0..1) を返す ( Ericson )。 */
	private static double[] closestSegSeg(net.minecraft.world.phys.Vec3 p1, net.minecraft.world.phys.Vec3 q1,
	                                      net.minecraft.world.phys.Vec3 p2, net.minecraft.world.phys.Vec3 q2) {
		net.minecraft.world.phys.Vec3 d1 = q1.subtract(p1);
		net.minecraft.world.phys.Vec3 d2 = q2.subtract(p2);
		net.minecraft.world.phys.Vec3 r = p1.subtract(p2);
		double a = d1.lengthSqr();
		double e = d2.lengthSqr();
		double f = d2.dot(r);
		double EPS = 1.0e-8;
		double s, t;
		if (a <= EPS && e <= EPS) {
			s = 0; t = 0;
		} else if (a <= EPS) {
			s = 0; t = clamp01(f / e);
		} else {
			double c = d1.dot(r);
			if (e <= EPS) {
				t = 0; s = clamp01(-c / a);
			} else {
				double b = d1.dot(d2);
				double denom = a * e - b * b;
				s = (denom != 0) ? clamp01((b * f - c * e) / denom) : 0;
				t = (b * s + f) / e;
				if (t < 0) { t = 0; s = clamp01(-c / a); }
				else if (t > 1) { t = 1; s = clamp01((b - c) / a); }
			}
		}
		net.minecraft.world.phys.Vec3 c1 = p1.add(d1.scale(s));
		net.minecraft.world.phys.Vec3 c2 = p2.add(d2.scale(t));
		return new double[]{c1.subtract(c2).lengthSqr(), s};
	}

	private static double clamp01(double v) {
		return v < 0 ? 0 : (v > 1 ? 1 : v);
	}

	private void retrieveTo(Player player) {
        if (isRemoved()) return;
        ItemStack drop = getItem();
		if (!drop.isEmpty()) {
			ItemStack give = drop.copy();
			if (!player.addItem(give)) player.drop(give, false);
		}
		this.level().playSound(null, getX(), getY(), getZ(),
				SoundEvents.ARMOR_EQUIP_IRON, SoundSource.PLAYERS, 0.7f, 1.2f);
		this.discard();
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("VaultOwner")) vaultOwner = tag.getUUID("VaultOwner");
        vaultTethered = tag.getBoolean("VaultTethered");
        entityData.set(DATA_TETHER_OWNER, vaultTethered && vaultOwner != null
            ? java.util.Optional.of(vaultOwner) : java.util.Optional.empty());
		if (tag.contains("StabItem")) setItem(ItemStack.of(tag.getCompound("StabItem")));
        entityData.set(DATA_TETHER_CHAIN, the_four_primitives_and_weapons.util.NinjatoVault.isChain(getItem()));
		setStabYaw(tag.getFloat("StabYaw"));
		if (tag.contains("StabTilt")) setTilt(tag.getFloat("StabTilt"));
		if (tag.contains("StabRadius")) setRadius(tag.getFloat("StabRadius"));
		if (tag.contains("StabRoll")) setRoll(tag.getFloat("StabRoll"));
		if (tag.contains("StabScale")) setScale(tag.getFloat("StabScale"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
        if (vaultOwner != null) tag.putUUID("VaultOwner", vaultOwner);
        tag.putBoolean("VaultTethered", vaultTethered);
		if (!getItem().isEmpty()) tag.put("StabItem", getItem().save(new CompoundTag()));
		tag.putFloat("StabYaw", getStabYaw());
		tag.putFloat("StabTilt", getTilt());
		tag.putFloat("StabRadius", getRadius());
		tag.putFloat("StabRoll", getRoll());
		tag.putFloat("StabScale", getScale());
	}
}
