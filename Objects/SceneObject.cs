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
using Java.Nio;
using Android.Opengl;
using System.IO;

namespace SeaBan
{
    class SceneObject
    {
        public int pos = 0;
        public float alpha = 0;
        public float radius = 0;
        public float x = 0.0f;
        public float y = 0.0f;
        public float z = 0.0f;
        protected Context context;
        protected SceneRender render;
        protected static String TAG = null;
        public bool levelScale = false;

        public float[] mModelMatrix = new float[16];

        public float[] mMVPMatrix = new float[16];

        public const int FLOAT_SIZE = 4; // GL float size?


        protected FloatBuffer vertexBuffer;
        protected FloatBuffer normalBuffer;
        protected FloatBuffer textureBuffer;


        public String fileName;
        public String shaderName;
        protected List<String> files = new List<String>();

        public int blend = 0;
        public int blendType = 0;

        public int useLight = 0;

        public float ColorR = 0.0f;
        public float ColorG = 0.0f;
        public float ColorB = 0.0f;
        public float ColorA = 0.0f;

        public int health = 100;
        public bool live = true;
        public bool parentDraw = false;
        public int textureResID;
        public int texture2ResID;
        public int texture3ResID;

        public SceneObject(Context context, String fileName, String shaderName, SceneRender render, int textureResID, int texture2ResID, int texture3ResID)
        {

            if (context == null) return;

            if (fileName != "")
            {
                this.context = context;
                this.render = render;
                this.fileName = fileName;                
                this.shaderName = shaderName;
                this.textureResID = textureResID;
                this.texture2ResID = texture2ResID;
                this.texture3ResID = texture3ResID;
                initShader();
            }
            else
            {
                this.context = context;
                this.render = render;
                this.fileName = "";
                this.textureResID = -1;
                this.texture2ResID = -1;
                this.texture3ResID = -1;
            }

            ObjectManager.add(this);
        }
        public virtual void initShader()
        {
            if (string.IsNullOrEmpty(fileName)) return;

            ShaderManager.addShader(context, shaderName);

            TextureManager.getTextureHandle(context, textureResID);
            TextureManager.getTextureHandle(context, texture2ResID);
            TextureManager.getTextureHandle(context, texture3ResID);

            loadFileToVBO(fileName);
        }
        //----------------------------------------------------------------------
        public virtual void loadFileToVBO(String fileName)
        {
            if (string.IsNullOrEmpty(fileName)) return;
            files.Add(fileName);
            int indexResult = VBOManager.getVBOIndex(fileName);
            if (indexResult == -1)
            {
                if (loadingBinModel(fileName))
                {
                    GLES20.GlBindBuffer(GLES20.GlArrayBuffer, VBOManager.getVBO(fileName)[0]);
                    GLES20.GlBufferData(GLES20.GlArrayBuffer, vertexBuffer.Capacity() * FLOAT_SIZE, vertexBuffer, GLES20.GlStaticDraw); // vbb.capacity()
                    vertexBuffer = null;
                    
                    GLES20.GlBindBuffer(GLES20.GlArrayBuffer, VBOManager.getVBO(fileName)[2]);
                    GLES20.GlBufferData(GLES20.GlArrayBuffer, normalBuffer.Capacity() * FLOAT_SIZE, normalBuffer, GLES20.GlStaticDraw); // vbb.capacity()
                    normalBuffer = null;
                    
                    GLES20.GlBindBuffer(GLES20.GlArrayBuffer, VBOManager.getVBO(fileName)[1]);
                    GLES20.GlBufferData(GLES20.GlArrayBuffer, textureBuffer.Capacity() * FLOAT_SIZE, textureBuffer, GLES20.GlStaticDraw); // vbb.capacity()
                    textureBuffer = null;
                    System.GC.Collect();
                }
            }
        }

        //----------------------------------------------------------------------
        public void selectUsedVBO(int number)
        {
            if (files.Count == 0) return;
            if (number > files.Count - 1) return;
            fileName = files[number];
        }

        public void drawBlend()
        {
            /*
            GLES20.GlEnable(GLES20.GlBlend);
            //GLES20.GlBlendFunc(GLES20.Gl_ONE_MINUS_DST_COLOR, GLES20.Gl_ONE_MINUS_SRC_ALPHA); //smoke
            //GLES20.GlBlendFunc(GLES20.Gl_SRC_ALPHA, GLES20.Gl_ONE); //water
            switch (blendType)
            {
                case 0:
                    GLES20.GlBlendFunc(GLES20.GlSrcAlpha, GLES20.GlOne);
                    break;
                case 1:
                    GLES20.GlBlendFunc(GLES20.GlOneMinusDstColor, GLES20.GlOneMinusSrcAlpha);
                    break;
            }

            draw();

            GLES20.GlDisable(GLES20.GlBlend);
            */
        }

    public virtual void draw()
        {
            try
            {
              //  if (levelScale) Matrix.ScaleM(mModelMatrix, 0, GlobalVar.levelScale, GlobalVar.levelScale, GlobalVar.levelScale);

                ShaderCompiller shader = ShaderManager.getShader(shaderName);

                GLES20.GlUseProgram(shader.program);


                int size = 4; // data count 2 for vec2 4 for vec4
                int b = 0;
                int c = 0;

                GLES20.GlEnableVertexAttribArray(ShaderManager.getShader(shaderName).attrib_vertex);
                GLES20.GlBindBuffer(GLES20.GlArrayBuffer, VBOManager.getVBO(fileName)[0]);
                GLES20.GlVertexAttribPointer(ShaderManager.getShader(shaderName).attrib_vertex, size, GLES20.GlFloat, false, b, c);

                GLES20.GlEnableVertexAttribArray(ShaderManager.getShader(shaderName).normal);
                GLES20.GlBindBuffer(GLES20.GlArrayBuffer, VBOManager.getVBO(fileName)[2]);
                GLES20.GlVertexAttribPointer(ShaderManager.getShader(shaderName).normal, size, GLES20.GlFloat, false, b, c);

                GLES20.GlUniform3f(ShaderManager.getShader(shaderName).lightPos, render.eyeX, render.eyeY, render.eyeZ);

                GLES20.GlUniformMatrix4fv(ShaderManager.getShader(shaderName).mMVMatrixHandle, 1, false, render._mViewMatrix, 0); //MV Matrix
                                                                                                                                  //Matrix.SetIdentityM(mMVPMatrix, 0);
                float[] mModetView = new float[16];
                Matrix.MultiplyMM(mModetView, 0, render._mViewMatrix, 0, mModelMatrix, 0);
                
                Matrix.MultiplyMM(mMVPMatrix, 0, render.mProjectionMatrix, 0, mModetView, 0);
                GLES20.GlUniformMatrix4fv(ShaderManager.getShader(shaderName).mMVPMatrixHandle, 1, false, mMVPMatrix, 0); //MVP Matrix

                if (textureResID != -1)
                {
                    GLES20.GlEnableVertexAttribArray(ShaderManager.getShader(shaderName).mTextureCoordinateHandle);
                    GLES20.GlBindBuffer(GLES20.GlArrayBuffer, VBOManager.getVBO(fileName)[1]);
                    int bsize = 2;
                    GLES20.GlVertexAttribPointer(ShaderManager.getShader(shaderName).mTextureCoordinateHandle, bsize, GLES20.GlFloat, false, b, c);

                    GLES20.GlActiveTexture(GLES20.GlTexture0);
                    GLES20.GlBindTexture(GLES20.GlTexture2d, TextureManager.getTextureHandle(context, textureResID));
                    GLES20.GlUniform1i(ShaderManager.getShader(shaderName).mTextureUniformHandle, 0);


                    GLES20.GlActiveTexture(GLES20.GlTexture1);
                    GLES20.GlBindTexture(GLES20.GlTexture2d, TextureManager.getTextureHandle(context, texture2ResID));
                    GLES20.GlUniform1i(ShaderManager.getShader(shaderName).mTextureUniformHandleF, 1);

                    GLES20.GlActiveTexture(GLES20.GlTexture2);
                    GLES20.GlBindTexture(GLES20.GlTexture2d, TextureManager.getTextureHandle(context, texture3ResID));
                    GLES20.GlUniform1i(ShaderManager.getShader(shaderName).mTextureUniformHandleS, 2);
                }

                GLES20.GlDrawArrays(GLES20.GlTriangles, 0, VBOManager.getSize(fileName));

                GLES20.GlUseProgram(0);
            }
            catch { }
        }

        public bool loadingBinModel(String fileName)
        {
            float[] floatArray;
            long size;
            try
            {
                // Vertex
                vertexBuffer = null;
                System.GC.Collect();
                int resourceId = context.Resources.GetIdentifier(fileName + "_vert", "raw", context.PackageName);
                Stream fileIn = context.Resources.OpenRawResource(resourceId) as Stream;
                MemoryStream m = new MemoryStream();
                fileIn.CopyTo(m);
                size = m.Length;
                floatArray = new float[size / 4];
                System.Buffer.BlockCopy(m.ToArray(), 0, floatArray, 0, (int)size);
                 
                vertexBuffer = FloatBuffer.Allocate((int)size / 4); // float array to
                vertexBuffer.Put(floatArray, 0, (int)size / 4);
                vertexBuffer.Flip();

                VBOManager.setSize(fileName, vertexBuffer.Capacity() / 4); //is size of vertex count = 1 vertex 4 float x,y,z, 1                                
                floatArray = null;                
                fileIn.Close();
                m.Close();
                //----------------------------------------------------------------------------------------------------

                normalBuffer = null;
                System.GC.Collect();
                resourceId = context.Resources.GetIdentifier(fileName + "_norm", "raw", context.PackageName);
                fileIn = context.Resources.OpenRawResource(resourceId) as Stream;
                m = new MemoryStream();
                fileIn.CopyTo(m);
                size = m.Length;
                floatArray = new float[size / 4];
                System.Buffer.BlockCopy(m.ToArray(), 0, floatArray, 0, (int)size);

                normalBuffer = FloatBuffer.Allocate((int)size / 4); // float array to
                normalBuffer.Put(floatArray, 0, (int)size / 4);
                normalBuffer.Flip();
         
                floatArray = null;
                fileIn.Close();
                m.Close();
                //----------------------------------------------------------------------------------------------------
                textureBuffer = null;
                System.GC.Collect();
                resourceId = context.Resources.GetIdentifier(fileName + "_texture", "raw", context.PackageName);
                fileIn = context.Resources.OpenRawResource(resourceId) as Stream;
                m = new MemoryStream();
                fileIn.CopyTo(m);
                size = m.Length;
                floatArray = new float[size / 4];
                System.Buffer.BlockCopy(m.ToArray(), 0, floatArray, 0, (int)size);

                textureBuffer = FloatBuffer.Allocate((int)size / 4); // float array to
                textureBuffer.Put(floatArray, 0, (int)size / 4);
                textureBuffer.Flip();

                floatArray = null;
                fileIn.Close();
                m.Close();

                return true;

            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return false;
            }
        }



    }
}