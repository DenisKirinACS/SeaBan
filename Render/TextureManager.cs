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

namespace SeaBan
{
    class TextureManager
    {
        private static List<TextureItem> TextureItems = new List<TextureItem>();

        public static void clearTextures()
        {
            for (int i=0; i < TextureItems.Count; i++)
            {
                TextureItems[i] = null;
            }

            TextureItems.Clear();
        }

        public static void addTextureHandle(int resID, int handle)
        {
            TextureItem newTextItem = new TextureItem(resID);
            newTextItem.handle = handle;
            TextureItems.Add(newTextItem);

        }

        public static int getTextureHandle(Context context, int resID)
        {
            foreach (TextureItem textItem in TextureItems)
            {
                if (textItem.resID == resID)
                {
                    return textItem.handle;
                }
            }

            TextureItem newTextItem = new TextureItem(resID);
            if (context != null)
            {
                newTextItem.handle = Texture.loadTexture(context, resID);
                TextureItems.Add(newTextItem);
            }

            return newTextItem.handle;
        }

    }
}