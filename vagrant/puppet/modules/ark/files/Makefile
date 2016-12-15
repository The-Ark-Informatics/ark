# The output is a shared library
TARGET := libmadeline.so
# The C++ compiler to use.
CPP_COMPILER := g++
# The C compiler to use.
C_COMPILER := gcc
# Compile all .cpp source files
CPP_FILES := $(wildcard *.cpp)
# Produce C++ object files in the  obj/ subdirectory.
CPP_OBJ_FILES := $(addprefix obj/,$(notdir $(CPP_FILES:.cpp=.o)))

# We need to create a shared library linked against libcurl, libbz2 and libxml2.
LD_FLAGS := -lcurl -lbz2 -lxml2 -shared -Wl,-soname,madeline.so
# Comment the follwing line on 32-bit platforms.
FLAG_64_BIT := -m64
# Compiler flags, assumming g++
CC_FLAGS := -fPIC $(FLAG_64_BIT)

# Edit the following line to reflect the location of your JDK and its headers
JAVA_BASE := /usr/lib/jvm/java-8-oracle
JAVA_INCLUDES := -I$(JAVA_BASE)/include -I$(JAVA_BASE)/include/linux
# Need to give the path to the libxml2 headers explicitly
CPP_INCLUDES := -I/usr/include/libxml2

# Default target builds libmadeline
$(TARGET): $(CPP_OBJ_FILES) unzip/unzip.o
	$(CPP_COMPILER) $(LD_FLAGS) -o $@ $^

obj/%.o: %.cpp
	$(CPP_COMPILER) $(JAVA_INCLUDES) $(CPP_INCLUDES) $(CC_FLAGS) -c -o $@ $<

unzip/unzip.o: unzip/unzip.c
	$(C_COMPILER) $(CC_FLAGS) -c -o $@ $<

clean:
	rm -f obj/*.o unzip/*.o

distclean: clean
	rm -f $(TARGET)

