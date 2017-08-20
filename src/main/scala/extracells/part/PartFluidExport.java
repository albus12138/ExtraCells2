package extracells.part;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.config.SecurityPermissions;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartCollisionHelper;
import appeng.api.parts.IPartModel;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.util.AECableType;
import extracells.models.PartModels;
import extracells.util.PermissionUtil;

public class PartFluidExport extends PartFluidIO {

	@Override
	public float getCableConnectionLength(AECableType aeCableType) {
		return 5.0F;
	}

	@Override
	public boolean doWork(int rate, int TicksSinceLastCall) {
		IFluidHandler facingTank = getFacingTank();
		if (facingTank == null || !isActive())
			return false;
		List<Fluid> filter = new ArrayList<Fluid>();
		filter.add(this.filterFluids[4]);

		if (this.filterSize >= 1) {
			for (byte i = 1; i < 9; i += 2) {
				if (i != 4) {
					filter.add(this.filterFluids[i]);
				}
			}
		}

		if (this.filterSize >= 2) {
			for (byte i = 0; i < 9; i += 2) {
				if (i != 4) {
					filter.add(this.filterFluids[i]);
				}
			}
		}

		for (Fluid fluid : filter) {
			if (fluid != null) {
				IAEFluidStack stack = extractFluid(AEApi.instance().storage().createFluidStack(new FluidStack(fluid, rate * TicksSinceLastCall)), Actionable.SIMULATE);

				if (stack == null)
					continue;
				int filled = facingTank.fill(stack.getFluidStack(), true);

				if (filled > 0) {
					extractFluid(AEApi.instance().storage().createFluidStack(new FluidStack(fluid, filled)), Actionable.MODULATE);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void getBoxes(IPartCollisionHelper bch) {
		bch.addBox(6, 6, 12, 10, 10, 13);
		bch.addBox(4, 4, 13, 12, 12, 14);
		bch.addBox(5, 5, 14, 11, 11, 15);
		bch.addBox(6, 6, 15, 10, 10, 16);
		bch.addBox(6, 6, 11, 10, 10, 12);
	}

	@Override
	public double getPowerUsage() {
		return 1.0D;
	}

	@Override
	public boolean onActivate(EntityPlayer player, EnumHand hand, Vec3d pos) {
		if (PermissionUtil.hasPermission(player, SecurityPermissions.BUILD, (IPart) this)) {
			return super.onActivate(player, hand, pos);
		}
		return false;
	}

	@Override
	public IPartModel getStaticModels() {
		if(isActive() && isPowered()) {
			return PartModels.EXPORT_HAS_CHANNEL;
		} else if(isPowered()) {
			return PartModels.EXPORT_ON;
		}
		return PartModels.EXPORT_OFF;
	}

	/*@SideOnly(Side.CLIENT)
	@Override
	public void renderInventory(IPartRenderHelper rh, RenderBlocks renderer) {
		Tessellator ts = Tessellator.instance;
		rh.setTexture(TextureManager.EXPORT_SIDE.getTexture());
		rh.setBounds(6, 6, 12, 10, 10, 13);
		rh.renderInventoryBox(renderer);

		rh.setBounds(4, 4, 13, 12, 12, 14);
		rh.renderInventoryBox(renderer);

		rh.setBounds(5, 5, 14, 11, 11, 15);
		rh.renderInventoryBox(renderer);

		IIcon side = TextureManager.EXPORT_SIDE.getTexture();
		rh.setTexture(side, side, side,
				TextureManager.EXPORT_FRONT.getTexture(), side, side);
		rh.setBounds(6, 6, 15, 10, 10, 16);
		rh.renderInventoryBox(renderer);

		rh.setInvColor(AEColor.Cyan.blackVariant);
		ts.setBrightness(15 << 20 | 15 << 4);
		rh.renderInventoryFace(TextureManager.EXPORT_FRONT.getTextures()[1],
				ForgeDirection.SOUTH, renderer);

		rh.setBounds(6, 6, 11, 10, 10, 12);
		renderInventoryBusLights(rh, renderer);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderStatic(int x, int y, int z, IPartRenderHelper rh,
			RenderBlocks renderer) {
		Tessellator ts = Tessellator.instance;
		rh.setTexture(TextureManager.EXPORT_SIDE.getTexture());
		rh.setBounds(6, 6, 12, 10, 10, 13);
		rh.renderBlock(x, y, z, renderer);

		rh.setBounds(4, 4, 13, 12, 12, 14);
		rh.renderBlock(x, y, z, renderer);

		rh.setBounds(5, 5, 14, 11, 11, 15);
		rh.renderBlock(x, y, z, renderer);

		IIcon side = TextureManager.EXPORT_SIDE.getTexture();
		rh.setTexture(side, side, side,
				TextureManager.EXPORT_FRONT.getTextures()[0], side, side);
		rh.setBounds(6, 6, 15, 10, 10, 16);
		rh.renderBlock(x, y, z, renderer);

		ts.setColorOpaque_I(getHost().getColor().blackVariant);
		if (isActive())
			ts.setBrightness(15 << 20 | 15 << 4);
		rh.renderFace(x, y, z, TextureManager.EXPORT_FRONT.getTextures()[1],
				ForgeDirection.SOUTH, renderer);

		rh.setBounds(6, 6, 11, 10, 10, 12);
		renderStaticBusLights(x, y, z, rh, renderer);
	}*/
}
