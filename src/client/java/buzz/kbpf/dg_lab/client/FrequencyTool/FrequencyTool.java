package buzz.kbpf.dg_lab.client.FrequencyTool;

import java.util.ArrayList;

public class FrequencyTool {

    public static String toFrequency(String Text){
        ArrayList<waveformPairs> waveformPairs = new ArrayList<>();
        StringBuilder number = new StringBuilder();
        int length = 0;
        //false = time, true = Strength
        {
            waveformPairs waveformPairs1 = new waveformPairs();
            for (char ch : Text.toCharArray()) {
                if (Character.isDigit(ch)) {
                    number.append(ch);
                } else if (ch == ',') {
                    waveformPairs1.setTime(Integer.parseInt(number.toString()));
                    number = new StringBuilder();
                    length += waveformPairs1.getTime() + 1;
                } else if (ch == ';') {
                    waveformPairs1.setStrength(Integer.parseInt(number.toString()));
                    waveformPairs.add(waveformPairs1);
                    waveformPairs1 = new waveformPairs();
                    number = new StringBuilder();
                }
            }
        }
        double[] Frequency = new double[length + 1];
        Frequency[0] = 0;
        int j = 1;
        for (waveformPairs waveformPairs1 : waveformPairs){
            double max = waveformPairs1.getStrength();
            double min = Frequency[j - 1];
            System.out.println(max + " " + min);
            double Difference = (max - min) / (waveformPairs1.getTime() + 1);
            for(int i = 0; i <= waveformPairs1.getTime(); i++){
                Frequency[j] = Frequency[j - 1] + Difference;
                j++;
            }

        }

        StringBuilder frequency = new StringBuilder();
        for (int i = 0; i <= length; i++) {
            if(i % 4 == 0) {
                if (i != 0) frequency.append("\",\"0A0A0A0A");
                else frequency.append("\"0A0A0A0A");
            }
            frequency.append(String.format("%02X", (int) Frequency[i]));
        }
        switch (length % 4){
            case 0 -> {
                frequency.append("000000");
            }
            case 1 -> {
                frequency.append("0000");
            }
            case 2 -> {
                frequency.append("00");
            }
        }
        System.out.println((length - 1) % 4);
        frequency.append('\"');
        return frequency.toString();
    }
}


class waveformPairs {
    private int time, Strength;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = Math.max(time, 0);
    }

    public int getStrength() {
        return Strength;
    }

    public void setStrength(int strength) {
        Strength = Math.min(100, Math.max(strength, 0));
    }
}