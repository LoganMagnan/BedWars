package rip.tilly.bedwars.villager;

import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.EntityVillager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import rip.tilly.bedwars.utils.CC;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

public class CustomVillager extends EntityVillager implements VillagerCallable {

    public CustomVillager(World world) {
        super(((CraftWorld) world).getHandle());
    }

    @Override
    public CustomVillager spawn(Location location, String name) {
        CustomVillager customVillager = new CustomVillager(location.getWorld());
        customVillager.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        ((CraftLivingEntity) customVillager.getBukkitEntity()).setRemoveWhenFarAway(false);
        customVillager.setHealth(customVillager.getMaxHealth());
        customVillager.setCustomName(CC.translate(name));
        customVillager.setCustomNameVisible(true);
        customVillager.setInvisible(false);
        ((CraftWorld) location.getWorld()).getHandle().addEntity(customVillager, CreatureSpawnEvent.SpawnReason.CUSTOM);
        this.register();
        return customVillager;
    }

    @Override
    public void register() {
        registerEntity("Villager", 120, EntityVillager.class);
    }

    private void registerEntity(String string, int paramInt, Class<EntityVillager> villagerClass) {
        try {
            ArrayList<Map> arrayList = new ArrayList();
            byte b;
            int i;
            Field[] arrayOfField;
            for (i = (arrayOfField = EntityTypes.class.getDeclaredFields()).length, b = 0; b < i; ) {
                Field field = arrayOfField[b];
                if (field.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    field.setAccessible(true);
                    arrayList.add((Map)field.get(null));
                }
                b++;
            }
            if (arrayList.get(2).containsKey(paramInt)) {
                arrayList.get(0).remove(string);
                arrayList.get(2).remove(paramInt);
            }
            Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
            method.setAccessible(true);
            method.invoke(null, villagerClass, string, paramInt);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
