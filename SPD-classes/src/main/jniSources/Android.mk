LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := FroyoGLES20Fix
LOCAL_SRC_FILES := FroyoGLES20Fix.c
LOCAL_LDLIBS    := -lGLESv2

include $(BUILD_SHARED_LIBRARY)
