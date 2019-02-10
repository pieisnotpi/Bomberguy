package com.pieisnotpi.bomberguy.shaders;

import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.rendering.shaders.ShaderProgram;
import com.pieisnotpi.engine.rendering.window.Window;

public class CharShader extends ShaderProgram
{
    public static final int ID = 29186;

    public CharShader(Window window)
    {
        super(window, ShaderFile.getShaderFile("character.vert", ShaderFile.TYPE_VERT),
                ShaderFile.getShaderFile("character.frag" ,ShaderFile.TYPE_FRAG));
    }
}
