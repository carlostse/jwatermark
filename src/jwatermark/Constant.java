/*
Copyright (c) 2011, Carlos Tse <copperoxide@gmail.com>
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package jwatermark;

import java.util.HashMap;

public class Constant {

	public static final String[] TXT = new String[] {
		"Watermark", "0.0.2",
		"Source Image: ", "Browse",
		"Watermark Image: ", "Browse",	// 5
		"Watermark Type:", "Save Output", "Done!", "Failed!", // 9
		"Developed by: ", "Carlos Tse", "copperoxide@gmail.com", // 12
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
