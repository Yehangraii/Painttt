/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shree
 */

public class LogSelection implements Runnable{
    String toolLabel;
    Logger logger;
    CheckSelected cS;
    public LogSelection(String toolLabel, Logger logger, CheckSelected cS){
        this.toolLabel = toolLabel;
        this.logger = logger;
        this.cS = cS;
    }

    @Override
    public void run() {
        long startTimer = System.currentTimeMillis();
        while(cS.selectedTool.equals(toolLabel)){
            try{
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(PainT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        long stopTimer = System.currentTimeMillis() - startTimer;
        logger.info(toolLabel + " for " + stopTimer + " ms.");
    }
}
