#include <jni.h>
#include <GL3/gl3w.h>
#include <string.h>

void getGLVersion(int *major, int *minor)
{
    const char *verstr = (const char *) glGetString(GL_VERSION);
    if ((verstr == NULL) || (sscanf(verstr,"%d.%d", major, minor) != 2))
    {
        *major = *minor = 0;
        fprintf(stderr, "Invalid GL_VERSION format!!!\n");
    }
}

void getGLSLVersion(int *major, int *minor)
{
    int gl_major, gl_minor;
    getGLVersion(&gl_major, &gl_minor);

    *major = *minor = 0;
    if (gl_major == 1)
    {
        /* GL v1.x can only provide GLSL v1.00 as an extension */
        const char *extstr = (const char *) glGetString(GL_EXTENSIONS);
        if ((extstr != NULL) &&
            (strstr(extstr, "GL_ARB_shading_language_100") != NULL))
        {
            *major = 1;
            *minor = 0;
        }
    }
    else if (gl_major >= 2)
    {
        /* GL v2.0 and greater must parse the version string */
        const char *verstr =
            (const char *) glGetString(GL_SHADING_LANGUAGE_VERSION);

        if((verstr == NULL) ||
            (sscanf(verstr, "%d.%d", major, minor) != 2))
        {
            *major = *minor = 0;
            fprintf(stderr,
                "Invalid GL_SHADING_LANGUAGE_VERSION format!!!\n");
        }
    }
}

extern "C"
{
	JNIEXPORT jboolean JNICALL Java_firststep_gl3w_GL3W_init(JNIEnv * env, jclass clz)
	{
		return !gl3wInit();
	}

	JNIEXPORT jint JNICALL Java_firststep_gl3w_GL3W_getGLVersionMajor(JNIEnv * env, jclass clz)
	{
		int major, minor;
		getGLVersion(&major, &minor);
		return major;
	}

	JNIEXPORT jint JNICALL Java_firststep_gl3w_GL3W_getGLVersionMinor(JNIEnv * env, jclass clz)
	{
		int major, minor;
		getGLVersion(&major, &minor);
		return minor;
	}

	JNIEXPORT jint JNICALL Java_firststep_gl3w_GL3W_getGLSLVersionMajor(JNIEnv * env, jclass clz)
	{
		int major, minor;
		getGLSLVersion(&major, &minor);
		return major;
	}

	JNIEXPORT jint JNICALL Java_firststep_gl3w_GL3W_getGLSLVersionMinor(JNIEnv * env, jclass clz)
	{
		int major, minor;
		getGLSLVersion(&major, &minor);
		return minor;
	}
}
