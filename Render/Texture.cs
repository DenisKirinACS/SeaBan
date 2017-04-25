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
using Java.Lang;

namespace SeaBan
{
    class Texture
    {
        public static int loadTexture(Context context, int resourceId)
        {

            if (resourceId == -1) return resourceId;

            System.GC.Collect();

            int[] textureHandle = new int[1];

            GLES20.GlGenTextures(1, textureHandle, 0);

            if (textureHandle[0] != 0)
            {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.InScaled = false; // No pre-scaling
                Bitmap bitmap = BitmapFactory.DecodeResource(context.Resources, resourceId, options);
                GLES20.GlBindTexture(GLES20.GlTexture2d, textureHandle[0]);
                GLES20.GlTexParameteri(GLES20.GlTexture2d, GLES20.GlTextureMinFilter, GLES20.GlNearest);
                GLES20.GlTexParameteri(GLES20.GlTexture2d, GLES20.GlTextureMagFilter, GLES20.GlNearest);
                GLES20.GlTexParameteri(GLES20.GlTexture2d, GLES20.GlTextureWrapS, GLES20.GlClampToEdge);
                GLES20.GlTexParameteri(GLES20.GlTexture2d, GLES20.GlTextureWrapT, GLES20.GlClampToEdge);
                GLUtils.TexImage2D(GLES20.GlTexture2d, 0, bitmap, 0);
                bitmap.Recycle();
            }

            if (textureHandle[0] == 0)
            {
                throw new RuntimeException("Error loading texture.");
            }

            return textureHandle[0];
        }
    }
}