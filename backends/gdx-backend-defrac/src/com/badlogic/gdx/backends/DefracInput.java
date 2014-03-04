package com.badlogic.gdx.backends;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Pixmap;
import defrac.event.Events;
import defrac.event.KeyboardEvent;
import defrac.event.PointerEvent;
import defrac.lang.Procedure;

/**
 *
 */
public final class DefracInput implements Input {
  private int state = 0;
  private final int[] x = new int[10];
  private final int[] y = new int[10];
  private final int[] dx = new int[10];
  private final int[] dy = new int[10];
  private final boolean[] keyDown = new boolean[0x100];
  private long currentEventTime = 0L;
  private InputProcessor inputProcessor;

  DefracInput() {
    Events.onPointerUp.attach(new Procedure<PointerEvent>() {
      @Override
      public void apply(PointerEvent pointerEvent) {
        onPointerUp(pointerEvent);
      }
    });

    Events.onPointerDown.attach(new Procedure<PointerEvent>() {
      @Override
      public void apply(PointerEvent pointerEvent) {
        onPointerDown(pointerEvent);
      }
    });

    Events.onPointerMove.attach(new Procedure<PointerEvent>() {
      @Override
      public void apply(PointerEvent pointerEvent) {
        onPointerMove(pointerEvent);
      }
    });

    Events.onKeyDown.attach(new Procedure<KeyboardEvent>() {
      @Override
      public void apply(KeyboardEvent keyboardEvent) {
        currentEventTime = System.currentTimeMillis() * 1000000;
        keyDown[keyboardEvent.keyCode] = true;
        if(null != inputProcessor) {
          inputProcessor.keyDown(keyboardEvent.keyCode);
        }
      }
    });

    Events.onKeyUp.attach(new Procedure<KeyboardEvent>() {
      @Override
      public void apply(KeyboardEvent keyboardEvent) {
        if(null != inputProcessor) {
          final boolean wasDown = keyDown[keyboardEvent.keyCode];
          keyDown[keyboardEvent.keyCode] = false;
          inputProcessor.keyUp(keyboardEvent.keyCode);
          if(wasDown) {
            inputProcessor.keyTyped(keyboardEvent.charCode);
          }
        } else {
          keyDown[keyboardEvent.keyCode] = false;
        }
      }
    });
  }

  private void onPointerUp(PointerEvent event) {
    currentEventTime = System.currentTimeMillis() * 1000000;
    final int index = event.index;
    final int newX = (int)event.pos.x;
    final int newY = (int)event.pos.y;
    dx[index] = newX - x[index];
    dy[index] = newY - y[index];
    x[index] = newX;
    y[index] = newY;
    state &= ~(1 << index);
    if(null != inputProcessor) {
      inputProcessor.touchUp(newX, newY, index, index);
    }
  }

  private void onPointerDown(PointerEvent event) {
    currentEventTime = System.currentTimeMillis() * 1000000;
    final int index = event.index;
    final int newX = (int)event.pos.x;
    final int newY = (int)event.pos.y;
    dx[index] = x[index] = newX;
    dy[index] = y[index] = newY;
    state |= (1 << index);
    if(null != inputProcessor) {
      inputProcessor.touchDown(newX, newY, index, index);
    }
  }

  private void onPointerMove(PointerEvent event) {
    currentEventTime = System.currentTimeMillis() * 1000000;
    final int index = event.index;
    final int newX = (int)event.pos.x;
    final int newY = (int)event.pos.y;
    if((state & (1 << index)) != 0) {
      dx[index] = newX - x[index];
      dy[index] = newY - y[index];
    }
    x[index] = newX;
    y[index] = newY;
    if(null != inputProcessor) {
      inputProcessor.touchDragged(newX, newY, index);
    }
  }

  @Override
  public float getAccelerometerX() {
    return 0.0f;
  }

  @Override
  public float getAccelerometerY() {
    return 0.0f;
  }

  @Override
  public float getAccelerometerZ() {
    return 0.0f;
  }

  @Override
  public int getX() {
    return x[0];
  }

  @Override
  public int getX(int pointer) {
    return x[pointer];
  }

  @Override
  public int getDeltaX() {
    return dx[0];
  }

  @Override
  public int getDeltaX(int pointer) {
    return dx[pointer];
  }

  @Override
  public int getY() {
    return y[0];
  }

  @Override
  public int getY(int pointer) {
    return y[pointer];
  }

  @Override
  public int getDeltaY() {
    return dy[0];
  }

  @Override
  public int getDeltaY(int pointer) {
    return dy[pointer];
  }

  @Override
  public boolean isTouched() {
    return state != 0;
  }

  @Override
  public boolean justTouched() {
    return isTouched();//?!
  }

  @Override
  public boolean isTouched(int pointer) {
    return (state & (1 << pointer)) != 0;
  }

  @Override
  public boolean isButtonPressed(int button) {
    return (state & (1 << button)) != 0;
  }

  @Override
  public boolean isKeyPressed(int key) {
    return keyDown[key];
  }

  @Override
  public void getTextInput(TextInputListener listener, String title, String text) {
  }

  @Override
  public void getPlaceholderTextInput(TextInputListener listener, String title, String placeholder) {
  }

  @Override
  public void setOnscreenKeyboardVisible(boolean visible) {
  }

  @Override
  public void vibrate(int milliseconds) {
  }

  @Override
  public void vibrate(long[] pattern, int repeat) {
  }

  @Override
  public void cancelVibrate() {
  }

  @Override
  public float getAzimuth() {
    return 0.0f;
  }

  @Override
  public float getPitch() {
    return 0.0f;
  }

  @Override
  public float getRoll() {
    return 0.0f;
  }

  @Override
  public void getRotationMatrix(float[] matrix) {
  }

  @Override
  public long getCurrentEventTime() {
    return currentEventTime;
  }

  @Override
  public void setCatchBackKey(boolean catchBack) {
  }

  @Override
  public void setCatchMenuKey(boolean catchMenu) {
  }

  @Override
  public void setInputProcessor(InputProcessor processor) {
    inputProcessor = processor;
  }

  @Override
  public InputProcessor getInputProcessor() {
    return inputProcessor;
  }

  @Override
  public boolean isPeripheralAvailable(Peripheral peripheral) {
    return false;
  }

  @Override
  public int getRotation() {
    return 0;
  }

  @Override
  public Orientation getNativeOrientation() {
    //TODO(joa): macro
    return Orientation.Landscape;
  }

  @Override
  public void setCursorCatched(boolean catched) {
  }

  @Override
  public boolean isCursorCatched() {
    return false;
  }

  @Override
  public void setCursorPosition(int x, int y) {
  }

  @Override
  public void setCursorImage(Pixmap pixmap, int xHotspot, int yHotspot) {
  }
}
