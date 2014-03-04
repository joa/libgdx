package com.badlogic.gdx.backends;

import com.badlogic.gdx.graphics.GL20;
import defrac.gl.*;
import defrac.lang.Maps;

import java.nio.*;
import java.util.Map;

/**
 *
 */
final class DefracGL20 implements GL20 {
  private final GL gl;

  private Map<Integer, GLProgram> programs = Maps.newHashMap();
  private Map<Integer, GLShader> shaders = Maps.newHashMap();
  private Map<Integer, GLBuffer> buffers = Maps.newHashMap();
  private Map<Integer, GLFrameBuffer> frameBuffers = Maps.newHashMap();
  private Map<Integer, GLRenderBuffer> renderBuffers = Maps.newHashMap();
  private int programId, shaderId, bufferId, frameBufferId, renderBufferId;
  DefracGL20(final GL gl) {
    this.gl = gl;
    reset();
  }

  void reset() {
    programs.clear();
    shaders.clear();
    buffers.clear();
    frameBuffers.clear();
    renderBuffers.clear();
    programId = 1;
    shaderId = 1;
    bufferId = 1;
    frameBufferId = 1;
    renderBufferId = 1;
  }

  private GLProgram program(final int id) {
    return 0 == id ? null : programs.get(id);
  }

  private GLShader shader(final int id) {
    return 0 == id ? null : shaders.get(id);
  }

  private GLBuffer buffer(final int id) {
    return 0 == id ? null : buffers.get(id);
  }

  private GLFrameBuffer framebuffer(final int id) {
    return 0 == id ? null : frameBuffers.get(id);
  }

  private GLRenderBuffer renderbuffer(final int id) {
    return 0 == id ? null : renderBuffers.get(id);
  }

  @Override
  public void glAttachShader(int program, int shader) {
    gl.attachShader(program(program), shader(shader));
  }

  @Override
  public void glBindAttribLocation(int program, int index, String name) {
    gl.bindAttribLocation(program(program), index, name);
  }

  @Override
  public void glBindBuffer(int target, int buffer) {
    gl.bindBuffer(target, buffer(buffer));
  }

  @Override
  public void glBindFramebuffer(int target, int framebuffer) {
    gl.bindFrameBuffer(target, framebuffer(framebuffer));
  }

  @Override
  public void glBindRenderbuffer(int target, int renderbuffer) {
    gl.bindRenderBuffer(target, renderbuffer(renderbuffer));
  }

  @Override
  public void glBlendColor(float red, float green, float blue, float alpha) {
    gl.blendColor(red, green,blue, alpha);
  }

  @Override
  public void glBlendEquation(int mode) {
    gl.blendEquation(mode);
  }

  @Override
  public void glBlendEquationSeparate(int modeRGB, int modeAlpha) {
    gl.blendEquationSeparate(modeRGB, modeAlpha);
  }

  @Override
  public void glBlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
    gl.blendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
  }

  @Override
  public void glBufferData(int target, int size, Buffer data, int usage) {
    if(data instanceof ByteBuffer) {
      gl.bufferData(target, ((ByteBuffer)data).array(), data.position(), data.limit(), usage);
    } else if(data instanceof ShortBuffer) {
      gl.bufferData(target, ((ShortBuffer)data).array(), data.position(), data.limit(), usage);
    } else if(data instanceof IntBuffer) {
      gl.bufferData(target, ((IntBuffer)data).array(), data.position(), data.limit(), usage);
    } else if(data instanceof FloatBuffer) {
      gl.bufferData(target, ((FloatBuffer)data).array(), data.position(), data.limit(), usage);
    } else if(data instanceof DoubleBuffer) {
      gl.bufferData(target, ((DoubleBuffer)data).array(), data.position(), data.limit(), usage);
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public void glBufferSubData(int target, int offset, int size, Buffer data) {
    if(data instanceof ByteBuffer) {
      gl.bufferSubData(target, offset, ((ByteBuffer)data).array(), data.position(), data.limit());
    } else if(data instanceof ShortBuffer) {
      gl.bufferSubData(target, offset, ((ShortBuffer)data).array(), data.position(), data.limit());
    } else if(data instanceof IntBuffer) {
      gl.bufferSubData(target, offset, ((IntBuffer)data).array(), data.position(), data.limit());
    } else if(data instanceof FloatBuffer) {
      gl.bufferSubData(target, offset, ((FloatBuffer)data).array(), data.position(), data.limit());
    } else if(data instanceof DoubleBuffer) {
      gl.bufferSubData(target, offset, ((DoubleBuffer)data).array(), data.position(), data.limit());
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public int glCheckFramebufferStatus(int target) {
    return gl.checkFrameBufferStatus(target);
  }

  @Override
  public void glCompileShader(int shader) {
    gl.compileShader(shader(shader));
  }

  @Override
  public int glCreateProgram() {
    final GLProgram program = gl.createProgram();
    if(null == program) {
      return 0;
    }
    final int id = programId++;
    programs.put(id, program);
    return id;
  }

  @Override
  public int glCreateShader(int type) {
    final GLShader shader = gl.createShader(type);
    if(null == shader) {
      return 0;
    }
    final int id = shaderId++;
    shaders.put(id, shader);
    return id;
  }

  @Override
  public void glDeleteBuffers(int n, IntBuffer buffers) {
    final int[] array = buffers.array();
    final int offset = buffers.arrayOffset();
    for(int i = offset; i < n; ++i) {
      final int id = array[i];
      if(0 == id) {
        continue;
      }
      final GLBuffer buffer = buffer(i);
      this.buffers.remove(i);
      gl.deleteBuffer(buffer);
    }
  }

  @Override
  public void glDeleteFramebuffers(int n, IntBuffer framebuffers) {
    final int[] array = framebuffers.array();
    final int offset = framebuffers.arrayOffset();
    for(int i = offset; i < n; ++i) {
      final int id = array[i];
      if(0 == id) {
        continue;
      }
      final GLFrameBuffer buffer = framebuffer(i);
      this.frameBuffers.remove(i);
      gl.deleteFrameBuffer(buffer);
    }
  }

  @Override
  public void glDeleteProgram(int program) {
    if(0 == program) {
      return;
    }

    final GLProgram p = program(program);
    programs.remove(program);
    gl.deleteProgram(p);
  }

  @Override
  public void glDeleteRenderbuffers(int n, IntBuffer renderbuffers) {
    final int[] array = renderbuffers.array();
    final int offset = renderbuffers.arrayOffset();
    for(int i = offset; i < n; ++i) {
      final int id = array[i];
      if(0 == id) {
        continue;
      }
      final GLRenderBuffer buffer = renderbuffer(i);
      this.renderBuffers.remove(i);
      gl.deleteRenderBuffer(buffer);
    }
  }

  @Override
  public void glDeleteShader(int shader) {
    if(0 == shader) {
      return;
    }

    final GLShader s = shader(shader);
    programs.remove(shader);
    gl.deleteShader(s);
  }

  @Override
  public void glDetachShader(int program, int shader) {
    gl.detachShader(program(program), shader(shader));
  }

  @Override
  public void glDisableVertexAttribArray(int index) {
    //TODO(joa): implement me
  }

  @Override
  public void glDrawElements(int mode, int count, int type, int indices) {
    //TODO(joa): implement me
  }

  @Override
  public void glEnableVertexAttribArray(int index) {
    //TODO(joa): implement me
  }

  @Override
  public void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
    //TODO(joa): implement me
  }

  @Override
  public void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
    //TODO(joa): implement me
  }

  @Override
  public void glGenBuffers(int n, IntBuffer buffers) {
    //TODO(joa): implement me
  }

  @Override
  public void glGenerateMipmap(int target) {
    //TODO(joa): implement me
  }

  @Override
  public void glGenFramebuffers(int n, IntBuffer framebuffers) {
    //TODO(joa): implement me
  }

  @Override
  public void glGenRenderbuffers(int n, IntBuffer renderbuffers) {
    //TODO(joa): implement me
  }

  @Override
  public String glGetActiveAttrib(int program, int index, IntBuffer size, Buffer type) {
    return null; //TODO(joa): implement me
  }

  @Override
  public String glGetActiveUniform(int program, int index, IntBuffer size, Buffer type) {
    return null; //TODO(joa): implement me
  }

  @Override
  public void glGetAttachedShaders(int program, int maxcount, Buffer count, IntBuffer shaders) {
    //TODO(joa): implement me
  }

  @Override
  public int glGetAttribLocation(int program, String name) {
    return 0; //TODO(joa): implement me
  }

  @Override
  public void glGetBooleanv(int pname, Buffer params) {
    //TODO(joa): implement me
  }

  @Override
  public void glGetBufferParameteriv(int target, int pname, IntBuffer params) {
    //TODO(joa): implement me
  }

  @Override
  public void glGetFloatv(int pname, FloatBuffer params) {
    //TODO(joa): implement me
  }

  @Override
  public void glGetFramebufferAttachmentParameteriv(int target, int attachment, int pname, IntBuffer params) {
    //TODO(joa): implement me
  }

  @Override
  public void glGetProgramiv(int program, int pname, IntBuffer params) {
    //TODO(joa): implement me
  }

  @Override
  public String glGetProgramInfoLog(int program) {
    return null; //TODO(joa): implement me
  }

  @Override
  public void glGetRenderbufferParameteriv(int target, int pname, IntBuffer params) {
    //TODO(joa): implement me
  }

  @Override
  public void glGetShaderiv(int shader, int pname, IntBuffer params) {
    //TODO(joa): implement me
  }

  @Override
  public String glGetShaderInfoLog(int shader) {
    return null; //TODO(joa): implement me
  }

  @Override
  public void glGetShaderPrecisionFormat(int shadertype, int precisiontype, IntBuffer range, IntBuffer precision) {
    //TODO(joa): implement me
  }

  @Override
  public void glGetTexParameterfv(int target, int pname, FloatBuffer params) {
    //TODO(joa): implement me
  }

  @Override
  public void glGetTexParameteriv(int target, int pname, IntBuffer params) {
    //TODO(joa): implement me
  }

  @Override
  public void glGetUniformfv(int program, int location, FloatBuffer params) {
    //TODO(joa): implement me
  }

  @Override
  public void glGetUniformiv(int program, int location, IntBuffer params) {
    //TODO(joa): implement me
  }

  @Override
  public int glGetUniformLocation(int program, String name) {
    return 0; //TODO(joa): implement me
  }

  @Override
  public void glGetVertexAttribfv(int index, int pname, FloatBuffer params) {
    //TODO(joa): implement me
  }

  @Override
  public void glGetVertexAttribiv(int index, int pname, IntBuffer params) {
    //TODO(joa): implement me
  }

  @Override
  public void glGetVertexAttribPointerv(int index, int pname, Buffer pointer) {
    //TODO(joa): implement me
  }

  @Override
  public boolean glIsBuffer(int buffer) {
    return false; //TODO(joa): implement me
  }

  @Override
  public boolean glIsEnabled(int cap) {
    return false; //TODO(joa): implement me
  }

  @Override
  public boolean glIsFramebuffer(int framebuffer) {
    return false; //TODO(joa): implement me
  }

  @Override
  public boolean glIsProgram(int program) {
    return false; //TODO(joa): implement me
  }

  @Override
  public boolean glIsRenderbuffer(int renderbuffer) {
    return false; //TODO(joa): implement me
  }

  @Override
  public boolean glIsShader(int shader) {
    return false; //TODO(joa): implement me
  }

  @Override
  public boolean glIsTexture(int texture) {
    return false; //TODO(joa): implement me
  }

  @Override
  public void glLinkProgram(int program) {
    //TODO(joa): implement me
  }

  @Override
  public void glReleaseShaderCompiler() {
    //TODO(joa): implement me
  }

  @Override
  public void glRenderbufferStorage(int target, int internalformat, int width, int height) {
    //TODO(joa): implement me
  }

  @Override
  public void glSampleCoverage(float value, boolean invert) {
    //TODO(joa): implement me
  }

  @Override
  public void glShaderBinary(int n, IntBuffer shaders, int binaryformat, Buffer binary, int length) {
    //TODO(joa): implement me
  }

  @Override
  public void glShaderSource(int shader, String string) {
    //TODO(joa): implement me
  }

  @Override
  public void glStencilFuncSeparate(int face, int func, int ref, int mask) {
    //TODO(joa): implement me
  }

  @Override
  public void glStencilMaskSeparate(int face, int mask) {
    //TODO(joa): implement me
  }

  @Override
  public void glStencilOpSeparate(int face, int fail, int zfail, int zpass) {
    //TODO(joa): implement me
  }

  @Override
  public void glTexParameterfv(int target, int pname, FloatBuffer params) {
    //TODO(joa): implement me
  }

  @Override
  public void glTexParameteri(int target, int pname, int param) {
    //TODO(joa): implement me
  }

  @Override
  public void glTexParameteriv(int target, int pname, IntBuffer params) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniform1f(int location, float x) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniform1fv(int location, int count, FloatBuffer v) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniform1i(int location, int x) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniform1iv(int location, int count, IntBuffer v) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniform2f(int location, float x, float y) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniform2fv(int location, int count, FloatBuffer v) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniform2i(int location, int x, int y) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniform2iv(int location, int count, IntBuffer v) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniform3f(int location, float x, float y, float z) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniform3fv(int location, int count, FloatBuffer v) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniform3i(int location, int x, int y, int z) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniform3iv(int location, int count, IntBuffer v) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniform4f(int location, float x, float y, float z, float w) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniform4fv(int location, int count, FloatBuffer v) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniform4i(int location, int x, int y, int z, int w) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniform4iv(int location, int count, IntBuffer v) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniformMatrix2fv(int location, int count, boolean transpose, FloatBuffer value) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniformMatrix3fv(int location, int count, boolean transpose, FloatBuffer value) {
    //TODO(joa): implement me
  }

  @Override
  public void glUniformMatrix4fv(int location, int count, boolean transpose, FloatBuffer value) {
    //TODO(joa): implement me
  }

  @Override
  public void glUseProgram(int program) {
    //TODO(joa): implement me
  }

  @Override
  public void glValidateProgram(int program) {
    //TODO(joa): implement me
  }

  @Override
  public void glVertexAttrib1f(int indx, float x) {
    //TODO(joa): implement me
  }

  @Override
  public void glVertexAttrib1fv(int indx, FloatBuffer values) {
    //TODO(joa): implement me
  }

  @Override
  public void glVertexAttrib2f(int indx, float x, float y) {
    //TODO(joa): implement me
  }

  @Override
  public void glVertexAttrib2fv(int indx, FloatBuffer values) {
    //TODO(joa): implement me
  }

  @Override
  public void glVertexAttrib3f(int indx, float x, float y, float z) {
    //TODO(joa): implement me
  }

  @Override
  public void glVertexAttrib3fv(int indx, FloatBuffer values) {
    //TODO(joa): implement me
  }

  @Override
  public void glVertexAttrib4f(int indx, float x, float y, float z, float w) {
    //TODO(joa): implement me
  }

  @Override
  public void glVertexAttrib4fv(int indx, FloatBuffer values) {
    //TODO(joa): implement me
  }

  @Override
  public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, Buffer ptr) {
    //TODO(joa): implement me
  }

  @Override
  public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, int ptr) {
    //TODO(joa): implement me
  }

  @Override
  public void glActiveTexture(int texture) {
    //TODO(joa): implement me
  }

  @Override
  public void glBindTexture(int target, int texture) {
    //TODO(joa): implement me
  }

  @Override
  public void glBlendFunc(int sfactor, int dfactor) {
    //TODO(joa): implement me
  }

  @Override
  public void glClear(int mask) {
    //TODO(joa): implement me
  }

  @Override
  public void glClearColor(float red, float green, float blue, float alpha) {
    //TODO(joa): implement me
  }

  @Override
  public void glClearDepthf(float depth) {
    //TODO(joa): implement me
  }

  @Override
  public void glClearStencil(int s) {
    //TODO(joa): implement me
  }

  @Override
  public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
    //TODO(joa): implement me
  }

  @Override
  public void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int imageSize, Buffer data) {
    //TODO(joa): implement me
  }

  @Override
  public void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, Buffer data) {
    //TODO(joa): implement me
  }

  @Override
  public void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height, int border) {
    //TODO(joa): implement me
  }

  @Override
  public void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
    //TODO(joa): implement me
  }

  @Override
  public void glCullFace(int mode) {
    //TODO(joa): implement me
  }

  @Override
  public void glDeleteTextures(int n, IntBuffer textures) {
    //TODO(joa): implement me
  }

  @Override
  public void glDepthFunc(int func) {
    //TODO(joa): implement me
  }

  @Override
  public void glDepthMask(boolean flag) {
    //TODO(joa): implement me
  }

  @Override
  public void glDepthRangef(float zNear, float zFar) {
    //TODO(joa): implement me
  }

  @Override
  public void glDisable(int cap) {
    //TODO(joa): implement me
  }

  @Override
  public void glDrawArrays(int mode, int first, int count) {
    //TODO(joa): implement me
  }

  @Override
  public void glDrawElements(int mode, int count, int type, Buffer indices) {
    //TODO(joa): implement me
  }

  @Override
  public void glEnable(int cap) {
    //TODO(joa): implement me
  }

  @Override
  public void glFinish() {
    //TODO(joa): implement me
  }

  @Override
  public void glFlush() {
    //TODO(joa): implement me
  }

  @Override
  public void glFrontFace(int mode) {
    //TODO(joa): implement me
  }

  @Override
  public void glGenTextures(int n, IntBuffer textures) {
    //TODO(joa): implement me
  }

  @Override
  public int glGetError() {
    return 0; //TODO(joa): implement me
  }

  @Override
  public void glGetIntegerv(int pname, IntBuffer params) {
    //TODO(joa): implement me
  }

  @Override
  public String glGetString(int name) {
    return null; //TODO(joa): implement me
  }

  @Override
  public void glHint(int target, int mode) {
    //TODO(joa): implement me
  }

  @Override
  public void glLineWidth(float width) {
    //TODO(joa): implement me
  }

  @Override
  public void glPixelStorei(int pname, int param) {
    //TODO(joa): implement me
  }

  @Override
  public void glPolygonOffset(float factor, float units) {
    //TODO(joa): implement me
  }

  @Override
  public void glReadPixels(int x, int y, int width, int height, int format, int type, Buffer pixels) {
    //TODO(joa): implement me
  }

  @Override
  public void glScissor(int x, int y, int width, int height) {
    //TODO(joa): implement me
  }

  @Override
  public void glStencilFunc(int func, int ref, int mask) {
    //TODO(joa): implement me
  }

  @Override
  public void glStencilMask(int mask) {
    //TODO(joa): implement me
  }

  @Override
  public void glStencilOp(int fail, int zfail, int zpass) {
    //TODO(joa): implement me
  }

  @Override
  public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, Buffer pixels) {
    //TODO(joa): implement me
  }

  @Override
  public void glTexParameterf(int target, int pname, float param) {
    //TODO(joa): implement me
  }

  @Override
  public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, Buffer pixels) {
    //TODO(joa): implement me
  }

  @Override
  public void glViewport(int x, int y, int width, int height) {
    //TODO(joa): implement me
  }
}
