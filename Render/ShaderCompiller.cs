using System;
using System.IO;
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
//using Java.IO;

namespace SeaBan
{
    class ShaderCompiller
    {
        private String shaderSource;
        private String fragmentSource;
        private Context context;
        public String shaderName;
        private String fileName;
        public int program;
        public String result = "";

        public int mTextureCoordinateHandle;
        public int mTextureUniformHandle;

        public int mTextureCoordinateHandleF;
        public int mTextureUniformHandleF;
        public int mTextureUniformHandleS;

        public int mTextPosHandle;

        public float[] mModelMatrix = new float[16];

        public float[] mMVPMatrix = new float[16];

        public int mMVPMatrixHandle;
        public int mMVMatrixHandle;

        public int attrib_vertex;
        public int unif_color;
        public int normal;
        public int lightPos;
        public int fragColorHandle;



        public ShaderCompiller(Context context, String shaderName, String fileName)
        {
            this.context = context;
            this.shaderName = shaderName;
            this.fileName = fileName;

            shaderSource = loadSource(fileName + "_shader");
            fragmentSource = loadSource(fileName + "_fragment");
            compileShader();

            mMVMatrixHandle  = GLES20.GlGetUniformLocation(program, "u_MVMatrix");            
            mMVPMatrixHandle = GLES20.GlGetUniformLocation(program, "u_MVPMatrix");
            
            attrib_vertex = GLES20.GlGetAttribLocation(program, "coord");
            normal = GLES20.GlGetAttribLocation(program, "a_Normal");




            mTextureCoordinateHandle = GLES20.GlGetAttribLocation(program, "a_TexCoordinate");
            mTextPosHandle = GLES20.GlGetUniformLocation(program, "u_TextPos");
            mTextureUniformHandle = GLES20.GlGetUniformLocation(program, "u_Texture");
            mTextureUniformHandleF = GLES20.GlGetUniformLocation(program, "u_TextureF");
            mTextureUniformHandleS = GLES20.GlGetUniformLocation(program, "u_TextureS");

            unif_color = GLES20.GlGetUniformLocation(program, "u_Color");
            lightPos = GLES20.GlGetUniformLocation(program, "u_LightPos");




        }

        public void compileShader()
        {

            int vShader = GLES20.GlCreateShader(GLES20.GlVertexShader);
            GLES20.GlShaderSource(vShader, shaderSource);
            GLES20.GlCompileShader(vShader);

            int[] compileStatus = new int[1];
            GLES20.GlGetShaderiv(vShader, GLES20.GlCompileStatus, compileStatus, 0);

            if (compileStatus[0] != GLES20.GlTrue)
            {
                result += "vShader: \n";
                result += GLES20.GlGetShaderInfoLog(vShader) + "\n";
                ShowMessage.ShowCrash(result);
            }

            // ----------------------------------------------------------------------
            int fShader = GLES20.GlCreateShader(GLES20.GlFragmentShader);
            GLES20.GlShaderSource(fShader, fragmentSource);
            GLES20.GlCompileShader(fShader);

            GLES20.GlGetShaderiv(fShader, GLES20.GlCompileStatus, compileStatus, 0);
            if (compileStatus[0] != GLES20.GlTrue)
            {
                result += "fShader: \n";
                result += GLES20.GlGetShaderInfoLog(fShader) + "\n";
                ShowMessage.ShowCrash(result);

            }
            // ----------------------------------------------------------------------

            program = GLES20.GlCreateProgram();




            GLES20.GlAttachShader(program, vShader);
            GLES20.GlAttachShader(program, fShader);

            GLES20.GlLinkProgram(program);

            int[] linkStatus = new int[1];
            GLES20.GlGetProgramiv(program, GLES20.GlLinkStatus, linkStatus, 0);
            if (linkStatus[0] != GLES20.GlTrue)
            {
                result += "Could not link program: \n";
                result += GLES20.GlGetProgramInfoLog(program) + "\n";
                ShowMessage.ShowCrash(result);
            }
        }

        // ---------------------------------------------------------------------------------------------------
        public String loadSource(String fileName)
        {
            int resourceId = context.Resources.GetIdentifier(fileName, "raw", context.PackageName);

            Stream fileIn = context.Resources.OpenRawResource(resourceId);

            StreamReader reader = new StreamReader(fileIn);
            //BinaryReader buffer = new BinaryReader(new InputStreamReader(fileIn));
            //BinaryReader buffer = new BinaryReader(fileIn);

            String line;
            String source = "";
            try
            {
                while ((line = reader.ReadLine()) != null)
                {
                    source += line + "\n";
                }
            }
            catch (IOException e)
            {
                ShowMessage.ShowCrash(e.Message);
            }
            return source;
        }


    }
}