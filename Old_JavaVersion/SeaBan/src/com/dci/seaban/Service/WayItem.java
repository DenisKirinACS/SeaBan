
package com.dci.seaban.Service;

public class WayItem {	
		public int x = 0;
		public int y = 0;
		public int cmd = 0; //0 = move, 1 = rotate
				
		
		public WayItem(int x, int y, int cmd)
		{
			this.x = x;
			this.y = y;
			this.cmd = cmd;
		}
		
}
