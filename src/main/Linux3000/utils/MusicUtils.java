package main.Linux3000.utils;

import net.dv8tion.jda.api.entities.emoji.Emoji;

public class MusicUtils {

    //Huge thanks to Xirado : https://github.com/Xirado  ,  with inspiration from https://github.com/Xirado/Bean/blob/master/src/main/java/at/xirado/bean/misc/MusicUtil.java


    private static final String BAR = Emoji.fromCustom("bar", 1007998422030229644L, false).getAsMention();
    private static final String KNOB_BEGINNING = Emoji.fromCustom("bar3", 1007999021400469604L, false).getAsMention();
    private static final String KNOB_BEGINNING_SPLIT = Emoji.fromCustom("bar2", 1007998550807945226L, false).getAsMention();
    private static final String KNOB_MIDDLE =  Emoji.fromCustom("bar4",1007999022524534924L, false ).getAsMention();
    private static final String KNOB_END = Emoji.fromCustom("bar5",1007999024093208647L, false ).getAsMention();
    private static final String KNOB_END_SPLIT = Emoji.fromCustom("bar6", 1007999025242439700L, false).getAsMention();



    // Ok look I know this looks ugly, but I was told I should do it like that so pls don't judge q_q
    private static final String[] PROGRESS_BARS = {
            KNOB_BEGINNING + StringUtils.repeatString(BAR, 11),
            KNOB_MIDDLE + StringUtils.repeatString(BAR, 11),
            KNOB_END + StringUtils.repeatString(BAR, 11),
            KNOB_END_SPLIT + KNOB_BEGINNING_SPLIT + StringUtils.repeatString(BAR, 10),
            BAR + KNOB_BEGINNING + StringUtils.repeatString(BAR, 10),
            BAR + KNOB_MIDDLE +StringUtils.repeatString(BAR, 10),
            BAR + KNOB_END + StringUtils.repeatString(BAR, 10),
            BAR + KNOB_END_SPLIT + KNOB_BEGINNING_SPLIT + StringUtils.repeatString(BAR, 9),
            StringUtils.repeatString(BAR, 2)+ KNOB_BEGINNING + StringUtils.repeatString(BAR, 9),
            StringUtils.repeatString(BAR, 2)+ KNOB_MIDDLE + StringUtils.repeatString(BAR, 9),
            StringUtils.repeatString(BAR, 2) + KNOB_END + StringUtils.repeatString(BAR, 9),
            StringUtils.repeatString(BAR, 2) + KNOB_END_SPLIT + KNOB_BEGINNING_SPLIT + StringUtils.repeatString(BAR, 8),
            StringUtils.repeatString(BAR, 3)+ KNOB_BEGINNING + StringUtils.repeatString(BAR, 8),
            StringUtils.repeatString(BAR, 3) + KNOB_MIDDLE +StringUtils.repeatString(BAR, 8),
            StringUtils.repeatString(BAR, 3)+ KNOB_END + StringUtils.repeatString(BAR, 8),
            StringUtils.repeatString(BAR, 3) + KNOB_END_SPLIT + KNOB_BEGINNING_SPLIT + StringUtils.repeatString(BAR, 7),
            StringUtils.repeatString(BAR, 4) + KNOB_BEGINNING + StringUtils.repeatString(BAR, 7),
            StringUtils.repeatString(BAR, 4) + KNOB_MIDDLE + StringUtils.repeatString(BAR, 7),
            StringUtils.repeatString(BAR, 4)+ KNOB_END +StringUtils.repeatString(BAR, 7),
            StringUtils.repeatString(BAR, 4)+ KNOB_END_SPLIT + KNOB_BEGINNING_SPLIT + StringUtils.repeatString(BAR, 6),
            StringUtils.repeatString(BAR, 5)+ KNOB_BEGINNING + StringUtils.repeatString(BAR, 6),
            StringUtils.repeatString(BAR, 5) + KNOB_MIDDLE + StringUtils.repeatString(BAR, 6),
            StringUtils.repeatString(BAR, 5) + KNOB_END + StringUtils.repeatString(BAR, 6),
            StringUtils.repeatString(BAR, 5) + KNOB_END_SPLIT + KNOB_BEGINNING_SPLIT + StringUtils.repeatString(BAR, 5),
            StringUtils.repeatString(BAR, 6) + KNOB_BEGINNING + StringUtils.repeatString(BAR, 5),
            StringUtils.repeatString(BAR, 6) + KNOB_MIDDLE + StringUtils.repeatString(BAR, 5),
            StringUtils.repeatString(BAR, 6)+ KNOB_END + StringUtils.repeatString(BAR, 5),
            StringUtils.repeatString(BAR, 6)+ KNOB_END_SPLIT + KNOB_BEGINNING_SPLIT + StringUtils.repeatString(BAR, 4),
            StringUtils.repeatString(BAR, 7) + KNOB_BEGINNING + StringUtils.repeatString(BAR, 4),
            StringUtils.repeatString(BAR, 7) + KNOB_MIDDLE + StringUtils.repeatString(BAR, 4),
            StringUtils.repeatString(BAR, 7)+ KNOB_END + StringUtils.repeatString(BAR, 4),
            StringUtils.repeatString(BAR, 7) + KNOB_END_SPLIT + KNOB_BEGINNING_SPLIT + StringUtils.repeatString(BAR, 3),
            StringUtils.repeatString(BAR, 8)+ KNOB_BEGINNING + StringUtils.repeatString(BAR, 3),
            StringUtils.repeatString(BAR, 8)+ KNOB_MIDDLE + StringUtils.repeatString(BAR, 3),
            StringUtils.repeatString(BAR, 8)+ KNOB_END + StringUtils.repeatString(BAR, 3),
            StringUtils.repeatString(BAR, 8) + KNOB_END_SPLIT + KNOB_BEGINNING_SPLIT + StringUtils.repeatString(BAR, 2),
            StringUtils.repeatString(BAR, 9) + KNOB_BEGINNING + StringUtils.repeatString(BAR, 2),
            StringUtils.repeatString(BAR, 9) + KNOB_MIDDLE +StringUtils.repeatString(BAR, 2),
            StringUtils.repeatString(BAR, 9) + KNOB_END + StringUtils.repeatString(BAR, 2),
            StringUtils.repeatString(BAR, 9) + KNOB_END_SPLIT + KNOB_BEGINNING_SPLIT + BAR,
            StringUtils.repeatString(BAR, 10) + KNOB_BEGINNING + BAR,
            StringUtils.repeatString(BAR, 10) + KNOB_MIDDLE + BAR,
            StringUtils.repeatString(BAR, 10)+ KNOB_END + BAR,
            StringUtils.repeatString(BAR, 10)+ KNOB_END_SPLIT + KNOB_BEGINNING_SPLIT,
            StringUtils.repeatString(BAR, 11) + KNOB_BEGINNING,
            StringUtils.repeatString(BAR, 11)+ KNOB_MIDDLE,
            StringUtils.repeatString(BAR, 11) + KNOB_END
    };

    public static String getProgressBar(int percentage) // from 0 to 100
    {
        double min = Math.min(PROGRESS_BARS.length - 1, ((percentage / 100d) * PROGRESS_BARS.length));
        return PROGRESS_BARS[(int) min];
    }



}
