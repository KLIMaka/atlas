package engine.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FontFileParser {

    private int                        m_charsCount;
    private Map<Character, charStruct> m_chars = new TreeMap<Character, charStruct>();
    private String                     m_fontImg;

    public FontFileParser(String file) throws IOException {
        this(new File(file));
    }

    public FontFileParser(File file) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(file));
        readInfo(br);
        readChars(br);
    }

    private void readInfo(BufferedReader br) throws IOException {
        String info = br.readLine();
        String common = br.readLine();
        String page = br.readLine();
        String chars = br.readLine();

        Matcher m = Pattern.compile("^chars count=(\\d+)$").matcher(chars);
        m.find();
        m_charsCount = Integer.valueOf(m.group(1));

        m = Pattern.compile("^page id=\\d+ file=\"(.+)\"$").matcher(page);
        m.find();
        m_fontImg = m.group(1);

    }

    private void readChars(BufferedReader br) throws IOException {

        for (int i = 0; i < m_charsCount; i++) {
            String c = br.readLine();
            final Pattern p = Pattern.compile("^char +id=(-?\\d+) +x=(\\d+) +y=(\\d+) +width=(\\d+) +height=(\\d+)"
                    + " +xoffset=(-?\\d+) +yoffset=(-?\\d+) +xadvance=(\\d+) +page=(\\d+) +chnl=(\\d+)$");
            Matcher m = p.matcher(c);

            m.find();

            charStruct cs = new charStruct();
            int id = Integer.valueOf(m.group(1));
            cs.x = Integer.valueOf(m.group(2));
            cs.y = Integer.valueOf(m.group(3));
            cs.w = Integer.valueOf(m.group(4));
            cs.h = Integer.valueOf(m.group(5));
            cs.xOff = Integer.valueOf(m.group(6));
            cs.yOff = Integer.valueOf(m.group(7));
            cs.xAdv = Integer.valueOf(m.group(8));
            cs.page = Integer.valueOf(m.group(9));
            cs.chnl = Integer.valueOf(m.group(10));
            cs.getTexCoords(512, 512);

            m_chars.put((char) id, cs);
        }
    }

    public charStruct getChar(char c) {
        charStruct cs = m_chars.get(c);
        if (cs == null) {
            cs = m_chars.get(-1);
        }
        return cs;
    }

    public String getFontImg() {
        return m_fontImg;
    }
}
