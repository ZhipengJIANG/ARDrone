/**
 * @author DreamTeam
 */
package lib.ardrone.flightscenarii;

import java.util.ResourceBundle.Control;

import lib.ardrone.ARDroneEntity;
import lib.ardrone.navigationdata.*;

public class Tests extends FlightScenario {

    /**
     * to track which drone executes the scenario, useful to send commands via
     * commands like drone.getController().backward() retrieving NavData with
     * drone.getNavDataHandler().getNavDataVisionDetect().getNbDetected()
     */
    private long timeOut;//chrono d'inactivité
    private float x, y, d;//caractéristiques spatiales du tag
    private boolean tag_already_detected;

    public Tests(ARDroneEntity drone) {
        this.drone = drone;
        initialize();
    }

    public void initialize() {
        super.setName(drone.getName() + " SuiviTag"); // to setup the Thread's
        // name --> JProfiler
        timeOut = 0;
        x = 0;
        y = 0;
        d = 0;
        tag_already_detected = false;// se mettra à true dès que j'aurai détecté le premier tag.

    }

    public void run() {


        //je fais décoller le drone
        this.drone.setSpeedBF((float) 0);
        this.drone.setSpeedLR((float) 0);
        this.drone.setSpeedYaw((float) 0);
        this.drone.setSpeedUD((float) 0);
        this.drone.getController().takeOff();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.drone.getController().hover();
        /*
         * //tests de tour sur soi même. this.drone.setSpeedLR((float)-0.05);
         * this.drone.setSpeedBF((float)0); this.drone.setSpeedYaw((float)0.5);
         * this.drone.getController().move(); try { Thread.sleep(8000); } catch
         * (InterruptedException e) { // TODO Auto-generated catch block
         * e.printStackTrace(); } kill = true; //fin du test
         */

        // maintenant le thread se lance de lui même
        while (!kill) {
            long startTime = System.currentTimeMillis();// chronomètre local

            if (this.drone.getNavDataHandler().getNavDataVisionDetect().getNbDetected() != 0) {// si je détecte quelque chose
                tag_already_detected = true;// j'ai détecté un tag au moins une fois dans a vie...
                timeOut = 0;// je réinitialise mon chrono.
                x = this.drone.getNavDataHandler().getNavDataVisionDetect().getXc(0) - 500;// je récupère la donéne x du tag
                y = this.drone.getNavDataHandler().getNavDataVisionDetect().getYc(0) - 500;// je récupère la donnée y du tag
                d = this.drone.getNavDataHandler().getNavDataVisionDetect().getDist(0);// je récupère la distance du tag
                // this.drone.setSpeedYaw((float)(x-500)/500);

                //vitesse Yaw et LR
                if (Math.abs(x) > 100) {// si le x du tag est supérieur à 100 ou inférieur à -100.. je prend une zone large car je veux pas avoir à faire des coups droite-gauche
                    this.drone.setSpeedYaw((float) 0.4 * x / Math.abs(x));//je lui mets une vitesse de yaw un peu forte, de 0,4 * 1 ou -1, ça dépende de x. pas de problème de x nul puisque |x|>100
                    this.drone.setSpeedLR((float) 0.1 * x / Math.abs(x));// je voudrais avoir un effet de drift...
                } else {//sinon
                    this.drone.setSpeedYaw((float) 0);//pas de vitesse
                    this.drone.setSpeedLR((float) 0);//pas de vitesse
                }

                //vitesse UD
                if (Math.abs(y) > 100) {//je vais faire la même chose pour y
                    this.drone.setSpeedUD((float) -0.4 * y / Math.abs(y));
                } else {//sinon
                    this.drone.setSpeedUD((float) 0);//pas de vitesse
                }

                //vitesse BF
                if (Math.abs(d) < 70) {// si le tag est à moins de 70 cm
                    this.drone.setSpeedBF((float) 0.1);// je recule
                } else if (Math.abs(d) < 100) {//entre 70cm et 1m
                    this.drone.setSpeedBF((float) 0);// rien du tout
                } else if (Math.abs(d) < 200) {//entre 1m et 2m
                    this.drone.setSpeedBF((float) -0.1);//j'avance un peu
                } else if (Math.abs(d) < 300) {// entre 2m et 3m
                    this.drone.setSpeedBF((float) -0.2);// j'avance un peu plus
                } else {// à plus de 3m
                    this.drone.setSpeedBF((float) -0.4);// je bourre un peu
                }

            } else {// si aucun tag n'est détecté
                timeOut += System.currentTimeMillis() - startTime;// je mets à jour mon timeOut

                if (tag_already_detected) {// si un tag a déjà été detecté auparavant, je cherche où il peut être d'après ce que je sais de ma dernière vision.
                    this.drone.setSpeedUD((float) 0);// on considère pour l'instant que le cas où le tag est trop haut ou trop bas n'arrive pas... ca lui évitera de se retroiuver dans les cieux
                    this.drone.setSpeedLR((float) 0);//faut plus qu'il translate droite/gauche
                    if (timeOut < 1000) {// je n'ai pas détecté de tag de puis plus de 1 seconde malgré tout
                        // je ne touche qu'à la vitesse BF, je laisse le LR et Yaw comme ils étaient car ils ont la bonne mémoire.
                        if (d > 300) {// je juge que le tag était trop loin
                            this.drone.setSpeedBF((float) -0.3);// je bourre la vitesse
                            this.drone.setSpeedYaw((float) 0);//idem
                        } else if (d < 50) {//j'étais vraiment trop près
                            this.drone.setSpeedBF((float) 0);// je stoppe
                            this.drone.setSpeedYaw((float) 0);//idem
                        } else {// sinon a priori tout devrait être normal
                            this.drone.setSpeedBF((float) -0.05);// je ralentis fortement mon allure pour chercher le tag
                            if (x != 0) {
                                this.drone.setSpeedYaw((float) 0.2 * x / Math.abs(x));// je tourne sur moi même...
                            } else {
                                this.drone.setSpeedYaw((float) 0);
                            }
                        }
                    } else if (timeOut < 15000) {// ça fait plus de 1 seconde que je n'ai rien détecté mais moins de 15 secondes...
                        // il faut que j'engage la procédure d'urgence, à savoir : le tour sur moi-même. même s'il n'en a jamais détecté car il pourrait très bien en voir un... (c'est pour cela que je j'ai le test du timeout avant le test du tag_detected).
                        this.drone.setSpeedBF((float) 0);// j'arrête d'avancer
                        if (x != 0) {
                            this.drone.setSpeedYaw((float) 0.2 * x / Math.abs(x));// idem sur le yaw
                        } else {// ben il a rien fait depuis que le thread est enclenché...
                            this.drone.setSpeedYaw((float) 0);
                        }
                    } else {
                        kill = true;
                    }

                } else {//j'ai pas encore vu de tag donc j'ai pas bougé
                    if (timeOut >= 10000) {//si le timeOut en est à plus de 10 secondes...
                        kill = true;// je kill le thread
                    }//sinon je fais rien !

                }
            }
            this.drone.getController().move();// je balance le mvt du drone peu importe la situation puisque normalement tous les cas ont été vus.
        }

        this.drone.getController().landing();

    }
}
