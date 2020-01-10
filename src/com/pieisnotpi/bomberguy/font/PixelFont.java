package com.pieisnotpi.bomberguy.font;

import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.ui.text.font.CharSprite;
import com.pieisnotpi.engine.ui.text.font.Font;

public class PixelFont extends Font
{
    private static final String name = "PixelFont";

    private PixelFont()
    {
        super(Texture.getTextureFile("text"), 4, 10, 1);

        // Register uppercase letters
        sprites.put('A', new CharSprite(texture, 1, 1, 8, 10, 0, 0));
        sprites.put('B', new CharSprite(texture, 10, 1, 17, 10, 0, 0));
        sprites.put('C', new CharSprite(texture, 19, 1, 26, 10, 0, 0));
        sprites.put('D', new CharSprite(texture, 28, 1, 35, 10, 0, 0));
        sprites.put('E', new CharSprite(texture, 37, 1, 44, 10, 0, 0));
        sprites.put('F', new CharSprite(texture, 46, 1, 53, 10, 0, 0));
        sprites.put('G', new CharSprite(texture, 55, 1, 62, 10, 0, 0));
        sprites.put('H', new CharSprite(texture, 64, 1, 71, 10, 0, 0));
        sprites.put('I', new CharSprite(texture, 73, 1, 80, 10, 0, 0));
        sprites.put('J', new CharSprite(texture, 82, 1, 89, 10, 0, 0));
        sprites.put('K', new CharSprite(texture, 91, 1, 98, 10, 0, 0));
        sprites.put('L', new CharSprite(texture, 100, 1, 107, 10, 0, 0));
        sprites.put('M', new CharSprite(texture, 108, 1, 117, 10, 0, 0));
        sprites.put('N', new CharSprite(texture, 1, 11, 8, 20, 0, 0));
        sprites.put('O', new CharSprite(texture, 10, 11, 17, 20, 0, 0));
        sprites.put('P', new CharSprite(texture, 19, 11, 26, 20, 0, 0));
        sprites.put('Q', new CharSprite(texture, 28, 11, 36, 20, 0, 0));
        sprites.put('R', new CharSprite(texture, 37, 11, 44, 20, 0, 0));
        sprites.put('S', new CharSprite(texture, 46, 11, 53, 20, 0, 0));
        sprites.put('T', new CharSprite(texture, 55, 11, 62, 20, 0, 0));
        sprites.put('U', new CharSprite(texture, 64, 11, 71, 20, 0, 0));
        sprites.put('V', new CharSprite(texture, 73, 11, 80, 20, 0, 0));
        sprites.put('W', new CharSprite(texture, 81, 11, 90, 20, 0, 0));
        sprites.put('X', new CharSprite(texture, 91, 11, 98, 20, 0, 0));
        sprites.put('Y', new CharSprite(texture, 100, 11, 107, 20, 0, 0));
        sprites.put('Z', new CharSprite(texture, 109, 11, 116, 20, 0, 0));

        // Register lowercase letters
        sprites.put('a', new CharSprite(texture, 1, 23, 8, 30, 0, 0));
        sprites.put('b', new CharSprite(texture, 10, 21, 17, 30, 0, 0));
        sprites.put('c', new CharSprite(texture, 19, 23, 26, 30, 0, 0));
        sprites.put('d', new CharSprite(texture, 28, 21, 35, 30, 0, 0));
        sprites.put('e', new CharSprite(texture, 37, 23, 44, 30, 0, 0));
        sprites.put('f', new CharSprite(texture, 46, 21, 53, 30, 0, 0));
        sprites.put('g', new CharSprite(texture, 55, 21, 62, 30, 0, -2));
        sprites.put('h', new CharSprite(texture, 64, 21, 71, 30, 0, 0));
        sprites.put('i', new CharSprite(texture, 75, 21, 78, 30, 0, 0));
        sprites.put('j', new CharSprite(texture, 83, 21, 88, 30, 0, 0));
        sprites.put('k', new CharSprite(texture, 91, 21, 97, 30, 0, 0));
        sprites.put('l', new CharSprite(texture, 102, 21, 105, 30, 0, 0));
        sprites.put('m', new CharSprite(texture, 109, 23, 116, 30, 0, 0));
        sprites.put('n', new CharSprite(texture, 1, 33, 8, 40, 0, 0));
        sprites.put('o', new CharSprite(texture, 10, 33, 17, 40, 0, 0));
        sprites.put('p', new CharSprite(texture, 19, 31, 26, 40, 0, -2));
        sprites.put('q', new CharSprite(texture, 28, 31, 35, 40, 0, -2));
        sprites.put('r', new CharSprite(texture, 37, 33, 44, 40, 0, 0));
        sprites.put('s', new CharSprite(texture, 46, 33, 53, 40, 0, 0));
        sprites.put('t', new CharSprite(texture, 56, 31, 61, 40, 0, 0));
        sprites.put('u', new CharSprite(texture, 64, 33, 71, 40, 0, 0));
        sprites.put('v', new CharSprite(texture, 73, 33, 80, 40, 0, 0));
        sprites.put('w', new CharSprite(texture, 82, 33, 89, 40, 0, 0));
        sprites.put('x', new CharSprite(texture, 91, 33, 98, 40, 0, 0));
        sprites.put('y', new CharSprite(texture, 100, 30, 107, 40, 0, -3));
        sprites.put('z', new CharSprite(texture, 109, 33, 116, 40, 0, 0));

        // Register numbers
        sprites.put('1', new CharSprite(texture, 2, 41, 7, 50, 0, 0));
        sprites.put('2', new CharSprite(texture, 10, 41, 17, 50, 0, 0));
        sprites.put('3', new CharSprite(texture, 19, 41, 26, 50, 0, 0));
        sprites.put('4', new CharSprite(texture, 28, 41, 35, 50, 0, 0));
        sprites.put('5', new CharSprite(texture, 37, 41, 44, 50, 0, 0));
        sprites.put('6', new CharSprite(texture, 46, 41, 53, 50, 0, 0));
        sprites.put('7', new CharSprite(texture, 55, 41, 62, 50, 0, 0));
        sprites.put('8', new CharSprite(texture, 64, 41, 71, 50, 0, 0));
        sprites.put('9', new CharSprite(texture, 73, 41, 80, 50, 0, 0));
        sprites.put('0', new CharSprite(texture, 82, 41, 89, 50, 0, 0));

        // Register special chars
        sprites.put('-', new CharSprite(texture, 91, 44, 98, 47, 0, 3));
        sprites.put('=', new CharSprite(texture, 100, 43, 107, 48, 0, 2));
        sprites.put('?', new CharSprite(texture, 109, 41, 116, 50, 0, 0));
        sprites.put('!', new CharSprite(texture, 3, 51, 6, 60, 0, 0));
        sprites.put('@', new CharSprite(texture, 9, 51, 18, 60, 0, 0));
        sprites.put('#', new CharSprite(texture, 19, 52, 26, 59, 0, 1));
        sprites.put('$', new CharSprite(texture, 28, 51, 35, 60, 0, 0));
        sprites.put('%', new CharSprite(texture, 36, 51, 45, 60, 0, 0));
        sprites.put('^', new CharSprite(texture, 46, 52, 53, 59, 0, 1));
        sprites.put('&', new CharSprite(texture, 55, 51, 63, 60, 0, 0));
        sprites.put('*', new CharSprite(texture, 64, 53, 71, 59, 0, 2));
        sprites.put('(', new CharSprite(texture, 74, 50, 78, 61, 0, -1));
        sprites.put(')', new CharSprite(texture, 78, 50, 74, 61, 0, -1));
        sprites.put('{', new CharSprite(texture, 82, 50, 87, 61, 0, -1));
        sprites.put('}', new CharSprite(texture, 87, 50, 82, 61, 0, -1));
        sprites.put('[', new CharSprite(texture, 92, 50, 96, 61, 0, -1));
        sprites.put(']', new CharSprite(texture, 96, 50, 92, 61, 0, -1));
        sprites.put('_', new CharSprite(texture, 91, 44, 98, 47, 0, 0));
        sprites.put('+', new CharSprite(texture, 46, 62, 53, 69, 0, 1));
        sprites.put('|', new CharSprite(texture, 111, 59, 114, 70, 0, -1));
        sprites.put(',', new CharSprite(texture, 20, 63, 24, 68, 0, -2));
        sprites.put('.', new CharSprite(texture, 30, 64, 33, 67, 0, 0));
        sprites.put('/', new CharSprite(texture, 37, 61, 44, 70, 0, 0));
        sprites.put('\\', new CharSprite(texture, 44, 61, 37, 70, 0, 0));
        sprites.put(':', new CharSprite(texture, 57, 61, 60, 70, 1, -2));
        sprites.put(';', new CharSprite(texture, 65, 61, 69, 70, 0, -1));
        sprites.put('<', new CharSprite(texture, 73, 62, 80, 69, 0, 1));
        sprites.put('>', new CharSprite(texture, 82, 62, 89, 69, 0, 1));
        sprites.put('"', new CharSprite(texture, 92, 63, 97, 68, 0, 4));
        sprites.put('\'', new CharSprite(texture, 102, 63, 105, 68, 0, 5));

        PiEngine.glInstance.registerFont(name, this);
    }

    public static PixelFont getFont()
    {
        Font f = PiEngine.glInstance.getFont(name);
        if(f != null) return (PixelFont) f;
        else
        {
            Logger.SYSTEM.debug("Creating new font: '" + name + '\'');
            return new PixelFont();
        }
    }
}
