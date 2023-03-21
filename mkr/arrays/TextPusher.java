package mkr.arrays;

import java.util.ArrayList;

public class TextPusher implements Runnable {
    private final ArrayList<String> list;
    private final ArrayList<String> text;

    public TextPusher(ArrayList<String> list, ArrayList<String> text) {
        this.list = list;
        this.text = text;
    }

    @Override
    public void run() {
        for (var str : this.list) {
            synchronized(this.text) {
                this.text.add(str);
            }
        }
    }
}
