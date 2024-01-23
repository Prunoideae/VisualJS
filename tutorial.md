# Introduction to GLSL, post-effects and VisualJS

## What is GLSL?

GLSL (OpenGL Shading Language) is a C-like language that is used to program shader in OpenGL. Almost everything in Minecraft's effect processing is done with GLSL, so it's highly recommended for you to learn about it before you start to write your own shaders.

However, we will not cover the details of GLSL here as you can find many tutorials online, we will be focus on more Minecraft and KubeJS related stuffs.

## Fragment Shader and Vertex Shader

Before start digging into the actual code, you need to know that there are two types of shaders in GLSL:

1. Fragment Shader
2. Vertex Shader

The two shaders are the fundamental aspects of shader programming, and they are executed in different stages of the rendering pipeline.

### 1. Fragment Shader

The aspect of fragment shader is simple: for every point on the screen, we need to determine its color, and that's what fragment shader does.

So, a fragment shader can be seen as a function that takes a coordinate of a point as input, and returns a color as output.

A very basic fragment shader in Minecraft looks like this (named `myshader.fsh`):

```glsl
#version 150
uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;

void main() {
    fragColor = texture(DiffuseSampler, texCoord);
}
```

Minecraft predefined some variables for us:

- `texCoord`: the coordinate of the point on the screen as input
- `oneTexel`: the size of a pixel on the screen as input
- `fragColor`: the color of the point on the screen as output

And there's a thing missing: the texture. We only have the coordinate of the point, but we don't know what color it should be originally. So now we need to introduce the texture by using the `DiffuseSampler` uniform variable.

The built-in function `texture` takes a sampler and a coordinate as input, and so returns the color of the coordinate on the texture.

Finally, we assign the sampled color to the output variable `fragColor`, and we are done. All thing this shader does is nothing. The color of each point on the screen is the same as the original texture.

To use a `DiffuseSampler` uniform, we need to define it in the shader json file later.

### 2. Vertex Shader

*To be added*

## Shader in Minecraft

A shader in Minecraft is a json file that defines the shader programs (both vertex shader and fragment shader), uniforms and samplers, as well as some other properties.

A simple shader json is defined as:

```json
{
    "blend": {
        "func": "add",
        "srcrgb": "one",
        "dstrgb": "zero"
    },
    "vertex": "sobel",
    "fragment": "myshader",
    "attributes": [ "Position" ],
    "samplers": [
        { "name": "DiffuseSampler" }
    ],
    "uniforms": []
}
```

To use such shader, you need to put it under a specific resource location with your programs, which is `minecraft:shaders/program`. So your file structure should look like this:

```
assets
└── minecraft
    └── shaders
        └── program
            ├── myshader.fsh
            └── myshader.json
```

The `sobel` vertex shader is a built-in shader in Minecraft, which is simply a pass-through shader that does nothing. You can find other built-in shaders in `assets/minecraft/shaders/program` in Minecraft's jar file.

## Post-effects

Post-effects are a series of effects that are applied to the screen after the scene is rendered. They are usually used to add some effects like blur, bloom, etc.

In Minecraft, post-effects are a series of shaders that are applied to the screen in order. The shaders are defined in `assets/minecraft/shaders/post` in Minecraft's jar file. And so, you can add your own post-effects by adding your post-effects to this resource location.

The most simple post-effect json looks like this:

```json
{
    "targets": [
        "temp",
    ],
    "passes": [
        {
            "name": "myshader",
            "intarget": "minecraft:main",
            "outtarget": "temp",
            "uniforms": []
        },
        {
            "name": "blit",
            "intarget": "temp",
            "outtarget": "minecraft:main"
        }
    ]
}
```

The `targets` field defines a list of targets that are used in the post-effect. Each target is a buffer that stores the color and position information of the screen. Here we defined a target named `temp`, which is used to store the output from the first pass `myshader`. A target is always needed in such case, as you can't define a pass that takes a target as both input and output.

The `passes` field defines a list of shaders that are applied to the screen in order. Each pass is a shader that is applied to the screen, and it can take a target as input and output. Here we defined two passes:

1. `myshader`: a shader that takes the screen as input, and outputs to the target `temp` as we can't overwrite the screen directly.
2. `blit`: a shader that takes the target `temp` as input, and outputs to the screen. `blit` is a built-in shader in Minecraft, which simply copies the input to the output. It is useful when you only have one pass in your post-effect.