package com.tomschlessinger.collision;

import com.tomschlessinger.Main;
import com.tomschlessinger.player.Player;
import com.tomschlessinger.util.Vector2i;
import org.joml.Vector2f;

public class BoundingBox {
	private final Vector2f half_extent, center;
	public BoundingBox(Vector2f center, Vector2f half_extent) {
		this.center = center;
		this.half_extent = half_extent;
	}

	public Collision getCollision(BoundingBox box2) {
		Vector2f distance = center.sub(box2.center, new Vector2f());
		distance.x = Math.abs(distance.x);
		distance.y = Math.abs(distance.y);

		distance.sub(half_extent.add(box2.half_extent, new Vector2f()));

		return new Collision(distance, distance.x < 0 && distance.y < 0);
	}

	public Collision getCollision(Vector2f point) {
		Vector2f distance = point.sub(center);
		distance.x = Math.abs(distance.x);
		distance.y = Math.abs(distance.y);

		distance.sub(half_extent);

		return new Collision(distance, distance.x < 0 && distance.y < 0);
	}

	public Vector2f correctPosition2(BoundingBox box2, Collision data, Player p) {
		System.out.println(this);
		System.out.println(box2);
		Vector2f correctionDistance = this.center.sub(box2.center, new Vector2f());
		Vector2f ret = new Vector2f(center);
		if (correctionDistance.x > 0) {
			ret.add(data.distance.x, 0);
		}
		else if(correctionDistance.x < 0){
			ret.add(-data.distance.x, 0);
		}
		if (correctionDistance.y > 0) {
			ret.add(0, data.distance.y);
		}
		else if(correctionDistance.y < 0){
			ret.add(0, -data.distance.y);
		}
		return ret;
//		realpos = x/32,(y+n)/32
//		x,y = 32realpos, 32realpos-n
	}
	public void correctPosition(BoundingBox box2, Collision data) {
		Vector2f correctionDistance = box2.center.sub(center, new Vector2f());
		if (data.distance.x > data.distance.y) {
			if (correctionDistance.x > 0) {
				center.add(data.distance.x, 0);
			}
			else {
				center.add(-data.distance.x, 0);
			}
		}
		else {
			if (correctionDistance.y > 0) {
				center.add(0, data.distance.y);
			}
			else {
				center.add(0, -data.distance.y);
			}
		}
	}

	public Vector2f getCenter() {
		return center;
	}
	public void moveCenter(Vector2f pos){
		center.set(pos);
	}
	public void moveCenter(float x, float y){
		center.set(x,y);
	}
	public Vector2f getHalfExtent() {
		return half_extent;
	}

	public String toString(){
		return "bounding box at at center " + center + " and half-extent " + half_extent;
	}
}
