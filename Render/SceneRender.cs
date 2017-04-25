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
using Javax.Microedition.Khronos.Opengles;
using Android.Opengl;

namespace SeaBan
{
    class SceneRender
    {
        public Context context;
        public int width;
        public int height;
        public float[] _mViewMatrix = new float[16];
        public float[] mProjectionMatrix = new float[16];

        public float eyeX = 0.0f;
        public float eyeY = 100.0f; 
        public float eyeZ = 0.0f;

        public float lookX = 0.0f;
        public float lookY = 0.0f; 
        public float lookZ = 0.0f;

        private double a = 1;

        long time;

        public Circle circle = null;

        public void CreateObjects(int level)
        {

            this.context = RenderManager.context;

            //create boat object to model loading sample
            //Boat boat = new Boat(context, "boat_obj", "map", this, Resource.Drawable.boat, Resource.Drawable.boatf, Resource.Drawable.boats);
            circle = Circle.FactoryCircle(context, RenderManager.edgeCount, "map", this);
            time = SystemClock.UptimeMillis();
        }

        public void initShaders()
        {

            for (int i = 0; i < ObjectManager.getCount(); i++)
            {
                ObjectManager.get(i).initShader();
            }

        }

        public void render(IGL10 gl)
        {

            DataItem dataItem = Http.command(1);

            if (dataItem != null)
            {
                eyeX = dataItem._eyeX;
                eyeY = dataItem._eyeY;
                eyeZ = dataItem._eyeZ;

                lookX = dataItem._lookX;
                lookY = dataItem._lookY;
                lookZ = dataItem._lookZ;

            }

            eyeY = 12.2f;

            long currentTime = (SystemClock.UptimeMillis() - time);

            time = SystemClock.UptimeMillis();
            a += currentTime * 0.001f;

            //float lx = (float)(20.0f * Math.Sin(a + 3.14 / 2 ));
            //float lz = (float)(20.0f * Math.Cos(a + 3.14 / 2));

            eyeX = (float)(20.0f * Math.Sin(a + 3.14 / 2));
            eyeZ = (float)(20.0f * Math.Cos(a + 3.14 / 2));

            eyeX = (float)(20.0f);
            eyeZ = (float)(20.0f);

            Matrix.SetLookAtM(_mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, 0.0f, 1.0f, 0.0f);


            for (int i = 0; i < ObjectManager.getCount(); i++)
            {

                if (!ObjectManager.get(i).parentDraw)
                {
                    // if ((ObjectManager.get(i).blend == GlobalVar.blendNone) || (ObjectManager.get(i).blend == GlobalVar.blendBoth))
                    ObjectManager.get(i).draw();

                }
            }
        }
    }
}
