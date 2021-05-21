package io.innocent.dream.worldBuilder.biomes;

import java.io.Serializable;

public class BiomeReference implements Serializable {

	private static final long serialVersionUID = 202132818957586952L;
	private final int biomeID;
	private final float center;
	private final float leftEdge;
	private final float rightEdge;

	public BiomeReference(int biomeID, float center) {
		this(biomeID, center, center - 5, center + 5);
	}
	
	public BiomeReference(int biomeID, float center, float leftEdge, float rightEdge) {
		this.biomeID = biomeID;
		this.center = center;
		this.leftEdge = leftEdge;
		this.rightEdge = rightEdge;
	}
	
	public boolean containsCoordinate(float x) {
		return (x >= leftEdge) && (x <= rightEdge);
	}
	
	public int getBiomeID() {
		return biomeID;
	}

}
