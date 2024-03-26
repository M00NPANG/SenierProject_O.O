package com.example.homework

import android.graphics.Bitmap
import android.util.Log

data class User(
    var user_id : Long? = null, // 시퀀스넘버
    var password : String? = null, // 비밀번호
    var email: String? = null,// 아이디(이메일)
    var name : String? = null,
    var user_percol : String? = null,
    var user_color : String? = null,
    var user_style : String? = null,
    var user_img : String? = null,
)

data class Post(      // 받기 위한 codiset 정보
    val post_id: Int?,
    val hashtag: String,
    val content: String,
    val title: String,
    val imagePath: String,
    val userEmail: String,
    val userName : String
)

data class CodyGridItem(  // 실제로 보이기 위한 codiset 정보
    val imageResId: Int? = null,
    val imagePath: String? = null,
    val title: String,
    val hashtag: String? = null,
    val content: String? = null,
    val useremail:String? = null,
    val username: String? = null
)

data class Clothes(
    val cl_id: Int?,                //클라이언트에서 보낼 땐 안줘도 될듯
    val user_id : User?,            //클라이언트에서 보낼 땐 안줘도 될듯
    val cl_category: Int?,
    val cl_brand : String?,
    val cl_name : String?,
    val cl_price : String?,
    val cl_photo_path: String?,
    val cl_personal_color : String?
)

data class image(
    val img_id : Int?,
    val fileName : String?,
    val fileOriName : String?,
    val fileUrl : String?
)

data class style(
    val sty_id : Long?,
    val sty_street : Int?,
    val sty_modern : Int?,
    val sty_minimal : Int?,
    val sty_feminine : Int?,
    val sty_simpleBasic : Int?,
    val sty_americanCasual : Int?,
    val sty_businessCasual : Int?,
    val sty_casual : Int?,
    val sty_retro : Int?,
    val sty_classic : Int?,
    val sty_elegant : Int?,
    val sty_girlish : Int?,
    val sty_tomboy : Int?,
    val sty_vintage : Int?,
    val user_id: Long?
)

object BitmapStorage { //액티비티간 이미지 전송하려고 만듬
    var Bitmap: Bitmap? = null
}



object ClothesRepository { // CreateOutfitActivity에서 쓰려고 만듬
    var allClothesUrls: MutableList<String> = mutableListOf()

    fun addClothesUrl(url: String) {
        allClothesUrls.add(url)
    }

    fun clearClothesUrls() {
        allClothesUrls.clear()
    }
    fun printAllUrls() {
        allClothesUrls.forEach { url ->
           Log.d("현재 저장된 url들",url)
        }
    }

    fun returnUrls(): MutableList<String> {
        return allClothesUrls
    }
}


data class CSSColor(val name: String, val r: Int, val g: Int, val b: Int)


val cssColors = listOf(
    CSSColor("aliceblue",240,248,255),
    CSSColor("antiquewhite",250,235,215),
    CSSColor("aqua",	0,255,255),
    CSSColor("aquamarine",	127,255,212),
    CSSColor("azure",	240,255,255),
    CSSColor("beige",	245,245,220),
    CSSColor("bisque",		255,228,196),
    CSSColor("black",			0,0,0),
    CSSColor("blanchedalmond",			255,235,205),
    CSSColor("blue",			0,0,255),
    CSSColor("blueviolet",			138,43,226),
    CSSColor("brown",			165,42,42),
    CSSColor("burlywood",			222,184,135),
    CSSColor("cadetblue",			95,158,160),
    CSSColor("chartreuse",			127,255,0),
    CSSColor("chocolate",			210,105,30),
    CSSColor("coral",			255,127,80),
    CSSColor("cornflowerblue",	100,149,237),
    CSSColor("cornsilk",			255,248,220),
    CSSColor("crimson",			220,20,60),
    CSSColor("cyan",			0,255,255),
    CSSColor("darkblue",		0,0,139),
    CSSColor("darkcyan",		0,139,139),
    CSSColor("darkgoldenrod",	184,134,11),
    CSSColor("darkgray",	169,169,169),
    CSSColor("darkgreen",	0,100,0),
    CSSColor("darkgrey",169,169,169),
    CSSColor("darkkhaki",189,183,107),
    CSSColor("darkmagenta",139,0,139),
    CSSColor("darkolivegreen",  85,107,47),
    CSSColor("darkorange",  255,140,0),
    CSSColor("darkorchid",  153,50,204),
    CSSColor("darkred",  139,0,0),
    CSSColor("darksalmon",  233,150,122),
    CSSColor("darkseagreen",  143,188,143),
    CSSColor("darkslateblue",  72,61,139),
    CSSColor("darkslategray",  47,79,79),
    CSSColor("darkturquoise",  0,206,209),
    CSSColor("darkviolet",  148,0,211),
    CSSColor("deeppink",  255,20,147),
    CSSColor("deepskyblue",  0,191,255),
    CSSColor("dimgray",  105,105,105),
    CSSColor("dimgrey",  105,105,105),
    CSSColor("dodgerblue",  30,144,255),
    CSSColor("firebrick",  178,34,34),
    CSSColor("floralwhite", 	255,250,240),
    CSSColor("forestgreen", 	34,139,34),
    CSSColor("fuchsia", 	255,0,255),
    CSSColor("gainsboro", 	220,220,220),
    CSSColor("ghostwhite", 	248,248,255),
    CSSColor("gold", 	255,215,0),
    CSSColor("goldenrod", 	218,165,32),
    CSSColor("gray", 	128,128,128),
    CSSColor("green", 	0,128,0),
    CSSColor("greenyellow", 	173,255,47),
    CSSColor("grey", 	128,128,128),
    CSSColor("honeydew", 	240,255,240),
    CSSColor("hotpink", 	255,105,180),
    CSSColor("indianred", 	205,92,92),
    CSSColor("indigo", 	75,0,130),
    CSSColor("ivory", 	255,255,240),
    CSSColor("khaki", 	240,230,140),
    CSSColor("lavender", 	230,230,250),
    CSSColor("lavenderblush", 	255,240,245),
    CSSColor("lawngreen", 	124,252,0),
    CSSColor("lemonchiffon", 	255,250,205),
    CSSColor("lightblue", 	173,216,230),
    CSSColor("lightcoral", 240,128,128),
    CSSColor("lightcyan", 224,255,255),
    CSSColor("lightgoldenrodyellow", 250,250,210),
    CSSColor("lightgray", 211,211,211),
    CSSColor("lightgreen", 144,238,144),
    CSSColor("lightgrey", 211,211,211),
    CSSColor("lightpink", 255,182,193),
    CSSColor("lightsalmon", 255,160,122),
    CSSColor("lightseagreen", 32,178,170),
    CSSColor("lightskyblue", 135,206,250),
    CSSColor("lightslategray", 119,136,153),
    CSSColor("lightslategrey", 119,136,153),
    CSSColor("lightsteelblue", 176,196,222),
    CSSColor("lightyellow", 255,255,224),
    CSSColor("lime", 0,255,0),
    CSSColor("limegreen", 50,205,50),
    CSSColor("linen", 250,240,230),
    CSSColor("magenta", 255,0,255),
    CSSColor("maroon", 128,0,0),
    CSSColor("mediumaquamarine", 102,205,170),
    CSSColor("mediumblue", 0,0,205),
    CSSColor("mediumorchid", 186,85,211),
    CSSColor("mediumpurple", 147,112,219),
    CSSColor("mediumseagreen", 60,179,113),
    CSSColor("mediumslateblue", 123,104,238),
    CSSColor("mediumspringgreen", 0,250,154),
    CSSColor("mediumturquoise", 72,209,204),
    CSSColor("mediumvioletred", 199,21,133),
    CSSColor("midnightblue", 25,25,112),
    CSSColor("mintcream", 245,255,250),
    CSSColor("mistyrose", 255,228,225),
    CSSColor("moccasin", 255,228,181),
    CSSColor("navajowhite", 255,222,173),
    CSSColor("navy", 	0,0,128),
    CSSColor("oldlace", 253,245,230),
    CSSColor("olive", 128,128,0),
    CSSColor("olivedrab", 107,142,35),
    CSSColor("orange", 255,165,0),
    CSSColor("orangered",255,69,0),
    CSSColor("orchid",	218,112,214),
    CSSColor("palegoldenrod",	238,232,170),
    CSSColor("palegreen",	152,251,152),
    CSSColor("paleturquoise",	175,238,238),
    CSSColor("palevioletred",	219,112,147),
    CSSColor("papayawhip",	255,239,213),
    CSSColor("peachpuff",	255,218,185),
    CSSColor("peru",	205,133,63),
    CSSColor("pink",	255,192,203),
    CSSColor("plum",	221,160,221),
    CSSColor("powderblue",176,224,230),
    CSSColor("purple", 128,0,128),
    CSSColor("red", 255,0,0),
    CSSColor("rosybrown", 188,143,143),
    CSSColor("royalblue", 65,105,225),
    CSSColor("saddlebrown", 139,69,19),
    CSSColor("salmon", 250,128,114),
    CSSColor("sandybrown", 244,164,96),
    CSSColor("seagreen", 46,139,87),
    CSSColor("seashell", 255,245,238),
    CSSColor("sienna", 160,82,45),
    CSSColor("silver", 192,192,192),
    CSSColor("skyblue",135,206,235),
    CSSColor("slategray",112,128,144),
    CSSColor("slategrey",112,128,144),
    CSSColor("snow",255,250,250),
    CSSColor("springgreen",0,255,127),
    CSSColor("steelblue",70,130,180),
    CSSColor("tan",210,180,140),
    CSSColor("teal",0,128,128),
    CSSColor("thistle",216,191,216),
    CSSColor("tomato",255,99,71),
    CSSColor("turquoise",64,224,208),
    CSSColor("violet",238,130,238),
    CSSColor("wheat",245,222,179),
    CSSColor("white",255,255,255),
    CSSColor("whitesmoke",245,245,245),
    CSSColor("yellow",255,255,0),
    CSSColor("yellowgreen",154,205,50)
)


/*
        aliceblue	#F0F8FF	240,248,255
 	 	antiquewhite	#FAEBD7	250,235,215
 	 	aqua	#00FFFF	0,255,255
 	 	aquamarine	#7FFFD4	127,255,212
 	 	azure	#F0FFFF	240,255,255
 	 	beige	#F5F5DC	245,245,220
 	 	bisque	#FFE4C4	255,228,196
 	 	black	#000000	0,0,0
 	 	blanchedalmond	#FFEBCD	255,235,205
 	 	blue	#0000FF	0,0,255
 	 	blueviolet	#8A2BE2	138,43,226
 	 	brown	#A52A2A	165,42,42
 	 	burlywood	#DEB887	222,184,135
 	 	cadetblue	#5F9EA0	95,158,160
 	 	chartreuse	#7FFF00	127,255,0
 	 	chocolate	#D2691E	210,105,30
 	 	coral	#FF7F50	255,127,80
 	 	cornflowerblue	#6495ED	100,149,237
 	 	cornsilk	#FFF8DC	255,248,220
 	 	crimson	#DC143C	220,20,60
 	 	cyan	#00FFFF	0,255,255
 	 	darkblue	#00008B	0,0,139
 	 	darkcyan	#008B8B	0,139,139
 	 	darkgoldenrod	#B8860B	184,134,11
 	 	darkgray	#A9A9A9	169,169,169
 	 	darkgreen	#006400	0,100,0
 	 	darkgrey	#A9A9A9	169,169,169
 	 	darkkhaki	#BDB76B	189,183,107
 	 	darkmagenta	#8B008B	139,0,139
 	 	darkolivegreen	#556B2F	85,107,47
 	 	darkorange	#FF8C00	255,140,0
 	 	darkorchid	#9932CC	153,50,204
 	 	darkred	#8B0000	139,0,0
 	 	darksalmon	#E9967A	233,150,122
 	 	darkseagreen	#8FBC8F	143,188,143
 	 	darkslateblue	#483D8B	72,61,139
 	 	darkslategray	#2F4F4F	47,79,79
 	 	darkslategrey	#2F4F4F	47,79,79
 	 	darkturquoise	#00CED1	0,206,209
 	 	darkviolet	#9400D3	148,0,211
 	 	deeppink	#FF1493	255,20,147
 	 	deepskyblue	#00BFFF	0,191,255
 	 	dimgray	#696969	105,105,105
 	 	dimgrey	#696969	105,105,105
 	 	dodgerblue	#1E90FF	30,144,255
 	 	firebrick	#B22222	178,34,34
 	 	floralwhite	#FFFAF0	255,250,240
 	 	forestgreen	#228B22	34,139,34
 	 	fuchsia	#FF00FF	255,0,255
 	 	gainsboro	#DCDCDC	220,220,220
 	 	ghostwhite	#F8F8FF	248,248,255
 	 	gold	#FFD700	255,215,0
 	 	goldenrod	#DAA520	218,165,32
 	 	gray	#808080	128,128,128
 	 	green	#008000	0,128,0
 	 	greenyellow	#ADFF2F	173,255,47
 	 	grey	#808080	128,128,128
 	 	honeydew	#F0FFF0	240,255,240
 	 	hotpink	#FF69B4	255,105,180
 	 	indianred	#CD5C5C	205,92,92
 	 	indigo	#4B0082	75,0,130
 	 	ivory	#FFFFF0	255,255,240
 	 	khaki	#F0E68C	240,230,140
 	 	lavender	#E6E6FA	230,230,250
 	 	lavenderblush	#FFF0F5	255,240,245
 	 	lawngreen	#7CFC00	124,252,0
 	 	lemonchiffon	#FFFACD	255,250,205
 	 	lightblue	#ADD8E6	173,216,230
 	 	lightcoral	#F08080	240,128,128
 	 	lightcyan	#E0FFFF	224,255,255
 	 	lightgoldenrodyellow	#FAFAD2	250,250,210
 	 	lightgray	#D3D3D3	211,211,211
 	 	lightgreen	#90EE90	144,238,144
 	 	lightgrey	#D3D3D3	211,211,211
 	 	lightpink	#FFB6C1	255,182,193
 	 	lightsalmon	#FFA07A	255,160,122
 	 	lightseagreen	#20B2AA	32,178,170
 	 	lightskyblue	#87CEFA	135,206,250
 	 	lightslategray	#778899	119,136,153
 	 	lightslategrey	#778899	119,136,153
 	 	lightsteelblue	#B0C4DE	176,196,222
 	 	lightyellow	#FFFFE0	255,255,224
 	 	lime	#00FF00	0,255,0
 	 	limegreen	#32CD32	50,205,50
 	 	linen	#FAF0E6	250,240,230
 	 	magenta	#FF00FF	255,0,255
 	 	maroon	#800000	128,0,0
 	 	mediumaquamarine	#66CDAA	102,205,170
 	 	mediumblue	#0000CD	0,0,205
 	 	mediumorchid	#BA55D3	186,85,211
 	 	mediumpurple	#9370DB	147,112,219
 	 	mediumseagreen	#3CB371	60,179,113
 	 	mediumslateblue	#7B68EE	123,104,238
 	 	mediumspringgreen	#00FA9A	0,250,154
 	 	mediumturquoise	#48D1CC	72,209,204
 	 	mediumvioletred	#C71585	199,21,133
 	 	midnightblue	#191970	25,25,112
 	 	mintcream	#F5FFFA	245,255,250
 	 	mistyrose	#FFE4E1	255,228,225
 	 	moccasin	#FFE4B5	255,228,181
 	 	navajowhite	#FFDEAD	255,222,173
 	 	navy	#000080	0,0,128
 	 	oldlace	#FDF5E6	253,245,230
 	 	olive	#808000	128,128,0
 	 	olivedrab	#6B8E23	107,142,35
 	 	orange	#FFA500	255,165,0
 	 	orangered	#FF4500	255,69,0
 	 	orchid	#DA70D6	218,112,214
 	 	palegoldenrod	#EEE8AA	238,232,170
 	 	palegreen	#98FB98	152,251,152
 	 	paleturquoise	#AFEEEE	175,238,238
 	 	palevioletred	#DB7093	219,112,147
 	 	papayawhip	#FFEFD5	255,239,213
 	 	peachpuff	#FFDAB9	255,218,185
 	 	peru	#CD853F	205,133,63
 	 	pink	#FFC0CB	255,192,203
 	 	plum	#DDA0DD	221,160,221
 	 	powderblue	#B0E0E6	176,224,230
 	 	purple	#800080	128,0,128
 	 	red	#FF0000	255,0,0
 	 	rosybrown	#BC8F8F	188,143,143
 	 	royalblue	#4169E1	65,105,225
 	 	saddlebrown	#8B4513	139,69,19
 	 	salmon	#FA8072	250,128,114
 	 	sandybrown	#F4A460	244,164,96
 	 	seagreen	#2E8B57	46,139,87
 	 	seashell	#FFF5EE	255,245,238
 	 	sienna	#A0522D	160,82,45
 	 	silver	#C0C0C0	192,192,192
 	 	skyblue	#87CEEB	135,206,235
 	 	slateblue	#6A5ACD	106,90,205
 	 	slategray	#708090	112,128,144
 	 	slategrey	#708090	112,128,144
 	 	snow	#FFFAFA	255,250,250
 	 	springgreen	#00FF7F	0,255,127
 	 	steelblue	#4682B4	70,130,180
 	 	tan	#D2B48C	210,180,140
 	 	teal	#008080	0,128,128
 	 	thistle	#D8BFD8	216,191,216
 	 	tomato	#FF6347	255,99,71
 	 	turquoise	#40E0D0	64,224,208
 	 	violet	#EE82EE	238,130,238
 	 	wheat	#F5DEB3	245,222,179
 	 	white	#FFFFFF	255,255,255
 	 	whitesmoke	#F5F5F5	245,245,245
 	 	yellow	#FFFF00	255,255,0
 	 	yellowgreen	#9ACD32	154,205,50

 */