package net.galacticprojects.bungeecord.util;

import dev.derklaro.aerogel.Inject;
import dev.derklaro.aerogel.Injector;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.driver.module.ModuleProvider;
import eu.cloudnetservice.driver.permission.PermissionManagement;
import eu.cloudnetservice.driver.provider.CloudServiceProvider;
import eu.cloudnetservice.driver.provider.GroupConfigurationProvider;
import eu.cloudnetservice.driver.service.ServiceInfoSnapshot;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.bungeecord.ProxyPlugin;
import net.galacticprojects.bungeecord.message.CommandMessages;
import net.galacticprojects.common.util.ComponentParser;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RestartCounter {

    private final Injector injector;

    @Inject
    public RestartCounter() {
        injector = InjectionLayer.ext().injector();
        engageStarter();
    }

    private void engageStarter() {
        ProxyServer.getInstance().getScheduler().schedule(ProxyPlugin.getInstance(), () -> {
            OffsetDateTime offsetDateTime = OffsetDateTime.now();
            String time = TimeHelper.BAN_TIME_FORMATTER.format(offsetDateTime);

            if (time.contains("04:00:00")) {
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    ProxyPlugin.getInstance().getCommonPlugin().getDatabaseRef().asOptional().ifPresent(sql -> {
                        sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
                            player.sendMessage(ComponentParser.parse(ProxyPlugin.getInstance().getCommonPlugin().getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
                                    Key.of("hour", 1), Key.of("minute", 0), Key.of("second", 0))));
                        });
                    });
                }
                return;
            }

            if (time.contains("04:15:00")) {
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    ProxyPlugin.getInstance().getCommonPlugin().getDatabaseRef().asOptional().ifPresent(sql -> {
                        sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
                            player.sendMessage(ComponentParser.parse(ProxyPlugin.getInstance().getCommonPlugin().getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
                                    Key.of("hour", 0), Key.of("minute", 45), Key.of("second", 0))));
                        });
                    });
                }
                return;
            }

            if (time.contains("04:30:00")) {
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    ProxyPlugin.getInstance().getCommonPlugin().getDatabaseRef().asOptional().ifPresent(sql -> {
                        sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
                            player.sendMessage(ComponentParser.parse(ProxyPlugin.getInstance().getCommonPlugin().getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
                                    Key.of("hour", 0), Key.of("minute", 30), Key.of("second", 0))));
                        });
                    });
                }
                return;
            }

            if (time.contains("04:45:00")) {
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    ProxyPlugin.getInstance().getCommonPlugin().getDatabaseRef().asOptional().ifPresent(sql -> {
                        sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
                            player.sendMessage(ComponentParser.parse(ProxyPlugin.getInstance().getCommonPlugin().getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
                                    Key.of("hour", 0), Key.of("minute", 15), Key.of("second", 0))));
                        });
                    });
                }
                return;
            }

            if (time.contains("04:50:00")) {
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    ProxyPlugin.getInstance().getCommonPlugin().getDatabaseRef().asOptional().ifPresent(sql -> {
                        sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
                            player.sendMessage(ComponentParser.parse(ProxyPlugin.getInstance().getCommonPlugin().getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
                                    Key.of("hour", 0), Key.of("minute", 10), Key.of("second", 0))));
                        });
                    });
                }
                return;
            }

            if (time.contains("04:55:00")) {
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    ProxyPlugin.getInstance().getCommonPlugin().getDatabaseRef().asOptional().ifPresent(sql -> {
                        sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
                            player.sendMessage(ComponentParser.parse(ProxyPlugin.getInstance().getCommonPlugin().getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
                                    Key.of("hour", 0), Key.of("minute", 5), Key.of("second", 0))));
                        });
                    });
                }
                return;
            }

            if (time.contains("04:59:00")) {
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    ProxyPlugin.getInstance().getCommonPlugin().getDatabaseRef().asOptional().ifPresent(sql -> {
                        sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
                            player.sendMessage(ComponentParser.parse(ProxyPlugin.getInstance().getCommonPlugin().getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
                                    Key.of("hour", 0), Key.of("minute", 1), Key.of("second", 0))));
                        });
                    });
                }
                return;
            }

            if (time.contains("04:59:30")) {
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    ProxyPlugin.getInstance().getCommonPlugin().getDatabaseRef().asOptional().ifPresent(sql -> {
                        sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
                            player.sendMessage(ComponentParser.parse(ProxyPlugin.getInstance().getCommonPlugin().getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
                                    Key.of("hour", 0), Key.of("minute", 0), Key.of("second", 30))));
                        });
                    });
                    return;
                }

                if (time.contains("04:59:50")) {
                    for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                        ProxyPlugin.getInstance().getCommonPlugin().getDatabaseRef().asOptional().ifPresent(sql -> {
                            sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
                                player.sendMessage(ComponentParser.parse(ProxyPlugin.getInstance().getCommonPlugin().getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
                                        Key.of("hour", 0), Key.of("minute", 0), Key.of("second", 10))));
                            });
                        });
                    }
                }
            }

            if (time.contains("04:59:55")) {
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    ProxyPlugin.getInstance().getCommonPlugin().getDatabaseRef().asOptional().ifPresent(sql -> {
                        sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
                            player.sendMessage(ComponentParser.parse(ProxyPlugin.getInstance().getCommonPlugin().getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_ALMOST, data.getLanguage(),
                                    Key.of("hour", 0), Key.of("minute", 0), Key.of("second", 5))));
                        });
                    });
                }
            }

            if (time.contains("05:00:00")) {
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    ProxyPlugin.getInstance().getCommonPlugin().getDatabaseRef().asOptional().ifPresent(sql -> {
                        sql.getPlayer(player.getUniqueId()).thenAccept(data -> {
                            player.sendMessage(ComponentParser.parse(ProxyPlugin.getInstance().getCommonPlugin().getMessageManager().translate(CommandMessages.SYSTEM_NETWORK_RESTART_NOW, data.getLanguage())));
                        });
                    });
                }
                ProxyServer.getInstance().getScheduler().schedule(ProxyPlugin.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        ProxyServer.getInstance().getScheduler().cancel(ProxyPlugin.getInstance());
                        injector.instance(PermissionManagement.class).reload();
                        injector.instance(GroupConfigurationProvider.class).reload();
                        injector.instance(ModuleProvider.class).stopAll();
                        for (ServiceInfoSnapshot serviceInfoSnapshot : injector.instance(CloudServiceProvider.class).runningServices()) {
                            Objects.requireNonNull(injector.instance(CloudServiceProvider.class).service(serviceInfoSnapshot.serviceId().uniqueId())).provider().stopAsync();
                        }
                        injector.instance(ModuleProvider.class).startAll();
                    }
                }, 1, TimeUnit.SECONDS);
            }

        }, 1L, 1L, TimeUnit.SECONDS);
    }
}