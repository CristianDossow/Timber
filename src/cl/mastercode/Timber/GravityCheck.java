package cl.mastercode.Timber;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class GravityCheck extends BukkitRunnable { 
	
    private final Main instance;
    private Block block;
    List<Block> chekedBlocks;
    Vector direction;
    double initialY;

    public GravityCheck(List<Block> chekedBlocks,Main plugin, Block block,Vector direction,double initialY) {
        this.instance = plugin;
        this.block = block;
        this.chekedBlocks = chekedBlocks;
        this.direction = direction;
        this.initialY = initialY;
    }
	@Override
	public void run() {
		if(!chekedBlocks.contains(block)){
			chekedBlocks.add(block);
			Location loc = block.getLocation().add(new Vector(0.5,0.5,0.5));
			Material mat = block.getType();
			byte data = block.getData();
			
			List<Block> blocks = sortList(direction,block);
			
			if(Main.isWood(mat)&&data<5){
				if(Math.abs(direction.getX())>Math.abs(direction.getZ())){
					data=(byte) (data+4);
				}
				else{
					data=(byte) (data+8);
				}
			}
			FallingBlock fb = block.getWorld().spawnFallingBlock(loc,new MaterialData(mat,data));
			
			double xf = direction.getX()/5;
			double zf = direction.getZ()/5;
			double x =direction.getX()+(xf*(block.getLocation().getBlockY()-initialY));
			double z =direction.getZ()+(zf*(block.getLocation().getBlockY()-initialY));
			fb.setVelocity(fb.getVelocity().add(direction).add(new Vector(x,0,z)));
			block.setType(Material.AIR);
			
			

			for(Block nextBlock :blocks){
				if((Main.isWood(nextBlock)||Main.isLeaves(nextBlock)) 
						&&!Main.isVerticalStated(nextBlock)){	
					
					BukkitTask task = new GravityCheck(chekedBlocks,instance,nextBlock,direction,initialY).runTaskLater(instance, 4);
				}
			}
		}
	}
	public static List<Block> sortList(Vector direc, Block block){
		List<Block> blocks = new ArrayList<>();
		if(Math.abs(direc.getX())>Math.abs(direc.getZ())){
			if(direc.getX()>0){
				blocks.add(block.getRelative(1, 0, 0));
				blocks.add(block.getRelative(0, 0, 1));
				blocks.add(block.getRelative(0, 0, -1));
				blocks.add(block.getRelative(-1, 0, 0));
				
				blocks.add(block.getRelative(1, 1, 1));
				blocks.add(block.getRelative(1, 1, -1));
				blocks.add(block.getRelative(-1, 1, 1));
				blocks.add(block.getRelative(-1, 1, -1));
				
				blocks.add(block.getRelative(1, 1, 0));
				blocks.add(block.getRelative(0, 1, 0));
				blocks.add(block.getRelative(0, 1, 1));
				blocks.add(block.getRelative(0, 1, -1));
				blocks.add(block.getRelative(-1, 1, 0));

			}else{
				blocks.add(block.getRelative(-1, 0, 0));
				blocks.add(block.getRelative(0, 0, 1));
				blocks.add(block.getRelative(0, 0, -1));
				blocks.add(block.getRelative(1, 0, 0));
				
				blocks.add(block.getRelative(-1, 1, 1));
				blocks.add(block.getRelative(-1, 1, -1));
				blocks.add(block.getRelative(1, 1, 1));
				blocks.add(block.getRelative(1, 1, -1));

				blocks.add(block.getRelative(-1, 1, 0));
				blocks.add(block.getRelative(0, 1, 0));
				blocks.add(block.getRelative(0, 1, 1));
				blocks.add(block.getRelative(0, 1, -1));
				blocks.add(block.getRelative(1, 1, 0));

			}
		}
		else{
			if(direc.getZ()>0){
				blocks.add(block.getRelative(0, 0, 1));
				blocks.add(block.getRelative(1, 0, 0));
				blocks.add(block.getRelative(-1, 0, 0));
				blocks.add(block.getRelative(0, 0, -1));
				
				blocks.add(block.getRelative(-1, 1, 1));
				blocks.add(block.getRelative(1, 1, 1));
				blocks.add(block.getRelative(-1, 1, -1));
				blocks.add(block.getRelative(1, 1, -1));

				blocks.add(block.getRelative(0, 1, 1));
				blocks.add(block.getRelative(0, 1, 0));
				blocks.add(block.getRelative(1, 1, 0));
				blocks.add(block.getRelative(-1, 1, 0));
				blocks.add(block.getRelative(0, 1, -1));
			}else{
				blocks.add(block.getRelative(0, 0, -1));
				blocks.add(block.getRelative(1, 0, 0));
				blocks.add(block.getRelative(-1, 0, 0));
				blocks.add(block.getRelative(0, 0, 1));
				
				blocks.add(block.getRelative(-1, 1, -1));
				blocks.add(block.getRelative(1, 1, -1));
				blocks.add(block.getRelative(-1, 1, 1));
				blocks.add(block.getRelative(1, 1, 1));
				
				blocks.add(block.getRelative(0, 1, -1));
				blocks.add(block.getRelative(0, 1, 0));
				blocks.add(block.getRelative(1, 1, 0));
				blocks.add(block.getRelative(-1, 1, 0));
				blocks.add(block.getRelative(0, 1, 1));
			}
		}
		
		

		return blocks;
	}
}
