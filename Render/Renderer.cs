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
using Javax.Microedition.Khronos.Egl;
using Javax.Microedition.Khronos.Opengles;
using System.Threading;

namespace SeaBan
{
    class Renderer : Java.Lang.Object, GLSurfaceView.IRenderer
    {
        public Context context;

        private long time;
        private float FPSSum = 0;
        private int FPSCount = 0;
        private int FrameCount = 0;
        private float fps = 0.0f;
        private static float maxFPS = 25.0f;
        private int maxFPSSec = (int)(1000 / maxFPS);


        // We are looking toward the distance
        public float lookX = 0.0f;
        public float lookY = 0.0f; // 1
        public float lookZ = 0.0f; // 3.1
                                   // Position the eye behind the origin.
        public float eyeX = 0.0f;
        public float eyeY = 100.0f; // 2.0f
        public float eyeZ = 0.0f;
        // Set our up vector. This is where our head would be pointing were we
        // holding the camera.
        protected float upX = 0.0f;
        protected float upY = 1.0f; // 1.0f; //1
        protected float upZ = 0.0f; // 2.0f;

        public float[] _mViewMatrix = new float[16];
        public float[] mViewMatrixCopy = new float[16];
        public float[] mProjectionMatrix = new float[16];


        #region IRenderer implementation

        public void OnDrawFrame(IGL10 gl)
        {
            long currentTime = (SystemClock.UptimeMillis() - time);
            time = SystemClock.UptimeMillis();

            if (currentTime == 0) currentTime = 1;
            float fps = (float)(1000 / currentTime);

            FPSSum += fps;
            FPSCount++;

            if (FPSCount > 100)
            {
                maxFPS = (int)((FPSSum / FPSCount) / 5.0f * 4.0f);
                maxFPSSec = (int)(1000 / maxFPS);
            }

            FrameCount++;
            if (FrameCount > 200)
            {
                FrameCount = 0;
            }


            if (RenderManager.canvasView != null)
            {
                RenderManager.canvasView.dataText = "FPS: " + ((int)FPSSum / FPSCount).ToString() + "mFPS " + ((int)maxFPS).ToString() + ":" + ((int)fps).ToString();
                
            }

            if (GlobalVar.greenMode != 0)
                if (currentTime < maxFPSSec)
                {
                    Thread.Sleep((int)(maxFPSSec - currentTime));                
                    fps = 0.0f;
                }

            RenderManager.Render(gl);
        }

        public void OnSurfaceChanged(IGL10 gl, int width, int height)
        {
            RenderManager.width = width;
            RenderManager.height = height;
            RenderManager.gl = gl;
            RenderManager.GetCurrent().width = width;
            RenderManager.GetCurrent().height = height;
            GLES20.GlViewport(0, 0, width, height);
            float ratio = (float)width / height;
            float left = -ratio;
            float right = ratio;
            float top = 1.0f;
            float bottom = -1.0f;
            float near = 5.4f;
            float far = 200.0f;
            Matrix.FrustumM(RenderManager.GetCurrent().mProjectionMatrix, 0, left, right, bottom, top, near, far);
            //ORTHO!
            //Matrix.OrthoM(RenderManager.GetCurrent().mProjectionMatrix, 0, 0f, width, 0.0f, height, 0, 50);
        }

        public void OnSurfaceCreated(IGL10 gl, Javax.Microedition.Khronos.Egl.EGLConfig config)
        {
            if (RenderManager.externalPaused) RenderManager.initShaders();
            else
                RenderManager.createObjects();

            GLES20.GlClearColor(0.1f, 0.1f, 0.9f, 0.0f);
            GLES20.GlEnable(GLES20.GlDepthTest);
            GLES20.GlEnable(GLES20.GlCullFaceMode);
            GLES20.GlCullFace(GLES20.GlBack);
        }
        #endregion
    }
}