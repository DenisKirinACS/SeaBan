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
    class TextureItem
    {
        public int resID = 0;
        public int handle = -1;

        public TextureItem(int resID)
        {
            this.resID = resID;
        }
    }
}