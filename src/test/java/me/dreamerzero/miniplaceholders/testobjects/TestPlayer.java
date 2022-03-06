package me.dreamerzero.miniplaceholders.testobjects;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.player.PlayerSettings;
import com.velocitypowered.api.proxy.player.ResourcePackInfo;
import com.velocitypowered.api.proxy.player.SkinParts;
import com.velocitypowered.api.proxy.player.TabList;
import com.velocitypowered.api.proxy.player.TabListEntry;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.util.GameProfile;
import com.velocitypowered.api.util.GameProfile.Property;
import com.velocitypowered.api.util.ModInfo;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;

public class TestPlayer implements Player {
    private String username;
    public TestPlayer(String username){
        this.username = username;
    }

    @Override
    public Tristate getPermissionValue(String permission) {
        return Tristate.TRUE;
    }

    @Override
    public @NotNull Identity identity() {
        return Identity.nil();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return new InetSocketAddress("play.peruviankkit.net", 25565);
    }

    @Override
    public Optional<InetSocketAddress> getVirtualHost() {
        return Optional.empty();
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public ProtocolVersion getProtocolVersion() {
        return ProtocolVersion.MINECRAFT_1_18_2;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public @Nullable Locale getEffectiveLocale() {
        return Locale.ROOT;
    }

    @Override
    public void setEffectiveLocale(Locale locale) {
    }

    @Override
    public UUID getUniqueId() {
        return UUID.randomUUID();
    }

    @Override
    public Optional<ServerConnection> getCurrentServer() {
        return Optional.empty();
    }

    @Override
    public PlayerSettings getPlayerSettings() {
        return new PlayerSettings() {

            @Override
            public Locale getLocale() {
                return Locale.ROOT;
            }

            @Override
            public byte getViewDistance() {
                return 0;
            }

            @Override
            public ChatMode getChatMode() {
                return ChatMode.SHOWN;
            }

            @Override
            public boolean hasChatColors() {
                return true;
            }

            @Override
            public SkinParts getSkinParts() {
                return null;
            }

            @Override
            public MainHand getMainHand() {
                return MainHand.RIGHT;
            }

            @Override
            public boolean isClientListingAllowed() {
                return true;
            }
        };
    }

    @Override
    public Optional<ModInfo> getModInfo() {
        return Optional.empty();
    }

    @Override
    public long getPing() {
        return 4;
    }

    @Override
    public boolean isOnlineMode() {
        return true;
    }

    @Override
    public ConnectionRequestBuilder createConnectionRequest(RegisteredServer server) {
        return new ConnectionRequestBuilder() {

            @Override
            public RegisteredServer getServer() {
                return null;
            }

            @Override
            public CompletableFuture<Result> connect() {
                return CompletableFuture.completedFuture(new Result(){

                    @Override
                    public Status getStatus() {
                        return Status.ALREADY_CONNECTED;
                    }

                    @Override
                    public Optional<Component> getReasonComponent() {
                        return Optional.empty();
                    }

                    @Override
                    public RegisteredServer getAttemptedConnection() {
                        return null;
                    }
                });
            }

            @Override
            public CompletableFuture<Boolean> connectWithIndication() {
                return CompletableFuture.completedFuture(true);
            }

            @Override
            public void fireAndForget() {
            }
        };
    }

    @Override
    public List<Property> getGameProfileProperties() {
        return List.of();
    }

    @Override
    public void setGameProfileProperties(List<Property> properties) {

    }

    @Override
    public GameProfile getGameProfile() {
        return new GameProfile(UUID.randomUUID(), "4drian3d", List.of());
    }

    @Override
    public void clearHeaderAndFooter() {

    }

    @Override
    public Component getPlayerListHeader() {
        return Component.text("Header");
    }

    @Override
    public Component getPlayerListFooter() {
        return Component.text("Footer");
    }

    @Override
    public TabList getTabList() {
        return new TabList() {

            @Override
            public void setHeaderAndFooter(Component header, Component footer) {

            }

            @Override
            public void clearHeaderAndFooter() {

            }

            @Override
            public void addEntry(TabListEntry entry) {

            }

            @Override
            public Optional<TabListEntry> removeEntry(UUID uuid) {
                return Optional.empty();
            }

            @Override
            public boolean containsEntry(UUID uuid) {
                return false;
            }

            @Override
            public Collection<TabListEntry> getEntries() {
                return List.of();
            }

            @Override
            public TabListEntry buildEntry(GameProfile profile, @Nullable Component displayName, int latency,
                    int gameMode) {
                return new TabListEntry() {

                    @Override
                    public TabList getTabList() {
                        return null;
                    }

                    @Override
                    public GameProfile getProfile() {
                        return null;
                    }

                    @Override
                    public Optional<Component> getDisplayNameComponent() {
                        return Optional.empty();
                    }

                    @Override
                    public TabListEntry setDisplayName(@Nullable Component displayName) {
                        return null;
                    }

                    @Override
                    public int getLatency() {
                        return 0;
                    }

                    @Override
                    public TabListEntry setLatency(int latency) {
                        return null;
                    }

                    @Override
                    public int getGameMode() {
                        return 0;
                    }

                    @Override
                    public TabListEntry setGameMode(int gameMode) {
                        return null;
                    }
                };
            }
        };
    }

    @Override
    public void disconnect(Component reason) {

    }

    @Override
    public void spoofChatInput(String input) {

    }

    @Override
    public void sendResourcePack(String url) {

    }

    @Override
    public void sendResourcePack(String url, byte[] hash) {

    }

    @Override
    public void sendResourcePackOffer(ResourcePackInfo packInfo) {

    }

    @Override
    public @Nullable ResourcePackInfo getAppliedResourcePack() {
        return null;
    }

    @Override
    public @Nullable ResourcePackInfo getPendingResourcePack() {

        return null;
    }

    @Override
    public boolean sendPluginMessage(ChannelIdentifier identifier, byte[] data) {

        return true;
    }

    @Override
    public @Nullable String getClientBrand() {
        return null;
    }
}
