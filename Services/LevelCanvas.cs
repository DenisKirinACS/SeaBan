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
using Android.Graphics;

namespace SeaBan
{
    class LevelCanvas : View
    {
        public int hintNumber = 0;
        protected Paint paint = new Paint();
        public String dataText;
        public static Bitmap b = null;

        public int a = 0;
        public int viewMode = 0;



    public LevelCanvas(Context context):base(context)
        {
            Typeface tf = Typeface.CreateFromAsset(RenderManager.context.Assets, "fonts/comic.ttf");
            paint.SetTypeface(tf);
            SetMinimumWidth(RenderManager.metrics.WidthPixels);
            SetMinimumHeight(RenderManager.metrics.HeightPixels);

            Focusable = false;

        }

        
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            SetMeasuredDimension( SuggestedMinimumWidth, SuggestedMinimumHeight);
        }

        private void drawText(Canvas canvas, String text, int x, int y)
        {

            paint.TextSize = GlobalVar.GetScrX(60);
            Color color = Color.Rgb(0x31, 0x31, 0x31);
            paint.Color = color;
            paint.Alpha = 100;;
            canvas.DrawText(text, x + 5, y + 5, paint);
            color = Color.Rgb(0xD1, 0xD1, 0x31);
            paint.Color = color;
            paint.Alpha  = 255;
            canvas.DrawText(text, x, y, paint);

        }


       protected override void OnDraw(Canvas canvas)
        {

            base.OnDraw(canvas);
            if (LevelCanvas.b != null)
            {
                try
                {
                //    canvas.DrawBitmap(b, 0, 0, paint); 
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.Message);
                }
            }

            drawText(canvas, "Edge: " + Circle.steps , (int)GlobalVar.GetScrX(200), (int)GlobalVar.GetScrY(400));
            GlobalVar.drawMultilineText(dataText, 40, 40, canvas, 20, 300);

            Paint paint = new Paint();
            paint.Color = Android.Graphics.Color.White;
            paint.AntiAlias = true;
            paint.TextSize = 126;
            paint.TextAlign = Paint.Align.Center;
            canvas.DrawText("+", GlobalVar.calculateWidthFromFontSize("+", 126, paint) , 100, paint);

            canvas.DrawText("-", RenderManager.metrics.WidthPixels- GlobalVar.calculateWidthFromFontSize("-", 126, paint), 100, paint);

        }

    }
}