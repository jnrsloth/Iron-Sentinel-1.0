package com.Supernaturali.ironSentinel.entity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

import com.Supernaturali.ironSentinel.IronSentinel;
import com.Supernaturali.ironSentinel.entity.ai.EntityAIAttackFromStanding;
import com.Supernaturali.ironSentinel.entity.ai.EntityAIAttackWithinRange;
import com.Supernaturali.ironSentinel.entity.ai.EntityAIReturnToBase;
import com.Supernaturali.ironSentinel.gui.GuiSentinel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntitySentinel extends EntityTameable 
{
    /** deincrements, and a distance-to-home check is done at 0 */
    private int homeCheckTimer;
    Village villageObj;
    private int attackTimer;
    private int holdRoseTick;
    private double guardXPos;
    private double guardYPos;
    private double guardZPos;
    public EntityPlayer owner;
    private static final String __OBFID = "CL_00001652";
	public static final double MaxRadius = 10.0D;


    public EntitySentinel(World p_i1694_1_)
    {
        super(p_i1694_1_);
        this.setSize(1.4F, 2.9F);
        this.getNavigator().setAvoidsWater(true);    
        startAI();
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(23, Byte.valueOf((byte)0));
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    /**
     * main AI tick function, replaces updateEntityActionState
     */
    protected void updateAITick()
    {
        if (--this.homeCheckTimer <= 0)
        {
            this.homeCheckTimer = 70 + this.rand.nextInt(50);
            this.villageObj = this.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 32);

            if (this.villageObj == null)
            {
                this.detachHome();
            }
            else
            {
                ChunkCoordinates chunkcoordinates = this.villageObj.getCenter();
                this.setHomeArea(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, (int)((float)this.villageObj.getVillageRadius() * 0.6F));
            }
        }

        super.updateAITick();
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);       
    }

    /**
     * Decrements the entity's air supply when underwater
     */
    protected int decreaseAirSupply(int p_70682_1_)
    {
        return p_70682_1_;
    }

    protected void collideWithEntity(Entity p_82167_1_)
    {
        if (p_82167_1_ instanceof IMob && this.getRNG().nextInt(20) == 0)
        {
            this.setAttackTarget((EntityLivingBase)p_82167_1_);
        }

        super.collideWithEntity(p_82167_1_);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.attackTimer > 0)
        {
            --this.attackTimer;
        }

        if (this.holdRoseTick > 0)
        {
            --this.holdRoseTick;
        }

        if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7D && this.rand.nextInt(5) == 0)
        {
            int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.posY - 0.20000000298023224D - (double)this.yOffset);
            int k = MathHelper.floor_double(this.posZ);
            Block block = this.worldObj.getBlock(i, j, k);

            if (block.getMaterial() != Material.air)
            {
                this.worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(block) + "_" + this.worldObj.getBlockMetadata(i, j, k), this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.boundingBox.minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, 4.0D * ((double)this.rand.nextFloat() - 0.5D), 0.5D, ((double)this.rand.nextFloat() - 0.5D) * 4.0D);
            }
        }
    }

    /**
     * Returns true if this entity can attack entities of the specified class.
     */
    public boolean canAttackClass(Class p_70686_1_)
    {
        return  super.canAttackClass(p_70686_1_);//if it is created by a player and the potential target is a player type
    }//this.isPlayerCreated() && EntityPlayer.class.isAssignableFrom(p_70686_1_) ? false :

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound p_70014_1_)
    {
        super.writeEntityToNBT(p_70014_1_);
        p_70014_1_.setBoolean("PlayerCreated", this.isPlayerCreated());
        p_70014_1_.setBoolean("Angry", this.isAngry());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound p_70037_1_)
    {
        super.readEntityFromNBT(p_70037_1_);
        this.setPlayerCreated(p_70037_1_.getBoolean("PlayerCreated"));
        this.setAngry(p_70037_1_.getBoolean("Angry"));

    }

    public boolean attackEntityAsMob(Entity p_70652_1_)
    {
        this.attackTimer = 10;
        this.worldObj.setEntityState(this, (byte)4);
        boolean flag = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(7 + this.rand.nextInt(15)));

        if (flag)
        {
            p_70652_1_.motionY += 0.4000000059604645D;
        }

        this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
        return flag;
    }

    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte p_70103_1_)
    {
        if (p_70103_1_ == 4)
        {
            this.attackTimer = 10;
            this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
        }
        else if (p_70103_1_ == 11)
        {
            this.holdRoseTick = 400;
        }
        else
        {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

   // @SideOnly(Side.CLIENT)
    public int getAttackTimer()
    {
        return this.attackTimer;
    }

    public void setHoldingRose(boolean p_70851_1_)
    {
        this.holdRoseTick = p_70851_1_ ? 400 : 0;
        this.worldObj.setEntityState(this, (byte)11);
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.irongolem.hit";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.irongolem.death";
    }
    
    /**
     * Plays the walking sound of the Sentinel
     */
    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
    {
        this.playSound("mob.irongolem.walk", 1.0F, 1.0F);
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        int j = this.rand.nextInt(3);
        int k;

        for (k = 0; k < j; ++k)
        {
            this.func_145778_a(Item.getItemFromBlock(Blocks.red_flower), 1, 0.0F);
        }

        k = 3 + this.rand.nextInt(3);

        for (int l = 0; l < k; ++l)
        {
            this.dropItem(Items.iron_ingot, 1);
            this.dropItem(Items.redstone, 1);
        }
    }

    public int getHoldRoseTick()
    {
        return this.holdRoseTick;
    }

    public boolean isPlayerCreated()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setPlayerCreated(boolean p_70849_1_)
    {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (p_70849_1_)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 1)));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & -2)));
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource p_70645_1_)
    {
        super.onDeath(p_70645_1_);
    }

	@Override
	public EntityAgeable createChild(EntityAgeable p_90011_1_) {
		return null;
	}

	//ADDED CODE
	
	// Called when the entity is attacked.
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
    {
//        if (this.isEntityInvulnerable())
//        {
//            return false;
//        }
       if (this.isSitting() == true)
        {
        	return false;
        }
        else
        {
            Entity entity = p_70097_1_.getEntity();
            //this.aiSit.setSitting(false);
            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))//if the entity exists and it isn't a player or arrow
            {
                p_70097_2_ = (p_70097_2_ + 1.0F) / 2.0F;
            }

            return super.attackEntityFrom(p_70097_1_, p_70097_2_);
        }
    }

    public void setTamed(boolean p_70903_1_)
    {
        super.setTamed(p_70903_1_);

        if (p_70903_1_)
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
        }
        else
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
        }
    }

  /*
   * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.    (non-Javadoc)
   * @see net.minecraft.entity.passive.EntityAnimal#interact(net.minecraft.entity.player.EntityPlayer)
   */
   public boolean interact(EntityPlayer player)
   {
       ItemStack itemstack = player.inventory.getCurrentItem();
     
       
       if (this.isTamed())
       {
           if (itemstack != null)
           {
               if (itemstack.getItem() instanceof ItemFood)
               {
                   ItemFood itemfood = (ItemFood)itemstack.getItem();
                   //TODO change item that increases health.
                   if (itemfood.isWolfsFavoriteMeat() && this.dataWatcher.getWatchableObjectFloat(18) < 20.0F) //datawatcher index 18 = health 
                   {
                       if (!player.capabilities.isCreativeMode)
                       {
                           --itemstack.stackSize;
                       }

                       this.heal((float)itemfood.func_150905_g(itemstack));

                       if (itemstack.stackSize <= 0)
                       {
                    	   player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                       }

                       return true;
                   }
               }
               else if (this.func_152114_e(player) && player.isSneaking()){           	   //itemstack.getItem() == ItemIndex.sentinel_remote && 
            	   GuiSentinel.getInstance(this);
            	   player.openGui(IronSentinel.instance, 20, this.worldObj, 0, 0, 0);
               }
          }
      }

           if (this.func_152114_e(player) && !this.worldObj.isRemote)//this.func_152114_e(p_70085_1_) checks if it the owner
           {
        	   
        	   this.aiSit.setSitting(!this.isSitting());
               this.isJumping = false;
               this.setPathToEntity((PathEntity)null);
               this.setTarget((Entity)null);
               this.setAttackTarget((EntityLivingBase)null);
           }
       
       else if (itemstack != null && itemstack.getItem() == Items.gold_ingot && !this.isAngry() )//&& this.isTamed()==false)
       {
           if (!player.capabilities.isCreativeMode)
           {
               --itemstack.stackSize;
           }

           if (itemstack.stackSize <= 0)
           {
        	   player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
           }

           if (!this.worldObj.isRemote)
           {
               if (this.rand.nextInt(3) == 0)
               {
            	   this.owner = player;
            	   neutralAI();
                   this.setTamed(true);
                   this.setPathToEntity((PathEntity)null);
                   this.setAttackTarget((EntityLivingBase)null);
                   this.aiSit.setSitting(true);
                   this.setHealth(20.0F);
                   this.func_152115_b(player.getUniqueID().toString());
                   this.playTameEffect(true);
                   this.worldObj.setEntityState(this, (byte)7);
               }
               else
               {
                   this.playTameEffect(false);
                   this.worldObj.setEntityState(this, (byte)6);
               }
           }

           return true;
       }

       return super.interact(player);
   }

   //Determines whether this wolf is angry or not.
   public boolean isAngry()
   {
       return (this.dataWatcher.getWatchableObjectByte(23) & 2) != 0;//datawatcher index 16 represents angry
   }

  /**
   * Sets whether this golem is angry or not.
   */
   public void setAngry(boolean p_70916_1_)
   {
       byte b0 = this.dataWatcher.getWatchableObjectByte(23); //datawatcher index 16 represents angry

      if (p_70916_1_)
      {
          this.dataWatcher.updateObject(23, Byte.valueOf((byte)(b0 | 2)));
      }
      else
      {
          this.dataWatcher.updateObject(23, Byte.valueOf((byte)(b0 & -3)));
      }
   }

   protected void clearAITasks()
   {
      tasks.taskEntries.clear();
      targetTasks.taskEntries.clear();
   }

   /**
    * Sets the active target the Task system uses for tracking
    */
   public void setAttackTarget(EntityLivingBase p_70624_1_)
   {
       super.setAttackTarget(p_70624_1_);

       if (p_70624_1_ == null)
       {
           this.setAngry(false);
       }
       else if (!this.isTamed())
       {
           this.setAngry(true);
       }
   }
   /*
    * the ai that the golem starts with, acts like a normal golem without the village loyalty stuff
    */
   public void startAI(){
//	   owner.addChatMessage(new ChatComponentTranslation("START AI"));  
	   System.out.println("START AI");
	   clearAITasks(); // clear any tasks assigned in super classes, or currently running AITasks
 
       this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.0D, true));
       this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
       this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 1.0D));
       this.tasks.addTask(4, new EntityAIWander(this, 0.6D));
       this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
       this.tasks.addTask(6, new EntityAILookIdle(this));
       
       this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
       this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, true, IMob.mobSelector));
       this.setTamed(false); 
   }
   /*
    *  the ai that gets activated once tamed, gets rid of attacking the nearest mob when sitting.
    */
   public void neutralAI(){
	   this.setTamed(true); 
	   owner.addChatMessage(new ChatComponentTranslation("AI now set to Neutral"));
	   clearAITasks(); // clear any tasks assigned in super classes, or currently running AITasks
       this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.0D, true));
       this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
       this.tasks.addTask(2, this.aiSit);
       this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
       this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
       this.tasks.addTask(6, new EntityAIWander(this, 0.6D));
       this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
       this.tasks.addTask(8, new EntityAILookIdle(this));
       
       this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
       this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
       this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
       
   }
  
   public void standGroundAI()
   		{
	   owner.addChatMessage(new ChatComponentTranslation("AI now set to StandGround"));
	   
	   clearAITasks(); // clear any tasks assigned in super classes, or currently running AITasks
//	   this.setTamed(true); 
       this.aiSit.setSitting(true);
	   this.tasks.addTask(1, new EntityAIAttackFromStanding(this, true));
       this.tasks.addTask(2, this.aiSit);
       this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
       this.tasks.addTask(4, new EntityAILookIdle(this));
//       this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.0D, true));
       
       this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, true, IMob.mobSelector));
       this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
       this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
       this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));      
   		}
   
   public void defensiveAI()
   		{
	   owner.addChatMessage(new ChatComponentTranslation("AI now set to Defensive at: "+ this.posX +" "+ this.posY +" "+ this.posZ));   
	   clearAITasks(); // clear any tasks assigned in super classes, or currently running AITasks
	   this.setGuardPoint(this.posX, this.posY, this.posZ);
	   
       this.tasks.addTask(1, new EntityAIReturnToBase(this, guardXPos, guardYPos, guardZPos, 1.5D));
       this.tasks.addTask(1, new EntityAIAttackWithinRange(this, guardXPos, guardYPos, guardZPos, MaxRadius, 1.5D)); 
       this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F)); //possible
       this.tasks.addTask(8, new EntityAILookIdle(this));      
       this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, true, IMob.mobSelector));
       this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
       this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
       this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
           
   		}
   
   public void noAttackAI()
   		{
	   owner.addChatMessage(new ChatComponentTranslation("AI now set to No Attack"));
	   clearAITasks(); // clear any tasks assigned in super classes, or currently running AITasks	   
   		}	
 
   public void setGuardPoint(double guardXPos,double guardYPos, double guardZPos)
   		{
	   this.guardXPos = guardXPos;
	   this.guardYPos = guardYPos;
	   this.guardZPos = guardZPos;   
   		}
   
   public boolean isSentinelInArea()
   		{
		if (getDistance(guardXPos, guardYPos, guardZPos) < (MaxRadius))
			{
			return true;
			}
		else
			{
			return false;
			}
   		}
   
   public boolean isSentinelOnSpot()
	   	{
		return (this.guardXPos == this.posX && 
				this.guardYPos == this.posX && 
				this.guardZPos == this.posZ);
	   	}
}	
