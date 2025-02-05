package superlord.prehistoricfauna.common.entities;

import java.util.Random;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public abstract class AbstractBottomDwellerFishEntity extends WaterMobEntity {
	private static final DataParameter<Boolean> FROM_BUCKET = EntityDataManager.createKey(AbstractBottomDwellerFishEntity.class, DataSerializers.BOOLEAN);

	public AbstractBottomDwellerFishEntity(EntityType<? extends AbstractBottomDwellerFishEntity> type, World worldIn) {
		super(type, worldIn);
		this.moveController = new AbstractBottomDwellerFishEntity.MoveHelperController(this);
	}

	public static AttributeModifierMap.MutableAttribute func_234176_m_() {
		return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 3.0D);
	}

	public boolean preventDespawn() {
		return super.preventDespawn() || this.isFromBucket();
	}

	public static boolean canSpawn(EntityType<? extends AbstractBottomDwellerFishEntity> type, IWorld worldIn, SpawnReason reason, BlockPos p_223363_3_, Random randomIn) {
		return worldIn.getBlockState(p_223363_3_).isIn(Blocks.WATER) && worldIn.getBlockState(p_223363_3_.up()).isIn(Blocks.WATER);
	}

	public boolean canDespawn(double distanceToClosestPlayer) {
		return !this.isFromBucket() && !this.hasCustomName();
	}

	/**
	 * Will return how many at most can spawn in a chunk at once.
	 */
	public int getMaxSpawnedInChunk() {
		return 8;
	}

	protected void registerData() {
		super.registerData();
		this.dataManager.register(FROM_BUCKET, false);
	}

	private boolean isFromBucket() {
		return this.dataManager.get(FROM_BUCKET);
	}

	public void setFromBucket(boolean p_203706_1_) {
		this.dataManager.set(FROM_BUCKET, p_203706_1_);
	}

	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean("FromBucket", this.isFromBucket());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setFromBucket(compound.getBoolean("FromBucket"));
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 1.6D, 1.4D, EntityPredicates.NOT_SPECTATING::test));
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 1));
	}

	/**
	 * Returns new PathNavigateGround instance
	 */
	protected PathNavigator createNavigator(World worldIn) {
		return new SwimmerPathNavigator(this, worldIn);
	}

	public void livingTick() {
		if (!this.isInWater() && this.onGround && this.collidedVertically) {
			this.setMotion(this.getMotion().add((double)((this.rand.nextFloat() * 2.0F - 1.0F) * 0.05F), (double)0.4F, (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 0.05F)));
			this.onGround = false;
			this.isAirBorne = true;
			this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getSoundPitch());
		}

		super.livingTick();
	}

	protected ActionResultType func_230254_b_(PlayerEntity p_230254_1_, Hand p_230254_2_) {
		ItemStack itemstack = p_230254_1_.getHeldItem(p_230254_2_);
		if (itemstack.getItem() == Items.WATER_BUCKET && this.isAlive()) {
			this.playSound(SoundEvents.ITEM_BUCKET_FILL_FISH, 1.0F, 1.0F);
			itemstack.shrink(1);
			ItemStack itemstack1 = this.getFishBucket();
			this.setBucketData(itemstack1);
			if (!this.world.isRemote) {
				CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity)p_230254_1_, itemstack1);
			}

			if (itemstack.isEmpty()) {
				p_230254_1_.setHeldItem(p_230254_2_, itemstack1);
			} else if (!p_230254_1_.inventory.addItemStackToInventory(itemstack1)) {
				p_230254_1_.dropItem(itemstack1, false);
			}

			this.remove();
			return ActionResultType.func_233537_a_(this.world.isRemote);
		} else {
			return super.func_230254_b_(p_230254_1_, p_230254_2_);
		}
	}

	protected void setBucketData(ItemStack bucket) {
		if (this.hasCustomName()) {
			bucket.setDisplayName(this.getCustomName());
		}

	}

	protected abstract ItemStack getFishBucket();

	protected boolean func_212800_dy() {
		return true;
	}

	protected abstract SoundEvent getFlopSound();

	protected SoundEvent getSwimSound() {
		return SoundEvents.ENTITY_FISH_SWIM;
	}

	protected void playStepSound(BlockPos pos, BlockState blockIn) {
	}

	static class MoveHelperController extends MovementController {
		private final AbstractBottomDwellerFishEntity fish;

		MoveHelperController(AbstractBottomDwellerFishEntity fish) {
			super(fish);
			this.fish = fish;
		}

		public void tick() {
			if (this.fish.areEyesInFluid(FluidTags.WATER)) {
				this.fish.setMotion(this.fish.getMotion().add(0.0D, -0.005D, 0.0D));
			}

			if (this.action == MovementController.Action.MOVE_TO && !this.fish.getNavigator().noPath()) {
				float f = (float)(this.speed * this.fish.getAttributeValue(Attributes.MOVEMENT_SPEED));
				this.fish.setAIMoveSpeed(MathHelper.lerp(0.125F, this.fish.getAIMoveSpeed(), f));
				double d0 = this.posX - this.fish.getPosX();
				double d2 = this.posZ - this.fish.getPosZ();
				if (d0 != 0.0D || d2 != 0.0D) {
					float f1 = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
					this.fish.rotationYaw = this.limitAngle(this.fish.rotationYaw, f1, 90.0F);
					this.fish.renderYawOffset = this.fish.rotationYaw;
					if (this.fish.collidedHorizontally && this.fish.world.getBlockState(this.fish.getPosition().up()).getBlock() == Blocks.WATER) {
						this.fish.setMotion(this.fish.getMotion().add(0.0D, 0.025D, 0.0D));
					}
				}
			} else {
				this.fish.setAIMoveSpeed(0.0F);
			}
		}
	}
}

