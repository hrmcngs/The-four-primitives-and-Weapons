package the_four_primitives_and_weapons.mixin;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import the_four_primitives_and_weapons.entity.StabbedWeaponEntity;

@Mixin(Entity.class)
public abstract class StabbedWeaponCollisionMixin {
    @Redirect(method = "collide", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/Level;getEntityCollisions(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"))
    private List<VoxelShape> maw$thinWeaponCollision(Level level, Entity mover, AABB area) {
        List<VoxelShape> original = level.getEntityCollisions(mover, area);
        if (original.isEmpty()) return original;
        List<Entity> weapons = level.getEntities(mover, area.inflate(1.0E-7),
            e -> e instanceof StabbedWeaponEntity && !e.isRemoved());
        if (weapons.isEmpty()) return original;
        List<VoxelShape> result = new ArrayList<>(original);
        for (Entity entity : weapons) {
            StabbedWeaponEntity weapon = (StabbedWeaponEntity)entity;
            // Only replace shapes already accepted by the vanilla collision rules.
            if (result.removeIf(shape -> shape.bounds().equals(weapon.getBoundingBox())))
                for (VoxelShape piece : weapon.collisionPieces())
                    if (piece.bounds().intersects(area)) result.add(piece);
        }
        return result;
    }
}
