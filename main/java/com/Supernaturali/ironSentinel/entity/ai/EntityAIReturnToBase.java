package com.Supernaturali.ironSentinel.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.ChatComponentTranslation;

import com.Supernaturali.ironSentinel.entity.EntitySentinel;

public class EntityAIReturnToBase extends EntityAIBase{
/*
 * this AI checks to see if the entity is at the set point and if it isn't send the entity back to that point.
 * 		this should only execute when:
 * 			1. the entity is outside of the set area
 * 			2. the entity is inside the area but does not have a target
 * 		This should continue executing when:
 * 			1. the entity is not attacked 
 * 			2. another target is not in the area  (should attack the other target)
 * 		when it executes it should:
 * 			1. figure out a path to the target
 * 			2. execute the path to the target
 */
	private double guardXPos;
	private double guardYPos;
	private double guardZPos;
	private double speed;
	private EntityLivingBase targetEntity;
	private EntityLivingBase newTargetEntity;
	private EntitySentinel entity;
	/** The PathEntity of our entity. */
    PathEntity entityPathEntity;

	public EntityAIReturnToBase(EntitySentinel entity, double guardXPos, double guardYPos, double guardZPos, double speed)
		{
		this.entity = entity;
		this.guardXPos = guardXPos;
		this.guardYPos = guardYPos;
		this.guardZPos = guardZPos;
		this.speed = speed;
		this.setMutexBits(1);
		}
	
	@Override
	public boolean shouldExecute() 
		{
		EntityLivingBase targetEntity = this.entity.getAttackTarget();
		if (entity.isSentinelOnSpot())
			{
			return false;
			}
		if (!entity.isSentinelInArea())// if the entity is outside of the set radius
			{
			return true;
			}
		if (entity.isSentinelInArea() && targetEntity == null) // if the entity is inside the radius but doesn't have a target or target is dead
			{
			return true;
			}
		else
			{
			return false;
			}
		}
	
	public void startExecuting()
		{
		targetEntity = this.entity.getAttackTarget();
		this.entity.getNavigator().tryMoveToXYZ(this.guardXPos,this.guardYPos,this.guardZPos, this.speed);
		}
	
	public boolean continueExecuting()
		{
		targetEntity = this.entity.getAttackTarget();
		this.entity.getNavigator().tryMoveToXYZ(this.guardXPos,this.guardYPos,this.guardZPos, this.speed);
		return (!this.entity.isSentinelOnSpot() ? ((targetEntity != null && targetEntity.isEntityAlive()) ? false : true) : false );
		
//		if(this.entity.isSentinelInArea() ? )
//			{
//			if (this.entity.isSentinelOnSpot() != true)
//				{
//				entity.owner.addChatMessage(new ChatComponentTranslation("test1"));   
//				if (targetEntity != null && targetEntity.isEntityAlive())//target exists and is alive
//					{
//					return false;
//					}
//				}
//			}
//		entity.owner.addChatMessage(new ChatComponentTranslation("test"));   
//		this.entity.getNavigator().tryMoveToXYZ(this.guardXPos,this.guardYPos,this.guardZPos, this.speed);
//		return true;			
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
    	
    	}
    
}


