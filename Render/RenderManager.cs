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
using Javax.Microedition.Khronos.Opengles;
using Android.Util;

namespace SeaBan
{
    class RenderManager
    {
        public static GLSurfaceView mGLSurfaceView;
        public static int width;
        public static int height;
        public static IGL10 gl;
        public static Context context;
        public static SceneRender render;
        public static LevelCanvas canvasView;
        public static DisplayMetrics metrics;
        public static RelativeLayout relativeLayout;
        public static bool externalPaused = false;
        public static int edgeCount = 12;

        public static SceneRender GetCurrent()
        {
            return render;
        }

        public static void Render(IGL10 gl)
        {
            GLES20.GlClear(GLES20.GlColorBufferBit | GLES20.GlDepthBufferBit);
            render.render(gl);
        }

        public static void Resume()
        {

            if (ObjectManager.getCount() != 0)
            {
                ObjectManager.del(0);
            }

            if (RenderManager.relativeLayout.ChildCount != 0)
            {
                RenderManager.relativeLayout.RemoveView(RenderManager.mGLSurfaceView);
                RenderManager.relativeLayout.RemoveView(RenderManager.canvasView);
            }

            if (RenderManager.relativeLayout.ChildCount == 0)
            {
                RenderManager.relativeLayout.AddView(RenderManager.mGLSurfaceView);
                RenderManager.relativeLayout.AddView(RenderManager.canvasView);
            }
            RenderManager.externalPaused = false;
            RenderManager.mGLSurfaceView.OnResume();
        }

        public static void Pause()
        {
            RenderManager.externalPaused = true;
            RenderManager.mGLSurfaceView.OnPause();
        }

        public static void initShaders()
        {
            VBOManager.clearVBO();
            TextureManager.clearTextures();
            ShaderManager.clear();
            render.initShaders();
        }


        public static void createObjects()
        {
            TextureManager.clearTextures();
            ShaderManager.clear();
            VBOManager.clearVBO();
            render = new SceneRender();
            render.CreateObjects(1);
        }

    }
}