package com.Supernaturali.ironSentinel.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.Supernaturali.ironSentinel.IronSentinel;
import com.Supernaturali.ironSentinel.entity.EntitySentinel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RedPumpkin extends Block {	
	
public IIcon[] icons = new IIcon[3];
	
public RedPumpkin(){
	super(Material.gourd);
	setBlockName(IronSentinel.MODID + "_" + "redPumpkin");
	setCreativeTab(CreativeTabs.tabBlock);
	setBlockTextureName(IronSentinel.MODID + ":" + "redPumpkin_face");
	}

@SideOnly(Side.CLIENT)
private IIcon redPumpkin_Top;
@SideOnly(Side.CLIENT)
private IIcon redPumpkin_Face;
@SideOnly(Side.CLIENT)
private IIcon redPumpkin_Side;

public IIcon getIcon(int par1, int par2)
{
	if (par1 == 4 && par2 == 0)
		return redPumpkin_Face;
	if (par1 == 1)
		return redPumpkin_Top;
	if (par2 == -1 || par1 == par2)
		return redPumpkin_Top;
	return redPumpkin_Side;
}

public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_)
{
    super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);

    
    if (p_149726_1_.getBlock(p_149726_2_, p_149726_3_ - 1, p_149726_4_) == Blocks.iron_block && p_149726_1_.getBlock(p_149726_2_, p_149726_3_ - 2, p_149726_4_) == Blocks.iron_block)
    {
        boolean flag = p_149726_1_.getBlock(p_149726_2_ - 1, p_149726_3_ - 1, p_149726_4_) == Blocks.iron_block && p_149726_1_.getBlock(p_149726_2_ + 1, p_149726_3_ - 1, p_149726_4_) == Blocks.iron_block;
        boolean flag1 = p_149726_1_.getBlock(p_149726_2_, p_149726_3_ - 1, p_149726_4_ - 1) == Blocks.iron_block && p_149726_1_.getBlock(p_149726_2_, p_149726_3_ - 1, p_149726_4_ + 1) == Blocks.iron_block;

        if (flag || flag1)
        {
            p_149726_1_.setBlock(p_149726_2_, p_149726_3_, p_149726_4_, getBlockById(0), 0, 2);
            p_149726_1_.setBlock(p_149726_2_, p_149726_3_ - 1, p_149726_4_, getBlockById(0), 0, 2);
            p_149726_1_.setBlock(p_149726_2_, p_149726_3_ - 2, p_149726_4_, getBlockById(0), 0, 2);

            if (flag)
            {
                p_149726_1_.setBlock(p_149726_2_ - 1, p_149726_3_ - 1, p_149726_4_, getBlockById(0), 0, 2);
                p_149726_1_.setBlock(p_149726_2_ + 1, p_149726_3_ - 1, p_149726_4_, getBlockById(0), 0, 2);
            }
            else
            {
                p_149726_1_.setBlock(p_149726_2_, p_149726_3_ - 1, p_149726_4_ - 1, getBlockById(0), 0, 2);
                p_149726_1_.setBlock(p_149726_2_, p_149726_3_ - 1, p_149726_4_ + 1, getBlockById(0), 0, 2);
            }

            EntitySentinel entitysentinel = new EntitySentinel(p_149726_1_);
            //entitysentinel.setPlayerCreated(true);
            entitysentinel.setLocationAndAngles((double)p_149726_2_ + 0.5D, (double)p_149726_3_ - 1.95D, (double)p_149726_4_ + 0.5D, 0.0F, 0.0F);
            p_149726_1_.spawnEntityInWorld(entitysentinel);

            for (int l = 0; l < 120; ++l)
            {
                p_149726_1_.spawnParticle("snowballpoof", (double)p_149726_2_ + p_149726_1_.rand.nextDouble(), (double)(p_149726_3_ - 2) + p_149726_1_.rand.nextDouble() * 3.9D, (double)p_149726_4_ + p_149726_1_.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
            }

            p_149726_1_.notifyBlockChange(p_149726_2_, p_149726_3_, p_149726_4_, getBlockById(0));
            p_149726_1_.notifyBlockChange(p_149726_2_, p_149726_3_ - 1, p_149726_4_, getBlockById(0));
            p_149726_1_.notifyBlockChange(p_149726_2_, p_149726_3_ - 2, p_149726_4_, getBlockById(0));

            if (flag)
            {
                p_149726_1_.notifyBlockChange(p_149726_2_ - 1, p_149726_3_ - 1, p_149726_4_, getBlockById(0));
                p_149726_1_.notifyBlockChange(p_149726_2_ + 1, p_149726_3_ - 1, p_149726_4_, getBlockById(0));
            }
            else
            {
                p_149726_1_.notifyBlockChange(p_149726_2_, p_149726_3_ - 1, p_149726_4_ - 1, getBlockById(0));
                p_149726_1_.notifyBlockChange(p_149726_2_, p_149726_3_ - 1, p_149726_4_ + 1, getBlockById(0));
            }
        }
    }
}


@Override
@SideOnly(Side.CLIENT)
public void registerBlockIcons(IIconRegister par1IconRegister)
{
    this.redPumpkin_Side = par1IconRegister.registerIcon(IronSentinel.MODID +":"+ "redPumpkin_Side");
    this.redPumpkin_Face = par1IconRegister.registerIcon(IronSentinel.MODID +":"+"redPumpkin_Face");
    this.redPumpkin_Top = par1IconRegister.registerIcon(IronSentinel.MODID +":"+"redPumpkin_Top");
}

}
	

