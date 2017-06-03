package cl.mastercode.Timber;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Animals;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class Main
extends JavaPlugin
implements Listener {
	
    public Main plugin;
    static public Main splugin;
    File file;
    YamlConfiguration cfg;
    static public Map<ArmorStand, Long> armorStands;
    public static final ConsoleCommandSender console = Bukkit.getConsoleSender();
    
    public void reload() {
        //this.file = new File(this.getDataFolder() + "/", "config.yml");
        //this.cfg = YamlConfiguration.loadConfiguration((File)this.file);
        //armorStands = new HashMap<>();
        
        //if (!this.file.exists()) {
        //    this.saveResource("settings.yml", false);
        //}
    	//System.out.println("Load Complete");
    }
    
    @Override
    public void onEnable() {
        this.plugin = this;
        splugin= this;
        
        reload();
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        PluginCommand command1 = getCommand("timber");
        command1.setExecutor(new CommandHandler());
        
        
    }
    
    @Override
    public void onDisable() {

    }
    

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onBlockBreakEvent(BlockBreakEvent e) {
    	if(!e.isCancelled() && IsAxe(e.getPlayer().getInventory().getItemInMainHand())){
    		if(e.getPlayer().hasPermission("timber.cuttree")){
    			if(isWood(e.getBlock())){
    				Block under = e.getBlock().getRelative(0, -1, 0);
    				if(under.getType().equals(Material.GRASS)||under.getType().equals(Material.DIRT)||isWood(under.getType())){
    					if(!isHorizontalStated(e.getBlock())&&!isDiagonalStated(e.getBlock())){
    						if(isWood(e.getBlock().getRelative(0, 1, 0))){
    							List<Block> chekedBlocks = new ArrayList<>();
    							Location loc1 = e.getPlayer().getLocation();
    							Location loc2 = e.getBlock().getLocation().add(new Vector(0.5,0.5,0.5));
    							//double xa = loc1.getX()-loc2.getBlockX();
    							//double za = loc1.getZ()-loc2.getBlockZ();
    							//double x = xa/(xa+za);
    							//double z = za/(xa+za);
    							double x = loc1.getDirection().getX()/5;
    							double z = loc1.getDirection().getZ()/5;
    							new GravityCheck(chekedBlocks,this,e.getBlock().getRelative(0, 1, 0),new Vector(x,0,z),loc2.getBlockY()).runTaskLater(this, 2);
    						}
    					}
    				}
    			}
    		}
    	}
    }
    
    @EventHandler
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.FALLING_BLOCK) {
        	FallingBlock block =(FallingBlock)event.getEntity();
        	if(isWood(block.getMaterial())||isLeaves(block.getMaterial())){
        		
        		if(Math.abs(block.getVelocity().getX())>Math.abs(block.getVelocity().getZ())){
        			
        			if(block.getVelocity().getX()>0){
        				Block newBlock=event.getBlock().getRelative(1, -1, 0);
        				if(newBlock.getType().equals(Material.AIR)){
        					event.getEntity().getLocation().getWorld().spawnFallingBlock(newBlock.getLocation().add(new Vector(0.5,0.75,0.5)), block.getMaterial(), block.getBlockData());
        					event.getEntity().remove();
        					//event.getEntity().teleport(newBlock.getLocation().add(new Vector(0.5,0.75,0.5)));
        					event.setCancelled(true);
        				}
        			} 
        			if(block.getVelocity().getX()<0){
        				Block newBlock=event.getBlock().getRelative(-1, -1, 0);
        				if(newBlock.getType().equals(Material.AIR)){
        					event.getEntity().getLocation().getWorld().spawnFallingBlock(newBlock.getLocation().add(new Vector(0.5,0.75,0.5)), block.getMaterial(), block.getBlockData());
        					event.getEntity().remove();
        					event.setCancelled(true);
        				}
        			}
        		}else{
        			if(block.getVelocity().getZ()>0){
        				Block newBlock=event.getBlock().getRelative(0, -1, 1);
        				if(newBlock.getType().equals(Material.AIR)){
        					event.getEntity().getLocation().getWorld().spawnFallingBlock(newBlock.getLocation().add(new Vector(0.5,0.75,0.5)), block.getMaterial(), block.getBlockData());
        					event.getEntity().remove();
        					event.setCancelled(true);
        				}
        			} 
        			if(block.getVelocity().getZ()<0){
        				Block newBlock=event.getBlock().getRelative(0, -1, -1);
        				if(newBlock.getType().equals(Material.AIR)){
        					event.getEntity().getLocation().getWorld().spawnFallingBlock(newBlock.getLocation().add(new Vector(0.5,0.75,0.5)), block.getMaterial(), block.getBlockData());
        					event.getEntity().remove();
        					event.setCancelled(true);
        				}
        			}
        		}
        	}
        }
    }
    
    static public boolean isHorizontalStated(Block block){
    	if(isWood(block.getRelative(1, 0, 0))){
    		return true;
    	}
    	if(isWood(block.getRelative(-1, 0, 0))){
    		return true;
    	}
    	if(isWood(block.getRelative(0, 0, 1))){
    		return true;
    	}
    	if(isWood(block.getRelative(0, 0, -1))){
    		return true;
    	}
    	return false;
    }
    static public boolean isDiagonalStated(Block block){
    	if(isWood(block.getRelative(1, 0, 1))){
    		return true;
    	}
    	if(isWood(block.getRelative(1, 0, -1))){
    		return true;
    	}
    	if(isWood(block.getRelative(-1, 0, 1))){
    		return true;
    	}
    	if(isWood(block.getRelative(-1, 0, -1))){
    		return true;
    	}
    	return false;
    }
    static public boolean isVerticalStated(Block block){
    	if(isSolid(block.getRelative(0, -1, 0).getType())){
    		return true;
    	}
    	return false;
    }
    static public boolean isSolid(Material material){
    	if(material.equals(Material.AIR)){
    		return false;
    	}
    	if(material.equals(Material.VINE)){
    		return false;
    	}
    	if(material.equals(Material.SAPLING)){
    		return false;
    	}
    	if(material.equals(Material.SIGN)){
    		return false;
    	}
    	if(material.equals(Material.LEAVES)){
    		return false;
    	}
    	if(material.equals(Material.LEAVES_2)){
    		return false;
    	}
    	return true;
    }
    static public boolean isLeaves(Block block){
    	return isLeaves(block.getType());
    }
    static public boolean isLeaves(Material type){
    	if(type.equals(Material.LEAVES) || type.equals(Material.LEAVES_2)){
    		return true;
    	}
    	return false;
    }
    static public boolean isWood(Block block){
    	return isWood(block.getType());
    }
    static public boolean isWood(Material type){
    	if(type.equals(Material.LOG) || type.equals(Material.LOG_2)){
    		return true;
    	}
    	return false;
    }
    static public boolean IsAxe(ItemStack item){
    	if(item.getType().equals(Material.DIAMOND_AXE)){
    		return true;
    	}
    	if(item.getType().equals(Material.IRON_AXE)){
    		return true;
    	}
    	if(item.getType().equals(Material.STONE_AXE)){
    		return true;
    	}
    	if(item.getType().equals(Material.WOOD_AXE)){
    		return true;
    	}

    	if(item.getType().equals(Material.GOLD_AXE)){
    		return true;
    	}
    	return false;
    }
}
