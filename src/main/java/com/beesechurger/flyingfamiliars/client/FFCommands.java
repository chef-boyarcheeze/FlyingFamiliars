package com.beesechurger.flyingfamiliars.client;

import com.beesechurger.flyingfamiliars.pantheon.PantheonAffinityProvider;
import com.beesechurger.flyingfamiliars.util.FFTypes;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class FFCommands
{
    public static final String PANTHEON_COMMAND = "ff_pantheon";

    public static final String PANTHEON_SUBCOMMAND_ADD = "add";
    public static final String PANTHEON_SUBCOMMAND_MULTIPLY = "multiply";
    public static final String PANTHEON_SUBCOMMAND_QUERY = "query";
    public static final String PANTHEON_SUBCOMMAND_SET = "set";

    public static final String PANTHEON_TARGET = "target";
    public static final String PANTHEON_TYPE = "type";
    public static final String PANTHEON_AMOUNT = "amount";

    private static final List<String> VALID_TYPES = List.of(
        FFTypes.FAMILIAR_TYPE_WATER.type,
        FFTypes.FAMILIAR_TYPE_LIFE.type,
        FFTypes.FAMILIAR_TYPE_AIR.type,
        FFTypes.FAMILIAR_TYPE_EARTH.type,
        FFTypes.FAMILIAR_TYPE_FIRE.type,
        FFTypes.FAMILIAR_TYPE_LIGHT.type,
        FFTypes.FAMILIAR_TYPE_VOID.type
    );

    private static final SuggestionProvider<CommandSourceStack> VALID_TYPE_SUGGESTIONS = (context, builder) -> SharedSuggestionProvider.suggest(VALID_TYPES, builder);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(
            Commands.literal(PANTHEON_COMMAND)
                .requires(source -> source.hasPermission(4))
                .then(Commands.argument(PANTHEON_TARGET, EntityArgument.player())
                    .then(Commands.literal(PANTHEON_SUBCOMMAND_ADD)
                        .then(Commands.argument(PANTHEON_TYPE, StringArgumentType.string())
                            .suggests(VALID_TYPE_SUGGESTIONS)
                            .then(Commands.argument(PANTHEON_AMOUNT, FloatArgumentType.floatArg())
                                .executes(context -> {
                                    ServerPlayer target = EntityArgument.getPlayer(context, PANTHEON_TARGET);
                                    String type = StringArgumentType.getString(context, PANTHEON_TYPE);
                                    float amount = FloatArgumentType.getFloat(context, PANTHEON_AMOUNT);

                                    if (VALID_TYPES.contains(type.toLowerCase()))
                                    {
                                        var affinity = target.getCapability(PantheonAffinityProvider.PANTHEON_AFFINITY);
                                        affinity.resolve().get().add(type, amount);

                                        context.getSource().sendSuccess(
                                            () -> Component.literal("[" + target.getScoreboardName() + "] add: " + type + " (" + affinity.resolve().get().query(type) + ")").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC),
                                            false // don't broadcast to admins
                                        );

                                        return 1;
                                    }

                                    return 0;
                                })
                            )
                        )
                    )
                    .then(Commands.literal(PANTHEON_SUBCOMMAND_MULTIPLY)
                        .then(Commands.argument(PANTHEON_TYPE, StringArgumentType.string())
                            .suggests(VALID_TYPE_SUGGESTIONS)
                            .then(Commands.argument(PANTHEON_AMOUNT, FloatArgumentType.floatArg())
                                .executes(context -> {
                                    ServerPlayer target = EntityArgument.getPlayer(context, PANTHEON_TARGET);
                                    String type = StringArgumentType.getString(context, PANTHEON_TYPE);
                                    float amount = FloatArgumentType.getFloat(context, PANTHEON_AMOUNT);

                                    if (VALID_TYPES.contains(type.toLowerCase()))
                                    {
                                        var affinity = target.getCapability(PantheonAffinityProvider.PANTHEON_AFFINITY);
                                        affinity.resolve().get().multiply(type, amount);

                                        context.getSource().sendSuccess(
                                            () -> Component.literal("[" + target.getScoreboardName() + "] multiply: " + type + " (" + affinity.resolve().get().query(type) + ")").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC),
                                            false // don't broadcast to admins
                                        );

                                        return 1;
                                    }

                                    return 0;
                                })
                            )
                        )
                    )
                    .then(Commands.literal(PANTHEON_SUBCOMMAND_QUERY)
                        .then(Commands.argument(PANTHEON_TYPE, StringArgumentType.string())
                            .suggests(VALID_TYPE_SUGGESTIONS)
                            .executes(context -> {
                                ServerPlayer target = EntityArgument.getPlayer(context, PANTHEON_TARGET);
                                String type = StringArgumentType.getString(context, PANTHEON_TYPE);

                                if (VALID_TYPES.contains(type.toLowerCase()))
                                {
                                    var affinity = target.getCapability(PantheonAffinityProvider.PANTHEON_AFFINITY);

                                    context.getSource().sendSuccess(
                                        () -> Component.literal("[" + target.getScoreboardName() + "] query: " + type + " (" + affinity.resolve().get().query(type) + ")").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC),
                                        false // don't broadcast to admins
                                    );

                                    return 1;
                                }

                                return 0;
                            })
                        )
                    )
                    .then(Commands.literal(PANTHEON_SUBCOMMAND_SET)
                        .then(Commands.argument(PANTHEON_TYPE, StringArgumentType.string())
                            .suggests(VALID_TYPE_SUGGESTIONS)
                            .then(Commands.argument(PANTHEON_AMOUNT, FloatArgumentType.floatArg())
                                .executes(context -> {
                                    ServerPlayer target = EntityArgument.getPlayer(context, PANTHEON_TARGET);
                                    String type = StringArgumentType.getString(context, PANTHEON_TYPE);
                                    float amount = FloatArgumentType.getFloat(context, PANTHEON_AMOUNT);

                                    if (VALID_TYPES.contains(type.toLowerCase()))
                                    {
                                        var affinity = target.getCapability(PantheonAffinityProvider.PANTHEON_AFFINITY);
                                        affinity.resolve().get().set(type, amount);

                                        context.getSource().sendSuccess(
                                            () -> Component.literal("[" + target.getScoreboardName() + "] set: " + type + " (" + affinity.resolve().get().query(type) + ")").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC),
                                            false // don't broadcast to admins
                                        );

                                        return 1;
                                    }

                                    return 0;
                                })
                            )
                        )
                    )
                )
        );
    }
}
