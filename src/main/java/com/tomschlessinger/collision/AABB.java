package com.tomschlessinger.collision;

import com.tomschlessinger.util.Vector2f;

public class AABB {
	private final Vector2f center, half_extent;
	
	public AABB(Vector2f center, Vector2f half_extent) {
		this.center = center;
		this.half_extent = half_extent;
	}
	
	public Collision getCollision(AABB box2) {
		Vector2f distance = box2.center.subtract(Vector2f.ZERO.copy());
		distance.abs();
		
		distance.subtract(Vector2f.ZERO.copy().add(half_extent).add(box2.half_extent));
		
		return new Collision(distance, distance.getX() < 0 && distance.getY() < 0);
	}
	
	public Collision getCollision(Vector2f point) {
		Vector2f distance = point.subtract(center);
		distance.abs();
		
		distance.subtract(half_extent);
		
		return new Collision(distance, distance.getX() < 0 && distance.getY() < 0);
	}
	
	public void correctPosition(AABB box2, Collision data) {
		Vector2f correctionDistance = box2.center.copy().subtract(center);
		if (data.distance.getX() > data.distance.getY()) {
			if (correctionDistance.getX() > 0) {
				center.add(data.distance.getX(), 0);
			}
			else {
				center.add(-data.distance.getX(), 0);
			}
		}
		else {
			if (correctionDistance.getY() > 0) {
				center.add(0, data.distance.getY());
			}
			else {
				center.add(0, -data.distance.getY());
			}
		}
	}
	
	public Vector2f getCenter() {
		return center;
	}
	
	public Vector2f getHalfExtent() {
		return half_extent;
	}
}