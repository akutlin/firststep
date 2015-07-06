cmake_minimum_required (VERSION 2.8)

set(PROJNAME "firststep")

file(GLOB SOURCES_C "${CMAKE_CURRENT_SOURCE_DIR}/src/native/*.c")
file(GLOB SOURCES_CPP "${CMAKE_CURRENT_SOURCE_DIR}/src/native/*.cpp")

set(SOURCES
	${SOURCES_C} 
	${SOURCES_CPP}
	"${CMAKE_CURRENT_SOURCE_DIR}/nanovg/src/nanovg.c" 
)

project (${PROJNAME})

# Native library

if(MINGW)
	SET(CMAKE_SHARED_LIBRARY_PREFIX "")
	SET(CMAKE_SHARED_MODULE_PREFIX "")
endif (MINGW)

find_package(JNI)

add_library(
	${PROJNAME}
	SHARED 
	${SOURCES}
)

include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}/src 
	${CMAKE_CURRENT_SOURCE_DIR}/include
	${CMAKE_CURRENT_SOURCE_DIR}/nanovg/src
	${CMAKE_CURRENT_SOURCE_DIR}/glfw/include
	${JAVA_INCLUDE_PATH}
	${JAVA_INCLUDE_PATH2}
)

SET(MACOSX_RPATH FALSE)

SET(GLFW_BUILD_DOCS OFF CACHE BOOL "build docs")
SET(GLFW_BUILD_EXAMPLES OFF CACHE BOOL "build examples")
SET(GLFW_BUILD_TESTS OFF CACHE BOOL "build tests")

add_subdirectory("glfw")

SET(CMAKE_C_FLAGS "-std=gnu99")

IF(APPLE)
    SET(EXTRA_LIBS "-framework Cocoa -framework OpenGL")
	SET(CMAKE_EXE_LINKER_FLAGS "-std=gnu99")
ELSEIF(WIN32)
	SET(EXTRA_LIBS "-lglu32 -lopengl32")
	SET(CMAKE_EXE_LINKER_FLAGS "-std=gnu99 -static -static-libgcc -static-libstdc++ -mwindows")
ENDIF()

target_link_libraries(${PROJNAME} glfw ${GLFW_LIBRARIES} ${EXTRA_LIBS})


# Java bindings

set(JARNAME "firststep-java")

find_package(Java REQUIRED)
include(cmake_patches/UseJavaCustom.cmake)

set(CMAKE_JAVA_COMPILE_FLAGS "-source" "1.7" "-target" "1.7")

file(GLOB_RECURSE SOURCES_JAVA "${CMAKE_CURRENT_SOURCE_DIR}/src/java/*.java")

add_jar(${JARNAME} ${SOURCES_JAVA})

get_target_property(_jarFile ${JARNAME} JAR_FILE)
get_target_property(_classDir ${JARNAME} CLASSDIR)

message(STATUS "Jar file ${_jarFile}")
message(STATUS "Class compiled to ${_classDir}")

add_dependencies(${PROJNAME} ${JARNAME})