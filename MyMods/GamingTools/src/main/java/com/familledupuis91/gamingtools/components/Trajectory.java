package com.familledupuis91.gamingtools.components;

import com.familledupuis91.gamingtools.utils.Vector2D;

public class Trajectory extends Vector2D{

private float force;


public Trajectory(){
super();
this.force =1;
}

public void randomize(){
	this.x = (float) (Math.random() * this.force);
	this.y = (float) (Math.random() * this.force);

}

public float getForce(){
	return this.force;
	
}


public void setForce(float force){
	this.force = force;
	
}
}
