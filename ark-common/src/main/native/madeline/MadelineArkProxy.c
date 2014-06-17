#include <jni.h>
#include <string>
#include "MadelineArkProxy.h"
#include "HelloImpl.h"
#include "ArkMadeline.h"

JNIEXPORT void JNICALL Java_au_org_theark_core_jni_MadelineArkProxy_sayHello (JNIEnv *env, jobject obj) {
    sayHello();
    return;
}

JNIEXPORT void JNICALL Java_au_org_theark_core_jni_MadelineArkProxy_generatePedigree (JNIEnv *env, jobject obj, jstring jstr, jstring jstr2) {

	const char *filename = env->GetStringUTFChars(jstr, NULL);

	const char *outputFilename = env->GetStringUTFChars(jstr2, NULL);

	generatePedigree (filename, outputFilename);

	env->ReleaseStringUTFChars(jstr, filename);

	env->ReleaseStringUTFChars(jstr2, outputFilename);
	return;
}
