package com.runemate.jonas.AutoRegear;


import com.runemate.game.api.hybrid.input.direct.DirectInput;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.osrs.local.SpecialAttack;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingBot;
import com.runemate.ui.DefaultUI;
import lombok.extern.log4j.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@Log4j2(topic = "Banking")

public class AutoRegear extends LoopingBot implements KeyListener {
private boolean shouldRegear = false;

    @Override
    public void onStart(String... args) {
      getEventDispatcher().addListener(this);
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
        if (e.getKeyChar() == 'b' || e.getKeyChar() == 'B')  {
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


        if (!Inventory.isEmpty()){
            Bank.depositInventory();
        }


        DirectInput.send(MenuAction.forBankWithdrawal(Bank.getItems("Coif").first(), 1));
        DirectInput.send(MenuAction.forBankWithdrawal(Bank.getItems("Ava's accumulator").first(), 1));
        DirectInput.send(MenuAction.forBankWithdrawal(Bank.getItems("Amulet of power").first(), 1));
        DirectInput.send(MenuAction.forBankWithdrawal(Bank.getItems("Leather body").first(), 1));
        DirectInput.send(MenuAction.forBankWithdrawal(Bank.getItems("Dark bow").first(), 1));
        DirectInput.send(MenuAction.forBankWithdrawal(Bank.getItems("Black d'hide chaps").first(), 1));
        DirectInput.send(MenuAction.forBankWithdrawal(Bank.getItems("Black d'hide vambraces").first(), 1));
        DirectInput.send(MenuAction.forBankWithdrawal(Bank.getItems("Ring of recoil").first(), 1));
        DirectInput.send(MenuAction.forBankWithdrawal(Bank.getItems("Dragon arrow").first(), 4));
        DirectInput.send(MenuAction.forBankWithdrawal(Bank.getItems("Dragon thrownaxe").first(), 1));

        Execution.delay(600);

        DirectInput.send(MenuAction.forBankWithdrawal(Bank.getItems("Blighted karambwan").first(), 1));
        DirectInput.send(MenuAction.forBankWithdrawal(Bank.getItems("Blighted anglerfish").first(), 1));
        DirectInput.send(MenuAction.forBankWithdrawal(Bank.getItems("Divine ranging potion(1)").first(), 1));



        DefaultUI.setStatus("Closing bank...");
        Bank.close();
        Execution.delayWhile(Bank::isOpen, 10000);

        DefaultUI.setStatus("Equipping and pre potting...");

        // Wear items
        DirectInput.send(MenuAction.forSpriteItem(Inventory.getItems("Coif").first(),"Wear" ));
        DirectInput.send(MenuAction.forSpriteItem(Inventory.getItems("Amulet of power").first(),"Wear" ));
        DirectInput.send(MenuAction.forSpriteItem(Inventory.getItems("Leather body").first(),"Wear" ));
        Execution.delay(600);
        DirectInput.send(MenuAction.forSpriteItem(Inventory.getItems("Black d'hide chaps").first(),"Wear" ));
        DirectInput.send(MenuAction.forSpriteItem(Inventory.getItems("Black d'hide vambraces").first(),"Wear" ));
        DirectInput.send(MenuAction.forSpriteItem(Inventory.getItems("Ring of recoil").first(),"Wear" ));
        DirectInput.send(MenuAction.forSpriteItem(Inventory.getItems("Ava's accumulator").first(),"Wear" ));
        Execution.delay(600);
        DirectInput.send(MenuAction.forSpriteItem(Inventory.getItems("Dark bow").first(),"Wield" ));
        DirectInput.send(MenuAction.forSpriteItem(Inventory.getItems("Dragon arrow").first(),"Wield"));
        Execution.delay(600);
        DirectInput.send(MenuAction.forSpriteItem(Inventory.getItems("Blighted anglerfish").first(),"Eat" ));
        DirectInput.send(MenuAction.forSpriteItem(Inventory.getItems("Divine ranging potion(1)").first(),"Drink" ));
        DirectInput.send(MenuAction.forSpriteItem(Inventory.getItems("Blighted karambwan").first(),"Eat" ));


        DefaultUI.setStatus("Activating spec...");
        SpecialAttack.activate();

        DefaultUI.setStatus("Activating prayer...");
        Prayer.PROTECT_ITEM.activate();

        DefaultUI.setStatus("Entering arena...");
        var arena = GameObjects.newQuery().names("Arena").results().nearest();
        if (arena != null && arena.isVisible())
            DirectInput.send(MenuAction.forGameObject(arena, "Enter"));

        DefaultUI.setStatus("All done...");
    }

}