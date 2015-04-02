package com.Supernaturali.ironSentinel.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
	
	public class EntityAIAttackWithinRange extends EntityAIBase
	{
	    World worldObj;
	    EntityCreature entity;
	    /** An amount of decrementing ticks that allows the entity to attack once the tick reaches 0. */
	    int attackTick;
	    /** The speed with which the mob will approach the target */
	    double speedTowardsTarget;
	    /** The PathEntity of our entity. */
	    PathEntity entityPathEntity;
	    Class classTarget;
	    private int field_75445_i;
	    private double field_151497_i;
	    private double field_151495_j;
	    private double field_151496_k;
	    private double guardXPos;
		private double guardYPos;
		private double guardZPos;
	    private static final String __OBFID = "CL_00001595";

	    private int failedPathFindingPenalty;
		private double maxRadius;

	    public EntityAIAttackWithinRange(EntityCreature entity, Class classTarget, double guardXPos, double guardYPos, double guardZPos, double radius, double speed)
	    {
	        this(entity, guardXPos, guardYPos, guardZPos, radius, speed);
	        this.classTarget = classTarget;
	    }

	    public EntityAIAttackWithinRange(EntityCreature entity ,double guardXPos, double guardYPos, double guardZPos, double radius, double speed)
	    {
	        this.entity = entity;
	        this.worldObj = entity.worldObj;
	        this.guardXPos = guardXPos;
			this.guardYPos = guardYPos;
			this.guardZPos = guardZPos;
	        this.maxRadius = radius;
	        this.speedTowardsTarget = speed;
	        this.setMutexBits(3);     
	    }

	    /**
	     * Returns whether the EntityAIBase should begin execution.
	     */
	    public boolean shouldExecute()
	    {
	        EntityLivingBase entitylivingbase = this.entity.getAttackTarget();

	        if (entitylivingbase == null)
	        {
	            return false;
	        }
	        else if (!entitylivingbase.isEntityAlive())
	        {
	            return false;
	        }
	        else if (this.classTarget != null && !this.classTarget.isAssignableFrom(entitylivingbase.getClass()))
	        {
	            return false;
	        }
	        else
	        {
	            if (-- this.field_75445_i <= 0)
	            	{            			            	
		                this.entityPathEntity = this.entity.getNavigator().getPathToEntityLiving(entitylivingbase);
		                this.field_75445_i = 4 + this.entity.getRNG().nextInt(7);
		                return this.entityPathEntity != null && this.isTargetWithinRadius(entitylivingbase) == true;
		            	
	            	}
	            else
	            {
	                return true;
	            }
	        }
	    }

	    /**
	     * Returns whether an in-progress EntityAIBase should continue executing
	     */
	    public boolean continueExecuting()
	    {
	        EntityLivingBase entitylivingbase = this.entity.getAttackTarget();
	        return entitylivingbase == null ? false : (!entitylivingbase.isEntityAlive() ? false : this.isTargetWithinRadius(entitylivingbase) ? true : false);
	    }

	    /**
	     * Execute a one shot task or start executing a continuous task
	     */
	    public void startExecuting()
	    {
	        this.entity.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
	        this.field_75445_i = 0;
	    }

	    /**
	     * Resets the task
	     */
	    public void resetTask()
	    {
	        this.entity.getNavigator().clearPathEntity();
	    }

	    /**
	     * Updates the task
	     */
	    public void updateTask()
	    {
	        EntityLivingBase entitylivingbase = this.entity.getAttackTarget();
	        this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
	        double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.boundingBox.minY, entitylivingbase.posZ);
	        double d1 = (double)(this.entity.width * 2.0F * this.entity.width * 2.0F + entitylivingbase.width);
	        --this.field_75445_i;

	        if ((this.entity.getEntitySenses().canSee(entitylivingbase)) && this.field_75445_i <= 0 && (this.field_151497_i == 0.0D && this.field_151495_j == 0.0D && this.field_151496_k == 0.0D || entitylivingbase.getDistanceSq(this.field_151497_i, this.field_151495_j, this.field_151496_k) >= 1.0D || this.entity.getRNG().nextFloat() < 0.05F))
	        {
	            this.field_151497_i = entitylivingbase.posX;
	            this.field_151495_j = entitylivingbase.boundingBox.minY;
	            this.field_151496_k = entitylivingbase.posZ;
	            this.field_75445_i = failedPathFindingPenalty + 4 + this.entity.getRNG().nextInt(7);

	            if (this.entity.getNavigator().getPath() != null)
	            {
	                PathPoint finalPathPoint = this.entity.getNavigator().getPath().getFinalPathPoint();
	                if (finalPathPoint != null && entitylivingbase.getDistanceSq(finalPathPoint.xCoord, finalPathPoint.yCoord, finalPathPoint.zCoord) < 1)
	                {
	                    failedPathFindingPenalty = 0;
	                }
	                else
	                {
	                    failedPathFindingPenalty += 10;
	                }
	            }
	            else
	            {
	                failedPathFindingPenalty += 10;
	            }

	            if (d0 > 1024.0D)
	            {
	                this.field_75445_i += 10;
	            }
	            else if (d0 > 256.0D)
	            {
	                this.field_75445_i += 5;
	            }

	            if (!this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget))
	            {
	                this.field_75445_i += 15;
	            }
	        }

	        this.attackTick = Math.max(this.attackTick - 1, 0);

	        if (d0 <= d1 && this.attackTick <= 20)
	        {
	            this.attackTick = 20;

	            if (this.entity.getHeldItem() != null)
	            {
	                this.entity.swingItem();
	            }

	            this.entity.attackEntityAsMob(entitylivingbase);
	        }
	    }
	    public boolean isTargetWithinRadius(EntityLivingBase target)
   		{
	    return target.getDistance(guardXPos, guardYPos, guardZPos) < this.maxRadius;
   		}
	}

