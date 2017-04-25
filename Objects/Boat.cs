using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.Opengl;

namespace SeaBan
{
    class Boat: SceneObject
    {

    //public SceneRender render;


    public float lightX;
    public float lightY;
    public float lightZ;

    //private BufferedReader buffer;
    private float countFrame = 0;

    //private final List<SmokeItem> smokeItems = new ArrayList<SmokeItem>();

    public float alphaPlus;


    public float speedX;
    public float speedZ;

    public float force;
    public float beta;


    public float accXdef = 0xFFf;
    public float accYdef = 0xFFf;
    public float accZdef = 0xFFf;

    public float accX = 0;
    public float accY = 0;
    public float accZ = 0;

    public long lastTime;

    public float lastRadius = 0.0f;
    public float lastAlpha = 0.0f;


    private float leftWeelRotate;
    private float rightWeelRotate;
    private float weelMoveCoef = 60.0f;

    public SceneObject wL1, wL2, wL3, wR1, wR2, wR3;
    public SceneObject rocket1, rocket2;

    

    public int gameX = 2;
    public int gameY = 2;

    //private List<WayItem> way;
    private int wayIndex;

    private SceneObject smokeObj;
    private int streamOnID;
    private long lastNext = -1;

    public bool isMove = false;

    public bool noSwing = true;



    public Boat(Context context, String fileName, String shaderName, SceneRender render, int textureResID, int texture2ResID, int texture3ResID)
            :base(context, fileName, shaderName, render, textureResID, texture2ResID, texture3ResID)
        {

        //super (context, fileName, shaderName, render, textureResID, texture2ResID, texture3ResID);
        levelScale = true;

       /*
        for (int i = 0; i < 20; i++)
        {
            SmokeItem smokeItem = new SmokeItem();
            smokeItems.add(smokeItem);
        }
        */

        //smokeObj = new SceneObject(context, "smoke_obj", "water", render, R.drawable.smoke, R.drawable.smoke, R.drawable.smoke);
        //smokeObj.parentDraw = true;
        //smokeObj.blendType = 1;
        //smokeObj.blend = GlobalVar.blendYes;

        //SoundManager.Add("boat", Sound.diselonSoundID);
        //SoundManager.Play("boat", 0.0f,  0.0f, true);

        //streamOnID = Sound.soundPool.play(Sound.diselonSoundID, 0.0f, 0.0f, 1, -1, 1.0f);

    }


    public override void draw()
    {
        Matrix.SetIdentityM(mModelMatrix, 0);
        Matrix.TranslateM(mModelMatrix, 0, x, y, z);

         base.draw();
    }

    /*
    public void drawBlend()
    {

        //	if (RenderManager.quality == 0) return;

        Random r = new Random();


        for (SmokeItem smokeItem: smokeItems)
        {
            if (smokeItem.move.stop)
            {
                if ((lastNext != -1) && (((float)(SystemClock.uptimeMillis() - lastNext) / 1000.0f) < 1.0f)) continue;
                lastNext = SystemClock.uptimeMillis();
                smokeItem.reset(x, y + 1.0f, z, 0.0f);
                smokeItem.scale = 1.01f;
                smokeItem.lastSmokeY = 0;


                for (int i = 0; i < mModelMatrix.length; i++)
                    smokeItem.mModelMatrix[i] = mModelMatrix[i];


                Matrix.translateM(smokeItem.mModelMatrix, 0, 0.5f, 0.0f, -1.3f);
                Matrix.scaleM(smokeItem.mModelMatrix, 0, 0.2f, 0.2f, 0.2f);
            }
            else
            {

                smokeItem.alpha = r.nextFloat() * 5.0f;
                smokeObj.ColorA = (70.0f * GlobalVar.levelScale - smokeItem.move.GetY()) / (100.0f * GlobalVar.levelScale);


                //Matrix.scaleM(smokeItem.mModelMatrix, 0, smokeItem.scale,  smokeItem.scale,  smokeItem.scale);
                Matrix.translateM(smokeItem.mModelMatrix, 0, 0.0f, smokeItem.move.GetY() - smokeItem.lastSmokeY, 0.0f);


                smokeItem.lastSmokeY = smokeItem.move.GetY();

                Matrix.rotateM(smokeItem.mModelMatrix, 0, (float)smokeItem.alpha, 0.0f, 1.0f, 0.0f);

                for (int i = 0; i < smokeItem.mModelMatrix.length; i++)
                    smokeObj.mModelMatrix[i] = smokeItem.mModelMatrix[i];

                smokeObj.drawBlend();


            }

        }


    }

        */

    /*
	@SuppressLint("NewApi")
	private void drawSmoke(SmokeItem smokeItem)
	{
		
		Matrix.setIdentityM(smokeMatrix, 0);
		
				
		Matrix.translateM(smokeMatrix, 0, smokeItem.x, smokeItem.move.GetY(), smokeItem.z);
		Matrix.rotateM(smokeMatrix, 0, (float) smokeItem.alpha, 1.0f, 1.0f, 1.0f);
		Matrix.scaleM(smokeMatrix, 0, smokeItem.scale, smokeItem.scale, smokeItem.scale);
		

		//Draw GLES 
		GLES20.glUseProgram(program); //temp
		final int size = 4;
		final int b = 0;
		final int c = 0;


		GLES20.glEnableVertexAttribArray(attrib_vertex);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOManager.getVBO(fileName)[0]);
		GLES20.glVertexAttribPointer(attrib_vertex, size, GLES20.GL_FLOAT, false, b, c);
		
		GLES20.glEnableVertexAttribArray(normal);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOManager.getVBO(fileName)[2]);
		GLES20.glVertexAttribPointer(normal, size, GLES20.GL_FLOAT, false, b, c);

		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOManager.getVBO(fileName)[1]);
		int bsize = 2;
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle, bsize, GLES20.GL_FLOAT, false, b, c);
		
		
		GLES20.glUniform3f(lightPos, 0.0f, 0.0f, 0.0f);
				
		
		Matrix.multiplyMM(mMVPMatrix, 0, render._mViewMatrix, 0, smokeMatrix, 0);
		GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0); //MV Matrix
		
		Matrix.multiplyMM(mMVPMatrix, 0, render.mProjectionMatrix, 0, mMVPMatrix, 0);
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0); //MVP Matrix


		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TextureManager.getTextureHandle(context, textureResID));
		GLES20.glUniform1i(mTextureUniformHandle, 0);

		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TextureManager.getTextureHandle(context, texture2ResID));
		GLES20.glUniform1i(mTextureUniformHandleF, 1);
		

		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VBOManager.getSize(fileName));



		GLES20.glUseProgram(0);
		
	
	}
	*/

}
}