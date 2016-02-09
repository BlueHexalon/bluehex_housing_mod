package com.bluehex.bh_housing.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/*
 * ModelNPC is essentially a copy of the standard Villager Model
 * since that's what I wanted the model to look like.  If it works
 * why change it, eh?
 */

public class ModelNPC extends ModelBiped
{
  //fields
  public ModelNPC()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      this.bipedHead = new ModelRenderer(this, 0, 0);
      this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
      this.bipedHead.setRotationPoint(0F, 0F, 0F);
      this.bipedHead.setTextureSize(64, 32);
      this.bipedHead.mirror = true;
      setRotation(this.bipedHead, 0F, 0F, 0F);
      this.bipedBody = new ModelRenderer(this, 16, 16);
      this.bipedBody.addBox(-4F, 0F, -2F, 8, 12, 4);
      this.bipedBody.setRotationPoint(0F, 0F, 0F);
      this.bipedBody.setTextureSize(64, 32);
      this.bipedBody.mirror = true;
      setRotation(this.bipedBody, 0F, 0F, 0F);
      this.bipedRightArm = new ModelRenderer(this, 40, 16);
      this.bipedRightArm.addBox(-3F, -2F, -2F, 4, 12, 4);
      this.bipedRightArm.setRotationPoint(-5F, 2F, 0F);
      this.bipedRightArm.setTextureSize(64, 32);
      this.bipedRightArm.mirror = true;
      setRotation(this.bipedRightArm, 0F, 0F, 0F);
      this.bipedLeftArm = new ModelRenderer(this, 40, 16);
      this.bipedLeftArm.addBox(-1F, -2F, -2F, 4, 12, 4);
      this.bipedLeftArm.setRotationPoint(5F, 2F, 0F);
      this.bipedLeftArm.setTextureSize(64, 32);
      this.bipedLeftArm.mirror = true;
      setRotation(this.bipedLeftArm, 0F, 0F, 0F);
      this.bipedRightLeg = new ModelRenderer(this, 0, 16);
      this.bipedRightLeg.addBox(-2F, 0F, -2F, 4, 12, 4);
      this.bipedRightLeg.setRotationPoint(-2F, 12F, 0F);
      this.bipedRightLeg.setTextureSize(64, 32);
      this.bipedRightLeg.mirror = true;
      setRotation(this.bipedRightLeg, 0F, 0F, 0F);
      this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
      this.bipedLeftLeg.addBox(-2F, 0F, -2F, 4, 12, 4);
      this.bipedLeftLeg.setRotationPoint(2F, 12F, 0F);
      this.bipedLeftLeg.setTextureSize(64, 32);
      this.bipedLeftLeg.mirror = true;
      setRotation(this.bipedLeftLeg, 0F, 0F, 0F);
  }
  
  @Override
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    this.bipedHead.render(f5);
    this.bipedBody.render(f5);
    this.bipedRightArm.render(f5);
    this.bipedLeftArm.render(f5);
    this.bipedRightLeg.render(f5);
    this.bipedLeftLeg.render(f5);
  }
  
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  @Override
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entityIn)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entityIn);
  }
}
