#include <jni.h>

extern "C"
{
	#define PLATFORM_ENUM "firststep/os/GlobalConsts$Platform"
	
	JNIEXPORT jobject JNICALL Java_firststep_os_GlobalConsts_getPlatform(JNIEnv * env, jclass clz)
	{
		jclass platformEnum = env->FindClass(PLATFORM_ENUM);
#if defined(__MINGW32__) || defined(__MINGW64__)
		jfieldID windowsPlatformEnumValue = env->GetStaticFieldID(platformEnum , "WINDOWS", "L" PLATFORM_ENUM ";");
		return env->GetStaticObjectField(platformEnum, windowsPlatformEnumValue);
#elif defined(__APPLE__)
		jfieldID osxPlatformEnumValue = env->GetStaticFieldID(platformEnum , "OSX", "L" PLATFORM_ENUM ";");
		return env->GetStaticObjectField(platformEnum, osxPlatformEnumValue);
#else
		jfieldID otherPlatformEnumValue = env->GetStaticFieldID(platformEnum , "OTHER", "L" PLATFORM_ENUM ";");
		return env->GetStaticObjectField(platformEnum, otherPlatformEnumValue);
#endif
	}
}
