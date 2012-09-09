package jp.ac.fun.ateam.oekaki;

import java.util.ArrayList;

import android.graphics.Point;

public class Line {

	//線の色
	private int color;
	//一本の線
	private ArrayList<Point> points;
	
	//コンストラクター
	public Line(int color){
		points = new ArrayList<Point>();
		this.color = color;
	}
	//色を取得
	public int getColor(){
		return color;
	}
	
	//点を線に追加
	public void addPoint(Point p){
		points.add(p);
	}
	//線を構成する点のArrayListを戻す
	public ArrayList<Point> getPoints(){
		return points;
	} 
}
