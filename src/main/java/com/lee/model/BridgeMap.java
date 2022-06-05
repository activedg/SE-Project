package com.lee.model;

import java.io.*;
import java.util.ArrayList;

public class BridgeMap {
    // Singleton 기법
    public static BridgeMap m = null;
    private boolean isMapSet = false;
    private String[] mapData;

    // BridgeMap 객체가 하나만 생성되게 함
    public static BridgeMap getInstance() {
        if (m == null)
            m = new BridgeMap();
        return m;
    }

    // 초기에 파일을 설정 안한 경우 default.map으로 맵 데이터를 설정함
    public void defaultMap() {
        File file = new File("src/main/resources/com/lee/mapdata/default.map");
        if (file.exists()) {
            fileToData(file);
        } else {
            mapData = new String[]{
                    "S R", "C L R", "C L D", "B U D", "S U D", "C U D", "C U D", "C U R", "C L R", "H L U", "B D U", "C D U", "C D U",
                    "C D U", "b D U", "P D U", "C D R", "C L R", "H L D", "C U D", "C U D", "C U D", "B U D", "C U D", "b U D", "C U D",
                    "S U D", "P U D", "C U D", "C U D", "C U R", "C L R", "C L U", "H D U", "C D U", "B D U", "C D U", "C D U", "C D U",
                    "C D U", "b D U", "C D R", "C L R", "H L D", "C U D", "C U D", "C U D", "P U D", "B U D", "b U D", "C U D", "H U D",
                    "C U R", "C L R", "S L U", "P D U", "C D U", "C D U", "b D U", "C D U", "C D U", "E"
            };
        }
        writeCurMap();
    }

    // getter
    public String[] getMapData() {
        if (isMapSet == false) {
            if (isPrevMapExists()){
                File file = new File("src/main/resources/com/lee/mapdata/prev.map");
                fileToData(file);
            }
            else defaultMap();
            isMapSet = true;
        }
        return mapData;
    }

    // setter
    public void setMapData(String[] mapData) {
        this.mapData = mapData;
        writeCurMap();
        isMapSet = true;
    }

    // 파일에서 데이터 읽어오기
    private void fileToData(File file){
        ArrayList<String> mapData = new ArrayList<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = br.readLine()) != null) {
                mapData.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mapData = mapData.toArray(new String[mapData.size()]);
    }

    // 현재 플레이 중인 맵을 작성하는 함수, 다음에 게임을 실행할 때 prev.map을 이용해서 사용함
    private void writeCurMap(){
        try {
            File file = new File("src/main/resources/com/lee/mapdata/prev.map");
            if (!file.exists())
                file.createNewFile();
            else {
                file.delete();
                file.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (int i=0; i<mapData.length; i++)
                bw.append(mapData[i]+"\n");
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isPrevMapExists(){
        return new File("src/main/resources/com/lee/mapdata/prev.map").exists();
    }

    // 맵의 가로 길이
    public int getMapWidth() {
        int leftCount = 0;
        int rightCount = 0;
        int heightMax = 0;
        for (int i = 0; i < mapData.length; i++) {
            if (mapData[i].endsWith("L")) {
                leftCount++;
                if (rightCount > 0) rightCount--;
                if (leftCount > heightMax)
                    heightMax = leftCount;
            } else if (mapData[i].endsWith("R")) {
                rightCount++;
                if (leftCount > 0) leftCount--;
                if (rightCount > heightMax)
                    heightMax = rightCount;
            }
        }
        return heightMax + 1;
    }

    // 맵의 세로 길이
    public int getMapHeight() {
        int upCount = 0;
        int downCount = 0;
        int heightMax = 0;
        for (int i = 0; i < mapData.length; i++) {
            if (mapData[i].endsWith("U")) {
                downCount++;
                if (upCount > 0) upCount--;
                if (downCount > heightMax)
                    heightMax = downCount;
            } else if (mapData[i].endsWith("D")) {
                upCount++;
                if (downCount > 0) downCount--;
                if (upCount > heightMax)
                    heightMax = upCount;
            }
        }
        return heightMax + 1;
    }

    // 시작 포인트와 게임에서 가장 높은 타일의 높이 차이
    public int getStartHeightGap() {
        int upCount = 0;
        int downCount = 0;
        int gapMax = 0;
        for (int i = 0; i < mapData.length; i++) {
            if (mapData[i].endsWith("U"))
                upCount++;
            else if (mapData[i].endsWith("D"))
                downCount++;
            else {
                if (downCount > 0 && upCount > 0) {
                    if (upCount - downCount > gapMax) {
                        gapMax = upCount - downCount;
                        upCount = 0;
                        downCount = 0;
                    }
                }
            }
        }
        return gapMax > 0 ? gapMax : 0;
    }

    // 시작 포인트와 게임에서 가장 멀리있는 타일의 폭 차이
    public int getStartWidthGap() {
        int leftCount = 0;
        int rightCount = 0;
        int gapMax = 0;
        for (int i = 0; i < mapData.length; i++) {
            if (mapData[i].endsWith("L"))
                leftCount++;
            else if (mapData[i].endsWith("R"))
                rightCount++;
            else {
                if (leftCount > 0 && rightCount > 0) {
                    if (leftCount - rightCount > gapMax) {
                        gapMax = leftCount - rightCount;
                        leftCount = 0;
                        rightCount = 0;
                    }
                }
            }
        }
        return gapMax > 0 ? gapMax : 0;
    }

}
