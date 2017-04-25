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
    class VBOItem
    {
        public String name = "";
        public int size = 0;
        public int[] VBO = new int[3];

        public VBOItem(String name)
        {
            this.name = name;
        }
    }
}