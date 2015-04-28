package materialtest.vivz.slidenerd.materialtest.utils;

import java.util.ArrayList;

public class Types {

    public class Format {
        public static final String BLU_RAY = "Blu-Ray";
        public static final String DVD = "DVD";
        public static final String VHS = "VHS";
        public static final String UMD = "UMD";
        public static final String Betamax = "Betamax";
        public static final String HD_DVD = "HD-DVD";
    }

    public static ArrayList<String> formats(){
        final ArrayList<String> strings = new ArrayList<>();
        strings.add(Format.BLU_RAY);
        strings.add(Format.DVD);
        strings.add(Format.VHS);
        strings.add(Format.UMD);
        strings.add(Format.Betamax);
        strings.add(Format.HD_DVD);
        return strings;
    }

    public enum ChosenListType {
        Your,
        Borrowed,
        Lent
    }
}
