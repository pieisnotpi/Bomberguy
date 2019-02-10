#version 130

uniform sampler2D sampler;
uniform int size = 1;

in vec2 TexCoord;

out vec4 FragColor;

vec4 colorOutline()
{
	ivec2 tsize = textureSize(sampler, 0);
    ivec2 point = ivec2(TexCoord.x*tsize.x, TexCoord.y*tsize.y);
    vec4 search;
    vec4 color;
    int count;

    for(int x = -size; x <= size; x++)
    {
        for(int y = -size; y <= size; y++)
        {
            float nx = point.x + x, ny = point.y + y;

            search = texelFetch(sampler, ivec2(x + point.x, y + point.y), 0);

            if(search.w > 0)
            {
                color += search;
                count++;
            };
        }
    }

    return (color/count)*vec4(0.4f, 0.4f, 0.4f, 1f);
}

vec4 dropShadow()
{
	ivec2 tsize = textureSize(sampler, 0);
    ivec2 point = ivec2(TexCoord.x*tsize.x, TexCoord.y*tsize.y);

	vec4 search = texelFetch(sampler, ivec2(point.x - 1, point.y - 1), 0);

	if(search.w > 0)
	{
		return vec4(0.2f, 0.2f, 0.2f, 0.7f);
	};
	
	search = texelFetch(sampler, ivec2(point.x, point.y - 1), 0);

	if(search.w > 0)
	{
		return vec4(0.2f, 0.2f, 0.2f, 0.7f);
	};
	
	search = texelFetch(sampler, ivec2(point.x - 1, point.y), 0);

	if(search.w > 0)
	{
		return vec4(0.2f, 0.2f, 0.2f, 0.7f);
	};

    return vec4(0, 0, 0, 0);
}

vec4 blackOutline()
{
    ivec2 tsize = textureSize(sampler, 0);
    ivec2 point = ivec2(TexCoord.x*tsize.x, TexCoord.y*tsize.y);
    vec4 search;

    for(int x = -size; x <= size; x++)
    {
        for(int y = -size; y <= size; y++)
        {
            float nx = point.x + x, ny = point.y + y;

            search = texelFetch(sampler, ivec2(x + point.x, y + point.y), 0);

            if(search.w > 0)
            {
                return vec4(0.2f, 0.2f, 0.2f, 1f);
            };
        }
    }

    return vec4(0, 0, 0, 0);
}

void main()
{
    vec4 texColor = texture(sampler, TexCoord);

	if(texColor.w == 0)
	{
	    if(size == 0) discard;

	    vec4 color = blackOutline();
        if(color.w != 0)
        {
            FragColor = color;
            return;
        }
        else discard;
	}
	else FragColor = texColor;
}