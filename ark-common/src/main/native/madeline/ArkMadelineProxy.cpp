#include <jni.h>
#include <string>
#include <iostream>
#include "ArkMadelineProxy.h"
#include "ArkMadeline.h"

JNIEXPORT void JNICALL Java_au_org_theark_core_jni_ArkMadelineProxy_connect(JNIEnv *env, jobject obj){
	connect();
	return;
}

JNIEXPORT jstring JNICALL Java_au_org_theark_core_jni_ArkMadelineProxy_generatePedigree(JNIEnv *env, jobject obj, jstring jstr1, jstring jstr2){
	const char *pedString = env->GetStringUTFChars(jstr1, NULL);
	const char *columnList = env->GetStringUTFChars(jstr2, NULL);

	jstring r;
	std::string result = generatePedigree (pedString, columnList);

	r = env->NewStringUTF(result.c_str());

	env->ReleaseStringUTFChars(jstr1, pedString);
	env->ReleaseStringUTFChars(jstr2, columnList);

	return r;
}
