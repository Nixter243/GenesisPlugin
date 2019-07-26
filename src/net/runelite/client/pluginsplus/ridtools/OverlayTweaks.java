package net.runelite.client.pluginsplus.ridtools;

import com.runeliteplus.core.tweaks.Tweak;
import javassist.CtMethod;


public class OverlayTweaks {

    static class VarpsTweak extends Tweak {

        @Override
        public String className() {
            return "client";
        }

        @Override
        public String methodName() {
            return "getVarbitValue";
        }

        @Override
        public String beforeCode(CtMethod ctMethod) {
            return "";
        }

        @Override
        public String afterCode(CtMethod ctMethod) {
            return "if (false)";
        }

        @Override
        public String[] importClasses() {
            return new String[0];
        }
    }

    static class PluginGameObjects extends Tweak {

        @Override
        public String className() {
            return "net.runelite.client.plugins.agility.AgilityPlugin";
        }

        @Override
        public String methodName() {
            return "onTileObject";
        }

        @Override
        public String beforeCode(CtMethod ctMethod) {
            return "obstacles.remove(oldObject);" +
                    "if (newObject == null)" +
                    "{" +
                    "return;" +
                    "}" +
                    "obstacles.put(newObject, tile);" +
                    "} return;";
        }

        @Override
        public String afterCode(CtMethod ctMethod) {
            return "obstacles.remove(oldObject);" +
                    "if (newObject == null)" +
                    "{" +
                    "return;" +
                    "}" +
                    "obstacles.put(newObject, tile);" +
                    "ridtools.GenesisPlugin.objs.put(newObject, tile);" +
                    "return;";
        }

        @Override
        public String[] importClasses() {
            return new String[]{ "ridtools.GenesisPlugin" };
        }
    }

    static class OverlayUtils extends Tweak {

        @Override
        public String className() {
            return "net.runelite.client.ui.overlay.OverlayUtil";
        }

        @Override
        public String methodName() {
            return "renderPolygon";
        }

        @Override
        public String beforeCode(CtMethod ctMethod) {
            return "graphics.setColor(color);" +
                    "graphics.drawPolygon(poly);" +
                    //"graphics.setColor(new Color(0, 0, 0, 50));" +
                    //"graphics.fillPolygon(poly);" +
                    "return;";
        }

        @Override
        public String afterCode(CtMethod ctMethod) {
            return "if (poly != null)" +
                    "{" +
                    "graphics.setColor(color);" +
                    "graphics.drawPolygon(poly);" +
                    "graphics.fillPolygon(poly);" +

                    "graphics.setColor(color.darker());" +

                    "int X = 0;"+
                    "int Y = 0;"+


                    "" +

                    "for(int i = 0; i < poly.npoints; i++) {" +
                    "    X += poly.xpoints[i];" +
                    "    Y += poly.ypoints[i];" +
                    "}" +

                    "X = X/poly.npoints;" +
                    "Y = Y/poly.npoints;" +

                    "graphics.fillOval(X-5, Y-5, 10, 10);" +

                    "return;" +
                    "}";
        }

        @Override
        public String[] importClasses() {
            return new String[0];
        }
    }

    static class NPCOverlay extends Tweak {

        @Override
        public String className() {
            return "net.runelite.client.plugins.npchighlight.NpcSceneOverlay";
        }

        @Override
        public String methodName() {
            return "renderPoly";
        }

        @Override
        public String beforeCode(CtMethod ctMethod) {
            return "if (polygon != null)" +
                    "{" +
                    "graphics.setColor(color);" +
                    "graphics.draw(polygon);" +
                    //"graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));" +
                    //"graphics.fill(polygon);" +
                    "return;" +
                    "}";
        }

        @Override
        public String afterCode(CtMethod ctMethod) {
            return "if (polygon != null) {" +
                    "graphics.setColor(color);" +
                    "graphics.fillPolygon(polygon);" +

                    "graphics.setColor(color.brighter());" +
                    "graphics.drawPolygon(polygon);" +


                    "graphics.setColor(color.darker());" +

                    "int X = 0;"+
                    "int Y = 0;"+


                    "" +

                    "for(int i = 0; i < polygon.npoints; i++) {" +
                    "    X += polygon.xpoints[i];" +
                    "    Y += polygon.ypoints[i];" +
                    "}" +

                    "X = X/polygon.npoints;" +
                    "Y = Y/polygon.npoints;" +

                    "graphics.fillOval(X-5, Y-5, 10, 10);" +

                    "return;" +
                    "}";

        }

        @Override
        public String[] importClasses() {
            return new String[0];
        }

    }




}

