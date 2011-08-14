package jwatermark;

import java.util.HashMap;

public class Constant {

	public static final String[] TXT = new String[] {
		"Watermark", "0.0.2",
		"Source Image: ", "Browse",
		"Watermark Image: ", "Browse",	// 5
		"Watermark Type:", "Save Output", "Done!", "Failed!", // 9
		"Developed by: ", "Carlos Tse", "iusers@hotmail.com", // 12
		"Homepage: ", "http://my.no-ip.info/carlos/", "OK", // 15
		"Please select ", "source", "watermark", " image!", // 19
		"&File", "&Open Source\tCTRL+O", "&Open Watermark\tCTRL+W", "&Save\tCTRL+S",
		"E&xit\tCTRL+Q", "&Help", "&About"
	};

	public static final byte 	ER_MSG_ST 	= 16,
								ABT_MSG_ST 	= 10;

	public static final byte 	MARK_LEFT_TOP 		= 1,
								MARK_RIGHT_TOP 		= 2,
								MARK_CENTER 		= 3,
								MARK_LEFT_BOTTOM 	= 4,
								MARK_RIGHT_BOTTOM 	= 5,
								MARK_REPEAT 		= 6;

	public static final String[] MARK_ITEMS = new String[] {
		"Left Top", "Right Top", "Centre", "Left Bottom", "Right Bottom", "Repeat"
	};

	public static final HashMap<String, Byte> MARK_MAP = new HashMap<String, Byte>();

	static {
		byte i = 1;
		for (String s: MARK_ITEMS)
			MARK_MAP.put(s, i++);
	}

}
