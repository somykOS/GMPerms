package net.somyk.gmperms.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.maxhenkel.admiral.annotations.Command;
import de.maxhenkel.admiral.annotations.Name;
import de.maxhenkel.admiral.annotations.RequiresPermission;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;

import static net.somyk.gmperms.GMPerms.LOGGER;

public class GameModeCommand {

    private static void sendFeedback(ServerCommandSource source, ServerPlayerEntity player, GameMode gameMode) {
        Text text = Text.translatable("gameMode." + gameMode.getName());
        if (source.getEntity() == player) {
            source.sendFeedback(() -> Text.translatable("commands.gamemode.success.self", text), false);
        } else {
            if (source.getWorld().getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK)) {
                player.sendMessage(Text.translatable("gameMode.changed", text));
            }
            source.sendFeedback(() -> Text.translatable("commands.gamemode.success.other", player.getDisplayName(), text), false);
        }
    }

    private static void setGameMode(CommandContext<ServerCommandSource> context, ServerPlayerEntity player, GameMode gameMode, String modeName) throws CommandSyntaxException {
        if (player.interactionManager.getGameMode() != gameMode) {
            player.changeGameMode(gameMode);
            LOGGER.info("[{}: Set {} game mode to {}]", context.getSource().getName(),
                    player == context.getSource().getPlayer() ? "own" : player.getName().getString() + "'s", modeName);
            sendFeedback(context.getSource(), player, gameMode);
        }
    }

    @RequiresPermission("gmperms.survival.own")
    @Command("gms")
    public void survival(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (context.getSource().isExecutedByPlayer()) {
            setGameMode(context, context.getSource().getPlayer(), GameMode.SURVIVAL, "Survival Mode");
        }
    }

    @RequiresPermission("gmperms.survival.other")
    @Command("gms")
    public void setSurvival(CommandContext<ServerCommandSource> context, @Name("target") ServerPlayerEntity target) throws CommandSyntaxException {
        if (context.getSource().isExecutedByPlayer() && target.isPlayer()) {
            setGameMode(context, target, GameMode.SURVIVAL, "Survival Mode");
        }
    }

    @RequiresPermission("gmperms.adventure.own")
    @Command("gma")
    public void adventure(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (context.getSource().isExecutedByPlayer()) {
            setGameMode(context, context.getSource().getPlayer(), GameMode.ADVENTURE, "Adventure Mode");
        }
    }

    @RequiresPermission("gmperms.adventure.other")
    @Command("gma")
    public void setAdventure(CommandContext<ServerCommandSource> context, @Name("target") ServerPlayerEntity target) throws CommandSyntaxException {
        if (context.getSource().isExecutedByPlayer() && target.isPlayer()) {
            setGameMode(context, target, GameMode.ADVENTURE, "Adventure Mode");
        }
    }

    @RequiresPermission("gmperms.spectator.own")
    @Command("gmsp")
    public void spectator(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (context.getSource().isExecutedByPlayer()) {
            setGameMode(context, context.getSource().getPlayer(), GameMode.SPECTATOR, "Spectator Mode");
        }
    }

    @RequiresPermission("gmperms.spectator.other")
    @Command("gmsp")
    public void setSpectator(CommandContext<ServerCommandSource> context, @Name("target") ServerPlayerEntity target) throws CommandSyntaxException {
        if (context.getSource().isExecutedByPlayer() && target.isPlayer()) {
            setGameMode(context, target, GameMode.SPECTATOR, "Spectator Mode");
        }
    }

    @RequiresPermission("gmperms.creative.own")
    @Command("gmc")
    public void creative(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (context.getSource().isExecutedByPlayer()) {
            setGameMode(context, context.getSource().getPlayer(), GameMode.CREATIVE, "Creative Mode");
        }
    }

    @RequiresPermission("gmperms.creative.other")
    @Command("gmc")
    public void setCreative(CommandContext<ServerCommandSource> context, @Name("target") ServerPlayerEntity target) throws CommandSyntaxException {
        if (context.getSource().isExecutedByPlayer() && target.isPlayer()) {
            setGameMode(context, target, GameMode.CREATIVE, "Creative Mode");
        }
    }
}