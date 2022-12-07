package com.beesechurger.flyingfamiliars.entity.custom;

import org.jetbrains.annotations.Nullable;

import com.beesechurger.flyingfamiliars.init.FFKeys;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class CloudRayEntity extends TamableAnimal implements IAnimatable
{
// Member variables and constructor/attributes:
	
	/*private AnimationFactory factory = new AnimationFactory(this);
	private static final EntityDataAccessor<Boolean> SITTING = 
			SynchedEntityData.defineId(CloudRayEntity.class, EntityDataSerializers.BOOLEAN);

	public CloudRayEntity(EntityType<? extends TamableAnimal> p_27557_, Level p_27558_)
	{
		super(p_27557_, p_27558_);
		this.setTame(false);
		this.moveControl = new FlyingMoveControl(this, 2, true);
		this.lookControl = new LookControl(this);
	}
	
	public static AttributeSupplier setAttributes()
	{
		return TamableAnimal.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 10.00)
				.add(Attributes.ATTACK_DAMAGE, 3.0f)
				.add(Attributes.ATTACK_SPEED, 2.0f)
				.add(Attributes.FLYING_SPEED, 2.0f)
				.add(Attributes.MOVEMENT_SPEED, 0.3f).build();
	}

// AI-controlling methods:
	
	protected void registerGoals()
	{
		this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
		this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1, 16, 8, true));
		this.goalSelector.addGoal(2, new FloatGoal(this));
		this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0f));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.00));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(7, (new HurtByTargetGoal(this)).setAlertOthers());
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_)
	{
		return null;
	}
	
// Sound-controlling methods:
	
	protected void playStepSound(BlockPos pos, BlockState blockIn)
	{
	}
	
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.CONDUIT_AMBIENT;
	}
	
	protected SoundEvent getHurtSound()
	{
		return SoundEvents.CONDUIT_ACTIVATE;
	}
	
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.CONDUIT_DEACTIVATE;
	}
	
	protected float getSoundVolume()
	{
		return 0.3f;
	}
	
// General boolean values:
	
    @Override
    public boolean rideableUnderWater() {
        return true;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

	public boolean isSitting()
	{
		return this.entityData.get(SITTING);
	}
	
	public boolean canBeLeashed(Player player)
	{
		return false;
	}
	
    @Override
    public boolean causeFallDamage(float p_148750_, float p_148751_, DamageSource p_148752_)
    {
        return false;
    }

    @Override
    protected void checkFallDamage(double p_27754_, boolean p_27755_, BlockState p_27756_, BlockPos p_27757_)
    {}
	
// Entity controls: 
	
	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		// Item held by player:
		ItemStack itemstack = player.getItemInHand(hand);
		Item item = itemstack.getItem();
		
		// Item used to tame:
		Item TAME_ITEM = Items.AMETHYST_SHARD;
		
		if(item == TAME_ITEM && !isTame())
		{
			if(this.level.isClientSide)
			{
				return InteractionResult.CONSUME;
			}
			else
			{
				if(!player.getAbilities().instabuild)
				{
					itemstack.shrink(1);
				}
				
				if(!ForgeEventFactory.onAnimalTame(this, player))
				{
					if(!this.level.isClientSide)
					{
						super.tame(player);
						this.navigation.recomputePath();
						this.setTarget(null);
						this.level.broadcastEntityEvent(this, (byte)7);
						setSitting(true);
					}
				}
				
				return InteractionResult.SUCCESS;
			}
		}
		
		if(isTame() && !this.level.isClientSide && hand == InteractionHand.MAIN_HAND)
		{
			if(player.isSecondaryUseActive())
			{
				setSitting(!isSitting());
				return InteractionResult.SUCCESS;
			}
			else
			{
				this.mountCloudRay(player);
				return InteractionResult.sidedSuccess(this.level.isClientSide);
			}
		}
			
		return super.mobInteract(player, hand);
	}
	
	private void mountCloudRay(Player player)
	{
		if(!this.level.isClientSide)
		{
			player.setYRot(this.getYRot());
			player.setXRot(this.getXRot());
			player.startRiding(player);
		}
	}
	
	@Override
	public void positionRider(Entity rider)
	{
		if(this.hasPassenger(rider))
		{
			double verticalOffset = this.getPassengersRidingOffset() + rider.getMyRidingOffset();
			rider.setPos(this.getX(), this.getY() + verticalOffset, this.getZ());
		}
	}
	
    @Override
    public void travel(Vec3 travelVector)
    {
        if (this.isAlive())
        {
            if (this.isVehicle() && this.canBeControlledByRider())
            {
                LivingEntity livingentity = (LivingEntity)this.getControllingPassenger();
                this.setYRot(Mth.rotLerp(0.05F, this.getYRot(), livingentity.getYRot()));
                this.yRotO = this.getYRot();
                this.setXRot(livingentity.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
                float forwardMovement = livingentity.zza;
                
                if (forwardMovement <= 0.0F)
                {
                    forwardMovement *= 0.25F;
                }

                float verticalMovement = 0;

                if (Mth.abs(livingentity.getXRot()) > 7.0F)
                {
                    verticalMovement = Mth.rotLerp(0.01F, this.getXRot(), livingentity.getXRot()) * -forwardMovement/50;
                }

                this.flyingSpeed = this.getSpeed() * 0.1F;
                
                if (this.isControlledByLocalInstance())
                {
                    this.setSpeed((float)this.getAttributeValue(Attributes.FLYING_SPEED));

                    Vec3 proposedMovement = new Vec3(0, verticalMovement, forwardMovement);

                    if (this.isInLava())
                    {
                        this.moveRelative(0.02F, proposedMovement);
                        this.move(MoverType.SELF, this.getDeltaMovement());
                        this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
                    }
                    else
                    {
                        BlockPos ground = new BlockPos(this.getX(), this.getY() - 1.0D, this.getZ());
                        float f = 0.91F;
                        
                        if (this.onGround)
                        {
                            f = this.level.getBlockState(ground).getFriction(this.level, ground, this) * 0.91F;
                        }

                        float f1 = 0.16277137F / (f * f * f);

                        this.moveRelative(this.onGround ? 0.1F * f1 : 0.1F, proposedMovement);
                        this.move(MoverType.SELF, this.getDeltaMovement());
                        this.setDeltaMovement(this.getDeltaMovement().scale(f));
                    }
                }
                else if (livingentity instanceof Player)
                {
                    this.setDeltaMovement(Vec3.ZERO);
                }

                this.calculateEntityAnimation(this, false);
                this.tryCheckInsideBlocks();
            }
            else
            {
                this.flyingSpeed = 0.02F;

                if (this.isInLava())
                {
                    this.moveRelative(0.02F, travelVector);
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
                }
                else
                {
                    BlockPos ground = new BlockPos(this.getX(), this.getY() - 1.0D, this.getZ());
                    float f = 0.91F;
                    
                    if(this.onGround)
                    {
                        f = this.level.getBlockState(ground).getFriction(this.level, ground, this) * 0.91F;
                    }

                    float f1 = 0.16277137F / (f * f * f);

                    this.moveRelative(this.onGround ? 0.1F * f1 : 0.02F, travelVector);
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    this.setDeltaMovement(this.getDeltaMovement().scale(f));
                }

                this.calculateEntityAnimation(this, false);
            }
        }
    }
	
// Controls utilities methods:
	
	public void setSitting(boolean sitting)
	{
		this.entityData.set(SITTING, sitting);
		this.setOrderedToSit(sitting);
	}
	
	@Override
	public Team getTeam()
	{
		return super.getTeam();
	}
	
    @Override
    public boolean canBeControlledByRider() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }
    
    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getFirstPassenger();
    }
	
	@Override
	public void setTame(boolean tamed)
	{
		super.setTame(tamed);
		if (tamed) 
		{
			getAttribute(Attributes.MAX_HEALTH).setBaseValue(40.00);
			getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5.00);
		}
		else
		{
			getAttribute(Attributes.MAX_HEALTH).setBaseValue(40.00);
			getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5.00);
		}
	}
	
// Save data preserving methods:
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag)
	{
		super.readAdditionalSaveData(tag);
		setSitting(tag.getBoolean("isSitting"));
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag tag)
	{
		super.addAdditionalSaveData(tag);
		tag.putBoolean("isSitting", this.isSitting());
	}
	
	@Override
	protected void defineSynchedData() 
	{
		super.defineSynchedData();
		this.entityData.define(SITTING, false);
	}*/
	
	private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(CloudRayEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(CloudRayEntity.class, EntityDataSerializers.BOOLEAN);
	
	public static final float FLIGHT_THRESHOLD = 0.1f;
	public static final float MAX_HEALTH = 50.00f;
	public static final float FOLLOW_RANGE = 16;
    public static final float FOLLOW_RANGE_FLYING = FOLLOW_RANGE * 2;
    public static final float FLYING_SPEED = 0.6f;
    public static final float MOVEMENT_SPEED = 0.3f;
    public static final float ATTACK_DAMAGE = 3.0f;
    public static final float ATTACK_SPEED = 2.0f;
    
    private static final float MAX_POS_SPEED = 0.5f;
	private static final float MAX_POS_OFFSET = 5.0f;
	private static final float DIVIDE_OFFSET = 10;
    private float offsetUp = 0;
	private float offsetDown = 0;
    
    private final Item FOOD_ITEM = Items.APPLE;
    private final Item TAME_ITEM = Items.GLOW_BERRIES;
    
    private final AnimationFactory factory = new AnimationFactory(this);

    public CloudRayEntity(EntityType<CloudRayEntity> entityType, Level level)
    {
        super(entityType, level);
        this.setTame(false);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        //this.lookControl = new LookControl(this);
    }

	public static AttributeSupplier setAttributes()
	{
		return TamableAnimal.createMobAttributes()
				.add(Attributes.MAX_HEALTH, MAX_HEALTH)
				.add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
				.add(Attributes.FLYING_SPEED, FLYING_SPEED)
				.add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED)
				.add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE)
				.add(Attributes.ATTACK_SPEED, ATTACK_SPEED)
				.build();
	}
	
	protected void registerGoals()
	{	
		this.goalSelector.addGoal(0, new SitWhenOrderedToGoal(this));
		//this.goalSelector.addGoal(1, new MeleeAttackGoal((PathfinderMob) this.getTarget(), ATTACK_DAMAGE, false));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
		this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1, FOLLOW_RANGE, 8, true));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.00));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
	}
	
// GeckoLib animation controls:
	
	private <E extends IAnimatable> PlayState predicateGeneral(AnimationEvent<E> event)
	{
        if(this.isFlying())
        {
        	event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cloud_ray.flying"));
			return PlayState.CONTINUE;
        }
        else
        {
        	if(!event.isMoving())
        	{
        		if(this.isSitting() && !this.hasExactlyOnePlayerPassenger())
        		{
        			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cloud_ray.seated_long"));
    				return PlayState.CONTINUE;
        		}

        		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cloud_ray.grounded_long"));
				return PlayState.CONTINUE;
        	}
        	else
        	{
        		if(this.isSitting() && !this.hasExactlyOnePlayerPassenger())
        		{
        			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cloud_ray.seated_long"));
    				return PlayState.CONTINUE;
        		}
        		
        		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cloud_ray.walking"));
				return PlayState.CONTINUE;
        	}
        }
    }
	
	private <E extends IAnimatable> PlayState predicateFins(AnimationEvent<E> event)
	{		
		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cloud_ray.move_fins", EDefaultLoopTypes.LOOP));
		return PlayState.CONTINUE;
	}
    
    @Override
    public void registerControllers(AnimationData data)
    {
        data.addAnimationController(new AnimationController<>(this, "controllerGeneral", 0, this::predicateGeneral));
        data.addAnimationController(new AnimationController<>(this, "controllerFins", 0, this::predicateFins));
    }

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

// Save data preservation:	

	@Override
	public void readAdditionalSaveData(CompoundTag tag)
	{
		super.readAdditionalSaveData(tag);
		setSitting(tag.getBoolean("isSitting"));
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag tag)
	{
		super.addAdditionalSaveData(tag);
		tag.putBoolean("isSitting", this.isSitting());
	}
	
	@Override
	protected void defineSynchedData() 
	{
		super.defineSynchedData();
		this.entityData.define(SITTING, false);
		this.entityData.define(FLYING, false);
	}
	
// Entity booleans:
	
    @Override
    public boolean rideableUnderWater() {
        return true;
    }
    
    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }
    
    public boolean isSitting()
	{
		return this.entityData.get(SITTING);
	}
   
 	public boolean isFlying()
 	{
 		return entityData.get(FLYING);
 	}
 	
	public boolean canBeLeashed(Player player)
	{
		return false;
	}
	
    @Override
    public boolean causeFallDamage(float p_148750_, float p_148751_, DamageSource p_148752_)
    {
        return false;
    }
    
    @Override
    protected void checkFallDamage(double p_27754_, boolean p_27755_, BlockState p_27756_, BlockPos p_27757_)
    {}
    
    public boolean shouldFly()
    {
        return isHighEnough(FLIGHT_THRESHOLD);
    }

    public boolean isHighEnough(float height)
    {
    	var pointer = blockPosition().mutable().move(0, -1, 0);
        var min = level.dimensionType().minY();
        var i = 0;

        while(i <= height && pointer.getY() > min && !level.getBlockState(pointer).getMaterial().isSolid())
            pointer.setY(getBlockY() - ++i);
    	
        return i >= height;
    }
    
// Control utilities:
	
 	public void setSitting(boolean sitting)
 	{
 		this.entityData.set(SITTING, sitting);
 		this.setOrderedToSit(sitting);
 	}
 	
 	public void setFlying(boolean flying)
    {
        this.entityData.set(FLYING, flying);
    }
 	
 	@Override
 	public Team getTeam()
 	{
 		return super.getTeam();
 	}
 	
    @Override
    public boolean canBeControlledByRider()
    {
    	return this.getControllingPassenger() instanceof LivingEntity;
    }
    
    @Nullable
    @Override
    public Entity getControllingPassenger()
    {
    	return this.getFirstPassenger();
    }
    
 	@Override
    public boolean isFood(ItemStack stack)
 	{
        return stack.is(FOOD_ITEM);
    }
 	
 	public boolean isTamedFor(Player player)
    {
        return isTame() && isOwnedBy(player);
    }
 	
 	@Override
    public InteractionResult mobInteract(Player player, InteractionHand hand)
    {
 		ItemStack stack = player.getItemInHand(hand);

        InteractionResult stackResult = stack.interactLivingEntity(player, this, hand);
        if (stackResult.consumesAction()) return stackResult;

        final InteractionResult SUCCESS = InteractionResult.sidedSuccess(this.level.isClientSide);

        // tame
        if (!isTame())
        {
            if (stack.is(TAME_ITEM))
            {
                if (!player.getAbilities().instabuild)
                {
                    stack.shrink(1);
                }

                if (this.random.nextInt(10) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.setOrderedToSit(true);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level.broadcastEntityEvent(this, (byte) 6);
                }

                return InteractionResult.SUCCESS;
            }//*/
        }

        // heal
        if (getHealth() < getAttribute(Attributes.MAX_HEALTH).getValue() && stack.is(FOOD_ITEM))
        {
            heal(5);
            playSound(getEatingSound(stack), 0.7f, 1);
            stack.shrink(1);
            
            return SUCCESS;
        }

        // sit
        if (isTamedFor(player) && player.isShiftKeyDown())
        {
            if (!this.level.isClientSide)
            {
                navigation.stop();
                setSitting(!isSitting());
                
                if (isOrderedToSit()) setTarget(null);
            }
            return SUCCESS;
        }

        // ride on
        if (isTamedFor(player) && (!isFood(stack) || getHealth() < getAttribute(Attributes.MAX_HEALTH).getValue()))
        {
            if (!this.level.isClientSide)
            {
                setRidingPlayer(player);
                navigation.stop();
                setTarget(null);
            }
            
            //setSitting(false);
            return SUCCESS;
        }

        return super.mobInteract(player, hand);
    }
 	
 	@Override
    public void travel(Vec3 vec3)
    {
        float speed = (float) getAttributeValue(isFlying() ? Attributes.FLYING_SPEED : Attributes.MOVEMENT_SPEED) * 0.25f;

        if (canBeControlledByRider())
        {
            LivingEntity driver = (LivingEntity) getControllingPassenger();
            double moveSideways = vec3.x;
            double verticalMove = vec3.y;
            double forwardMove = Math.min(Math.abs(driver.zza) + Math.abs(driver.xxa), 1);

            // rotate head to match driver
            float yaw = driver.yHeadRot;
            if (forwardMove > 0) // rotate in the direction of the drivers controls
                yaw += (float) Mth.atan2(driver.zza, driver.xxa) * (180f / (float) Math.PI) - 90;
            yHeadRot = yaw;
            
            // rotate body towards the head
            setYRot(Mth.rotateIfNecessary(yHeadRot, getYRot(), 8));
            setXRot(driver.getXRot());

            if (isControlledByLocalInstance()) // Client applies motion
            {
                if(isFlying())
                {
                    forwardMove = forwardMove > 0 ? forwardMove : 0;
                    if(FFKeys.ascend.isDown()) verticalMove += 0.6;
                    if(FFKeys.descend.isDown()) verticalMove += -0.6;
                    if (forwardMove > 0) verticalMove += -driver.getXRot() * (Math.PI / 180);
                }
                else if(FFKeys.ascend.isDown()) jumpFromGround();

                vec3 = new Vec3(moveSideways, verticalMove, forwardMove);
                setSpeed(speed);
            }
            else if (driver instanceof Player)
            {
                calculateEntityAnimation(this, true);
                setDeltaMovement(Vec3.ZERO);
                return;
            }
        }

        if (isFlying())
        {
        	// allows motion
            moveRelative(speed, vec3);
            move(MoverType.SELF, getDeltaMovement());
            
            // bobbing up and down while flying
            if (getDeltaMovement().lengthSqr() < 0.1) setDeltaMovement(getDeltaMovement().add(0, Math.sin((tickCount / 6f)) * 0.01, 0));
            
            // impose speed limit, and acceleration/deceleration
            setDeltaMovement(getDeltaMovement().scale(0.9f));
            
            calculateEntityAnimation(this, true);
        }
        else super.travel(vec3);
    }

 	@Override
    public void tick()
    {
        super.tick();

        if (!this.level.isClientSide)
        {
            // update flying state based on the distance to the ground
            boolean flying = shouldFly();
            if (flying != isFlying())
            {
                // notify client
                setFlying(flying);

                // update AI follow range (needs to be updated before creating
                // new PathNavigate!)
                getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(flying? FOLLOW_RANGE_FLYING : FOLLOW_RANGE);

                // update pathfinding method
                if (flying) navigation = new FlyingPathNavigation(this, level);
                else navigation = new GroundPathNavigation(this, level);
            }
        }
    }

 	public void setRidingPlayer(Player player)
    {
        player.setYRot(getYRot());
        player.setXRot(getXRot());
        player.startRiding(this);
    }    
    
    @Override
    public void positionRider(Entity rider)
    {      
        if (this.hasPassenger(rider))
        {
        	float posOffset = 0;
        	
        	if(isFlying())
        	{
	        	if(FFKeys.ascend.isDown() && FFKeys.descend.isDown())
	            {
	            	offsetUp += offsetUp == 0 ? 0 : MAX_POS_SPEED;
	            	offsetDown -= offsetDown == 0 ? 0 : MAX_POS_SPEED;
	            }
	            
	            if(FFKeys.ascend.isDown() && !FFKeys.descend.isDown()) offsetUp -= offsetUp > -MAX_POS_OFFSET ? MAX_POS_SPEED : 0;
	            else offsetUp += offsetUp == 0 ? 0 : MAX_POS_SPEED;
	            
	            if(FFKeys.descend.isDown() && !FFKeys.ascend.isDown()) offsetDown += offsetDown < MAX_POS_OFFSET ? MAX_POS_SPEED : 0;
	            else offsetDown -= offsetDown == 0 ? 0 : MAX_POS_SPEED;
	            
	            if(offsetUp < -MAX_POS_OFFSET) posOffset = -MAX_POS_OFFSET;
	            else if(offsetDown > MAX_POS_OFFSET) posOffset = MAX_POS_OFFSET;
	            else posOffset = (offsetUp + offsetDown) / DIVIDE_OFFSET;
        	}
        	
        	double zOffset = isFlying() ? 2.0 : 2.2;
        	
            Vec3 pos = new Vec3(0, (getPassengersRidingOffset() + rider.getMyRidingOffset()), getScale() - zOffset)
                    .yRot((float) Math.toRadians(-yBodyRot))
                    .add(position());
            rider.setPos(pos.x, pos.y + posOffset, pos.z);

            // fix rider rotation
            if (rider instanceof LivingEntity)
            {
                LivingEntity driver = ((LivingEntity) rider);
                driver.xRotO = driver.getXRot();
                driver.yRotO = driver.getYRot();
                driver.yBodyRot = yBodyRot;
            }
        }
    }
    
    @Override
    public double getPassengersRidingOffset()
    {
        if(isFlying())
    	{
    		return ((double)this.getDimensions(this.getPose()).height * 0.2);
    	}
    	
    	return ((double)this.getDimensions(this.getPose()).height * 0.25);
    }
    
    @Override
    public void onPassengerTurned(Entity rider)
    {
    	rider.setYBodyRot(this.getYRot());
    }
    
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob cloudRay) {
        return null;
    }
    
    @Override
    protected int getExperienceReward(Player player) {
        return 12 + this.level.random.nextInt(5);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level)
    {
        return 10.0F;
    }
    
    @Override
    protected PathNavigation createNavigation(Level level)
    {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(false);
        flyingpathnavigation.setCanPassDoors(false);
        return flyingpathnavigation;
    }
}
