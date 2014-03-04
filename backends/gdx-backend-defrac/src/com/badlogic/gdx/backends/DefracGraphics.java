package com.badlogic.gdx.backends;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.GLCommon;
import defrac.display.Stage;
import defrac.event.EnterFrameEvent;
import defrac.event.Events;
import defrac.lang.Procedure;
import defrac.util.Platform;

/**
 *
 */
final class DefracGraphics implements Graphics {
  private final Stage stage;
  private final DefracGL20 gl;
  private final BufferFormat bufferFormat;
  private float ppiX, ppiY, ppcX, ppcY, density;
  private int fps = 0;
  private int frames = 0;
  private long lastTime;
  private float deltaTime;

  DefracGraphics(final Stage stage) {
    this.stage = stage;
    this.gl = new DefracGL20(stage.gl());
    Gdx.gl = gl;
    Gdx.gl20 = gl;

    //TODO(joa): incorrect buffer format!
    bufferFormat = new BufferFormat(8, 8, 8, 0, 16, 8, 0, false);
    updatePPI();
    lastTime = System.currentTimeMillis();

    Events.onEnterFrame.attach(new Procedure<EnterFrameEvent>() {
      @Override
      public void apply(EnterFrameEvent enterFrameEvent) {
        //TODO(joa): add monotonous clock
        final long currentTime = System.currentTimeMillis();
        deltaTime = (currentTime - lastTime);
        lastTime = currentTime;
        if(deltaTime >= 1000.0f) {
          fps = frames;
          frames = 0;
        } else {
          ++frames;
        }
      }
    });
  }

  private void updatePPI() {
    // TODO(joa): Need @Macro implementation
    ppiX = 96.0f;
    ppiY = 96.0f;
    ppcX = ppiX / 2.54f;
    ppcY = ppiY / 2.54f;
    density = 96.0f / 160.0f;
  }

  @Override
  public boolean isGL30Available() {
    return false;
  }

  @Override
  public GLCommon getGLCommon() {
    return gl;
  }

  @Override
  public GL20 getGL20() {
    return gl;
  }

  @Override
  public GL30 getGL30() {
    return null;
  }

  @Override
  public int getWidth() {
    return (int)stage.width();
  }

  @Override
  public int getHeight() {
    return (int)stage.height();
  }

  @Override
  public float getDeltaTime() {
    return deltaTime;
  }

  @Override
  public float getRawDeltaTime() {
    return getDeltaTime();
  }

  @Override
  public int getFramesPerSecond() {
    return fps;
  }

  @Override
  public GraphicsType getType() {
    if(Platform.isWeb()) {
      return GraphicsType.WebGL;
    } else if(Platform.isIOS()) {
      return GraphicsType.iOSGL;
    } else if(Platform.isJVM()) {
      return GraphicsType.LWJGL;
    } else if(Platform.isAndroid()) {
      return GraphicsType.AndroidGL;
    } else {
      throw new IllegalStateException();
    }
  }

  @Override
  public float getPpiX() {
    return ppiX;
  }

  @Override
  public float getPpiY() {
    return ppiY;
  }

  @Override
  public float getPpcX() {
    return ppcX;
  }

  @Override
  public float getPpcY() {
    return ppcY;
  }

  @Override
  public float getDensity() {
    return density;
  }

  @Override
  public boolean supportsDisplayModeChange() {
    return false;
  }

  @Override
  public DisplayMode[] getDisplayModes() {
    return new DisplayMode[] { getDesktopDisplayMode() };
  }

  @Override
  public DisplayMode getDesktopDisplayMode() {
    return new DefracDisplayMode(getWidth(), getHeight(), 0, 8);
  }

  @Override
  public boolean setDisplayMode(DisplayMode displayMode) {
    return false;
  }

  @Override
  public boolean setDisplayMode(int width, int height, boolean fullscreen) {
    return false;
  }

  @Override
  public void setTitle(String title) {
    //TODO(joa): @macro for JVM/js
  }

  @Override
  public void setVSync(boolean vsync) {

  }

  @Override
  public BufferFormat getBufferFormat() {
    return bufferFormat;
  }

  @Override
  public boolean supportsExtension(String extension) {
    return false;//for now
  }

  @Override
  public void setContinuousRendering(boolean isContinuous) {

  }

  @Override
  public boolean isContinuousRendering() {
    return true;
  }

  @Override
  public void requestRendering() {
  }

  @Override
  public boolean isFullscreen() {
    return false;
  }
}
