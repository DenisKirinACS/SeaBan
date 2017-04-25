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
using Android.Graphics;

namespace SeaBan
{
    class GlobalVar
    {
        public static Activity mainActivity = null;
        public static Boolean debugMode = true;

        public static float defaultWidth = 1920;
        public static float defaultHeight = 1080;
        public static int greenMode = 1;

        public static int levelScale = 11;


        public static float GetScrX(float x, float y, float z)
        {
            if (RenderManager.GetCurrent() == null)
                return -1;

            float[] vector = new float[4];
            int[] viewport = new int[] { 0, 0, RenderManager.width, RenderManager.height };

            GLU.GluProject(x, y, z, RenderManager.GetCurrent()._mViewMatrix, 0, RenderManager.GetCurrent().mProjectionMatrix, 0, viewport, 0, vector, 0);

            if (vector[2] > 1.0f)
            {
                return -1.0f;

            }
            else
            {
                return vector[0];
                // screenY = viewport[3] - vector[1];
            }
        }

        public static float GetScrY(float x, float y, float z)
        {
            if (RenderManager.GetCurrent() == null)
                return -1;

            float[] vector = new float[4];
            int[] viewport = new int[] { 0, 0, RenderManager.width, RenderManager.height };

            GLU.GluProject(x, y, z, RenderManager.GetCurrent()._mViewMatrix, 0, RenderManager.GetCurrent().mProjectionMatrix, 0, viewport, 0, vector, 0);

            if (vector[2] > 1.0f)
            {
                return -1.0f;

            }
            else
            {
                return viewport[3] - vector[1];
            }
        }

        public static float GetScrX(float x)
        {
            if ((Build.VERSION.SdkInt > Build.VERSION_CODES.Kitkat) || (RenderManager.width == 0))
            {
                return x * (RenderManager.metrics.WidthPixels / defaultWidth);
            }
            else
                return x * (RenderManager.width / defaultWidth);
        }

        public static float GetScrY(float y)
        {
            if ((Build.VERSION.SdkInt > Build.VERSION_CODES.Kitkat) || (RenderManager.height == 0))
            {
                return y * (RenderManager.metrics.HeightPixels / defaultHeight);
            }
            else
                return y * (RenderManager.height / defaultHeight);
        }

        public static int calculateWidthFromFontSize(String testString, int currentSize, Paint paint)
        {
            Rect bounds = new Rect();

            paint.TextSize = GetScrX(currentSize);
            paint.GetTextBounds(testString, 0, testString.Length, bounds);

            return (int)Math.Ceiling((double)bounds.Width());
        }

        public static int calculateHeightFromFontSize(Paint paint, String testString, int currentSize)
        {
            Rect bounds = new Rect();

            paint.TextSize = currentSize;
            paint.GetTextBounds(testString, 0, testString.Length, bounds);

            return (int)Math.Ceiling((double)bounds.Height());
        }

        public static void drawMultilineText(String str, float x, float y, Canvas canvas, int fontSize, float drawWidth)
        {
            if (string.IsNullOrEmpty(str)) return;
            int lineHeight = 0;

            String[] lines = str.Split(' ');

            Paint paint = new Paint();



            lineHeight = (int)(calculateHeightFromFontSize(paint, str, fontSize) * 1.2);
            int yoffset = lineHeight;

            String line = "";
            for (int i = 0; i < lines.Length; ++i)
            {

                if (calculateWidthFromFontSize(line + " " + lines[i], fontSize, paint) <= drawWidth)
                {
                    line = line + " " + lines[i];

                }
                else
                {
                    canvas.DrawText(line, GetScrX(x), GetScrY(y + yoffset), paint);
                    yoffset += lineHeight;
                    line = lines[i];
                }
            }
            canvas.DrawText(line, GetScrX(x), GetScrY(y + yoffset), paint);

        }

    }
}