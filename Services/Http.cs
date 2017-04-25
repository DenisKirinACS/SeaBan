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
using System.Net;
using System.IO;
using Newtonsoft.Json;

namespace SeaBan
{
    public class DataItem
    {
        public float _eyeX = 0.0f;
        public float _eyeY = 0.0f;
        public float _eyeZ = 0.0f;

        public float _lookX = 0.0f;
        public float _lookY = 0.0f;
        public float _lookZ = 0.0f;

    }

    class Http
    {
        
        public static DataItem command(int commandCode)
        {
            string url = "http://192.168.1.77:62008/api/seaban/command?commandCode=2";

            try
            { 
                HttpWebRequest httpWebRequest = (HttpWebRequest)WebRequest.Create(url);

                httpWebRequest.Method = "GET";
                httpWebRequest.ContentType = "application/json";
            
                HttpWebResponse response = (HttpWebResponse)httpWebRequest.GetResponse();
                Stream resStream = response.GetResponseStream();

                var sr = new StreamReader(response.GetResponseStream());
                var responseText = sr.ReadToEnd();

                DataItem result = Newtonsoft.Json.JsonConvert.DeserializeObject<DataItem>(responseText) as DataItem;

                response.Close();
                resStream.Close();

                return result;
            }
            catch (Exception e)
            {
                return null;
            }

        }


    }
}