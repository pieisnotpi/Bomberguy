package com.pieisnotpi.bomberguy;

import com.pieisnotpi.bomberguy.shaders.CharShader;
import com.pieisnotpi.engine.GameInstance;
import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.input.keyboard.Keybind;
import com.pieisnotpi.engine.input.keyboard.Keyboard;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexShader;
import com.pieisnotpi.engine.rendering.shaders.types.text.TextShader;
import com.pieisnotpi.engine.rendering.window.GLInstance;
import com.pieisnotpi.engine.rendering.window.ShaderInitializer;
import com.pieisnotpi.engine.rendering.window.Window;
import com.pieisnotpi.engine.utility.ShaderReloadUtility;

import java.nio.file.Paths;

public class MainInstance extends GameInstance
{
    private Window window;

    public void init() throws Exception
    {
        Window.shaderInitializer = new ShaderInitializer()
        {
            @Override
            public void init(GLInstance inst)
            {
                inst.registerShaderProgram(TextShader.ID, new TextShader(inst.window).init());
                inst.registerShaderProgram(TexShader.ID, new TexShader(inst.window).init());
                inst.registerShaderProgram(CharShader.ID, new CharShader(inst.window).init());
            }
        };

        String shaderPath = System.getProperty("test.shader_path");
        if(shaderPath != null && PiEngine.debug) shaderReload = new ShaderReloadUtility(this, Paths.get(shaderPath));

        window = new Window("Bomberguy", 900, 600).init();
        windows.add(window);

        window.setScene(new GameScene().init());
        window.inputManager.keybinds.add(new Keybind(Keyboard.KEY_F11, () -> window.setFullscreen(!window.isFullscreen()), null, null));
    }

    @Override
    public void start() throws Exception
    {
        window.show();

        super.start();
    }
}
