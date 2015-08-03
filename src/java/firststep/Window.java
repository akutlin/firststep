package firststep;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import firststep.internal.GL3W;
import firststep.internal.GLFW;
import firststep.internal.OS;
import firststep.internal.GLFW.Callback;

public class Window {

	/**
	 * Mouse buttons
	 */
	public enum MouseButton {
		LEFT(0), RIGHT(1), MIDDLE(2),
		BUTTON_4(3), BUTTON_5(4), BUTTON_6(5), BUTTON_7(6), BUTTON_8(7);
		final int id;
		MouseButton(int id) {
			this.id = id;
		}
		static MouseButton fromId(int id) {
			for (MouseButton v : MouseButton.values()) {
				if (v.id == id) return v;
			}
			throw new RuntimeException("Incorrect id");
		}
	}
	
	/**
	 * Keyboard key value
	 */
	public enum Key {
		UNKNOWN            (-1),

		/* Printable keys */
		SPACE              (32),
		APOSTROPHE         (39),  /* ' */
		COMMA              (44),  /* , */
		MINUS              (45),  /* - */
		PERIOD             (46),  /* . */
		SLASH              (47),  /* / */
		TOP_0              (48),
		TOP_1              (49),
		TOP_2              (50),
		TOP_3              (51),
		TOP_4              (52),
		TOP_5              (53),
		TOP_6              (54),
		TOP_7              (55),
		TOP_8              (56),
		TOP_9              (57),
		SEMICOLON          (59),  /* ; */
		EQUAL              (61),  /* = */
		A                  (65),
		B                  (66),
		C                  (67),
		D                  (68),
		E                  (69),
		F                  (70),
		G                  (71),
		H                  (72),
		I                  (73),
		J                  (74),
		K                  (75),
		L                  (76),
		M                  (77),
		N                  (78),
		O                  (79),
		P                  (80),
		Q                  (81),
		R                  (82),
		S                  (83),
		T                  (84),
		U                  (85),
		V                  (86),
		W                  (87),
		X                  (88),
		Y                  (89),
		Z                  (90),
		LEFT_BRACKET       (91),  /* [ */
		BACKSLASH          (92),  /* \ */
		RIGHT_BRACKET      (93),  /* ] */
		GRAVE_ACCENT       (96),  /* ` */
		WORLD_1            (161), /* non-US #1 */
		WORLD_2            (162), /* non-US #2 */

		/* Function keys */
		ESCAPE             (256),
		ENTER              (257),
		TAB                (258),
		BACKSPACE          (259),
		INSERT             (260),
		DELETE             (261),
		RIGHT              (262),
		LEFT               (263),
		DOWN               (264),
		UP                 (265),
		PAGE_UP            (266),
		PAGE_DOWN          (267),
		HOME               (268),
		END                (269),
		CAPS_LOCK          (280),
		SCROLL_LOCK        (281),
		NUM_LOCK           (282),
		PRINT_SCREEN       (283),
		PAUSE              (284),
		F1                 (290),
		F2                 (291),
		F3                 (292),
		F4                 (293),
		F5                 (294),
		F6                 (295),
		F7                 (296),
		F8                 (297),
		F9                 (298),
		F10                (299),
		F11                (300),
		F12                (301),
		F13                (302),
		F14                (303),
		F15                (304),
		F16                (305),
		F17                (306),
		F18                (307),
		F19                (308),
		F20                (309),
		F21                (310),
		F22                (311),
		F23                (312),
		F24                (313),
		F25                (314),
		KP_0               (320),
		KP_1               (321),
		KP_2               (322),
		KP_3               (323),
		KP_4               (324),
		KP_5               (325),
		KP_6               (326),
		KP_7               (327),
		KP_8               (328),
		KP_9               (329),
		KP_DECIMAL         (330),
		KP_DIVIDE          (331),
		KP_MULTIPLY        (332),
		KP_SUBTRACT        (333),
		KP_ADD             (334),
		KP_ENTER           (335),
		KP_EQUAL           (336),
		LEFT_SHIFT         (340),
		LEFT_CONTROL       (341),
		LEFT_ALT           (342),
		LEFT_SUPER         (343),
		RIGHT_SHIFT        (344),
		RIGHT_CONTROL      (345),
		RIGHT_ALT          (346),
		RIGHT_SUPER        (347),
		MENU               (348);
		
		final int id;
		Key(int id) {
			this.id = id;
		}
		static Key fromId(int id) {
			for (Key v : Key.values()) {
				if (v.id == id) return v;
			}
			throw new RuntimeException("Incorrect id");
		}
	}
	
	/**
	 * Mouse button states
	 */
	public enum MouseButtonState {
		RELEASE(0), PRESS(1);
		final int id;
		MouseButtonState(int id) {
			this.id = id;
		}
		static MouseButtonState fromId(int id) {
			for (MouseButtonState v : MouseButtonState.values()) {
				if (v.id == id) return v;
			}
			throw new RuntimeException("Incorrect id");
		}
	}
	
	/**
	 * Keyboard key states
	 */
	public enum KeyState {
		RELEASE(0), PRESS(1), REPEAT(2);
		final int id;
		KeyState(int id) {
			this.id = id;
		}
		static KeyState fromId(int id) {
			for (KeyState v : KeyState.values()) {
				if (v.id == id) return v;
			}
			throw new RuntimeException("Incorrect id");
		}
	}
	
	
	public enum Modifier {
		SHIFT(0x0001), CONTROL(0x0002), ALT(0x0004), SUPER(0x0008);
		final int value;
		Modifier(int value) {
			this.value = value;
		}
		static Modifier fromId(int id) {
			switch (id) {
			case 0x0001: return SHIFT;
			case 0x0002: return CONTROL;
			case 0x0004: return ALT;
			case 0x0008: return SUPER;
			default: throw new IllegalArgumentException("incorrect id");
			}
		}
	}
	
	public static class Modifiers {
		private final EnumSet<Modifier> values;
		public Modifiers(EnumSet<Modifier> values) {
			this.values = values.clone();
		}
		static Modifiers fromFlags(int flags) {
			EnumSet<Modifier> values = EnumSet.noneOf(Modifier.class);
			if ((flags & Modifier.SHIFT.value) != 0) values.add(Modifier.SHIFT);
			if ((flags & Modifier.CONTROL.value) != 0) values.add(Modifier.CONTROL);
			if ((flags & Modifier.ALT.value) != 0) values.add(Modifier.ALT);
			if ((flags & Modifier.SUPER.value) != 0) values.add(Modifier.SUPER);
			return new Modifiers(values);
		}
		public boolean contains(Modifier mod) {
			return values.contains(mod);
		}
		public boolean isEmpty() {
			return values.isEmpty();
		}
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Modifiers) {
				if (((Modifiers) obj).values.size() == values.size()) {
					for (Modifier v : values) {
						if (!((Modifiers) obj).values.contains(v)) return false;
					}
					return true;
				}
			}
			return false;
		}
		
		public static Modifiers of(Modifier mod) {
			return new Modifiers(EnumSet.of(mod));
		}
		public static Modifiers of(Modifier mod1, Modifier... rest) {
			return new Modifiers(EnumSet.of(mod1, rest));
		}
		
		int toFlags() {
			int res = 0;
			for (Modifier mod : values) {
				res |= mod.value;
			}
			return res;
		}
		
		@Override
		public String toString() {
			if (values.size() == 0) return "()";
			Iterator<Modifier> iter = values.iterator(); 
			String s = "(" + iter.next();
			while (iter.hasNext()) {
				s += ", " + iter.next();
			}
			return s += ")";
		}
	}
	
	private static Callback globalCallback = new Callback() {
		
		@Override
		public void windowSize(long window, int width, int height) {
			openedWindows.get(window).internalWindowSize(width, height);
		}
		
		@Override
		public void windowRefresh(long window) {
			openedWindows.get(window).internalDraw();
		}
		
		@Override
		public void windowPos(long window, int x, int y) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void windowIconify(long window, boolean iconified) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void windowFocus(long window, boolean focused) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void windowClose(long window) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void scroll(long window, double scrollX, double scrollY) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseButton(long window, int button, int action, int mods) {
			openedWindows.get(window).mouseButton(MouseButton.fromId(button), MouseButtonState.fromId(action), Modifiers.fromFlags(mods));
		}
		
		@Override
		public void monitor(long monitor, boolean connected) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void key(long window, int key, int scancode, int action, int mods) {
			openedWindows.get(window).key(Key.fromId(key), scancode, KeyState.fromId(action), Modifiers.fromFlags(mods));
		}
		
		@Override
		public void error(int error, String description) {
			getLogger().log(Level.SEVERE, "GLFW error #" + error + ": " + description);
		}
		
		@Override
		public void cursorPos(long window, double x, double y) {
			openedWindows.get(window).cursorPos(x, y);
		}
		
		@Override
		public void cursorEnter(long window, boolean entered) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void character(long window, char character) {
			openedWindows.get(window).character(character);
		}
	};
	
	private static boolean gl3wInitialized = false;
	private static HashMap<Long, Window> openedWindows = new HashMap<Long, Window>();
	
	static {
		boolean initSuccess = GLFW.init();
		if (!initSuccess) {
			throw new RuntimeException("GLFW initialization failed");
		}
		getLogger().log(Level.INFO, "GLFW initialized");
		
		GLFW.setCallback(globalCallback);
	}
	
	private static Logger getLogger() {
		return Logger.getLogger(Window.class.getName(), null);
	}
	
	public static void loop(float fpsMax) {
		while (!openedWindows.isEmpty()) {
			double t1 = GLFW.getTime();
			
			// Removing closed windows from the list
			HashSet<Long> toErase = new HashSet<>();
			for (Long glfwWindow : openedWindows.keySet()) {
				if (GLFW.windowShouldClose(glfwWindow)) {
					toErase.add(glfwWindow);
				}
			}
			openedWindows.keySet().removeAll(toErase);
		
			for (Window window : openedWindows.values()) {
				if (window.justCreated) {
					window.justCreated = false;
					window.internalWindowSize(window.width, window.height);
				} else {
					window.internalDraw();
				}
			}			
			
			GLFW.pollEvents();
			
			double t2 = GLFW.getTime();

			try {
				Thread.sleep((long) Math.max((1.0 / fpsMax - (t2 - t1)) * 1000, 0));
			} catch (InterruptedException e) {
				getLogger().log(Level.WARNING, "Thread sleep interrupted");
			}

		}
	}

	private long glfwWindow; 
	private Canvas canvas;
	private Color background = new Color(0f, 0f, 0f, 1f);
	private int width, height;
	private boolean justCreated;
	
	private Framebuffer rootFramebuffer;
	
	long getGLFWWindow() {
		return glfwWindow;
	}
	
	public Window(String title, int width, int height, Color background) {
		if (OS.getPlatform() == OS.Platform.OSX) {
			// We initialize OpenGL 3.2 Core profile on OSX cause
			// it is the only GL3 that Apple knows.
			
			GLFW.windowHint(GLFW.CONTEXT_VERSION_MAJOR, 3);
			GLFW.windowHint(GLFW.CONTEXT_VERSION_MINOR, 2);
			
			GLFW.windowHint(GLFW.OPENGL_PROFILE, GLFW.OPENGL_CORE_PROFILE);
			GLFW.windowHint(GLFW.OPENGL_FORWARD_COMPAT, 1);
		} else {
			// On other platforms OpenGL 3.0 is sufficient for us.
			
			GLFW.windowHint(GLFW.CONTEXT_VERSION_MAJOR, 3);
			GLFW.windowHint(GLFW.CONTEXT_VERSION_MINOR, 0);			
		}

		glfwWindow = GLFW.createWindow(width, height, title, 0, 0);
		if (glfwWindow == 0) {
			GLFW.terminate();
			throw new RuntimeException("GLFW can't create a window");
		}
		getLogger().log(Level.INFO, "GLFW window \"" + title + "\" [" + width + "x" + height + "] is created");

		if (!gl3wInitialized) {
			makeContextCurrent();
			if (!GL3W.init()) {
				GLFW.terminate();
				throw new RuntimeException("GL3W initialization failed");
			}
			gl3wInitialized = true;
			getLogger().log(Level.INFO, "GL3W context is initialized");
		}
		
		getLogger().log(Level.INFO, "GL version: " + GL3W.getGLVersionMajor() + "." + GL3W.getGLVersionMinor());
		getLogger().log(Level.INFO, "GLSL version: " + GL3W.getGLSLVersionMajor() + "." + GL3W.getGLSLVersionMinor());

		this.background = background;
		this.width = width;
		this.height = height;

		canvas = new Canvas(this);
		
		openedWindows.put(glfwWindow, this);
		justCreated = true;
		
	}
	
	private void internalDraw() {
		int fbWidth = width;	// TODO FramebufferSize
		int fbHeight = height;	// TODO FramebufferSize
		
		// Calculate pixel ration for hi-dpi devices.
		float pxRatio = (float)fbWidth / (float)width;

		GLFW.makeContextCurrent(glfwWindow);
		rootFramebuffer.setBackground(background);
		rootFramebuffer.draw(canvas);

        GLFW.swapBuffers(glfwWindow);
	}

	void makeContextCurrent() {
		GLFW.makeContextCurrent(glfwWindow);
	}
	
	public void internalWindowSize(int width, int height) {
		if (rootFramebuffer == null) {
			rootFramebuffer = new Framebuffer(canvas, width, height, 0);
		} else {
			rootFramebuffer.resize(width, height);
		}

		windowSize(width, height);
		this.width = width;
		this.height = height;

		internalDraw();
	}

	public void close() {
		GLFW.setWindowShouldClose(glfwWindow, 1);
	}
	
	public DoubleXY getCursorPos() {
		return new DoubleXY(GLFW.getCursorPosX(glfwWindow), GLFW.getCursorPosY(glfwWindow));
	}
	
	public void setTitle(String title) {
		GLFW.setWindowTitle(glfwWindow, title);
	}
	
	public Color getBackground() {
		return background;
	}
	
	public void setBackground(Color background) {
		this.background = background;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public void setSize(int width, int height) {
		internalWindowSize(width, height);
	}
	
	public Framebuffer getMainFramebuffer() {
		return rootFramebuffer;
	}
	
	public Framebuffer createFramebuffer(int width, int height, Image.Flags imageFlags) {
		return canvas.createFramebuffer(width, height, imageFlags);
	}
	
	// User events
		
	protected void windowSize(int width, int height) {
		
	}

	public void mouseButton(MouseButton button, MouseButtonState state, Modifiers modifiers) {
		
	}
	
	public void key(Key key, int scancode, KeyState state, Modifiers modifiers) {
		
	}
	
	public void character(char character) {
		
	}
	
	protected void cursorPos(double x, double y) {

	}

}
