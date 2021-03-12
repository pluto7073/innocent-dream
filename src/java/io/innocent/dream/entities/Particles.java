package io.innocent.dream.entities;

import io.innocent.dream.drawing.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Particles {

    public static final List<Particle> PARTICLES;

    static {
        PARTICLES = new ArrayList<>();
    }

    public static void init() {}

    public static void renderParticles() {
        for (Particle p : PARTICLES) {
            p.render();
            p.tick();
        }
    }

    public static void createParticle(Vector2f location, String texturePath) {
        createParticle(location, new Vector2f(2, 2), texturePath);
    }

    public static void createParticle(Vector2f location, Vector2f size, String texturePath) {
        createParticle(location, size, texturePath, 1000);
    }

    public static void createParticle(Vector2f location, Vector2f size, String texturePath, int lifespan) {
        Texture texture = new Texture(texturePath);
        createParticle(location, size, texture, lifespan);
    }

    public static void createParticle(Vector2f location, Vector2f size, Texture texture, int lifespan) {
        Particle particle = new Particle(location, texture, size, lifespan);
        PARTICLES.add(particle);
    }

}
