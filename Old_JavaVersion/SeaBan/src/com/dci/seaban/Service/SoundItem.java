package com.dci.seaban.Service;

public class SoundItem {
	public String Name;
	private float ValumeR;
	private float ValumeL;
	public int SoundID;
	public int StreamID;
	public boolean Loop;
	
	public void setValume(float ValumeR, float ValumeL){
		if (ValumeR < 0) ValumeR=0;
		if (ValumeR > 1) ValumeR=1;
		if (ValumeL < 0) ValumeL=0;
		if (ValumeL > 1) ValumeL=1;
		this.ValumeR=ValumeR;
		this.ValumeL=ValumeL;

	}
	public float getVolumeR(){
		return ValumeR;
	}
	public float getVolumeL(){
		return ValumeL;
	}

}
