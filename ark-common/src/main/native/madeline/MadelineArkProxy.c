#include <jni.h>
#include <string>
#include "MadelineArkProxy.h"
#include "HelloImpl.h"
#include "ArkMadeline.h"

JNIEXPORT void JNICALL Java_au_org_theark_core_jni_MadelineArkProxy_sayHello (JNIEnv *env, jobject obj) {
    sayHello();
    return;
}

JNIEXPORT void JNICALL Java_au_org_theark_core_jni_MadelineArkProxy_generatePedigree (JNIEnv *env, jobject obj, jstring jstr1, jstring jstr2, jstring jstr3) {

	const char *filename = env->GetStringUTFChars(jstr1, NULL);

	const char *outputFilename = env->GetStringUTFChars(jstr2, NULL);

	const char *columnList = env->GetStringUTFChars(jstr3, NULL);

	generatePedigree (filename, outputFilename, columnList);

	env->ReleaseStringUTFChars(jstr1, filename);

	env->ReleaseStringUTFChars(jstr2, outputFilename);

	env->ReleaseStringUTFChars(jstr3, columnList);
	return;
}
