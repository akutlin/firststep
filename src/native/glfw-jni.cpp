#include <jni.h>

#define GLFW_INCLUDE_GLCOREARB
#include <GLFW/glfw3.h>

static jmethodID errorId = 0;
static jmethodID monitorId = 0;
static jmethodID windowPosId = 0;
static jmethodID windowSizeId = 0;
static jmethodID windowCloseId = 0;
static jmethodID windowRefreshId = 0;
static jmethodID windowFocusId = 0;
static jmethodID windowIconifyId = 0;
static jmethodID keyId = 0;
static jmethodID characterId = 0;
static jmethodID mouseButtonId = 0;
static jmethodID cursorPosId = 0;
static jmethodID cursorEnterId = 0;
static jmethodID scrollId = 0;
static jobject callback = 0;
static JavaVM* staticVM = 0;

#ifndef _WIN32
	#include <pthread.h>
	static pthread_key_t envTLS = 0;

	void createTLS() {
		pthread_key_create(&envTLS, NULL);
	}

	JNIEnv* getEnv () {
		JNIEnv* env = (JNIEnv*)pthread_getspecific(envTLS);
		if (!env) {
			if (staticVM->GetEnv((void**)&env, JNI_VERSION_1_2) != JNI_OK) {
				printf("Unable to get Env."); fflush(stdout);
				return 0;
			}
			pthread_setspecific(envTLS, env);
		}
		return env;
	}

	void destroyEnv() {
		if (envTLS) {
			pthread_key_delete(envTLS);
			envTLS = 0;
		}
	}
#else
	static __thread JNIEnv* envTLS = 0;

	void createTLS() {
	}

	JNIEnv* getEnv () {
		if (!envTLS) {
			if (staticVM->GetEnv((void**)&envTLS, JNI_VERSION_1_2) != JNI_OK) {
				printf("Unable to get Env."); fflush(stdout);
				return 0;
			}
		}
		return envTLS;
	}

	void destroyEnv() {
		envTLS = 0;
	}
#endif

extern "C"
{
	void error(int errorCode, const char* description) {
		if(callback) {
			JNIEnv* env = getEnv();
			env->CallVoidMethod(callback, errorId, (jint)errorCode, env->NewStringUTF(description));
		}
	}

	void windowPos(GLFWwindow* window, int x, int y) {
		if(callback) {
			getEnv()->CallVoidMethod(callback, windowPosId, (jlong)window, (jint)x, (jint)y);
		}
	}

	void windowSize(GLFWwindow* window, int width, int height) {
		if(callback) {
			getEnv()->CallVoidMethod(callback, windowSizeId, (jlong)window, (jint)width, (jint)height);
		}
	}

	void windowClose(GLFWwindow* window) {
		if(callback) {
			getEnv()->CallVoidMethod(callback, windowCloseId, (jlong)window);
		}
	}

	void windowRefresh(GLFWwindow* window) {
		if(callback) {
			getEnv()->CallVoidMethod(callback, windowRefreshId, (jlong)window);
		}
	}

	void windowFocus(GLFWwindow* window, int focused) {
		if(callback) {
			getEnv()->CallVoidMethod(callback, windowFocusId, (jlong)window, (jboolean)(GL_TRUE==focused));
		}
	}

	void windowIconify(GLFWwindow* window, int iconified) {
		if(callback) {
			getEnv()->CallVoidMethod(callback, windowIconifyId, (jlong)window, (jboolean)(GL_TRUE==iconified));
		}
	}

	void mouseButton(GLFWwindow* window, int button, int action, int mods) {
		if(callback) {
			getEnv()->CallVoidMethod(callback, mouseButtonId, (jlong)window, (jint)button, (jint)action, (jint)mods);
		}
	}

	void cursorPos(GLFWwindow* window, double x, double y) {
		if(callback) {
			getEnv()->CallVoidMethod(callback, cursorPosId, (jlong)window, (jdouble)x, (jdouble)y);
		}
	}

	void cursorEnter(GLFWwindow* window, int entered) {
		if(callback) {
			getEnv()->CallVoidMethod(callback, cursorEnterId, (jlong)window, (jboolean)(GL_TRUE==entered));
		}
	}

	void scroll(GLFWwindow* window, double xpos, double ypos) {
		if(callback) {
			getEnv()->CallVoidMethod(callback, scrollId, (jlong)window, (jdouble)xpos, (jdouble)ypos);
		}
	}

	void key(GLFWwindow* window, int key, int scancode, int action, int mods) {
		if(callback) {
			getEnv()->CallVoidMethod(callback, keyId, (jlong)window, (jint)key, (jint)scancode, (jint)action, (jint)mods);
		}
	}

	void character(GLFWwindow* window, unsigned int character) {
		if(callback) {
			getEnv()->CallVoidMethod(callback, characterId, (jlong)window, (jchar)character);
		}
	}

	void monitor(GLFWmonitor* monitor, int event) {
		if(callback) {
			getEnv()->CallVoidMethod(callback, monitorId, (jlong)monitor, (jboolean)(GLFW_CONNECTED==event));
		}
	}

	JNIEXPORT jboolean JNICALL Java_firststep_glfw_GLFW_glfwInitJni(JNIEnv* env, jclass clazz) {

		env->GetJavaVM(&staticVM);
		createTLS();

		jclass exception = env->FindClass("firststep/glfw/GLFWException");

		jclass callbackClass = env->FindClass("firststep/glfw/GLFWCallback");
		if(!callbackClass) {
			env->ThrowNew(exception, "Couldn't find class GLFWCallback");
			return false;
		}

		errorId = env->GetMethodID(callbackClass, "error", "(ILjava/lang/String;)V");
		if(!errorId) {
			env->ThrowNew(exception, "Couldn't find error() method");
			return false;
		}

		monitorId = env->GetMethodID(callbackClass, "monitor", "(JZ)V");
		if(!monitorId) {
			env->ThrowNew(exception, "Couldn't find monitor() method");
			return false;
		}

		windowPosId = env->GetMethodID(callbackClass, "windowPos", "(JII)V");
		if(!windowPosId) {
			env->ThrowNew(exception, "Couldn't find windowPosId() method");
			return false;
		}

		windowSizeId = env->GetMethodID(callbackClass, "windowSize", "(JII)V");
		if(!windowSizeId) {
			env->ThrowNew(exception, "Couldn't find windowSizeId() method");
			return false;
		}

		windowCloseId = env->GetMethodID(callbackClass, "windowClose", "(J)V");
		if(!windowCloseId) {
			env->ThrowNew(exception, "Couldn't find windowCloseId() method");
			return false;
		}

		windowRefreshId = env->GetMethodID(callbackClass, "windowRefresh", "(J)V");
		if(!windowRefreshId) {
			env->ThrowNew(exception, "Couldn't find windowRefresh() method");
			return false;
		}

		windowFocusId = env->GetMethodID(callbackClass, "windowFocus", "(JZ)V");
		if(!windowFocusId) {
			env->ThrowNew(exception, "Couldn't find windowFocus() method");
			return false;
		}

		windowIconifyId = env->GetMethodID(callbackClass, "windowIconify", "(JZ)V");
		if(!windowIconifyId) {
			env->ThrowNew(exception, "Couldn't find windowIconify() method");
			return false;
		}

		keyId = env->GetMethodID(callbackClass, "key", "(JIIII)V");
		if(!keyId) {
			env->ThrowNew(exception, "Couldn't find key() method");
			return false;
		}

		characterId = env->GetMethodID(callbackClass, "character", "(JC)V");
		if(!characterId) {
			env->ThrowNew(exception, "Couldn't find character() method");
			return false;
		}

		mouseButtonId = env->GetMethodID(callbackClass, "mouseButton", "(JIII)V");
		if(!mouseButtonId) {
			env->ThrowNew(exception, "Couldn't find mouseButton() method");
			return false;
		}

		cursorPosId = env->GetMethodID(callbackClass, "cursorPos", "(JDD)V");
		if(!cursorPosId) {
			env->ThrowNew(exception, "Couldn't find cursorPos() method");
			return false;
		}

		cursorEnterId = env->GetMethodID(callbackClass, "cursorEnter", "(JZ)V");
		if(!cursorEnterId) {
			env->ThrowNew(exception, "Couldn't find cursorEnter() method");
			return false;
		}

		scrollId = env->GetMethodID(callbackClass, "scroll", "(JDD)V");
		if(!scrollId) {
			env->ThrowNew(exception, "Couldn't find scroll() method");
			return false;
		}

		jboolean result = glfwInit() == GL_TRUE;
		if(result) {
			glfwSetErrorCallback(error);
			glfwSetMonitorCallback(monitor);

		}
		return result;
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwTerminate(JNIEnv* env, jclass clazz) {

		if (callback) {
			env->DeleteGlobalRef(callback);
			callback = 0;
		}
		destroyEnv();
		glfwTerminate();
	}

	JNIEXPORT jstring JNICALL Java_firststep_glfw_GLFW_glfwGetVersionString(JNIEnv* env, jclass clazz) {
		return env->NewStringUTF(glfwGetVersionString());
	}

	static inline jint wrapped_Java_firststep_glfw_GLFW_glfwGetMonitorsJni(JNIEnv* env, jclass clazz, jlongArray obj_monitors, long long* monitors) {

		int count = 0;
		GLFWmonitor** mons = glfwGetMonitors(&count);
		if(!mons) return 0;

		for(int i = 0; i < count; i++) {
			monitors[i] = (jlong)mons[i];
		}
		return count;

	}

	JNIEXPORT jint JNICALL Java_firststep_glfw_GLFW_glfwGetMonitorsJni(JNIEnv* env, jclass clazz, jlongArray obj_monitors) {
		long long* monitors = (long long*)env->GetPrimitiveArrayCritical(obj_monitors, 0);

		jint JNI_returnValue = wrapped_Java_firststep_glfw_GLFW_glfwGetMonitorsJni(env, clazz, obj_monitors, monitors);

		env->ReleasePrimitiveArrayCritical(obj_monitors, monitors, 0);

		return JNI_returnValue;
	}

	JNIEXPORT jlong JNICALL Java_firststep_glfw_GLFW_glfwGetPrimaryMonitor(JNIEnv* env, jclass clazz) {
		return (jlong)glfwGetPrimaryMonitor();
	}

	JNIEXPORT jint JNICALL Java_firststep_glfw_GLFW_glfwGetMonitorX(JNIEnv* env, jclass clazz, jlong monitor) {

		int x = 0;
		int y = 0;
		glfwGetMonitorPos((GLFWmonitor*)monitor, &x, &y);
		return x;

	}

	JNIEXPORT jint JNICALL Java_firststep_glfw_GLFW_glfwGetMonitorY(JNIEnv* env, jclass clazz, jlong monitor) {
		int x = 0;
		int y = 0;
		glfwGetMonitorPos((GLFWmonitor*)monitor, &x, &y);
		return y;
	}

	JNIEXPORT jint JNICALL Java_firststep_glfw_GLFW_glfwGetMonitorPhysicalWidth(JNIEnv* env, jclass clazz, jlong monitor) {

		int width = 0;
		int height = 0;
		glfwGetMonitorPhysicalSize((GLFWmonitor*)monitor, &width, &height);
		return width;
	}

	JNIEXPORT jint JNICALL Java_firststep_glfw_GLFW_glfwGetMonitorPhysicalHeight(JNIEnv* env, jclass clazz, jlong monitor) {
		int width = 0;
		int height = 0;
		glfwGetMonitorPhysicalSize((GLFWmonitor*)monitor, &width, &height);
		return height;
	}

	JNIEXPORT jstring JNICALL Java_firststep_glfw_GLFW_glfwGetMonitorName(JNIEnv* env, jclass clazz, jlong monitor) {
		return env->NewStringUTF(glfwGetMonitorName((GLFWmonitor*)monitor));
	}

	static inline jint wrapped_Java_firststep_glfw_GLFW_glfwGetVideoModesJni(JNIEnv* env, jclass clazz, jlong monitor, jintArray obj_modes, int* modes) {

		int numModes = 0;
		const GLFWvidmode* vidModes = glfwGetVideoModes((GLFWmonitor*)monitor, &numModes);
		for(int i = 0, j = 0; i < numModes; i++) {
			modes[j++] = vidModes[i].width;
			modes[j++] = vidModes[i].height;
			modes[j++] = vidModes[i].redBits;
			modes[j++] = vidModes[i].greenBits;
			modes[j++] = vidModes[i].blueBits;
		}
		return numModes;

	}

	JNIEXPORT jint JNICALL Java_firststep_glfw_GLFW_glfwGetVideoModesJni(JNIEnv* env, jclass clazz, jlong monitor, jintArray obj_modes) {
		int* modes = (int*)env->GetPrimitiveArrayCritical(obj_modes, 0);

		jint JNI_returnValue = wrapped_Java_firststep_glfw_GLFW_glfwGetVideoModesJni(env, clazz, monitor, obj_modes, modes);

		env->ReleasePrimitiveArrayCritical(obj_modes, modes, 0);

		return JNI_returnValue;
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwGetVideoModeJni(JNIEnv* env, jclass clazz, jlong monitor, jintArray obj_buffer) {
		int* buffer = (int*)env->GetPrimitiveArrayCritical(obj_buffer, 0);

		const GLFWvidmode* mode = glfwGetVideoMode((GLFWmonitor*)monitor);
		buffer[0] = mode->width;
		buffer[1] = mode->height;
		buffer[2] = mode->redBits;
		buffer[3] = mode->greenBits;
		buffer[4] = mode->blueBits;

		env->ReleasePrimitiveArrayCritical(obj_buffer, buffer, 0);

	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwSetGamma(JNIEnv* env, jclass clazz, jlong monitor, jfloat gamma) {
		glfwSetGamma((GLFWmonitor*)monitor, gamma);
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwGetGammaRamp(JNIEnv* env, jclass clazz) {
		jclass exception = env->FindClass("firststep/glfw/GLFWException");
		env->ThrowNew(exception, "Unimplemented");
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwSetGammaRamp(JNIEnv* env, jclass clazz) {
		jclass exception = env->FindClass("firststep/glfw/GLFWException");
		env->ThrowNew(exception, "Unimplemented");
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwDefaultWindowHints(JNIEnv* env, jclass clazz) {
		glfwDefaultWindowHints();
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwWindowHint(JNIEnv* env, jclass clazz, jint target, jint hint) {
		glfwWindowHint(target, hint);
	}

	static inline jlong wrapped_Java_firststep_glfw_GLFW_glfwCreateWindowJni(JNIEnv* env, jclass clazz, jint width, jint height, jstring obj_title, jlong monitor, jlong share, char* title) {

		GLFWwindow* window = glfwCreateWindow(width, height, title, (GLFWmonitor*)monitor, (GLFWwindow*)share);
		if (window) {
			glfwSetWindowPosCallback(window, windowPos);
			glfwSetWindowSizeCallback(window, windowSize);
			glfwSetWindowCloseCallback(window, windowClose);
			glfwSetWindowRefreshCallback(window, windowRefresh);
			glfwSetWindowFocusCallback(window, windowFocus);
			glfwSetWindowIconifyCallback(window, windowIconify);
			glfwSetKeyCallback(window, key);
			glfwSetCharCallback(window, character);
			glfwSetMouseButtonCallback(window, mouseButton);
			glfwSetCursorPosCallback(window, cursorPos);
			glfwSetCursorEnterCallback(window, cursorEnter);
			glfwSetScrollCallback(window, scroll);
		}
		return (jlong)window;

	}

	JNIEXPORT jlong JNICALL Java_firststep_glfw_GLFW_glfwCreateWindowJni(JNIEnv* env, jclass clazz, jint width, jint height, jstring obj_title, jlong monitor, jlong share) {
		char* title = (char*)env->GetStringUTFChars(obj_title, 0);

		jlong JNI_returnValue = wrapped_Java_firststep_glfw_GLFW_glfwCreateWindowJni(env, clazz, width, height, obj_title, monitor, share, title);

		env->ReleaseStringUTFChars(obj_title, title);

		return JNI_returnValue;
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwDestroyWindow(JNIEnv* env, jclass clazz, jlong window) {
		glfwDestroyWindow((GLFWwindow*)window);
	}

	JNIEXPORT jboolean JNICALL Java_firststep_glfw_GLFW_glfwWindowShouldClose(JNIEnv* env, jclass clazz, jlong window) {
		return GL_TRUE == glfwWindowShouldClose((GLFWwindow*)window);
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwSetWindowShouldClose(JNIEnv* env, jclass clazz, jlong window, jint value) {
		glfwSetWindowShouldClose((GLFWwindow*)window, value);
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwSetWindowTitle(JNIEnv* env, jclass clazz, jlong window, jstring obj_title) {
		char* title = (char*)env->GetStringUTFChars(obj_title, 0);

		glfwSetWindowTitle((GLFWwindow*)window, title);

		env->ReleaseStringUTFChars(obj_title, title);
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwSetWindowPos(JNIEnv* env, jclass clazz, jlong window, jint x, jint y) {
		glfwSetWindowPos((GLFWwindow*)window, x, y);
	}

	JNIEXPORT jint JNICALL Java_firststep_glfw_GLFW_glfwGetWindowX(JNIEnv* env, jclass clazz, jlong window) {

		int x = 0;
		int y = 0;
		glfwGetWindowPos((GLFWwindow*)window, &x, &y);
		return x;

	}

	JNIEXPORT jint JNICALL Java_firststep_glfw_GLFW_glfwGetWindowY(JNIEnv* env, jclass clazz, jlong window) {

		int x = 0;
		int y = 0;
		glfwGetWindowPos((GLFWwindow*)window, &x, &y);
		return y;
	}

	JNIEXPORT jint JNICALL Java_firststep_glfw_GLFW_glfwGetWindowWidth(JNIEnv* env, jclass clazz, jlong window) {

		int width = 0;
		int height = 0;
		glfwGetWindowSize((GLFWwindow*)window, &width, &height);
		return width;

	}

	JNIEXPORT jint JNICALL Java_firststep_glfw_GLFW_glfwGetWindowHeight(JNIEnv* env, jclass clazz, jlong window) {

		int width = 0;
		int height = 0;
		glfwGetWindowSize((GLFWwindow*)window, &width, &height);
		return height;

	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwSetWindowSize(JNIEnv* env, jclass clazz, jlong window, jint width, jint height) {
		glfwSetWindowSize((GLFWwindow*)window, width, height);
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwIconifyWindow(JNIEnv* env, jclass clazz, jlong window) {
		glfwIconifyWindow((GLFWwindow*)window);
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwRestoreWindow(JNIEnv* env, jclass clazz, jlong window) {
		glfwRestoreWindow((GLFWwindow*)window);
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwHideWindow(JNIEnv* env, jclass clazz, jlong window) {
		glfwHideWindow((GLFWwindow*)window);
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwShowWindow(JNIEnv* env, jclass clazz, jlong window) {
		glfwShowWindow((GLFWwindow*)window);
	}

	JNIEXPORT jlong JNICALL Java_firststep_glfw_GLFW_glfwGetWindowMonitor(JNIEnv* env, jclass clazz, jlong window) {
		return (jlong)glfwGetWindowMonitor((GLFWwindow*)window);
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwSetCallbackJni(JNIEnv* env, jclass clazz, jobject javaCallback) {

		if (callback) {
			env->DeleteGlobalRef(callback);
			callback = 0;
		}
		if (javaCallback) callback = env->NewGlobalRef(javaCallback);

	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwPollEventsJni(JNIEnv* env, jclass clazz, jobject javaCallback) {
		glfwPollEvents();
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwWaitEventsJni(JNIEnv* env, jclass clazz, jobject javaCallback) {
		glfwWaitEvents();
	}

	JNIEXPORT jint JNICALL Java_firststep_glfw_GLFW_glfwGetInputMode(JNIEnv* env, jclass clazz, jlong window, jint mode) {
		return glfwGetInputMode((GLFWwindow*)window, mode);
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwSetInputMode(JNIEnv* env, jclass clazz, jlong window, jint mode, jint value) {
		glfwSetInputMode((GLFWwindow*)window, mode, value);
	}

	JNIEXPORT jboolean JNICALL Java_firststep_glfw_GLFW_glfwGetKey(JNIEnv* env, jclass clazz, jlong window, jint key) {
		return glfwGetKey((GLFWwindow*)window, key) == GLFW_PRESS;
	}

	JNIEXPORT jboolean JNICALL Java_firststep_glfw_GLFW_glfwGetMouseButton(JNIEnv* env, jclass clazz, jlong window, jint button) {
		return glfwGetMouseButton((GLFWwindow*)window, button) == GLFW_PRESS;
	}

	JNIEXPORT jdouble JNICALL Java_firststep_glfw_GLFW_glfwGetCursorPosX(JNIEnv* env, jclass clazz, jlong window) {

		double x = 0;
		double y = 0;
		glfwGetCursorPos((GLFWwindow*)window, &x, &y);
		return x;

	}

	JNIEXPORT jdouble JNICALL Java_firststep_glfw_GLFW_glfwGetCursorPosY(JNIEnv* env, jclass clazz, jlong window) {

		double x = 0;
		double y = 0;
		glfwGetCursorPos((GLFWwindow*)window, &x, &y);
		return y;

	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwSetCursorPos(JNIEnv* env, jclass clazz, jlong window, jint x, jint y) {
		glfwSetCursorPos((GLFWwindow*)window, x, y);
	}

/*static inline jint wrapped_Java_firststep_glfw_GLFW_glfwGetJoystickAxes(JNIEnv* env, jclass clazz, jint joy, jfloatArray obj_axes, float* axes) {
		return glfwGetJoystickAxes(joy, axes, env->GetArrayLength(obj_axes));
}

JNIEXPORT jint JNICALL Java_firststep_glfw_GLFW_glfwGetJoystickAxes(JNIEnv* env, jclass clazz, jint joy, jfloatArray obj_axes) {
	float* axes = (float*)env->GetPrimitiveArrayCritical(obj_axes, 0);

	jint JNI_returnValue = wrapped_Java_firststep_glfw_GLFW_glfwGetJoystickAxes(env, clazz, joy, obj_axes, axes);

	env->ReleasePrimitiveArrayCritical(obj_axes, axes, 0);

	return JNI_returnValue;
}

static inline jint wrapped_Java_firststep_glfw_GLFW_glfwGetJoystickButtons(JNIEnv* env, jclass clazz, jint joy, jbyteArray obj_buttons, char* buttons) {
		return glfwGetJoystickButtons(joy, (unsigned char*)buttons, env->GetArrayLength(obj_buttons));
}

JNIEXPORT jint JNICALL Java_firststep_glfw_GLFW_glfwGetJoystickButtons(JNIEnv* env, jclass clazz, jint joy, jbyteArray obj_buttons) {
	char* buttons = (char*)env->GetPrimitiveArrayCritical(obj_buttons, 0);

	jint JNI_returnValue = wrapped_Java_firststep_glfw_GLFW_glfwGetJoystickButtons(env, clazz, joy, obj_buttons, buttons);

	env->ReleasePrimitiveArrayCritical(obj_buttons, buttons, 0);

	return JNI_returnValue;
}*/

	JNIEXPORT jstring JNICALL Java_firststep_glfw_GLFW_glfwGetJoystickName(JNIEnv* env, jclass clazz, jint joy) {
		return env->NewStringUTF(glfwGetJoystickName(joy));
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwSetClipboardString(JNIEnv* env, jclass clazz, jlong window, jstring obj_string) {
		char* string = (char*)env->GetStringUTFChars(obj_string, 0);
		glfwSetClipboardString((GLFWwindow*)window, string);
		env->ReleaseStringUTFChars(obj_string, string);
	}

	JNIEXPORT jstring JNICALL Java_firststep_glfw_GLFW_glfwGetClipboardString(JNIEnv* env, jclass clazz, jlong window) {
		return env->NewStringUTF(glfwGetClipboardString((GLFWwindow*)window));
	}

	JNIEXPORT jdouble JNICALL Java_firststep_glfw_GLFW_glfwGetTime(JNIEnv* env, jclass clazz) {
		return glfwGetTime();
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwSetTime(JNIEnv* env, jclass clazz, jdouble time) {
		glfwSetTime(time);
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwMakeContextCurrentJni(JNIEnv* env, jclass clazz, jlong window) {
		glfwMakeContextCurrent((GLFWwindow*)window);
	}

	JNIEXPORT jlong JNICALL Java_firststep_glfw_GLFW_glfwGetCurrentContext(JNIEnv* env, jclass clazz) {
		return (jlong)glfwGetCurrentContext();
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwSwapBuffers(JNIEnv* env, jclass clazz, jlong window) {
		glfwSwapBuffers((GLFWwindow*)window);
	}

	JNIEXPORT void JNICALL Java_firststep_glfw_GLFW_glfwSwapInterval(JNIEnv* env, jclass clazz, jint interval) {
		glfwSwapInterval(interval);
	}

	static inline jboolean wrapped_Java_firststep_glfw_GLFW_glfwExtensionSupported(JNIEnv* env, jclass clazz, jstring obj_extension, char* extension) {
		return glfwExtensionSupported(extension) == GL_TRUE;
	}

	JNIEXPORT jboolean JNICALL Java_firststep_glfw_GLFW_glfwExtensionSupported(JNIEnv* env, jclass clazz, jstring obj_extension) {
		char* extension = (char*)env->GetStringUTFChars(obj_extension, 0);
		jboolean JNI_returnValue = wrapped_Java_firststep_glfw_GLFW_glfwExtensionSupported(env, clazz, obj_extension, extension);
		env->ReleaseStringUTFChars(obj_extension, extension);
		return JNI_returnValue;
	}

}
