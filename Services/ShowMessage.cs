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
using Android.Systems;
using Java.Lang;
using Android.Text;

namespace SeaBan
{
    class ShowMessage
    {
        protected static string _text;

        public static void ShowCrash(string text)
        {
            _text = text;

            GlobalVar.mainActivity.RunOnUiThread(() =>
            {


                for (int i = 0; i < 10; i++)
                    Toast.MakeText(GlobalVar.mainActivity, "Android version: " + Build.VERSION.Release, ToastLength.Long).Show();

                string DebugInfo = "<br><br> <b>Debug-infos:</b>"
                 + "<br> OS Version: " + Android.Systems.OsConstants.Sc2CVersion.ToString() + "(" + Android.OS.Build.VERSION.Incremental + ")"
                 + "<br> OS API Level: " + Android.OS.Build.VERSION.SdkInt
                 + "<br> Device: " + Android.OS.Build.Device
                 + "<br> Model (Product): " + Android.OS.Build.Model + " (" + Android.OS.Build.Product + ")";


                AlertDialog alertDialog = new AlertDialog.Builder(GlobalVar.mainActivity).Create();

                alertDialog.SetTitle("Houston, we have a problem");


                alertDialog.SetMessage(Html.FromHtml("We are very sorry, <b>SeaBan</b> is stop running :( <br><br>"
                        + "<b>The reson: </b><br><i>"
                        + ShowMessage._text
                        + "</i>"
                        + DebugInfo));

                alertDialog.SetIcon(Resource.Drawable.Icon);

                /*
                alertDialog.SetButton("Close", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        System.exit(1);
                    }
                });
                */
                alertDialog.Show();

            });

        }

        public static void ShowToast(string text)
        {

            _text = text;
            GlobalVar.mainActivity.RunOnUiThread(() =>
            {
                for (int i = 0; i < 10; i++)
                    Toast.MakeText(GlobalVar.mainActivity, ShowMessage._text, ToastLength.Long).Show();

            });
        }

    }
}