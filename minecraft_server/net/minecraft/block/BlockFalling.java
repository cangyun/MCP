package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFalling extends Block
{
    public static boolean fallInstantly;

    public BlockFalling()
    {
        super(Material.SAND);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public BlockFalling(Material materialIn)
    {
        super(materialIn);
    }

    /**
     * Called after the block is set in the Chunk data, but before the Tile Entity is set
     */
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_)
    {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            this.checkFallable(worldIn, pos);
        }
    }

    private void checkFallable(World worldIn, BlockPos pos)
    {
        if (canFallThrough(worldIn.getBlockState(pos.down())) && pos.getY() >= 0)
        {
            int i = 32;

            if (!fallInstantly && worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32)))
            {
                if (!worldIn.isRemote)
                {
                    EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
                    this.onStartFalling(entityfallingblock);
                    worldIn.spawnEntityInWorld(entityfallingblock);
                }
            }
            else
            {
                worldIn.setBlockToAir(pos);
                BlockPos blockpos;

                for (blockpos = pos.down(); canFallThrough(worldIn.getBlockState(blockpos)) && blockpos.getY() > 0; blockpos = blockpos.down())
                {
                    ;
                }

                if (blockpos.getY() > 0)
                {
                    worldIn.setBlockState(blockpos.up(), this.getDefaultState());
                }
            }
        }
    }

    protected void onStartFalling(EntityFallingBlock fallingEntity)
    {
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 2;
    }

    public static boolean canFallThrough(IBlockState state)
    {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        return block == Blocks.FIRE || material == Material.AIR || material == Material.WATER || material == Material.LAVA;
    }

    public void onEndFalling(World worldIn, BlockPos pos, IBlockState p_176502_3_, IBlockState p_176502_4_)
    {
    }

    public void func_190974_b(World p_190974_1_, BlockPos p_190974_2_)
    {
    }
}
