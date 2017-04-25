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
    class ShaderManager
    {
        private static List<ShaderCompiller> shaderItems = new List<ShaderCompiller>();

        public static void addShader(Context context, String name)
        { // Quality 1, 0

            if (getShader(name) != null) return;

            String fileName = "";

            //Temporary not use LOW shaders
            fileName = name;

            ShaderCompiller shader = new ShaderCompiller(context, name, fileName);

            shaderItems.Add(shader);

        }

        public static void clear()
        {
            shaderItems.Clear();
        }

        public static ShaderCompiller getShader(String shaderName)
        {
            foreach (ShaderCompiller shaderItem in shaderItems)
            {
                if (shaderItem.shaderName.CompareTo(shaderName) == 0)
                {
                    return shaderItem;
                }
            }
            return null;
        }

    }
}