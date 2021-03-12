package io.innocent.dream.util;

import java.util.HashMap;

public class KeyCodes {

    private static final HashMap<Integer, String> KEY_CODES;

    static {
        KEY_CODES = new HashMap<>();
        KEY_CODES.put(-1, "UNKNOWN KEY");
        KEY_CODES.put(32, "Space");
        KEY_CODES.put(39, "'");
        KEY_CODES.put(44, ",");
        KEY_CODES.put(45, "-");
        KEY_CODES.put(46, ".");
        KEY_CODES.put(47, "/");
        KEY_CODES.put(48, "0");
        KEY_CODES.put(49, "1");
        KEY_CODES.put(50, "2");
        KEY_CODES.put(51, "3");
        KEY_CODES.put(52, "4");
        KEY_CODES.put(53, "5");
        KEY_CODES.put(54, "6");
        KEY_CODES.put(55, "7");
        KEY_CODES.put(56, "8");
        KEY_CODES.put(57, "9");
        KEY_CODES.put(59, ";");
        KEY_CODES.put(61, "=");
        KEY_CODES.put(65, "A");
        KEY_CODES.put(66, "B");
        KEY_CODES.put(67, "C");
        KEY_CODES.put(68, "D");
        KEY_CODES.put(69, "E");
        KEY_CODES.put(70, "F");
        KEY_CODES.put(71, "G");
        KEY_CODES.put(72, "H");
        KEY_CODES.put(73, "I");
        KEY_CODES.put(74, "J");
        KEY_CODES.put(75, "K");
        KEY_CODES.put(76, "L");
        KEY_CODES.put(77, "M");
        KEY_CODES.put(78, "N");
        KEY_CODES.put(79, "O");
        KEY_CODES.put(80, "P");
        KEY_CODES.put(81, "Q");
        KEY_CODES.put(82, "R");
        KEY_CODES.put(83, "S");
        KEY_CODES.put(84, "T");
        KEY_CODES.put(85, "U");
        KEY_CODES.put(86, "V");
        KEY_CODES.put(87, "W");
        KEY_CODES.put(88, "X");
        KEY_CODES.put(89, "Y");
        KEY_CODES.put(90, "Z");
        KEY_CODES.put(91, "[");
        KEY_CODES.put(92, "\\");
        KEY_CODES.put(93, "]");
        KEY_CODES.put(96, "`");
        KEY_CODES.put(256, "Escape");
        KEY_CODES.put(257, "Return");
        KEY_CODES.put(258, "Tab");
        KEY_CODES.put(259, "Backspace");
        KEY_CODES.put(260, "Insert");
        KEY_CODES.put(261, "Delete");
        KEY_CODES.put(262, "Right");
        KEY_CODES.put(263, "Left");
        KEY_CODES.put(264, "Down");
        KEY_CODES.put(265, "Up");
        KEY_CODES.put(266, "Pg Up");
        KEY_CODES.put(267, "Pg Down");
        KEY_CODES.put(268, "Home");
        KEY_CODES.put(269, "End");
        KEY_CODES.put(280, "Caps");
        KEY_CODES.put(281, "Scroll Lock");
        KEY_CODES.put(282, "Num Lock");
        KEY_CODES.put(283, "Prt Scrn");
        KEY_CODES.put(284, "Pause");
        KEY_CODES.put(290, "F1");
        KEY_CODES.put(291, "F2");
        KEY_CODES.put(292, "F3");
        KEY_CODES.put(293, "F4");
        KEY_CODES.put(294, "F5");
        KEY_CODES.put(295, "F6");
        KEY_CODES.put(296, "F7");
        KEY_CODES.put(297, "F8");
        KEY_CODES.put(298, "F9");
        KEY_CODES.put(299, "F10");
        KEY_CODES.put(300, "F11");
        KEY_CODES.put(301, "F12");
        KEY_CODES.put(302, "F13");
        KEY_CODES.put(303, "F14");
        KEY_CODES.put(304, "F15");
        KEY_CODES.put(305, "F16");
        KEY_CODES.put(306, "F17");
        KEY_CODES.put(307, "F18");
        KEY_CODES.put(308, "F19");
        KEY_CODES.put(309, "F20");
        KEY_CODES.put(310, "F21");
        KEY_CODES.put(311, "F22");
        KEY_CODES.put(312, "F23");
        KEY_CODES.put(313, "F24");
        KEY_CODES.put(314, "F25");
        KEY_CODES.put(340, "Left Shift");
        KEY_CODES.put(341, "Left Control");
        KEY_CODES.put(342, "Left Alt");
        KEY_CODES.put(343, "Left Super");
        KEY_CODES.put(344, "Right Shift");
        KEY_CODES.put(345, "Right Control");
        KEY_CODES.put(346, "Right Alt");
        KEY_CODES.put(347, "Right Super");
        KEY_CODES.put(348, "Menu");
    }

    public static String getKeyName(int key) {
        String keyStr = KEY_CODES.get(key);
        if (keyStr == null) {
            keyStr = String.valueOf(key);
        }
        return KEY_CODES.get(key);
    }

}
