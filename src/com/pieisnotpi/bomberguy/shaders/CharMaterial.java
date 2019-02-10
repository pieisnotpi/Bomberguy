package com.pieisnotpi.bomberguy.shaders;

import com.pieisnotpi.engine.rendering.primitives.Primitive;
import com.pieisnotpi.engine.rendering.shaders.Material;
import com.pieisnotpi.engine.rendering.shaders.VertexArray;
import com.pieisnotpi.engine.rendering.shaders.buffers.Attribute;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.utility.BufferUtility;

import java.util.List;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class CharMaterial extends Material
{
    public Texture[] textures;

    public CharMaterial(int matrixID, Texture... textures)
    {
        super(CharShader.ID, matrixID);
        this.textures = textures;
    }

    @Override
    public Attribute[] genAttributes(boolean isStatic)
    {
        int mode = isStatic ? GL_STATIC_DRAW : GL_DYNAMIC_DRAW;
        return new Attribute[]{new Attribute("VertexPosition", shader, 3, mode, isStatic), new Attribute("VertexTexCoord", shader, 2, mode, isStatic)};
    }

    @Override
    public void putElements(List<? extends Primitive> renderables, VertexArray a)
    {
        renderables.forEach(r ->
        {
            if(!r.enabled) return;
            BufferUtility.putVec3s(a.attributes[0].buffer, r.points);
            BufferUtility.putVec2s(a.attributes[1].buffer, r.texCoords);
        });
    }

    @Override
    public void bind()
    {
        shader.setUniformInt("size", 1);
        for(int i = 0; i < textures.length; i++) textures[i].bind(i);
    }
}
