package com.bluehex.bh_housing.common.entity;

import java.util.Random;

import com.bluehex.bh_housing.client.model.ModelNPC;
import com.bluehex.bh_housing.client.renderer.RenderNPC;
import com.bluehex.bh_housing.common.NPCHouse;
import com.bluehex.bh_housing.common.entity.ai.EntityAINPCMoveIndoors;
import com.bluehex.bh_housing.common.entity.ai.EntityAINPCStareAtTPlayer;
import com.bluehex.bh_housing.common.entity.ai.EntityAINPCTradePlayer;
import com.bluehex.bh_housing.helpers.BlockHelper;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

/*
 * This is a custom mob, designed after the vanilla Villager
 * with a model derived from the player model.
 * 
 * Some of the code is copied from the Villager, but a good 
 * portion is from tutorials and reading through deobfuscated Minecraft code.
 * Needs to be cleaned up and made more efficient.
 * 
 * Currently has a simple AI that trades with the player, looks at them if they're near enough,
 * and will walk back to the block inside the door of their room, keeping them safe.
 */
public class EntityNPC extends EntityCreature implements IMerchant, INpc
{
	private EntityPlayer buyingPlayer;
	private MerchantRecipeList buyingList;
	private int wealth;
	private InventoryBasic villagerInventory;
	
	private NPCHouse npch;
	private int timeUntilReset;
	private boolean needsInitilization;
	private boolean isWillingToTrade;
	private String lastBuyingPlayer;
	
	public EntityNPC(World worldIn)
	{
		super(worldIn);
		super.setCustomNameTag("Timmy the Trader");
		setNPCHouse(new NPCHouse());
		
		this.villagerInventory = new InventoryBasic("Items", false, 8);
		((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
		((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAINPCTradePlayer(this));
		this.tasks.addTask(1, new EntityAINPCStareAtTPlayer(this));
		this.tasks.addTask(2, new EntityAINPCMoveIndoors(this));
		this.tasks.addTask(3, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));		
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
		getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.8D);
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
		
		getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
	}
	
	public static void spawnNewEntityNPC(World worldIn, NPCHouse npchIn)
	{
		BlockPos door = BlockHelper.findDoor(worldIn, npchIn);
		double north = npchIn.getCorner(0).getZ();
		double south = npchIn.getCorner(2).getZ();
		double west = npchIn.getCorner(0).getX();
		double east = npchIn.getCorner(1).getX();
		BlockPos spawnHere = door;
		if(door.getX() == west)
		{
			spawnHere = door.down().east();
		}
		else if(door.getX() == east)
		{
			spawnHere = door.down().west();
		}
		else if(door.getZ() == north)
		{
			spawnHere = door.down().south();
		}
		else if(door.getZ() == south)
		{
			spawnHere = door.down().north();
		}
		double x = spawnHere.getX() + 0.5D;
		double y = spawnHere.getY();
		double z = spawnHere.getZ() + 0.5D;
		if (!worldIn.isRemote)
		{
			Entity entity = new EntityNPC(worldIn);
			entity.setLocationAndAngles(x, y, z, entity.rotationYaw, 0.0F);
			worldIn.spawnEntityInWorld(entity);
			EntityNPC temp = (EntityNPC) entity;
			temp.setNPCHouse(npchIn);
		}
	}
	
	protected void updateAITasks()
	{
        if (!this.isTrading() && this.timeUntilReset > 0)
        {
            --this.timeUntilReset;

            if (this.timeUntilReset <= 0)
            {
                if (this.needsInitilization)
                {
                    for (MerchantRecipe merchantrecipe : this.buyingList)
                    {
                        if (merchantrecipe.isRecipeDisabled())
                        {
                            merchantrecipe.increaseMaxTradeUses(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
                        }
                    }
                    this.populateBuyingList();
                    this.needsInitilization = false;

                    if (this.lastBuyingPlayer != null)
                    {
                        this.worldObj.setEntityState(this, (byte)14);
                    }
                }

                this.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
            }
        }
	}
	
	public boolean isTrading()
	{
		return this.buyingPlayer != null;
	}
	
	public NPCHouse getNPCHouse()
	{
		return npch;
	}
	
	public void setNPCHouse(NPCHouse npchIn)
	{
		npch = npchIn;
	}
	
    private void populateBuyingList()
    {
        //TODO: Hook into VillagerRegistry
        EntityNPC.ITradeList[] itradelist;

        if (this.buyingList == null)
        {
            this.buyingList = new MerchantRecipeList();
        }
        this.buyingList.add(new MerchantRecipe(new ItemStack(Items.coal, 16, 0), Items.emerald));
        this.buyingList.add(new MerchantRecipe(new ItemStack(Items.iron_ingot, 4, 0), Items.emerald));
        this.buyingList.add(new MerchantRecipe(new ItemStack(Items.gold_ingot, 1, 0), Items.emerald));
        this.buyingList.add(new MerchantRecipe(new ItemStack(Items.emerald, 1, 0), new ItemStack(Items.cooked_porkchop, 4, 0)));
        this.buyingList.add(new MerchantRecipe(new ItemStack(Items.emerald, 1, 0), new ItemStack(Items.coal, 8, 0)));
        this.buyingList.add(new MerchantRecipe(new ItemStack(Items.emerald, 1, 0), new ItemStack(Items.iron_ingot, 2, 0)));
        this.buyingList.add(new MerchantRecipe(new ItemStack(Items.emerald, 2, 0), new ItemStack(Items.gold_ingot, 1, 0)));
        this.buyingList.add(new MerchantRecipe(new ItemStack(Items.emerald, 10, 0), new ItemStack(Items.diamond, 1, 0)));
    }

	@Override
	public boolean interact(EntityPlayer player)
	{
        ItemStack itemstack = player.inventory.getCurrentItem();
        boolean flag = itemstack != null && itemstack.getItem() == Items.spawn_egg;

        if (!flag && this.isEntityAlive() && !this.isTrading() && !player.isSneaking())
        {
            if (!this.worldObj.isRemote && (this.buyingList == null || this.buyingList.size() > 0))
            {
                this.setCustomer(player);
                player.displayVillagerTradeGui(this);
            }
            return true;
        }
        else
        {
            return super.interact(player);
        }
	}
	
	@Override
	protected boolean canDespawn()
	{
		return false;
	}
	
	@Override
	public void setCustomer(EntityPlayer player)
	{
		this.buyingPlayer = player;
	}

	@Override
	public EntityPlayer getCustomer()
	{
		return this.buyingPlayer;
	}


	@Override
	public MerchantRecipeList getRecipes(EntityPlayer player)
	{
		if (this.buyingList == null)
		{
			this.populateBuyingList();
		}
		return this.buyingList;
	}

	@Override
	public void setRecipes(MerchantRecipeList recipeList)
	{
	}

	@Override
	public void useRecipe(MerchantRecipe recipe)
	{
        recipe.incrementToolUses();
        this.livingSoundTime = -this.getTalkInterval();
        this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
        int i = 3 + this.rand.nextInt(4);

        if (recipe.getToolUses() == 1 || this.rand.nextInt(5) == 0)
        {
            this.timeUntilReset = 40;
            this.needsInitilization = true;
            this.isWillingToTrade = true;

            if (this.buyingPlayer != null)
            {
                this.lastBuyingPlayer = this.buyingPlayer.getCommandSenderEntity().getName();
            }
            else
            {
                this.lastBuyingPlayer = null;
            }

            i += 5;
        }

        if (recipe.getItemToBuy().getItem() == Items.emerald)
        {
            this.wealth += recipe.getItemToBuy().stackSize;
        }

        if (recipe.getRewardsExp())
        {
            this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY + 0.5D, this.posZ, i));
        }
	}

	@Override
	public void verifySellingItem(ItemStack stack) {
	}	
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tag)
	{
        super.writeEntityToNBT(tag);
        tag.setInteger("Riches", this.wealth);
        
        if (this.buyingList != null)
        {
            tag.setTag("Offers", this.buyingList.getRecipiesAsTags());
        }

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.villagerInventory.getStackInSlot(i);

            if (itemstack != null)
            {
                nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
            }
        }

        tag.setTag("Inventory", nbttaglist);
        this.npch.writeToNBT(tag);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag)
	{
        super.readEntityFromNBT(tag);
        this.wealth = tag.getInteger("Riches");

        if (tag.hasKey("Offers", 10))
        {
            NBTTagCompound nbttagcompound = tag.getCompoundTag("Offers");
            this.buyingList = new MerchantRecipeList(nbttagcompound);
        }

        NBTTagList nbttaglist = tag.getTagList("Inventory", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));

            if (itemstack != null)
            {
                this.villagerInventory.func_174894_a(itemstack);
            }
        }

        this.setCanPickUpLoot(true);
        this.npch.readFromNBT(tag);
	}
	
    public interface ITradeList
    {
        /**
         * Affects the given MerchantRecipeList to possibly add or remove MerchantRecipes.
         */
        void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random);
    }

    public static class ItemAndEmeraldToItem implements EntityNPC.ITradeList
        {
            public ItemStack field_179411_a;
            public EntityNPC.PriceInfo field_179409_b;
            public ItemStack field_179410_c;
            public EntityNPC.PriceInfo field_179408_d;

            public ItemAndEmeraldToItem(Item p_i45813_1_, EntityNPC.PriceInfo p_i45813_2_, Item p_i45813_3_, EntityNPC.PriceInfo p_i45813_4_)
            {
                this.field_179411_a = new ItemStack(p_i45813_1_);
                this.field_179409_b = p_i45813_2_;
                this.field_179410_c = new ItemStack(p_i45813_3_);
                this.field_179408_d = p_i45813_4_;
            }

            /**
             * Affects the given MerchantRecipeList to possibly add or remove MerchantRecipes.
             */
            public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
            {
                int i = 1;

                if (this.field_179409_b != null)
                {
                    i = this.field_179409_b.getPrice(random);
                }

                int j = 1;

                if (this.field_179408_d != null)
                {
                    j = this.field_179408_d.getPrice(random);
                }

                recipeList.add(new MerchantRecipe(new ItemStack(this.field_179411_a.getItem(), i, this.field_179411_a.getMetadata()), new ItemStack(Items.emerald), new ItemStack(this.field_179410_c.getItem(), j, this.field_179410_c.getMetadata())));
            }
        }

    public static class ListEnchantedBookForEmeralds implements EntityNPC.ITradeList
        {
            /**
             * Affects the given MerchantRecipeList to possibly add or remove MerchantRecipes.
             */
            public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
            {
                Enchantment enchantment = Enchantment.enchantmentsBookList[random.nextInt(Enchantment.enchantmentsBookList.length)];
                int i = MathHelper.getRandomIntegerInRange(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
                ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, i));
                int j = 2 + random.nextInt(5 + i * 10) + 3 * i;

                if (j > 64)
                {
                    j = 64;
                }

                recipeList.add(new MerchantRecipe(new ItemStack(Items.book), new ItemStack(Items.emerald, j), itemstack));
            }
        }

    public static class ListEnchantedItemForEmeralds implements EntityNPC.ITradeList
        {
            public ItemStack field_179407_a;
            public EntityNPC.PriceInfo field_179406_b;

            public ListEnchantedItemForEmeralds(Item p_i45814_1_, EntityNPC.PriceInfo p_i45814_2_)
            {
                this.field_179407_a = new ItemStack(p_i45814_1_);
                this.field_179406_b = p_i45814_2_;
            }

            /**
             * Affects the given MerchantRecipeList to possibly add or remove MerchantRecipes.
             */
            public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
            {
                int i = 1;

                if (this.field_179406_b != null)
                {
                    i = this.field_179406_b.getPrice(random);
                }

                ItemStack itemstack = new ItemStack(Items.emerald, i, 0);
                ItemStack itemstack1 = new ItemStack(this.field_179407_a.getItem(), 1, this.field_179407_a.getMetadata());
                itemstack1 = EnchantmentHelper.addRandomEnchantment(random, itemstack1, 5 + random.nextInt(15));
                recipeList.add(new MerchantRecipe(itemstack, itemstack1));
            }
        }

    public static class ListItemForEmeralds implements EntityNPC.ITradeList
        {
            public ItemStack field_179403_a;
            public EntityNPC.PriceInfo field_179402_b;

            public ListItemForEmeralds(Item par1Item, EntityNPC.PriceInfo priceInfo)
            {
                this.field_179403_a = new ItemStack(par1Item);
                this.field_179402_b = priceInfo;
            }

            public ListItemForEmeralds(ItemStack stack, EntityNPC.PriceInfo priceInfo)
            {
                this.field_179403_a = stack;
                this.field_179402_b = priceInfo;
            }

            /**
             * Affects the given MerchantRecipeList to possibly add or remove MerchantRecipes.
             */
            public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
            {
                int i = 1;

                if (this.field_179402_b != null)
                {
                    i = this.field_179402_b.getPrice(random);
                }

                ItemStack itemstack;
                ItemStack itemstack1;

                if (i < 0)
                {
                    itemstack = new ItemStack(Items.emerald, 1, 0);
                    itemstack1 = new ItemStack(this.field_179403_a.getItem(), -i, this.field_179403_a.getMetadata());
                }
                else
                {
                    itemstack = new ItemStack(Items.emerald, i, 0);
                    itemstack1 = new ItemStack(this.field_179403_a.getItem(), 1, this.field_179403_a.getMetadata());
                }

                recipeList.add(new MerchantRecipe(itemstack, itemstack1));
            }
        }

    public static class PriceInfo extends Tuple<Integer, Integer>
        {
            public PriceInfo(int p_i45810_1_, int p_i45810_2_)
            {
                super(Integer.valueOf(p_i45810_1_), Integer.valueOf(p_i45810_2_));
            }

            public int getPrice(Random rand)
            {
                return ((Integer)this.getFirst()).intValue() >= ((Integer)this.getSecond()).intValue() ? ((Integer)this.getFirst()).intValue() : ((Integer)this.getFirst()).intValue() + rand.nextInt(((Integer)this.getSecond()).intValue() - ((Integer)this.getFirst()).intValue() + 1);
            }
        }
	
}
