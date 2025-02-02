package de.maxhenkel.voicechat.mixin;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import de.maxhenkel.voicechat.events.PublishServerEvents;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.net.Proxy;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin extends MinecraftServer {

    public IntegratedServerMixin(Thread thread, LevelStorageSource.LevelStorageAccess levelStorageAccess, PackRepository packRepository, WorldStem worldStem, Proxy proxy, DataFixer dataFixer, @org.jetbrains.annotations.Nullable MinecraftSessionService minecraftSessionService, @org.jetbrains.annotations.Nullable GameProfileRepository gameProfileRepository, @org.jetbrains.annotations.Nullable GameProfileCache gameProfileCache, ChunkProgressListenerFactory chunkProgressListenerFactory) {
        super(thread, levelStorageAccess, packRepository, worldStem, proxy, dataFixer, minecraftSessionService, gameProfileRepository, gameProfileCache, chunkProgressListenerFactory);
    }

    @Inject(method = "publishServer", at = @At(value = "RETURN"))
    public void publishServer(@Nullable GameType gameType, boolean cheats, int port, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (!callbackInfo.getReturnValue()) {
            return;
        }
        PublishServerEvents.SERVER_PUBLISHED.invoker().accept(port);
    }

}
