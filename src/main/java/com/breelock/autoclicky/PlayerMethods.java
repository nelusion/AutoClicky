package com.breelock.autoclicky;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class PlayerMethods {


    public static void attack(MinecraftClient client) {

        if (client == null ||
                client.player == null ||
                client.interactionManager == null ||
                client.world == null)
            return;


        if (client.player.isSpectator())
            return;


        HitResult target = client.crosshairTarget;


        if (target == null) {
            return;
        }


        switch (target.getType()) {


            case ENTITY:

                EntityHitResult entityHit =
                        (EntityHitResult) target;


                Entity entity =
                        entityHit.getEntity();


                client.interactionManager.attackEntity(
                        client.player,
                        entity
                );


                client.player.swingHand(
                        Hand.MAIN_HAND
                );

                break;



            case BLOCK:

                BlockHitResult blockHit =
                        (BlockHitResult) target;


                BlockPos pos =
                        blockHit.getBlockPos();


                BlockState state =
                        client.world.getBlockState(pos);


                if (!state.isAir()) {

                    client.interactionManager.attackBlock(
                            pos,
                            blockHit.getSide()
                    );

                    client.player.swingHand(
                            Hand.MAIN_HAND
                    );
                }

                break;



            case MISS:

                client.player.swingHand(
                        Hand.MAIN_HAND
                );

                break;
        }
    }



    public static void interact(MinecraftClient client) {


        if (client == null ||
                client.player == null ||
                client.interactionManager == null ||
                client.world == null)
            return;


        if (client.player.isSpectator())
            return;



        HitResult target =
                client.crosshairTarget;


        if (target == null)
            return;



        for (Hand hand : Hand.values()) {


            if (target.getType() == HitResult.Type.ENTITY) {

                if (interactEntity(
                        client,
                        (EntityHitResult) target,
                        hand
                ))
                    return;

            }


            else if (target.getType() == HitResult.Type.BLOCK) {


                if (interactBlock(
                        client,
                        (BlockHitResult) target,
                        hand
                ))
                    return;

            }



            else {

                interactItem(
                        client,
                        hand
                );
            }
        }
    }




    private static boolean interactEntity(
            MinecraftClient client,
            EntityHitResult hit,
            Hand hand
    ) {


        ActionResult result =
                client.interactionManager.interactEntity(
                        client.player,
                        hit.getEntity(),
                        hand
                );


        if (result.isAccepted()) {

            if (result.shouldSwingHand())
                client.player.swingHand(hand);


            return true;
        }


        return false;
    }




    private static boolean interactBlock(
            MinecraftClient client,
            BlockHitResult hit,
            Hand hand
    ) {


        ItemStack stack =
                client.player.getStackInHand(hand);


        int oldCount =
                stack.getCount();



        ActionResult result =
                client.interactionManager.interactBlock(
                        client.player,
                        client.world,
                        hand,
                        hit
                );



        if (result.isAccepted()) {


            if (result.shouldSwingHand()) {

                client.player.swingHand(hand);


                if (!stack.isEmpty()
                        && stack.getCount() != oldCount) {

                    client.gameRenderer
                            .firstPersonRenderer
                            .resetEquipProgress(hand);
                }
            }


            return true;
        }


        return false;
    }




    private static void interactItem(
            MinecraftClient client,
            Hand hand
    ) {


        ItemStack stack =
                client.player.getStackInHand(hand);



        if (stack.isEmpty())
            return;



        ActionResult result =
                client.interactionManager.interactItem(
                        client.player,
                        client.world,
                        hand
                );



        if (result.isAccepted()
                && result.shouldSwingHand()) {

            client.player.swingHand(hand);


            client.gameRenderer
                    .firstPersonRenderer
                    .resetEquipProgress(hand);
        }
    }
}
