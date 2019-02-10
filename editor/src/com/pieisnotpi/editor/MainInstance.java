package com.pieisnotpi.editor;

import com.pieisnotpi.engine.GameInstance;
import com.pieisnotpi.engine.input.keyboard.Keybind;
import com.pieisnotpi.engine.input.keyboard.Keyboard;
import com.pieisnotpi.engine.rendering.window.Window;

public class MainInstance extends GameInstance
{
    private Window window;

    public void init() throws Exception
    {
        window = new Window("Bomberguy Editor", 600, 600).init();
        windows.add(window);

        window.setScene(new EditorScene().init());
        window.inputManager.keybinds.add(new Keybind(Keyboard.KEY_F11, () -> window.setFullscreen(!window.isFullscreen()), null, null));
    }

    @Override
    public void start() throws Exception
    {
        window.show();

        super.start();
    }
}
