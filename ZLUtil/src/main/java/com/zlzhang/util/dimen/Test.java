package com.zlzhang.util.dimen;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by zhangzhilai on 2018/7/16.
 */

public class Test {
    public static void main(String[] args) {
        DimensParser parser = new DimensParser();
        try {
            List<DimenValues> list = parser.parse(new FileInputStream(Config.path + File.separator + "dimens.xml"));
            DimensCreator creator = new DimensCreator(Config.path, list);
            creator.createAll();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
