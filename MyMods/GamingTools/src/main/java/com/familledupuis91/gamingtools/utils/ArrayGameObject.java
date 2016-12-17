package com.familledupuis91.gamingtools.utils;

import java.util.ArrayList;

import com.familledupuis91.gamingtools.components.shapes.Shape;
import com.familledupuis91.gamingtools.components.shapes.Rectangle2D;
import com.familledupuis91.gamingtools.enums.DrawingMode;

public class ArrayGameObject {

	public ArrayGameObject() {

	}

	public static ArrayList<Shape> make(float sceneWidth,
			float scenehight, int dimX, int dimY, Rectangle2D gameObject,
			float margin) {

		ArrayList<Shape> result = new ArrayList<Shape>();

		float taillex = (sceneWidth - ((dimX + 1) * margin)) / dimX;
		float tailley = (scenehight - ((dimY + 1) * margin)) / dimY;

		float x = 0;
		float y = 0;
		y = margin + (tailley / 2);
		for (int i = 1; i <= dimY; i++) {

			x = margin + (taillex / 2);
			for (int j = 0; j < dimX; j++) {

				Rectangle2D go = new Rectangle2D(DrawingMode.FILL);
				try {
					go = (Rectangle2D) gameObject.clone();

					go.setCoord(x, y);
					go.setHeight(tailley);
					go.setWidth(taillex);
					result.add(go);

				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				x += (margin + taillex);
			}
			y += (margin + tailley);

		}
		return result;
	}

}
