package com.Supernaturali.ironSentinel.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.Supernaturali.ironSentinel.IronSentinel;
import com.Supernaturali.ironSentinel.entity.EntitySentinel;


public class GuiSentinel extends GuiScreen{
	//public static final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, Textures.MARKER_GUI);
	public static final int GUI_ID = 20;
	public int xSize = 176;
    public int ySize = 165;
	private static EntitySentinel entityObj;  
    public static final ResourceLocation texture = new ResourceLocation(IronSentinel.MODID + ":" + "textures/gui/sentinel_gui.png");
    
    public static void getInstance(EntitySentinel obj){
    	entityObj = obj;
    }
    
    public GuiSentinel() 
    {
    	 xSize = 176;
		 ySize = 165;
    }
    public void initGui()
    {
    this.buttonList.clear();

    int posX = (this.width - xSize) / 2;
    int posY = (this.height - ySize) / 2;

    this.buttonList.add(new GuiButton(0, posX+ 40, posY + 20, 100, 20, "neutral"));
    this.buttonList.add(new GuiButton(1, posX+ 40, posY + 50, 100, 20, "Stand ground"));
    this.buttonList.add(new GuiButton(2, posX+ 40, posY + 80, 100, 20, "Defensive"));
    this.buttonList.add(new GuiButton(3, posX+ 40, posY + 110, 100, 20, "No Attack"));
    }
   
    public void actionPerformed(GuiButton button)
    {
    switch(button.id)
    {
	    case 0:{
	    	this.entityObj.neutralAI();
	    	}
	    break;
	    case 1:{
	    	this.entityObj.standGroundAI();
	    	}
	    break;
	    case 2:{
	    	this.entityObj.defensiveAI();
	    	}
	    break;
	    case 3:{
	    	this.entityObj.noAttackAI();
	    	}
	    break;
	    default:
	    }
    }
	@Override
    public void drawScreen(int par1, int par2, float par3)
		{		
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);
        int posX = (this.width - xSize) / 2;
        int posY = (this.height - ySize) / 2;
        drawTexturedModalRect(posX, posY, 0, 0, xSize, ySize);
        
        super.drawScreen(0, 0, 0);
        }

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
            //draw your Gui here, only thing you need to change is the path
    	this.mc.renderEngine.bindTexture(texture);   
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
	  
	  
	    
	
	   
	
	        

