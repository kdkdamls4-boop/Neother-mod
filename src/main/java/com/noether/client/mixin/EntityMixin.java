package com.noether.client.mixin;

import com.noether.client.core.ModuleManager;
import com.noether.client.modules.combat.HitBoxesModule;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "getTargetingMargin", at = @At("RETURN"), cancellable = true)
    private void noether$hitboxes(CallbackInfoReturnable<Float> cir) {
        HitBoxesModule module = ModuleManager.getInstance().get(HitBoxesModule.class);
        if (module == null) return;
        float extra = module.expandFor((Entity) (Object) this);
        if (extra > 0) {
            cir.setReturnValue(cir.getReturnValue() + extra);
        }
    }
}
