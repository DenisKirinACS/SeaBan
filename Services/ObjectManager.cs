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
    class ObjectManager
    {
        private static List<SceneObject> sceneObjects = new List<SceneObject>();

        public static void clear()
        {
            sceneObjects.Clear();
        }

        public static SceneObject get(int index)
        {
            if (index > sceneObjects.Count) return null;
            else
                return sceneObjects[index];
        }

        public static int getIndexByObject(SceneObject sObject)
        {

            for (int i = 0; i < sceneObjects.Count; i++)
            {
                if (sceneObjects[i] == sObject) return i;
            }

            return -1;
        }


        public static void del(int index)
        {
            try
            {
                if (index > sceneObjects.Count) return;
                else
                    sceneObjects.RemoveAt(index);
            }
            catch { }
        }


        public static int getCount()
        {
            return sceneObjects.Count;
        }

        public static void add(SceneObject sceneObject)
        {
            sceneObjects.Add(sceneObject);
        }


    }
}