package com.runemate.jonas.AutoRegear;


import com.runemate.game.api.hybrid.input.direct.DirectInput;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.osrs.local.SpecialAttack;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingBot;
import com.runemate.ui.DefaultUI;
import lombok.extern.log4j.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@Log4j2(topic = "Banking")

public class AutoRegear extends LoopingBot implements KeyListener {
private boolean shouldRegear = false;

//var bp = getPlatform();



    @Override
    public void onStart(String... args) {
//        getEventDispatcher().addListener(this);
    }

    @Override
    public void onLoop() {
        if (!shouldRegear){
            return; // Do nothing until the key is pressed
        }
        performRegear();
        shouldRegear = false; // Reset flag to avoid repeated executions

    }

    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        log.info("Key Typed: {}", keyChar); // Log the key character
        DefaultUI.setStatus("Keystroke " + keyChar + " heard");
        if (e.getKeyChar() == 'b' || e.getKeyChar() == 'b')  {
            DefaultUI.setStatus("Keystroke A heard");
            shouldRegear = true;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void performRegear(){

        if (SpecialAttack.getEnergy() != 100) {
            var pool = GameObjects.newQuery().names("Pool of Refreshment").results().nearest();
            if (pool != null && pool.isVisible())
                DirectInput.send(MenuAction.forGameObject(pool, "Drink"));
            Execution.delayUntil(() -> SpecialAttack.getEnergy() == 100, 10000);
        }

        if (!Bank.isOpen()) {
            DefaultUI.setStatus("Opening bank...");
            Bank.open();
            Execution.delayUntil(Bank::isOpen, 10000);
        }

        //Equipment
        Bank.withdraw("Coif", 1);
        Bank.withdraw("Ava's accumulator", 1);
        Bank.withdraw("Amulet of power", 1);
        Bank.withdraw("Leather body", 1);
        Bank.withdraw("Dark bow", 1);
        Bank.withdraw("Black d'hide chaps", 1);
        Bank.withdraw("Black d'hide vambraces", 1);
        Bank.withdraw("Ring of recoil", 1);
        Bank.withdraw("Dragon arrow", 4);
        Bank.withdraw("Dragon thrownaxe", 1);

        //Supplies
        Bank.withdraw("Blighted karambwan", 1);
        Bank.withdraw("Blighted anglerfish", 1);
        Bank.withdraw("Divine ranging potion(1)", 1);


        DefaultUI.setStatus("Equipping and pre potting...");

        Inventory.getItems("Coif").first().interact("Wear");
        Inventory.getItems("Ava's accumulator").first().interact("Wear");
        Inventory.getItems("Amulet of power").first().interact("Wear");
        Inventory.getItems("Leather body").first().interact("Wear");
        Inventory.getItems("Dark bow").first().interact("Wear");
        Inventory.getItems("Black d'hide chaps").first().interact("Wear");
        Inventory.getItems("Black d'hide vambraces").first().interact("Wear");
        Inventory.getItems("Ring of recoil").first().interact("Wear");
        Inventory.getItems("Dragon arrow").first().interact("Wear");
        Inventory.getItems("Dragon thrownaxe").first().interact("Wear");

        Inventory.getItems("Blighted anglerfish").first().interact("Eat");
        Inventory.getItems("Divine ranging potion(1)").first().interact("Drink");
        Inventory.getItems("Blighted karambwan").first().interact("Eat");
    }

}