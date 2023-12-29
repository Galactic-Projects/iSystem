package net.galacticprojects.common.util;

import dev.derklaro.aerogel.Inject;
import dev.derklaro.aerogel.Injector;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.driver.module.ModuleProvider;
import eu.cloudnetservice.driver.permission.PermissionManagement;
import eu.cloudnetservice.driver.provider.CloudServiceFactory;
import eu.cloudnetservice.driver.provider.CloudServiceProvider;
import eu.cloudnetservice.driver.provider.SpecificCloudServiceProvider;
import eu.cloudnetservice.driver.registry.ServiceRegistry;
import eu.cloudnetservice.modules.bridge.player.CloudPlayer;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import eu.cloudnetservice.modules.labymod.LabyModManagement;

/** This is a CloudNetDriver inspired by Dytanic
 *  for the new CloudNet Version 4.0.0-RC9 - Blizzard
 */
public class CloudNetDriver {

    private ServiceRegistry registry;
    private Injector injector;
    private InjectionLayer<? extends dev.derklaro.aerogel.Injector> injectionLayer = InjectionLayer.ext();


    /** Please use this Method and make a new Object in the Mainclass */
    @Inject
    public CloudNetDriver() {
        injector = InjectionLayer.ext().injector();
        registry = injector.instance(ServiceRegistry.class);
    }

    /**
     * If the CloudNetDriver Instance is null, it also returns a NullPointerException
     * @return PlayerManager
     */
    public PlayerManager cloudPlayer() {
        return CloudNetDriver.getAPI().getRegistry().firstProvider(PlayerManager.class);
    }

    /**
     * If the CloudNetDriver Instance is null, it also returns a NullPointerException
     * @return CloudServiceFactory
     */
    public CloudServiceFactory cloudServiceFactory() {
        return CloudNetDriver.getAPI().getRegistry().firstProvider(CloudServiceFactory.class);
    }

    /**
     * If the CloudNetDriver Instance is null, it also returns a NullPointerException
     * @return ModuleProvider
     */
    public ModuleProvider moduleProvider() {
        return CloudNetDriver.getAPI().getRegistry().firstProvider(ModuleProvider.class);
    }

    /**
     * If the CloudNetDriver Instance is null, it also returns a NullPointerException
     * @return LabyModManagement
     */
    public LabyModManagement labyModManagement() {
        return CloudNetDriver.getAPI().getRegistry().firstProvider(LabyModManagement.class);
    }

    /**
     * If the CloudNetDriver Instance is null, it also returns a NullPointerException
     * @return CloudServiceProvider
     */
    public CloudServiceProvider cloudServiceProvider() {
        return CloudNetDriver.getAPI().getRegistry().firstProvider(CloudServiceProvider.class);
    }

    /**
     * If the CloudNetDriver Instance is null, it also returns a NullPointerException
     * @return PermissionManagement
     */
    public PermissionManagement permissionManagement() {
        return CloudNetDriver.getAPI().getRegistry().firstProvider(PermissionManagement.class);
    }

    /**
     * If the CloudNetDriver Instance is null, it also returns a NullPointerException
     * @return SpecificCloudServiceProvider
     */
    private SpecificCloudServiceProvider specificCloudServiceProvider() {
        return CloudNetDriver.getAPI().getRegistry().firstProvider(SpecificCloudServiceProvider.class);
    }

    /**
     * If the CloudNetDriver Instance is null, it also returns a NullPointerException
     * @return CloudPlayer
     */
    public CloudPlayer cloudNetPlayer(String name) {
        return cloudPlayer().firstOnlinePlayer(name);
    }

    /**
     * If the CloudNetDriver Instance is null, it also returns a NullPointerException
     * @return String as the CloudServer
     */
    public String cloudServer(String player) {
        return cloudNetPlayer(player).connectedService().serverName();
    }

    /**
     * If the CloudNetDriver Instance is null, it also returns a NullPointerException
     * @return String as the name of CloudProxy
     */
    public String cloudProxy(String player) {
        return cloudNetPlayer(player).networkPlayerProxyInfo().networkService().serverName();
    }

    /**
     * If the CloudNetDriver Instance is null, it also returns a NullPointerException
     * Method the restart all running services
     */
    public void restartAllServices() {
        cloudServiceProvider().runningServicesAsync().join().forEach(services -> {
            if (services.connected()) {
                services.provider().stop();
            }
        });
    }

    /**
     * If the CloudNetDriver Instance is null, it also returns a NullPointerException
     * Reload all modules
     */
    public void reloadAllModules() {
        moduleProvider().reloadAll();
    }


    /* INTERAL */
    public static CloudNetDriver getAPI() {
        return new CloudNetDriver();
    }

    private ServiceRegistry getRegistry() {
        return registry;
    }

    @Deprecated(forRemoval = true)
    private InjectionLayer<? extends dev.derklaro.aerogel.Injector> getInjectionLayer() {
        return injectionLayer;
    }

    @Deprecated(forRemoval = true)
    private Injector getInjector() {
        return injector;
    }
}
