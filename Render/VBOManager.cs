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
    class VBOManager
    {
        private static List<VBOItem> VBOItems = new List<VBOItem>();

        public static void clearVBO()
        {
            VBOItems.Clear();
        }

        public static int[] getVBO(String name)
        {
            foreach (VBOItem vboItem in VBOItems)
            {
                if (vboItem.name.CompareTo(name) == 0)
                {
                    return vboItem.VBO;
                }
            }

            VBOItem newVBOItem = new VBOItem(name);
            GLES20.GlGenBuffers(3, newVBOItem.VBO, 0);
            VBOItems.Add(newVBOItem);
            return newVBOItem.VBO;
        }

        public static int getVBOIndex(String name)
        {
            int count = 0;
            foreach (VBOItem vboItem in VBOItems)
            {
                if (vboItem.name.CompareTo(name) == 0)
                {
                    return count;
                }
                count++;
            }

            return -1;
        }
        public static void setSize(String name, int size)
        {
            getVBO(name);
            foreach (VBOItem vboItem in VBOItems)
            {
                if (vboItem.name.CompareTo(name) == 0)
                {
                    vboItem.size = size;
                    break;
                }
            }
        }

        public static int getSize(String name)
        {
            foreach (VBOItem vboItem in VBOItems)
            {
                if (vboItem.name.CompareTo(name) == 0)
                {
                    return vboItem.size;
                }
            }
            return 0;
        }

    }
}