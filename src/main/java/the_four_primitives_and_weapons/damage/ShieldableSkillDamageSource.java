package the_four_primitives_and_weapons.damage;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.phys.Vec3;

/** A direct skill impact can be guarded even when its elemental damage ignores armor. */
public final class ShieldableSkillDamageSource extends DamageSource {
    public ShieldableSkillDamageSource(DamageSource source, Vec3 origin) {
        super(source.typeHolder(), source.getDirectEntity(), source.getEntity(), origin);
        if (source instanceof IElementalDamageSource from && (Object) this instanceof IElementalDamageSource to) {
            to.setElementType(from.getElementType());
            to.setElementLevel(from.getElementLevel());
        }
    }

    @Override
    public boolean is(TagKey<DamageType> tag) {
        return !DamageTypeTags.BYPASSES_SHIELD.equals(tag) && super.is(tag);
    }
}
