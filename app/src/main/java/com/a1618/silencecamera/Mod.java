package com.a1618.silencecamera;

import android.media.MediaActionSound;

import de.robv.android.xposed.IXposedHookLoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by a1618 on 2016/6/12.
 */
public class Mod implements IXposedHookLoadPackage {
    final static boolean DEBUG = false;

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {

        if (lpparam.packageName.equals("com.google.android.GoogleCamera") || lpparam.packageName.equals("com.android.camera2")) {

            if (DEBUG)
                XposedBridge.log("SC : " + lpparam.packageName + " hooked, looking for classes and methods");

            try { //disable media action sound
                findAndHookMethod("android.media.MediaActionSound", lpparam.classLoader, "play", int.class, new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam mparam) throws Throwable {
                        if (DEBUG)
                            XposedBridge.log("SC : Prevent MediaActionSound from playing sound! ");
                        return null;
                    }
                });

            } catch (Throwable e) {

            }
        }

        // only test on google camera 3.2.045
        if (lpparam.packageName.equals("com.google.android.GoogleCamera")) {
            String[] disableSoundList = {"playBurstEnd", "playBurstStart", "playHdrPlusShotComplete", "playShotComplete", "playShutter"};

            for (String toReplace : disableSoundList) {
                try {
                    findAndHookMethod("com.android.camera.module.capture.CaptureModuleSoundPlayer", lpparam.classLoader, toReplace, new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam mparam) throws Throwable {
                            if (DEBUG)
                                XposedBridge.log("SC : Prevent CaptureModuleSoundPlayer from playing sound! ");
                            return null;
                        }
                    });
                } catch (Throwable e) {

                }
            }

        }

    }
}
