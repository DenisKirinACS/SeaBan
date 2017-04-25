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
using Java.Nio;
using Android.Graphics;
using static Android.Graphics.Bitmap;

namespace SeaBan
{
    class Circle : SceneObject
    {
        static public float radius = 3.0f;
        static public float height = 2.0f;

        static public int steps = 0;

        private int textureStep = 50;

        public float rotate = 0;
        public double rotateSpeed = 0;
        public Boolean left = false;

        public Circle(Context context, String fileName, String shaderName, SceneRender render, int textureResID, int texture2ResID, int texture3ResID)
            : base(context, fileName, shaderName, render, textureResID, texture2ResID, texture3ResID)
        {
            levelScale = true;
        }

        public void newAdge(int newSteps)
        {
            loadFileToVBO("Circle");
        }

        public override void initShader() 
        {
            base.initShader();


            //make texture ----------------------------------------------------
            //draw texture 
            if (LevelCanvas.b != null) LevelCanvas.b.Recycle();
            int width = steps * textureStep;
            int height = steps * textureStep;
            Bitmap bitmap = Bitmap.CreateBitmap(width, height, Config.Argb8888);
            Canvas canvas = new Canvas(bitmap);

            Paint paint = new Paint();
            paint.Color = Android.Graphics.Color.Green;
            paint.SetStyle(Paint.Style.Fill);
            canvas.DrawPaint(paint);

            paint.Color = Android.Graphics.Color.White;
            paint.AntiAlias = true;
            paint.TextSize = textureStep;
            paint.TextAlign = Paint.Align.Center;


            for (int i = 0; i < steps; i++)
            {

                paint.Color = Android.Graphics.Color.Rgb(i * 0xF, i * 0xF, i * 0xF);
                canvas.DrawText((steps- i).ToString(), textureStep + textureStep / 2, i * textureStep + textureStep - textureStep /6, paint);
            }
            //load texture 

            int[] textureHandle = new int[1];
            GLES20.GlGenTextures(1, textureHandle, 0);

            if (textureHandle[0] != 0)
            {
                GLES20.GlBindTexture(GLES20.GlTexture2d, textureHandle[0]);
                GLES20.GlTexParameteri(GLES20.GlTexture2d, GLES20.GlTextureMinFilter, GLES20.GlNearest);
                GLES20.GlTexParameteri(GLES20.GlTexture2d, GLES20.GlTextureMagFilter, GLES20.GlNearest);
                GLES20.GlTexParameteri(GLES20.GlTexture2d, GLES20.GlTextureWrapS, GLES20.GlClampToEdge);
                GLES20.GlTexParameteri(GLES20.GlTexture2d, GLES20.GlTextureWrapT, GLES20.GlClampToEdge);

                GLUtils.TexImage2D(GLES20.GlTexture2d, 0, bitmap, 0);
                //    bitmap.Recycle();
                LevelCanvas.b = bitmap;
            }

            textureResID = textureHandle[0];
            TextureManager.addTextureHandle(textureResID, textureHandle[0]);        
        }
        public static Circle FactoryCircle(Context context, int steps, String shaderName, SceneRender render)
        {
            int textureResID = -1;
            int texture2ResID = -1;
            int texture3ResID = -1;
            string fileName = "Circle";
            Circle.steps = steps;
            Circle circle = new Circle(context, fileName, shaderName, render, textureResID, texture2ResID, texture3ResID);
            return circle;
        }

        private float coordToTexture(float coord)
        {
            float result = 1 / ((steps * textureStep) / coord);
            return result;
        }
        public override void loadFileToVBO(String fileName)
        {
            if (fileName == "") return;
            files.Add(fileName);
            int indexResult = VBOManager.getVBOIndex(fileName);
            if (indexResult == -1)
            {

                List<float[]> vertexes = new List<float[]>();
                List<float[]> normales = new List<float[]>();
                List<float[]> textures = new List<float[]>();
                float alphaStep = (float)(2 * Math.PI) / steps;
                float alpha = 0;

                //front and back faces
                for (int i = 0; i < steps; i++)
                {
                    //triagle face
                    float[] v1 = new float[4]; //vertex 1
                    float[] v2 = new float[4]; //vertex 2
                    float[] v3 = new float[4]; //vertex 3

                    //top face
                    v1[0] = 0; //x
                    v1[1] = 0; //y
                    v1[2] = height; //z
                    v1[3] = 1;

                    v2[0] = radius * (float)Math.Sin(alpha);
                    v2[1] = radius * (float)Math.Cos(alpha);
                    v2[2] = height; //z
                    v2[3] = 1;

                    alpha += alphaStep;

                    v3[0] = radius * (float)Math.Sin(alpha);
                    v3[1] = radius * (float)Math.Cos(alpha);
                    v3[2] = height; //z
                    v3[3] = 1;

                    vertexes.Add(v1);
                    vertexes.Add(v2);
                    vertexes.Add(v3);

                    //normales
                    float[] normale = new float[4]; //vertex 1
                    normale[0] = -1; //x
                    normale[1] = 0; //y
                    normale[2] = 0; //z
                    normale[3] = 1;

                    normales.Add(normale);
                    normales.Add(normale);
                    normales.Add(normale);

                    float[] t1 = new float[2];
                    float[] t2 = new float[2];
                    float[] t3 = new float[2];
                    t1[0] = 0.0f;
                    t1[1] = 0.0f;
                    t2[0] = 0.0f;
                    t2[1] = 0.0f;
                    t3[0] = 0.0f;
                    t3[1] = 0.0f;
                    textures.Add(t1);
                    textures.Add(t2);
                    textures.Add(t3);


                    //spin face (from top) -----------
                    float[] vS1 = new float[4]; //vertex 1
                    float[] vS2 = new float[4]; //vertex 2
                    float[] vS3 = new float[4]; //vertex 3
                    vS1[0] = radius * (float)Math.Sin(alpha);
                    vS1[1] = radius * (float)Math.Cos(alpha);
                    vS1[2] = 0; //z
                    vS1[3] = 1;
                    vS2 = v2;
                    vS3 = v3;
                    vertexes.Add(vS1);
                    vertexes.Add(vS2);
                    vertexes.Add(vS3);

                    normale = new float[4]; //vertex 1
                    normales.Add(normale);
                    normales.Add(normale);
                    normales.Add(normale);

                    //SPIN TEXTURE FACE
                    //Spin face texture -----------------------------------

                    t1 = new float[2];
                    t2 = new float[2];
                    t3 = new float[2];

                    t1[0] = coordToTexture(textureStep);
                    t1[1] = coordToTexture(textureStep * i);
                    t2[0] = coordToTexture(textureStep * 2);
                    t2[1] = coordToTexture(textureStep * i);
                    t3[0] = coordToTexture(textureStep); //texture x
                    t3[1] = coordToTexture(textureStep * i + textureStep); //texture y


                    textures.Add(t3);
                    textures.Add(t2);
                    textures.Add(t1);
                    //--------------------------------------------------------

                    //back face -------------------------------------
                    alpha -= alphaStep;
                    v1 = new float[4]; //vertex 1
                    v2 = new float[4]; //vertex 2
                    v3 = new float[4]; //vertex 3

                    //top face
                    v1[0] = 0; //x
                    v1[1] = 0; //y
                    v1[2] = 0; //z
                    v1[3] = 1;

                    v2[0] = radius * (float)Math.Sin(alpha);
                    v2[1] = radius * (float)Math.Cos(alpha);
                    v2[2] = 0; //z
                    v2[3] = 1;

                    alpha += alphaStep;

                    v3[0] = radius * (float)Math.Sin(alpha);
                    v3[1] = radius * (float)Math.Cos(alpha);
                    v3[2] = 0; //z
                    v3[3] = 1;

                    vertexes.Add(v1);
                    vertexes.Add(v2);
                    vertexes.Add(v3);

                    //normales
                    normale = new float[4]; //vertex 1
                    normale[0] = -1; //x
                    normale[1] = 0; //y
                    normale[2] = 0; //z
                    normale[3] = 1;

                    normales.Add(normale);
                    normales.Add(normale);
                    normales.Add(normale);

                    t1 = new float[2];
                    t2 = new float[2];
                    t3 = new float[2];
                    t1[0] = 0;
                    t1[1] = 0;
                    t2[0] = 0;
                    t2[1] = 0;
                    t3[0] = 0;
                    t3[1] = 0;
                    textures.Add(t1);
                    textures.Add(t2);
                    textures.Add(t3);


                    //spin face (from top) -----------
                    alpha -= alphaStep;
                    vS1 = new float[4]; //vertex 1
                    vS2 = new float[4]; //vertex 2
                    vS3 = new float[4]; //vertex 3
                    vS1[0] = radius * (float)Math.Sin(alpha);
                    vS1[1] = radius * (float)Math.Cos(alpha);
                    vS1[2] = height; //z
                    vS1[3] = 1;

                    vS2 = v2;
                    vS3 = v3;
                    vertexes.Add(vS1);
                    vertexes.Add(vS2);
                    vertexes.Add(vS3);

                    normale = new float[4]; //vertex 1
                    normales.Add(normale);
                    normales.Add(normale);
                    normales.Add(normale);

                    t1 = new float[2];
                    t2 = new float[2];
                    t3 = new float[2];
                    t1[0] = coordToTexture(textureStep * 2); //texture x
                    t1[1] = coordToTexture(textureStep * i); //texture y
                    t2[0] = coordToTexture(textureStep * 2);
                    t2[1] = coordToTexture(textureStep * i + textureStep);
                    t3[0] = coordToTexture(textureStep );
                    t3[1] = coordToTexture(textureStep * i + textureStep);
                    textures.Add(t1);
                    textures.Add(t2);
                    textures.Add(t3);

                    alpha += alphaStep;

                }

                long size = vertexes.Count; //all faces (faces*4 = all vertex) 

                float[] floatArray = new float[size * 4]; //4 float to one vertex, 3 vertex to one face  slow way 
                for (int i = 0; i < vertexes.Count; i++)
                {
                    floatArray[i * 4 + 0] = vertexes[i][0];
                    floatArray[i * 4 + 1] = vertexes[i][1];
                    floatArray[i * 4 + 2] = vertexes[i][2];
                    floatArray[i * 4 + 3] = vertexes[i][3];
                }

                vertexBuffer = FloatBuffer.Allocate((int)size * 4); // float array to
                vertexBuffer.Put(floatArray, 0, (int)size * 4);
                vertexBuffer.Flip();

                GLES20.GlBindBuffer(GLES20.GlArrayBuffer, VBOManager.getVBO(fileName)[0]);
                GLES20.GlBufferData(GLES20.GlArrayBuffer, vertexBuffer.Capacity() * FLOAT_SIZE, vertexBuffer, GLES20.GlStaticDraw); // vbb.capacity()
                vertexBuffer = null;
                floatArray = null;

                //-----------------------------------------------------------------------
                floatArray = new float[size * 4]; //4 float to one vertex, 3 vertex to one face  slow way 
                for (int i = 0; i < vertexes.Count; i++)
                {
                    floatArray[i * 4 + 0] = normales[i][0];
                    floatArray[i * 4 + 1] = normales[i][1];
                    floatArray[i * 4 + 2] = normales[i][2];
                    floatArray[i * 4 + 3] = normales[i][3];
                }
                normalBuffer = FloatBuffer.Allocate((int)size * 4);
                normalBuffer.Put(floatArray, 0, (int)size * 4);
                normalBuffer.Flip();

                GLES20.GlBindBuffer(GLES20.GlArrayBuffer, VBOManager.getVBO(fileName)[2]);
                GLES20.GlBufferData(GLES20.GlArrayBuffer, normalBuffer.Capacity() * FLOAT_SIZE, normalBuffer, GLES20.GlStaticDraw); // vbb.capacity()
                normalBuffer = null;

                //-----------------------------------------------------------------------
                floatArray = new float[size * 2]; //2 float 
                for (int i = 0; i < textures.Count; i++)
                {
                    floatArray[i * 2 + 0] = textures[i][0];
                    floatArray[i * 2 + 1] = textures[i][1];
                }
                textureBuffer = FloatBuffer.Allocate((int)size * 2);
                textureBuffer.Put(floatArray, 0, (int)size * 2);
                textureBuffer.Flip();

                GLES20.GlBindBuffer(GLES20.GlArrayBuffer, VBOManager.getVBO(fileName)[1]);
                GLES20.GlBufferData(GLES20.GlArrayBuffer, textureBuffer.Capacity() * FLOAT_SIZE, textureBuffer, GLES20.GlStaticDraw); // vbb.capacity()
                textureBuffer = null;

                VBOManager.setSize(fileName, (int)size);                
            }

        }

        public override void draw()
        {
            RenderManager.canvasView.PostInvalidate();

            Android.Opengl.Matrix.SetIdentityM(mModelMatrix, 0);
            Android.Opengl.Matrix.TranslateM(mModelMatrix, 0, 0, 0, 0);
            Android.Opengl.Matrix.RotateM(mModelMatrix, 0, 90, 1, 0, 0);
            Android.Opengl.Matrix.RotateM(mModelMatrix, 0, 180, 0, 1, 0);

            if (rotate != 0)
            {
                if (left)
                {
                    if (rotate >= 0)
                    {
                        alpha -= rotate;
                        rotate -= (float)rotateSpeed;
                    }
                }
                else
                {
                    if (rotate <= 0)
                    {
                        alpha -= rotate;
                        rotate += (float)rotateSpeed;
                    }
                }

            }
            Android.Opengl.Matrix.RotateM(mModelMatrix, 0, alpha, 0, 0, 1);

            base.draw();

        }


    }
}