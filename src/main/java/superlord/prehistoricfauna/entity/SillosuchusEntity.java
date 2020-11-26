package superlord.prehistoricfauna.entity;

import java.util.Random;
import java.util.function.Predicate;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import superlord.prehistoricfauna.block.SillosuchusEggBlock;
import superlord.prehistoricfauna.init.BlockInit;
import superlord.prehistoricfauna.init.ModEntityTypes;
import superlord.prehistoricfauna.util.SoundHandler;

public class SillosuchusEntity extends PrehistoricEntity {
	
	private static final DataParameter<Boolean> HAS_EGG = EntityDataManager.createKey(SillosuchusEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IS_DIGGING = EntityDataManager.createKey(SillosuchusEntity.class, DataSerializers.BOOLEAN);
	private int warningSoundTicks;
	private int isDigging;
	
	public SillosuchusEntity(EntityType<? extends SillosuchusEntity> type, World world) {
		super(type, world);
	}
	
	public AgeableEntity createChild(AgeableEntity ageable) {
		SillosuchusEntity entity = new SillosuchusEntity(ModEntityTypes.SILLOSUCHUS_ENTITY, this.world);
		entity.onInitialSpawn(world, this.world.getDifficultyForLocation(new BlockPos(entity)), SpawnReason.BREEDING, (ILivingEntityData)null, (CompoundNBT)null);
		return entity;
	}

	public boolean isDigging() {
		return this.dataManager.get(IS_DIGGING);
	}
	
	private void setDigging(boolean isDigging) {
		this.isDigging = isDigging ? 1 : 0;
		this.dataManager.set(IS_DIGGING, isDigging);
	}
	
	public boolean hasEgg() {
		return this.dataManager.get(HAS_EGG);
	}
	
	private void setHasEgg(boolean hasEgg) {
		this.dataManager.set(HAS_EGG, hasEgg);
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == BlockInit.DICROIDIUM.asItem();
	}
	
	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new SillosuchusEntity.MeleeAttackGoal());
		this.goalSelector.addGoal(1, new SillosuchusEntity.PanicGoal());
		this.goalSelector.addGoal(1, new SillosuchusEntity.HurtByTargetGoal());
		this.goalSelector.addGoal(2, new SillosuchusEntity.AttackPlayerGoal());
		this.goalSelector.addGoal(2, new SillosuchusEntity.MateGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.25D));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(7, new SillosuchusEntity.LayEggGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new AvoidEntityGoal<TyrannosaurusEntity>(this, TyrannosaurusEntity.class, 7F, 1.25D, 1.25D));
		this.goalSelector.addGoal(8, new AvoidEntityGoal<AllosaurusEntity>(this, AllosaurusEntity.class, 7F, 1.25D, 1.25D));
		this.goalSelector.addGoal(8, new AvoidEntityGoal<CamarasaurusEntity>(this, CamarasaurusEntity.class, 7F, 1.25D, 1.25D));
	}
	
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25.0D);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
		this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
	}
	
	protected SoundEvent getAmbientSound() {
		return SoundHandler.SILLOSUCHUS_IDLE;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundHandler.SILLOSUCHUS_HURT;
	}
	
	protected SoundEvent getDeathSound() {
		return SoundHandler.SILLOSUCHUS_DEATH;
	}
	
	protected void playWarningSound() {
		if (this.warningSoundTicks <= 0) {
			this.playSound(SoundHandler.SILLOSUCHUS_WARN, 1.0F, this.getSoundPitch());
			this.warningSoundTicks = 40;
		}
	}
	
	protected void registerData() {
		super.registerData();
		this.dataManager.register(HAS_EGG, false);
		this.dataManager.register(IS_DIGGING, false);
	}
	
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean("HasEgg", this.hasEgg());
	}
	
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setHasEgg(compound.getBoolean("HasEgg"));
	}
	
	public void tick() {
		super.tick();
		if (this.warningSoundTicks > 0) {
			--this.warningSoundTicks;
		}
	}
	
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int)this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue()));
		if (flag) {
			this.applyEnchantments(this, entityIn);
		}
		return flag;
	}
	
	class AttackPlayerGoal extends NearestAttackableTargetGoal<PlayerEntity> {
		public AttackPlayerGoal() {
			super(SillosuchusEntity.this, PlayerEntity.class, 20, true, true, (Predicate<LivingEntity>)null);
		}
		
		public boolean shouldExecute() {
			if (SillosuchusEntity.this.isChild()) {
				return false;
			} else {
				if (super.shouldExecute()) {
					for (SillosuchusEntity sillosuchus : SillosuchusEntity.this.world.getEntitiesWithinAABB(SillosuchusEntity.class, SillosuchusEntity.this.getBoundingBox().grow(8.0D, 4.0D, 8.0D))) {
						if(sillosuchus.isChild()) {
							return false;
						}
					}
				}
				return false;
			}
		}
		
		protected double getTargetDistance() {
			return super.getTargetDistance() * 0.5D;
		}
	}
	
	class HurtByTargetGoal extends net.minecraft.entity.ai.goal.HurtByTargetGoal {
		public HurtByTargetGoal() {
			super(SillosuchusEntity.this);
		}
		
		public void startExecuting() {
			super.startExecuting();
			if(SillosuchusEntity.this.isChild()) {
				this.alertOthers();
				this.resetTask();
			}
		}
		
		protected void setAttackTarget(MobEntity mobIn, LivingEntity targetIn) {
			if(mobIn instanceof SillosuchusEntity && !mobIn.isChild()) {
				super.setAttackTarget(mobIn, targetIn);
			}
		}
	}
	
	class MeleeAttackGoal extends net.minecraft.entity.ai.goal.MeleeAttackGoal {
		public MeleeAttackGoal() {
			super(SillosuchusEntity.this, 1.25D, true);
		}
		
		protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
			double d0 = this.getAttackReachSqr(enemy);
			if (distToEnemySqr <= d0 && this.attackTick <= 0) {
				this.attackTick = 20;
				this.attacker.attackEntityAsMob(enemy);
			} else if (distToEnemySqr <= d0 * 2.0D) {
				if (this.attackTick <= 0) {
					this.attackTick = 20;
				}
				if (this.attackTick <= 10) {
					SillosuchusEntity.this.playWarningSound();
				}
			} else {
				this.attackTick = 20;
			}
		}
		
		public void resetTask() {
			super.resetTask();
		}
		
		protected double getAttackReachSqr(LivingEntity attackTarget) {
			return (double)(4.0F + attackTarget.getWidth());
		}
	}
	
	class PanicGoal extends net.minecraft.entity.ai.goal.PanicGoal {
		public PanicGoal() {
			super(SillosuchusEntity.this, 2.0D);
		}
		
		public boolean shouldExecute() {
			return !SillosuchusEntity.this.isChild() && !SillosuchusEntity.this.isBurning()  ? false : super.shouldExecute();
		}
	}
	
	static class LayEggGoal extends MoveToBlockGoal {
		private final SillosuchusEntity sillosuchus;
		
		LayEggGoal(SillosuchusEntity sillosuchus, double speedIn) {
			super(sillosuchus, speedIn, 16);
			this.sillosuchus = sillosuchus;
		}
		
		public boolean shouldExecute() {
			return this.sillosuchus.hasEgg() ? super.shouldExecute() : false;
		}
		
		public boolean shouldContinueExecuting() {
			return super.shouldContinueExecuting() && this.sillosuchus.hasEgg();
		}
		
		public void tick() {
			super.tick();
			BlockPos blockpos = new BlockPos(this.sillosuchus);
			if (!this.sillosuchus.isInWater() && this.getIsAboveDestination()) {
				if(this.sillosuchus.isDigging < 1) {
					this.sillosuchus.setDigging(true);
				} else if (this.sillosuchus.isDigging > 200) {
					World world = this.sillosuchus.world;
					world.playSound((PlayerEntity)null, blockpos, SoundEvents.ENTITY_TURTLE_LAY_EGG, SoundCategory.BLOCKS, 0.3F, 0.9F + world.rand.nextFloat() * 0.2F);
					world.setBlockState(this.destinationBlock.up(), BlockInit.SILLOSUCHUS_EGG.getDefaultState().with(SillosuchusEggBlock.EGGS, Integer.valueOf(this.sillosuchus.rand.nextInt(4) + 1)), 3);
					this.sillosuchus.setHasEgg(false);
					this.sillosuchus.setDigging(false);
					this.sillosuchus.setInLove(600);
				}
				if (this.sillosuchus.isDigging()) {
					this.sillosuchus.isDigging++;
				}
			}
		}
		
		protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
			if (!worldIn.isAirBlock(pos.up())) {
				return false;
			} else {
				Block block = worldIn.getBlockState(pos).getBlock();
				return block == Blocks.COARSE_DIRT;
			}
		}
	}
	
	static class MateGoal extends BreedGoal {
		private final SillosuchusEntity sillosuchus;
		
		MateGoal(SillosuchusEntity sillosuchus, double speedIn) {
			super(sillosuchus, speedIn);
			this.sillosuchus = sillosuchus;
		}
		
		public boolean shouldExecute() {
			return super.shouldExecute() && !this.sillosuchus.hasEgg();
		}
		
		protected void spawnBaby() {
			ServerPlayerEntity serverplayerentity = this.animal.getLoveCause();
			if (serverplayerentity == null && this.targetMate.getLoveCause() != null) {
				serverplayerentity = this.targetMate.getLoveCause();
			}
			if (serverplayerentity != null) {
				serverplayerentity.addStat(Stats.ANIMALS_BRED);
				CriteriaTriggers.BRED_ANIMALS.trigger(serverplayerentity, this.animal, this.targetMate, (AgeableEntity)null);
			}
			this.sillosuchus.setHasEgg(true);
			this.animal.resetInLove();
			this.targetMate.resetInLove();
			Random random = this.animal.getRNG();
			if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
				this.world.addEntity(new ExperienceOrbEntity(this.world, this.animal.getPosX(), this.animal.getPosY(), this.animal.getPosZ(), random.nextInt(7) + 1));
			}
		}
	}
	
}
