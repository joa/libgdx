package com.badlogic.gdx.backends;

import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Clipboard;
import defrac.app.GenericApp;
import defrac.concurrent.Dispatchers;
import defrac.util.Platform;

import java.io.PrintWriter;

/**
 *
 */
public class DefracApplication extends GenericApp implements Application {
  protected final ApplicationListener listener;
  protected final Array<LifecycleListener> lifecycleListeners = new Array<LifecycleListener>();
  protected int logLevel = LOG_INFO;
  protected final PrintWriter stdOut = new PrintWriter(System.out);
  protected final PrintWriter stdErr = new PrintWriter(System.err);
  protected Graphics graphics;
  protected final Input input = new DefracInput();

  @Override
  protected void onCreate() {
    this.graphics = new DefracGraphics(stage());
  }

  public DefracApplication(ApplicationListener listener) {
    this.listener = listener;
  }

  @Override
  public ApplicationListener getApplicationListener() {
    return listener;
  }

  @Override
  public Graphics getGraphics() {
    return graphics;
  }

  @Override
  public Audio getAudio() {
    return null;
  }

  @Override
  public Input getInput() {
    return input;
  }

  @Override
  public Files getFiles() {
    return null;
  }

  @Override
  public Net getNet() {
    return null;
  }

  @Override
  public void debug (String tag, String message) {
    if (logLevel >= LOG_DEBUG) {
      System.out.println(tag + ": " + message);
    }
  }

  @Override
  public void debug (String tag, String message, Throwable exception) {
    if (logLevel >= LOG_DEBUG) {
      System.out.println(tag + ": " + message);
      exception.printStackTrace(stdOut);
    }
  }

  @Override
  public void log (String tag, String message) {
    if (logLevel >= LOG_INFO) {
      System.out.println(tag + ": " + message);
    }
  }

  @Override
  public void log (String tag, String message, Throwable exception) {
    if (logLevel >= LOG_INFO) {
      System.out.println(tag + ": " + message);
      exception.printStackTrace(stdOut);
    }
  }

  @Override
  public void error (String tag, String message) {
    if (logLevel >= LOG_ERROR) {
      System.err.println(tag + ": " + message);
    }
  }

  @Override
  public void error (String tag, String message, Throwable exception) {
    if (logLevel >= LOG_ERROR) {
      System.err.println(tag + ": " + message);
      exception.printStackTrace(stdErr);
    }
  }

  @Override
  public void setLogLevel (int logLevel) {
    this.logLevel = logLevel;
  }

  @Override
  public int getLogLevel() {
    return logLevel;
  }

  @Override
  public ApplicationType getType() {
    if(Platform.isAndroid()) {
      return ApplicationType.Android;
    } else if(Platform.isJVM()) {
      return ApplicationType.Desktop;
    } else if(Platform.isIOS()) {
      return ApplicationType.iOS;
    } else if(Platform.isWeb()) {
      return ApplicationType.WebGL;
    } else {
      throw new IllegalStateException("Unknown platform");
    }
  }

  @Override
  public int getVersion() {
    //TODO(joa): macro ala return android.os.Build.VERSION.SDK_INT
    return 0;
  }

  @Override
  public long getJavaHeap() {
    return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
  }

  @Override
  public long getNativeHeap() {
    //TODO(joa): macro for a5d -> return Debug.getNativeHeapAllocatedSize();
    return getJavaHeap();
  }

  @Override
  public Preferences getPreferences(String name) {
    return null; //TODO(joa): implement me
  }

  @Override
  public Clipboard getClipboard() {
    return null;
  }

  @Override
  public void postRunnable(Runnable runnable) {
    Dispatchers.FOREGROUND.exec(runnable);
  }

  @Override
  public void exit() {
  }

  @Override
  public void addLifecycleListener (LifecycleListener listener) {
    lifecycleListeners.add(listener);
  }

  @Override
  public void removeLifecycleListener (LifecycleListener listener) {
    lifecycleListeners.removeValue(listener, true);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  protected void onResume() {
    super.onResume();

    Gdx.app = this;
    Gdx.input = getInput();
    Gdx.audio = getAudio();
    Gdx.files = getFiles();
    Gdx.graphics = getGraphics();
    Gdx.net = getNet();

    for(final LifecycleListener listener : lifecycleListeners) {
      listener.resume();
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    for(final LifecycleListener listener : lifecycleListeners) {
      listener.pause();
    }
  }

  @Override
  protected void onDestroy() {
    for(final LifecycleListener listener : lifecycleListeners) {
      listener.dispose();
    }
  }
}
