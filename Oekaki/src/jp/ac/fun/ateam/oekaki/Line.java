package jp.ac.fun.ateam.oekaki;

import java.util.ArrayList;

import android.graphics.Point;

public class Line {

	//���̐F
	private int color;
	//��{�̐�
	private ArrayList<Point> points;
	
	//�R���X�g���N�^�[
	public Line(int color){
		points = new ArrayList<Point>();
		this.color = color;
	}
	//�F���擾
	public int getColor(){
		return color;
	}
	
	//�_����ɒǉ�
	public void addPoint(Point p){
		points.add(p);
	}
	//�����\������_��ArrayList��߂�
	public ArrayList<Point> getPoints(){
		return points;
	} 
}
