using System;
using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using Android.Content.PM;
using Java.Lang;
using static Android.OS.PowerManager;
using Android.Opengl;
using Android.Util;
using static Android.App.ActionBar;

namespace SeaBan
{
    [Activity(Label = "SeaBan", MainLauncher = true, Icon = "@drawable/icon")]
    public class MainActivity : Activity, View.IOnTouchListener
    {
        private WakeLock wakeLock;
        private Renderer renderer;

        private float touchX, touchY;
        private long touchTime;
        private bool touchLock = false;

        protected override void OnCreate(Bundle bundle)
        {
            // global exception handler        
            AndroidEnvironment.UnhandledExceptionRaiser += UnhandledExceptionHandler;

            GlobalVar.mainActivity = this;

            this.RequestedOrientation = ScreenOrientation.Landscape;
            this.RequestWindowFeature(WindowFeatures.NoTitle);
            this.Window.SetFlags(WindowManagerFlags.Fullscreen, WindowManagerFlags.Fullscreen);
            Window.DecorView.SystemUiVisibility = StatusBarVisibility.Hidden;

            base.OnCreate(bundle);

            PowerManager powerManager = (PowerManager)this.GetSystemService(Context.PowerService);
            wakeLock = powerManager.NewWakeLock(WakeLockFlags.Full, "SeabanUnlockScreen");

            RenderManager.mGLSurfaceView = new GLSurfaceView(this);

            ActivityManager activityManager = (ActivityManager)GetSystemService(Context.ActivityService);

            System.Boolean supportsEs2 = activityManager.DeviceConfigurationInfo.ReqGlEsVersion >= 0x20000;

            if (supportsEs2)
            {
                RenderManager.mGLSurfaceView.SetEGLContextClientVersion(2);

                renderer = new Renderer();
                renderer.context = this;

                SetContentView(Resource.Layout.Main);

                RenderManager.relativeLayout = (RelativeLayout)this.FindViewById(Resource.Id.scene1Holder);
                RenderManager.mGLSurfaceView.SetRenderer(renderer);
                //RenderManager.mGLSurfaceView.RenderMode = GLSurfaceView.RendermodeContinuously;
                
                RenderManager.context = renderer.context;
                DisplayMetrics metrics = new DisplayMetrics();

                WindowManager.DefaultDisplay.GetRealMetrics(metrics);
                RenderManager.metrics = metrics;


                RenderManager.canvasView = new LevelCanvas(this);
                RenderManager.canvasView.Focusable = true;
                RenderManager.canvasView.FocusableInTouchMode = false;

                RenderManager.relativeLayout.SetOnTouchListener(this);

            }
            else
            {
                ShowMessage.ShowCrash("OpenGL ES 2.x not supproted on this device.");
                return;
            }
        }

        void UnhandledExceptionHandler(object sender, RaiseThrowableEventArgs e)
        {
            Console.WriteLine(e.Exception.Message);
        }

        protected override void OnResume()
        {
            wakeLock.Acquire(100);
            base.OnResume();
            RenderManager.Resume();
        }

        protected override void OnPause()
        {
            wakeLock.Release();
            base.OnPause();
            RenderManager.Pause();
        }

        private void Move(MotionEvent e)
        {
            if (RenderManager.render.circle == null) return;
            double deltaX = touchX - e.GetX();
            double deltaY = touchY - e.GetY();
            long deltaTime = SystemClock.UptimeMillis() - touchTime;


            RenderManager.render.circle.rotateSpeed = (double)(10.0f / deltaTime);
            double delta = (System.Math.Sqrt(System.Math.Pow(deltaX, 2) + System.Math.Pow(deltaY, 2))) / deltaTime;

            if (touchX > e.GetX())
            {
                RenderManager.render.circle.left = true;
                RenderManager.render.circle.rotate = (float)delta;
            }
            else
            {
                RenderManager.render.circle.left = false;
                RenderManager.render.circle.rotate = (float)-delta;
            }
        }
        public bool OnTouch(View v, MotionEvent e)
        {
            if (touchLock) return false;
            touchLock = true;
            switch (e.Action)
            {
                case MotionEventActions.Down:
                    touchX = -1;
                    if ((e.GetX() > 0) && (e.GetX() < 100) && (e.GetY() > 0) && (e.GetY() < 100))
                    {
                        if (RenderManager.edgeCount > 39) break;
                        ObjectManager.del(0);
                        if (ObjectManager.getCount() != 0) ObjectManager.del(0);

                        RenderManager.edgeCount++;
                        RenderManager.Resume();
                    }
                    else
                    if ((e.GetX() > RenderManager.metrics.WidthPixels - 100) && (e.GetX() < RenderManager.metrics.WidthPixels) && (e.GetY() > 0) && (e.GetY() < 100))
                    {
                        if (RenderManager.edgeCount < 3) break;
                        ObjectManager.del(0);
                        if (ObjectManager.getCount() != 0) ObjectManager.del(0);
                        RenderManager.edgeCount--;
                        RenderManager.Resume();
                    }
                    else
                    {
                        touchX = e.GetX();
                        touchY = e.GetY();
                        touchTime = SystemClock.UptimeMillis();
                    }
                    break;

                case MotionEventActions.Up:
                    if (touchX != -1) Move(e);
                    break;

                case MotionEventActions.Move:
                    if (touchX != -1) Move(e);
                    break;
            }
            touchLock = false;
            return true;
        }


    }
}

