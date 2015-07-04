#include <stdio.h>
#include <time.h>
#include <unistd.h>
#include <stdbool.h>

#include <GL3/gl3w.h>

#define GLFW_INCLUDE_GLCOREARB
#include <GLFW/glfw3.h>

#include <nanovg.h>
#define NANOVG_GL3_IMPLEMENTATION
#include <nanovg_gl.h>

#ifndef BOOL
#define BOOL int
#define FALSE 0
#define TRUE 1
#endif

#define APPNAME "CoolLife"

void getGlVersion(int *major, int *minor)
{
    const char *verstr = (const char *) glGetString(GL_VERSION);
    if ((verstr == NULL) || (sscanf(verstr,"%d.%d", major, minor) != 2))
    {
        *major = *minor = 0;
        fprintf(stderr, "Invalid GL_VERSION format!!!\n");
    }
}

void getGlslVersion(int *major, int *minor)
{
    int gl_major, gl_minor;
    getGlVersion(&gl_major, &gl_minor);

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

bool glInit()
{
    if (gl3wInit())
    {
        printf("Problem initializing OpenGL\n");
        return false;
    }

    int maj, min, slmaj, slmin;
    getGlVersion(&maj, &min);
    getGlslVersion(&slmaj, &slmin);

    printf("OpenGL version: %d.%d\n", maj, min);
    printf("GLSL version: %d.%d\n", slmaj, slmin);

    return true;
}

void errorcb(int error, const char* desc)
{
	printf("GLFW error %d: %s\n", error, desc);
}

BOOL isPaused = TRUE;
int screenshot = 0;
int premult = 0;

GLFWwindow* window;
NVGcontext* vg = NULL;
int fbWidth, fbHeight;
int winWidth, winHeight;
float pxRatio;
double fps = 25.0;

void drawTextCenter(NVGcontext* vg, char* text, int x, int y) {
	float bounds[4];
	nvgTextBounds(vg, 0, 0, text, NULL, bounds);
	int dx = x - (bounds[2] - bounds[0]) / 2, dy = y - (bounds[3] + bounds[1]) / 2;
	nvgText(vg, dx, dy, text, NULL);
}

int width, height;
BOOL* cells;
int mouseI, mouseJ;

void setCell(int i, int j, BOOL value)
{
    cells[j * width + i] = value;
}

BOOL getCell(int i, int j)
{
    while (i < 0) i += width;
    i = i % width;

    while (j < 0) j += height;
    j = j % height;
    
    return cells[j * width + i];
}

void initLife(int w, int h)
{
    width = w;
    height = h;
    cells = malloc(width * height * sizeof(BOOL));
    for (int i = 0; i < width * height; i++)
    {
        cells[i] = FALSE;
    }
    
    setCell(1, 1, TRUE);
    setCell(1, 2, TRUE);
    setCell(2, 2, TRUE);
}

void lifeTurn()
{
    BOOL* newCells = malloc(width * height * sizeof(BOOL));

    for (int i = 0; i < width; i++)
    for (int j = 0; j < height; j++)
    {
        int alive = 0;
        if (getCell(i - 1, j - 1)) alive ++;
        if (getCell(i - 1, j)) alive ++;
        if (getCell(i - 1, j + 1)) alive ++;
        if (getCell(i + 1, j - 1)) alive ++;
        if (getCell(i + 1, j)) alive ++;
        if (getCell(i + 1, j + 1)) alive ++;
        if (getCell(i, j - 1)) alive ++;
        if (getCell(i, j + 1)) alive ++;
        
        if (getCell(i, j) && (alive == 2 || alive == 3)) newCells[j * width + i] = TRUE;
        else if (!getCell(i, j) && (alive == 3)) newCells[j * width + i] = TRUE;
        else newCells[j * width + i] = FALSE;
    }
    
    free(cells);
    cells = newCells;
}

float calcScale()
{
    float wpw = (float)width / winWidth;
    float hph = (float)height / winHeight;
    float k = 1;
    if (wpw > hph)
    {
        k = 1.0f / wpw;
    }
    else
    {
        k = 1.0f / hph;
    }
    return k;
}

void cellUnderMouse()
{
    double mx, my;
    glfwGetCursorPos(window, &mx, &my);

    float k = calcScale();
    float x0 = winWidth / 2 - (width * k / 2);
    float y0 = winHeight / 2 - (height * k / 2);
    
    mouseI = (mx - x0) / k;
    mouseJ = (my - y0) / k;
}

void updateTitle()
{
    if (isPaused)
    {
        glfwSetWindowTitle(window, APPNAME " [ Pause ]");
    }
    else
    {
        glfwSetWindowTitle(window, APPNAME);
    }
}

void drawLife(NVGcontext* vg, int screenWidth, int screenHeight)
{
    float k = calcScale();
    
    float x0 = winWidth / 2 - (width * k / 2);
    float y0 = winHeight / 2 - (height * k / 2);
    
    for (int i = 0; i < width; i++)
    for (int j = 0; j < height; j++)
    {
        if (getCell(i, j) == FALSE)
        {
            nvgFillColor(vg, nvgRGB(0, 0, 0));
        }
        else
        {
            nvgFillColor(vg, nvgRGB(192, 192, 192));
        }
        
        nvgBeginPath(vg);
        nvgRect(vg, x0 + i * k, y0 + j * k, k, k);
        nvgFill(vg);

        if (k > 8)
        {
            nvgStrokeColor(vg, nvgRGBA(64, 64, 64, 128));
            nvgStroke(vg);
        }
        
    }
    
    if (mouseI >= 0 && mouseI < width && mouseJ >= 0 && mouseJ < height)
    {
        nvgFillColor(vg, nvgRGBA(255, 255, 255, 64));
        nvgBeginPath(vg);
        nvgRect(vg, x0 + mouseI * k, y0 + mouseJ * k, k, k);
        nvgFill(vg);
    }
}

static void key(GLFWwindow* window, int key, int scancode, int action, int mods)
{
	NVG_NOTUSED(scancode);
	NVG_NOTUSED(mods);
	if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
		glfwSetWindowShouldClose(window, GL_TRUE);
	if (key == GLFW_KEY_SPACE && action == GLFW_PRESS)
    {
		isPaused = !isPaused;
        updateTitle();
    }
	if (key == GLFW_KEY_S && action == GLFW_PRESS)
		screenshot = 1;
	if (key == GLFW_KEY_P && action == GLFW_PRESS)
		premult = !premult;
}

static void mouse(GLFWwindow* window, int button, int action, int mods)
{
    if (button == GLFW_MOUSE_BUTTON_LEFT)
    {
        int state = glfwGetMouseButton(window, button);
        if (state == GLFW_PRESS)
        {
            if (getCell(mouseI, mouseJ) == FALSE)
            {
                setCell(mouseI, mouseJ, TRUE);
            }
            else
            {
                setCell(mouseI, mouseJ, FALSE);
            }
        }
    }
}

static void draw()
{
	glfwGetWindowSize(window, &winWidth, &winHeight);
	glfwGetFramebufferSize(window, &fbWidth, &fbHeight);

	// Calculate pixel ration for hi-dpi devices.
	pxRatio = (float)fbWidth / (float)winWidth;

	// Update and render
	glViewport(0, 0, fbWidth, fbHeight);
	glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT|GL_STENCIL_BUFFER_BIT);

	nvgBeginFrame(vg, winWidth, winHeight, pxRatio);

	double t1 = glfwGetTime();
	drawLife(vg, winWidth, winHeight);
	double t2 = glfwGetTime();

	usleep(fmax((1.0 / fps - (t2 - t1)) * 1000000, 0));
	nvgEndFrame(vg);

	glfwSwapBuffers(window);
}

static void windowSize(GLFWwindow* window, int width, int height)
{
	draw();
}

int main()
{

	if (!glfwInit()) {
		printf("Failed to init GLFW.");
		return -1;
	}

	glfwSetErrorCallback(errorcb);

#ifdef __APPLE__
	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
	
	glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
	glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
#else
	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
#endif
	
	
#ifdef DEMO_MSAA
	glfwWindowHint(GLFW_SAMPLES, 4);
#endif

	window = glfwCreateWindow(600, 400, "Alpha clock", NULL, NULL);
	if (!window) {
		glfwTerminate();
		return -1;
	}

	glfwSetKeyCallback(window, key);
    glfwSetMouseButtonCallback(window, mouse);
	glfwSetWindowSizeCallback(window, windowSize);

	glfwMakeContextCurrent(window);
    if (!glInit())
    {
		printf("Error: can't initialize GL3 API\n");
		glfwTerminate();
		return -1;

    }
	
#ifdef DEMO_MSAA
	vg = nvgCreateGL3(NVG_STENCIL_STROKES | NVG_DEBUG);
#else
	vg = nvgCreateGL3(NVG_ANTIALIAS | NVG_STENCIL_STROKES | NVG_DEBUG);
#endif
	if (vg == NULL) {
		printf("Could not init nanovg.\n");
		return -1;
	}

	glfwSwapInterval(0);

	glfwSetTime(0);

	nvgCreateFont(vg, "bold", "fonts/Walkway_Bold.ttf");
	nvgCreateFont(vg, "black", "fonts/Walkway_Black.ttf");

    updateTitle();
    
    initLife(15, 15);
    
	while (!glfwWindowShouldClose(window))
	{
        if (!isPaused)
        {
            lifeTurn();
        }

        cellUnderMouse();
		draw();
		
		glfwPollEvents();
	}

    free(cells);
    
	nvgDeleteGL3(vg);

	glfwTerminate();
	return 0;
}
