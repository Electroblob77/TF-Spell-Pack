package electroblob.tfspellpack.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelDruidMage extends ModelBiped {

	public ModelDruidMage() {

		super(0, 0, 64, 64);

		// Robe
		bipedBody.cubeList.add(new ModelBox(bipedBody, 24, 32, -4.0F, 0.0F, -2.0F, 8, 23, 4, 0.25F, false));
		// Right sleeve
		bipedRightArm.cubeList.add(new ModelBox(bipedRightArm, 0, 32, -3.0F, -2.0F, -2.0F, 4, 12, 8, 0.25F, false));
		// Left sleeve
		bipedLeftArm.cubeList.add(new ModelBox(bipedLeftArm, 48, 32, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F, true));
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity){
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
		float f = MathHelper.sin(this.swingProgress * (float)Math.PI);
		float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float)Math.PI);
		this.bipedRightArm.rotateAngleZ = 0.0F;
		//this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
		//this.bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
		float f2 = -(float)Math.PI / 2F;
		this.bipedRightArm.rotateAngleX = f2;
		//this.bipedLeftArm.rotateAngleX = f2;
		this.bipedRightArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
		//this.bipedLeftArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
		this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		//this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		//this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
	}

	//	@Override
//	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
//		bipedHead.render(f5);
//		bipedBody.render(f5);
//		bipedRightArm.render(f5);
//		bipedLeftArm.render(f5);
//		bipedRightLeg.render(f5);
//		bipedLeftLeg.render(f5);
//	}

//	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
//		modelRenderer.rotateAngleX = x;
//		modelRenderer.rotateAngleY = y;
//		modelRenderer.rotateAngleZ = z;
//	}

}
